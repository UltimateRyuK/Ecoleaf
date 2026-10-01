package com.example.ml

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import com.example.model.PlantType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.exp
import kotlin.math.max

data class ClassScore(
    val classKey: String,
    val score: Float
)

data class ModelPredictionResult(
    val isModelAvailable: Boolean,
    val topClass: String = "",
    val topConfidence: Float = 0f,
    val topPredictions: List<ClassScore> = emptyList(),
    val inferenceTimeMs: Long = 0,
    val errorMessage: String? = null
)

data class ModelStatusInfo(
    val plantType: PlantType,
    val isAvailable: Boolean,
    val sourceDescription: String,
    val fileSizeBytes: Long,
    val labels: List<String>,
    val isQuantized: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Native On-Device TensorFlow Lite Quantized Model Manager for EcoLeaf.
 * Loads and runs Google Teachable Machine exported quantized TFLite models.
 */
class EcoLeafModelManager(private val context: Context) {

    private val interpreters = mutableMapOf<PlantType, Interpreter>()
    private val modelFilesDir = File(context.filesDir, "models").apply { mkdirs() }

    companion object {
        const val DEFAULT_INPUT_SIZE = 224
        const val CHANNELS = 3
    }

    /**
     * Retrieves the status of the model for a given plant.
     */
    fun getModelStatus(plantType: PlantType): ModelStatusInfo {
        val customModelFile = getCustomModelFile(plantType)
        val hasCustomFile = customModelFile.exists() && customModelFile.length() > 0

        val assetPath = plantType.assetModelPath
        val hasAssetFile = try {
            val afd = context.assets.openFd(assetPath)
            val len = afd.length
            afd.close()
            len > 0
        } catch (e: Exception) {
            false
        }

        val labels = loadLabels(plantType)
        val hasValidLabels = labels.size == plantType.defaultClasses.size &&
                labels.containsAll(plantType.defaultClasses)

        return when {
            hasCustomFile -> {
                val interpreter = getOrInitInterpreter(plantType)
                val isQuantized = interpreter?.getInputTensor(0)?.dataType() != DataType.FLOAT32
                ModelStatusInfo(
                    plantType = plantType,
                    isAvailable = true,
                    sourceDescription = "Custom Quantized TFLite (${customModelFile.length() / 1024} KB)",
                    fileSizeBytes = customModelFile.length(),
                    labels = labels,
                    isQuantized = isQuantized
                )
            }
            hasAssetFile -> {
                val interpreter = getOrInitInterpreter(plantType)
                val isQuantized = interpreter?.getInputTensor(0)?.dataType() != DataType.FLOAT32
                val assetLen = try {
                    val afd = context.assets.openFd(assetPath)
                    val l = afd.length
                    afd.close()
                    l
                } catch (e: Exception) { 0L }

                ModelStatusInfo(
                    plantType = plantType,
                    isAvailable = true,
                    sourceDescription = "Bundled Quantized TFLite ($assetPath)",
                    fileSizeBytes = assetLen,
                    labels = labels,
                    isQuantized = isQuantized
                )
            }
            else -> ModelStatusInfo(
                plantType = plantType,
                isAvailable = false,
                sourceDescription = "Missing (Expected: $assetPath)",
                fileSizeBytes = 0L,
                labels = labels,
                errorMessage = if (!hasValidLabels) "Labels missing or incomplete." else "Model file 'model.tflite' not found in $assetPath"
            )
        }
    }

    /**
     * Loads labels for a specific plant model.
     * Looks in custom directory, then exact asset directory: models/{folder}/labels.txt.
     */
    fun loadLabels(plantType: PlantType): List<String> {
        val customLabelsFile = File(File(modelFilesDir, plantType.folderName), plantType.labelsFileName)
        if (customLabelsFile.exists() && customLabelsFile.length() > 0) {
            try {
                val lines = customLabelsFile.readLines().map { it.trim() }.filter { it.isNotEmpty() }
                if (lines.isNotEmpty()) return lines
            } catch (e: Exception) {
                // fall through
            }
        }

        // Try exact asset path: models/{folder}/labels.txt
        try {
            val stream = context.assets.open(plantType.assetLabelsPath)
            val lines = stream.bufferedReader().useLines { seq ->
                seq.map { it.trim() }.filter { it.isNotEmpty() }.toList()
            }
            if (lines.isNotEmpty()) return lines
        } catch (e: Exception) {
            // fall through
        }

        // Fallback to default expected classes
        return plantType.defaultClasses
    }

    /**
     * Imports a user-supplied exported Teachable Machine model (.tflite or .zip) for a specific plant.
     */
    suspend fun importModel(plantType: PlantType, tfliteUri: Uri, labelsUri: Uri? = null): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val plantDir = File(modelFilesDir, plantType.folderName).apply { mkdirs() }
                val destModelFile = File(plantDir, plantType.modelFileName)
                val destLabelsFile = File(plantDir, plantType.labelsFileName)

                // Check if it is a ZIP archive
                val isZip = context.contentResolver.openInputStream(tfliteUri)?.use { input ->
                    val header = ByteArray(4)
                    val read = input.read(header)
                    read == 4 && header[0] == 0x50.toByte() && header[1] == 0x4B.toByte()
                } ?: false

                if (isZip) {
                    context.contentResolver.openInputStream(tfliteUri)?.use { input ->
                        val zis = java.util.zip.ZipInputStream(input)
                        var entry = zis.nextEntry
                        while (entry != null) {
                            val name = entry.name.lowercase()
                            if (name.endsWith(".tflite")) {
                                FileOutputStream(destModelFile).use { out -> zis.copyTo(out) }
                            } else if (name.endsWith("labels.txt")) {
                                FileOutputStream(destLabelsFile).use { out -> zis.copyTo(out) }
                            }
                            zis.closeEntry()
                            entry = zis.nextEntry
                        }
                    }
                } else {
                    context.contentResolver.openInputStream(tfliteUri)?.use { input ->
                        FileOutputStream(destModelFile).use { output -> input.copyTo(output) }
                    } ?: return@withContext false

                    if (labelsUri != null) {
                        context.contentResolver.openInputStream(labelsUri)?.use { input ->
                            FileOutputStream(destLabelsFile).use { output -> input.copyTo(output) }
                        }
                    }
                }

                // Invalidate cached interpreter
                synchronized(interpreters) {
                    interpreters.remove(plantType)?.close()
                }

                destModelFile.exists() && destModelFile.length() > 0
            } catch (e: Exception) {
                false
            }
        }

    /**
     * Executes real on-device inference using the selected plant's quantized or float TFLite model.
     */
    suspend fun runInference(plantType: PlantType, bitmap: Bitmap): ModelPredictionResult =
        withContext(Dispatchers.Default) {
            val startTime = System.currentTimeMillis()
            val labels = loadLabels(plantType)

            val interpreter = getOrInitInterpreter(plantType)
            if (interpreter == null) {
                return@withContext ModelPredictionResult(
                    isModelAvailable = false,
                    errorMessage = "Quantized TFLite model for ${plantType.commonName} is not installed in '${plantType.assetModelPath}'. Please place model.tflite in that asset folder or import it via Settings."
                )
            }

            try {
                // 1. Inspect actual model input tensor dimensions and data type
                val inputTensor = interpreter.getInputTensor(0)
                val inputShape = inputTensor.shape() // [1, height, width, channels]
                val inputHeight = if (inputShape.size >= 2 && inputShape[1] > 0) inputShape[1] else DEFAULT_INPUT_SIZE
                val inputWidth = if (inputShape.size >= 3 && inputShape[2] > 0) inputShape[2] else DEFAULT_INPUT_SIZE
                val inputDataType = inputTensor.dataType()
                val inputQuantParams = inputTensor.quantizationParams()

                // 2. Preprocess input Bitmap to match model dimensions
                val scaled = if (bitmap.width == inputWidth && bitmap.height == inputHeight) {
                    bitmap
                } else {
                    Bitmap.createScaledBitmap(bitmap, inputWidth, inputHeight, true)
                }

                val pixels = IntArray(inputWidth * inputHeight)
                scaled.getPixels(pixels, 0, inputWidth, 0, 0, inputWidth, inputHeight)

                // 3. Populate input buffer matching model data type
                val inputBuffer: ByteBuffer
                when (inputDataType) {
                    DataType.FLOAT32 -> {
                        inputBuffer = ByteBuffer.allocateDirect(1 * inputHeight * inputWidth * CHANNELS * 4).apply {
                            order(ByteOrder.nativeOrder())
                            rewind()
                        }
                        for (pixel in pixels) {
                            val r = Color.red(pixel)
                            val g = Color.green(pixel)
                            val b = Color.blue(pixel)
                            // Teachable machine float normalization: [-1.0, 1.0]
                            inputBuffer.putFloat((r / 127.5f) - 1.0f)
                            inputBuffer.putFloat((g / 127.5f) - 1.0f)
                            inputBuffer.putFloat((b / 127.5f) - 1.0f)
                        }
                    }
                    DataType.UINT8 -> {
                        inputBuffer = ByteBuffer.allocateDirect(1 * inputHeight * inputWidth * CHANNELS * 1).apply {
                            order(ByteOrder.nativeOrder())
                            rewind()
                        }
                        for (pixel in pixels) {
                            val r = Color.red(pixel)
                            val g = Color.green(pixel)
                            val b = Color.blue(pixel)
                            inputBuffer.put((r and 0xFF).toByte())
                            inputBuffer.put((g and 0xFF).toByte())
                            inputBuffer.put((b and 0xFF).toByte())
                        }
                    }
                    DataType.INT8 -> {
                        inputBuffer = ByteBuffer.allocateDirect(1 * inputHeight * inputWidth * CHANNELS * 1).apply {
                            order(ByteOrder.nativeOrder())
                            rewind()
                        }
                        val scale = if (inputQuantParams.scale > 0f) inputQuantParams.scale else (1f / 127.5f)
                        val zeroPoint = inputQuantParams.zeroPoint

                        for (pixel in pixels) {
                            val r = Color.red(pixel)
                            val g = Color.green(pixel)
                            val b = Color.blue(pixel)

                            val normR = (r / 127.5f) - 1.0f
                            val normG = (g / 127.5f) - 1.0f
                            val normB = (b / 127.5f) - 1.0f

                            val qR = ((normR / scale) + zeroPoint).toInt().coerceIn(-128, 127).toByte()
                            val qG = ((normG / scale) + zeroPoint).toInt().coerceIn(-128, 127).toByte()
                            val qB = ((normB / scale) + zeroPoint).toInt().coerceIn(-128, 127).toByte()

                            inputBuffer.put(qR)
                            inputBuffer.put(qG)
                            inputBuffer.put(qB)
                        }
                    }
                    else -> {
                        return@withContext ModelPredictionResult(
                            isModelAvailable = false,
                            errorMessage = "Unsupported model input tensor type: $inputDataType"
                        )
                    }
                }

                // 4. Inspect actual model output tensor shape and data type
                val outputTensor = interpreter.getOutputTensor(0)
                val outputShape = outputTensor.shape()
                val numOutputs = if (outputShape.size >= 2 && outputShape[1] > 0) outputShape[1] else labels.size
                val outputDataType = outputTensor.dataType()
                val outputQuantParams = outputTensor.quantizationParams()

                val rawScores = FloatArray(numOutputs)

                when (outputDataType) {
                    DataType.FLOAT32 -> {
                        val outputBuffer = Array(1) { FloatArray(numOutputs) }
                        interpreter.run(inputBuffer, outputBuffer)
                        for (i in 0 until numOutputs) {
                            rawScores[i] = outputBuffer[0][i]
                        }
                    }
                    DataType.UINT8 -> {
                        val outputBuffer = Array(1) { ByteArray(numOutputs) }
                        interpreter.run(inputBuffer, outputBuffer)
                        val scale = if (outputQuantParams.scale > 0f) outputQuantParams.scale else (1f / 255f)
                        val zeroPoint = outputQuantParams.zeroPoint
                        for (i in 0 until numOutputs) {
                            val raw = outputBuffer[0][i].toInt() and 0xFF
                            rawScores[i] = (raw - zeroPoint) * scale
                        }
                    }
                    DataType.INT8 -> {
                        val outputBuffer = Array(1) { ByteArray(numOutputs) }
                        interpreter.run(inputBuffer, outputBuffer)
                        val scale = if (outputQuantParams.scale > 0f) outputQuantParams.scale else (1f / 255f)
                        val zeroPoint = outputQuantParams.zeroPoint
                        for (i in 0 until numOutputs) {
                            val raw = outputBuffer[0][i].toInt()
                            rawScores[i] = (raw - zeroPoint) * scale
                        }
                    }
                    else -> {
                        return@withContext ModelPredictionResult(
                            isModelAvailable = false,
                            errorMessage = "Unsupported model output tensor type: $outputDataType"
                        )
                    }
                }

                // 5. Softmax normalization for proper probabilities
                val probabilities = normalizeScores(rawScores)

                // 6. Map to labels and sort descending
                val classScores = (0 until minOf(labels.size, numOutputs)).map { i ->
                    val label = labels[i]
                    val score = probabilities.getOrElse(i) { 0f }.coerceIn(0f, 1f)
                    ClassScore(classKey = label, score = score)
                }.sortedByDescending { it.score }

                val top = classScores.firstOrNull() ?: ClassScore(labels.first(), 0f)
                val duration = max(10L, System.currentTimeMillis() - startTime)

                ModelPredictionResult(
                    isModelAvailable = true,
                    topClass = top.classKey,
                    topConfidence = top.score,
                    topPredictions = classScores.take(3),
                    inferenceTimeMs = duration
                )
            } catch (e: Exception) {
                ModelPredictionResult(
                    isModelAvailable = false,
                    errorMessage = "Quantized inference error for ${plantType.commonName}: ${e.localizedMessage ?: "Unknown error"}"
                )
            }
        }

    private fun getOrInitInterpreter(plantType: PlantType): Interpreter? {
        synchronized(interpreters) {
            interpreters[plantType]?.let { return it }

            val modelBuffer = loadModelBuffer(plantType) ?: return null
            return try {
                val options = Interpreter.Options().apply {
                    setNumThreads(4)
                }
                val interpreter = Interpreter(modelBuffer, options)
                interpreters[plantType] = interpreter
                interpreter
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun getCustomModelFile(plantType: PlantType): File {
        val folder = File(modelFilesDir, plantType.folderName)
        val fileInFolder = File(folder, plantType.modelFileName)
        if (fileInFolder.exists()) return fileInFolder
        // Check root modelsDir with old naming
        val legacyFile = File(modelFilesDir, "${plantType.id}.tflite")
        if (legacyFile.exists()) return legacyFile
        return fileInFolder
    }

    private fun loadModelBuffer(plantType: PlantType): MappedByteBuffer? {
        // 1. Check custom imported file
        val customFile = getCustomModelFile(plantType)
        if (customFile.exists() && customFile.length() > 0) {
            try {
                val fileInputStream = FileInputStream(customFile)
                val channel = fileInputStream.channel
                val buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
                fileInputStream.close()
                return buffer
            } catch (e: Exception) {
                // fall through
            }
        }

        // 2. Check bundled asset at exact path: models/{folder}/model.tflite
        try {
            val afd: AssetFileDescriptor = context.assets.openFd(plantType.assetModelPath)
            val inputStream = FileInputStream(afd.fileDescriptor)
            val channel = inputStream.channel
            val buffer = channel.map(FileChannel.MapMode.READ_ONLY, afd.startOffset, afd.declaredLength)
            inputStream.close()
            afd.close()
            return buffer
        } catch (e: Exception) {
            // try legacy asset name models/{plantType.id}.tflite
            try {
                val afd = context.assets.openFd("models/${plantType.id}.tflite")
                val inputStream = FileInputStream(afd.fileDescriptor)
                val channel = inputStream.channel
                val buffer = channel.map(FileChannel.MapMode.READ_ONLY, afd.startOffset, afd.declaredLength)
                inputStream.close()
                afd.close()
                return buffer
            } catch (e2: Exception) {
                return null
            }
        }
    }

    private fun normalizeScores(scores: FloatArray): FloatArray {
        var sum = 0f
        var isAlreadyProbability = true
        for (s in scores) {
            if (s < 0f || s > 1.05f) {
                isAlreadyProbability = false
            }
            sum += s
        }

        if (isAlreadyProbability && sum in 0.90f..1.10f) {
            return scores
        }

        // Apply Softmax
        var maxVal = Float.NEGATIVE_INFINITY
        for (s in scores) if (s > maxVal) maxVal = s

        var sumExp = 0.0
        val expScores = DoubleArray(scores.size)
        for (i in scores.indices) {
            val e = exp((scores[i] - maxVal).toDouble())
            expScores[i] = e
            sumExp += e
        }

        val result = FloatArray(scores.size)
        for (i in scores.indices) {
            result[i] = if (sumExp > 0) (expScores[i] / sumExp).toFloat() else (1f / scores.size)
        }
        return result
    }

    fun release() {
        synchronized(interpreters) {
            interpreters.values.forEach { it.close() }
            interpreters.clear()
        }
    }
}

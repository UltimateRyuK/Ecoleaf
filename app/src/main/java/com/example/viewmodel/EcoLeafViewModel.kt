package com.example.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PlantConditionRegistry
import com.example.data.PlantGuideData
import com.example.data.PlantGuideEntry
import com.example.data.db.AppDatabase
import com.example.data.db.ScanRecord
import com.example.ml.EcoLeafModelManager
import com.example.ml.ImageQualityChecker
import com.example.ml.ImageQualityResult
import com.example.ml.ModelPredictionResult
import com.example.ml.ModelStatusInfo
import com.example.model.PlantCondition
import com.example.model.PlantType
import com.example.sprig.engine.SprigEngine
import com.example.sprig.model.SprigEngineStatus
import com.example.sprig.model.SprigMessage
import com.example.sprig.model.SprigScanContext
import com.example.sprig.model.SprigSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

enum class EcoLeafScreen {
    HOME,
    PLANT_SELECT,
    CAPTURE_PREVIEW,
    DIAGNOSIS_RESULT,
    SPRIG,
    PLANT_GUIDE,
    HISTORY,
    SETTINGS,
    MODEL_STATUS
}

class EcoLeafViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val scanDao = db.scanDao()
    private val modelManager = EcoLeafModelManager(application)

    private val _currentScreen = MutableStateFlow(EcoLeafScreen.HOME)
    val currentScreen: StateFlow<EcoLeafScreen> = _currentScreen.asStateFlow()

    private val _selectedPlant = MutableStateFlow<PlantType?>(null)
    val selectedPlant: StateFlow<PlantType?> = _selectedPlant.asStateFlow()

    private val _currentBitmap = MutableStateFlow<Bitmap?>(null)
    val currentBitmap: StateFlow<Bitmap?> = _currentBitmap.asStateFlow()

    private val _qualityResult = MutableStateFlow<ImageQualityResult?>(null)
    val qualityResult: StateFlow<ImageQualityResult?> = _qualityResult.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _predictionResult = MutableStateFlow<ModelPredictionResult?>(null)
    val predictionResult: StateFlow<ModelPredictionResult?> = _predictionResult.asStateFlow()

    private val _currentConditionDetail = MutableStateFlow<PlantCondition?>(null)
    val currentConditionDetail: StateFlow<PlantCondition?> = _currentConditionDetail.asStateFlow()

    private val _confidenceThreshold = MutableStateFlow(0.70f) // 70% default threshold
    val confidenceThreshold: StateFlow<Float> = _confidenceThreshold.asStateFlow()

    private val _currentSavedRecordId = MutableStateFlow<Long?>(null)
    val currentSavedRecordId: StateFlow<Long?> = _currentSavedRecordId.asStateFlow()

    private val _selectedGuideEntry = MutableStateFlow<PlantGuideEntry?>(null)
    val selectedGuideEntry: StateFlow<PlantGuideEntry?> = _selectedGuideEntry.asStateFlow()

    private val _modelStatuses = MutableStateFlow<List<ModelStatusInfo>>(emptyList())
    val modelStatuses: StateFlow<List<ModelStatusInfo>> = _modelStatuses.asStateFlow()

    private val _historySearchQuery = MutableStateFlow("")
    val historySearchQuery: StateFlow<String> = _historySearchQuery.asStateFlow()

    // Sprig On-Device AI Engine & Chat State
    private val sprigEngine = SprigEngine(application)

    private val _sprigMessages = MutableStateFlow<List<SprigMessage>>(emptyList())
    val sprigMessages: StateFlow<List<SprigMessage>> = _sprigMessages.asStateFlow()

    private val _isSprigThinking = MutableStateFlow(false)
    val isSprigThinking: StateFlow<Boolean> = _isSprigThinking.asStateFlow()

    private val _sprigScanContext = MutableStateFlow<SprigScanContext?>(null)
    val sprigScanContext: StateFlow<SprigScanContext?> = _sprigScanContext.asStateFlow()

    private val _sprigEngineStatus = MutableStateFlow(SprigEngineStatus())
    val sprigEngineStatus: StateFlow<SprigEngineStatus> = _sprigEngineStatus.asStateFlow()

    val scanHistory: StateFlow<List<ScanRecord>> = scanDao.getAllScans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refreshModelStatuses()
    }

    fun refreshModelStatuses() {
        val statuses = PlantType.entries.map { modelManager.getModelStatus(it) }
        _modelStatuses.value = statuses
    }

    fun navigateTo(screen: EcoLeafScreen) {
        _currentScreen.value = screen
    }

    fun setConfidenceThreshold(threshold: Float) {
        _confidenceThreshold.value = threshold.coerceIn(0.50f, 0.95f)
    }

    fun setHistorySearchQuery(query: String) {
        _historySearchQuery.value = query
    }

    fun selectPlant(plantType: PlantType) {
        _selectedPlant.value = plantType
        _currentScreen.value = EcoLeafScreen.CAPTURE_PREVIEW
    }

    fun openPlantGuide(plantType: PlantType) {
        _selectedGuideEntry.value = PlantGuideData.getGuide(plantType)
        _currentScreen.value = EcoLeafScreen.PLANT_GUIDE
    }

    fun onImageSelected(bitmap: Bitmap) {
        _currentBitmap.value = bitmap
        // Run native image quality check
        val quality = ImageQualityChecker.checkQuality(bitmap)
        _qualityResult.value = quality
        _currentScreen.value = EcoLeafScreen.CAPTURE_PREVIEW
    }

    fun onUriSelected(uri: Uri) {
        viewModelScope.launch {
            val bitmap = loadBitmapFromUri(uri)
            if (bitmap != null) {
                onImageSelected(bitmap)
            }
        }
    }

    fun retakePhoto() {
        _currentBitmap.value = null
        _qualityResult.value = null
        _predictionResult.value = null
        _currentConditionDetail.value = null
        _currentScreen.value = EcoLeafScreen.CAPTURE_PREVIEW
    }

    fun chooseAnotherPlant() {
        _selectedPlant.value = null
        _currentBitmap.value = null
        _qualityResult.value = null
        _predictionResult.value = null
        _currentConditionDetail.value = null
        _currentScreen.value = EcoLeafScreen.PLANT_SELECT
    }

    fun analyzeLeaf() {
        val plant = _selectedPlant.value ?: return
        val bitmap = _currentBitmap.value ?: return

        _isAnalyzing.value = true
        _currentSavedRecordId.value = null
        _currentScreen.value = EcoLeafScreen.DIAGNOSIS_RESULT

        viewModelScope.launch {
            // Run inference with the chosen plant's model
            val result = modelManager.runInference(plant, bitmap)
            _predictionResult.value = result

            // Retrieve local bundled educational condition details
            val condition = PlantConditionRegistry.getCondition(result.topClass)
            _currentConditionDetail.value = condition

            _isAnalyzing.value = false

            // Auto-save to local Room database
            saveScanToLocalDb(plant, bitmap, result, condition)
        }
    }

    fun saveNotes(notes: String) {
        val recordId = _currentSavedRecordId.value ?: return
        viewModelScope.launch {
            scanDao.updateNotes(recordId, notes)
        }
    }

    fun toggleFavorite(record: ScanRecord) {
        viewModelScope.launch {
            scanDao.updateFavorite(record.id, !record.isFavorite)
        }
    }

    fun deleteScan(record: ScanRecord) {
        viewModelScope.launch {
            scanDao.deleteScan(record)
            // Clean up file if present
            try {
                val f = File(record.imageUri)
                if (f.exists()) f.delete()
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            scanDao.deleteAllScans()
        }
    }

    fun importCustomModel(plantType: PlantType, tfliteUri: Uri, labelsUri: Uri?) {
        viewModelScope.launch {
            val success = modelManager.importModel(plantType, tfliteUri, labelsUri)
            if (success) {
                refreshModelStatuses()
            }
        }
    }

    fun openSprigTab() {
        _currentScreen.value = EcoLeafScreen.SPRIG
        viewModelScope.launch {
            val status = sprigEngine.initialize()
            _sprigEngineStatus.value = status
        }
    }

    fun openSprigWithScanContext(
        plantType: PlantType,
        prediction: ModelPredictionResult?,
        condition: PlantCondition?
    ) {
        val careGuide = PlantGuideData.getGuide(plantType)
        val context = SprigScanContext(
            plantType = plantType,
            conditionKey = prediction?.topClass ?: "Healthy",
            conditionDisplayName = condition?.displayName ?: prediction?.topClass ?: "Healthy",
            confidence = prediction?.topConfidence ?: 0.90f,
            topPredictions = prediction?.topPredictions?.map { it.classKey to it.score } ?: emptyList(),
            symptoms = condition?.symptoms ?: emptyList(),
            whatToDo = condition?.whatToDo ?: "Provide balanced indirect light and let topsoil dry out before watering.",
            prevention = condition?.prevention ?: emptyList(),
            careGuide = careGuide
        )
        _sprigScanContext.value = context
        _currentScreen.value = EcoLeafScreen.SPRIG

        viewModelScope.launch {
            val status = sprigEngine.initialize()
            _sprigEngineStatus.value = status

            if (_sprigMessages.value.isEmpty()) {
                val intro = "Hello! I noticed your **${plantType.commonName}** scan matched **${context.conditionDisplayName}** (${(context.confidence * 100).toInt()}%). How can I help you care for it?"
                _sprigMessages.value = listOf(
                    SprigMessage(
                        sender = SprigSender.SPRIG,
                        text = intro,
                        scanContextSummary = "${plantType.commonName} (${context.conditionDisplayName})"
                    )
                )
            }
        }
    }

    fun sendSprigMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = SprigMessage(sender = SprigSender.USER, text = text.trim())
        val updatedHistory = _sprigMessages.value + userMsg
        _sprigMessages.value = updatedHistory
        _isSprigThinking.value = true

        viewModelScope.launch {
            val response = sprigEngine.generateResponse(
                userMessage = text,
                conversationHistory = updatedHistory,
                scanContext = _sprigScanContext.value
            )
            _sprigMessages.value = _sprigMessages.value + response
            _isSprigThinking.value = false
            _sprigEngineStatus.value = sprigEngine.getStatus()
        }
    }

    fun clearSprigScanContext() {
        _sprigScanContext.value = null
    }

    fun resetSprigChat() {
        _sprigMessages.value = emptyList()
        _isSprigThinking.value = false
    }

    private suspend fun loadBitmapFromUri(uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val context = getApplication<Application>()
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() } ?: return@withContext null
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return@withContext null

            // Safely correct EXIF orientation for phone cameras and gallery uploads
            val exif = try {
                android.media.ExifInterface(java.io.ByteArrayInputStream(bytes))
            } catch (e: Exception) {
                null
            }
            val orientation = exif?.getAttributeInt(
                android.media.ExifInterface.TAG_ORIENTATION,
                android.media.ExifInterface.ORIENTATION_NORMAL
            ) ?: android.media.ExifInterface.ORIENTATION_NORMAL

            val rotationAngle = when (orientation) {
                android.media.ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                android.media.ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                android.media.ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }

            if (rotationAngle != 0f) {
                val matrix = android.graphics.Matrix().apply { postRotate(rotationAngle) }
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun saveScanToLocalDb(
        plant: PlantType,
        bitmap: Bitmap,
        result: ModelPredictionResult,
        condition: PlantCondition?
    ) = withContext(Dispatchers.IO) {
        try {
            val context = getApplication<Application>()
            val filename = "leaf_${plant.id}_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, filename)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }

            val isHealthy = result.topClass.contains("Healthy", ignoreCase = true)
            val isConfident = result.topConfidence >= _confidenceThreshold.value

            val record = ScanRecord(
                plantId = plant.id,
                plantName = plant.commonName,
                conditionKey = result.topClass,
                conditionDisplayName = condition?.displayName ?: result.topClass,
                confidence = result.topConfidence,
                imageUri = file.absolutePath,
                category = condition?.category?.label ?: "Diagnostic",
                notes = "",
                timestamp = System.currentTimeMillis(),
                isFavorite = false,
                isConfident = isConfident
            )

            val id = scanDao.insertScan(record)
            _currentSavedRecordId.value = id
        } catch (e: Exception) {
            // non-fatal
        }
    }

    override fun onCleared() {
        super.onCleared()
        modelManager.release()
    }
}

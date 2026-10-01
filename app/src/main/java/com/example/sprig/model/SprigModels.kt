package com.example.sprig.model

import com.example.data.PlantGuideEntry
import com.example.model.PlantCondition
import com.example.model.PlantType
import java.util.UUID

enum class SprigSender {
    USER,
    SPRIG
}

data class SprigMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: SprigSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val scanContextSummary: String? = null
)

data class SprigScanContext(
    val plantType: PlantType,
    val conditionKey: String,
    val conditionDisplayName: String,
    val confidence: Float,
    val topPredictions: List<Pair<String, Float>> = emptyList(),
    val symptoms: List<String> = emptyList(),
    val whatToDo: String = "",
    val prevention: List<String> = emptyList(),
    val careGuide: PlantGuideEntry? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class SprigEngineStatus(
    val isModelAvailable: Boolean = true,
    val isInitialized: Boolean = false,
    val modelName: String = "Gemma (Mobile On-Device)",
    val lastInferenceTimeMs: Long = 0L,
    val verifiedFactsCount: Int = 0,
    val statusDescription: String = "Ready (Local Knowledge & On-Device Engine)"
)

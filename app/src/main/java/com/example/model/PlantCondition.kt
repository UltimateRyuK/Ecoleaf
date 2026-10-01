package com.example.model

enum class ConditionCategory(val label: String) {
    HEALTHY("Healthy"),
    FUNGAL("Fungal Disease"),
    BACTERIAL("Bacterial Disease"),
    VIRAL("Viral Disease"),
    PEST("Pest Damage"),
    ABIOTIC("Nutrient / Cultural Issue")
}

data class PlantCondition(
    val id: String,
    val plantType: PlantType,
    val classKey: String,
    val displayName: String,
    val category: ConditionCategory,
    val description: String,
    val symptoms: List<String>,
    val whatToDo: String,
    val prevention: List<String>,
    val precautions: List<String>,
    val sources: List<String>
)

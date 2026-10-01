package com.example.model

enum class PlantType(
    val id: String,
    val commonName: String,
    val scientificName: String,
    val shortDescription: String,
    val folderName: String,
    val modelFileName: String = "model.tflite",
    val labelsFileName: String = "labels.txt",
    val defaultClasses: List<String>
) {
    MONEY_PLANT(
        id = "money_plant",
        commonName = "Money Plant",
        scientificName = "Epipremnum aureum",
        shortDescription = "Hardy trailing vine with heart-shaped leaves, popular in home interiors.",
        folderName = "moneyplant",
        defaultClasses = listOf(
            "MoneyPlant_Healthy",
            "MoneyPlant_BacterialWilt",
            "MoneyPlant_ManganeseToxicity"
        )
    ),
    SNAKE_PLANT(
        id = "snake_plant",
        commonName = "Snake Plant",
        scientificName = "Dracaena trifasciata",
        shortDescription = "Upright sword-shaped succulent known for air purifying and low-water tolerance.",
        folderName = "snakeplant",
        defaultClasses = listOf(
            "SnakePlant_Healthy",
            "SnakePlant_Anthracnose",
            "SnakePlant_LeafWithering"
        )
    ),
    SPIDER_PLANT(
        id = "spider_plant",
        commonName = "Spider Plant",
        scientificName = "Chlorophytum comosum",
        shortDescription = "Arching ribbon-like foliage with bright variegation, safe for household pets.",
        folderName = "spiderplant",
        defaultClasses = listOf(
            "SpiderPlant_Healthy",
            "SpiderPlant_FungalLeafSpot",
            "SpiderPlant_LeafTipNecrosis"
        )
    ),
    ROSE(
        id = "rose",
        commonName = "Rose",
        scientificName = "Rosa spp.",
        shortDescription = "Classic flowering ornamental shrub with compound serrated leaflets.",
        folderName = "rose",
        defaultClasses = listOf(
            "Rose_Healthy",
            "Rose_MosaicVirus",
            "Rose_PestDamage",
            "Rose_BotrytisBlight"
        )
    ),
    MARIGOLD(
        id = "marigold",
        commonName = "Marigold",
        scientificName = "Tagetes spp.",
        shortDescription = "Vibrant sun-loving companion flower with finely pinnate aromatic leaves.",
        folderName = "marigold",
        defaultClasses = listOf(
            "Marigold_Healthy",
            "Marigold_AlternariaLeafBlight",
            "Marigold_PestDamage"
        )
    );

    val assetModelPath: String get() = "models/$folderName/$modelFileName"
    val assetLabelsPath: String get() = "models/$folderName/$labelsFileName"

    companion object {
        fun fromId(id: String): PlantType = entries.find { it.id == id } ?: MONEY_PLANT
    }
}

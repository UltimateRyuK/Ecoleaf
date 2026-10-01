package com.example.data

import com.example.model.PlantType

data class PlantGuideEntry(
    val plantType: PlantType,
    val botanicalFamily: String,
    val origin: String,
    val lightRequirement: String,
    val wateringGuide: String,
    val soilAndPotting: String,
    val temperatureRange: String,
    val toxicityInfo: String,
    val educationalOverview: String,
    val commonMistakes: List<String>
)

object PlantGuideData {
    val ENTRIES: List<PlantGuideEntry> = listOf(
        PlantGuideEntry(
            plantType = PlantType.MONEY_PLANT,
            botanicalFamily = "Araceae (Aroid Family)",
            origin = "Mo'orea, French Polynesia",
            lightRequirement = "Medium to bright indirect sunlight. Tolerates lower light levels but variegation may revert to solid green.",
            wateringGuide = "Water thoroughly when the top 2 inches of potting mix feel dry. Thrives under slight underwatering rather than overwatering.",
            soilAndPotting = "Chunky, well-aerated potting mix containing peat/coco coir, perlite, and pine bark to ensure healthy root respiration.",
            temperatureRange = "18°C to 29°C (65°F to 85°F). Sensitive to cold drafts below 10°C (50°F).",
            toxicityInfo = "Toxic to cats and dogs if chewed (insoluble calcium oxalate crystals causing oral irritation and swelling).",
            educationalOverview = "Money Plant (Golden Pothos) is one of the world's most resilient and widely cultivated indoor vines. In nature, it climbs trees using aerial roots, with mature leaves growing up to 3 feet long. In home cultivation, it filters indoor air pollutants and trails gracefully from shelves.",
            commonMistakes = listOf(
                "Leaving roots in soggy, anaerobic standing water leading to bacterial root rot",
                "Placing in direct midday summer sun causing foliar sunburn bleaching",
                "Over-fertilizing in winter during dormancy causing mineral salt toxicity"
            )
        ),
        PlantGuideEntry(
            plantType = PlantType.SNAKE_PLANT,
            botanicalFamily = "Asparagaceae (Asparagus Family)",
            origin = "Tropical West Africa (Nigeria to Congo)",
            lightRequirement = "Adaptable from low light to direct morning sunlight. Brighter indirect light enhances leaf growth and contrast.",
            wateringGuide = "Strictly soak-and-dry. Allow potting soil to dry out 100% between waterings. In winter, water only once every 4 to 6 weeks.",
            soilAndPotting = "Fast-draining cactus/succulent gritty mix with coarse sand, perlite, or pumice in a sturdy terracotta pot.",
            temperatureRange = "15°C to 32°C (60°F to 90°F). Keep away from freezing window panes.",
            toxicityInfo = "Mildly toxic to dogs and cats (saponins that cause nausea and salivation if ingested).",
            educationalOverview = "Snake Plant (Mother-in-Law's Tongue) is renowned for performing Crassulacean Acid Metabolism (CAM) photosynthesis, opening stomata at night to absorb carbon dioxide and release oxygen. Its dense succulent leaves store water internally.",
            commonMistakes = listOf(
                "Frequent watering leading to soft rhizome root rot and blade collapse",
                "Pouring water directly into the center rosette where it stagnates and rots the crown",
                "Using dense moisture-retentive garden soil without grit"
            )
        ),
        PlantGuideEntry(
            plantType = PlantType.SPIDER_PLANT,
            botanicalFamily = "Asparagaceae (Asparagus Family)",
            origin = "Coastal South Africa",
            lightRequirement = "Bright, filtered indirect sunlight. Direct hot sun can bleach and scorch the tender ribbon leaves.",
            wateringGuide = "Keep potting soil evenly moist during active spring and summer growth; allow top inch to dry before re-watering.",
            soilAndPotting = "Standard balanced indoor potting mix with good drainage. Thick fleshy tuberous roots store moisture.",
            temperatureRange = "13°C to 27°C (55°F to 80°F).",
            toxicityInfo = "100% non-toxic and pet-safe for cats and dogs according to ASPCA guidelines.",
            educationalOverview = "Spider Plant is a beloved classic home plant famous for sending out long arching wiry stems (stolons) laden with tiny star-shaped white flowers and miniature plantlets. These plantlets can be easily propagated in water or potting soil.",
            commonMistakes = listOf(
                "Using municipal tap water high in fluoride or chlorine, which causes brown crispy leaf tips",
                "Allowing the pot to sit completely submerged in runoff water",
                "Neglecting to divide or repot when tuberous roots crack plastic nursery pots"
            )
        ),
        PlantGuideEntry(
            plantType = PlantType.ROSE,
            botanicalFamily = "Rosaceae (Rose Family)",
            origin = "Asia, Europe, and North America",
            lightRequirement = "Full direct sunlight—minimum 6 hours of unshaded sun daily for optimal flowering and fungal resistance.",
            wateringGuide = "Deep, infrequent soaking at the base of the bush (1 to 2 inches of water per week). Avoid wetting leaves.",
            soilAndPotting = "Rich, loamy, slightly acidic soil (pH 6.0–6.8) enriched with compost and organic matter with excellent drainage.",
            temperatureRange = "Varies by cultivar; generally hardy from -10°C to 35°C (14°F to 95°F) with winter root mulching.",
            toxicityInfo = "Non-toxic petals and leaves; stems have sharp defensive thorns (prickles) requiring protective gloves.",
            educationalOverview = "Roses are among the most revered flowering shrubs in horticulture, cultivated for millennia. While mostly grown outdoors in garden beds and patio containers, miniature roses are popular potted house gifts. Good sanitation and airflow are essential to keep foliage healthy.",
            commonMistakes = listOf(
                "Sprinkling foliage with overhead garden hoses late in the evening, inviting Botrytis gray mold",
                "Planting in shaded corners where lack of sunlight causes weak, leggy stems and poor blooms",
                "Failing to deadhead spent blooms, which saps energy into seed rose hips instead of new foliage"
            )
        ),
        PlantGuideEntry(
            plantType = PlantType.MARIGOLD,
            botanicalFamily = "Asteraceae (Daisy Family)",
            origin = "Southern North America and Central America (Mexico)",
            lightRequirement = "Full sun—loves hot, bright, direct sunlight throughout the day.",
            wateringGuide = "Water thoroughly once or twice a week when the top inch of soil is dry. Drought-tolerant once established.",
            soilAndPotting = "Moderately fertile, well-draining garden soil. Avoid overly rich nitrogen-heavy soils which cause excess foliage and few flowers.",
            temperatureRange = "18°C to 35°C (65°F to 95°F). Frost-sensitive annual.",
            toxicityInfo = "Non-toxic to pets; foliage produces natural pungent essential oils that naturally repel garden nematodes.",
            educationalOverview = "Marigolds (Tagetes) are beloved companion plants in vegetable patches and container gardens for their golden, orange, and bronze blooms. Their aromatic leaves contain phototoxic thiophenes that protect root systems from microscopic soil pests.",
            commonMistakes = listOf(
                "Overcrowding seedlings, creating stagnant humid conditions where Alternaria leaf blight spreads",
                "Over-fertilizing with nitrogen, producing lush weak green foliage vulnerable to spider mites",
                "Allowing pots to dry to the point of severe wilting during hot summer heat waves"
            )
        )
    )

    fun getGuide(plantType: PlantType): PlantGuideEntry =
        ENTRIES.find { it.plantType == plantType } ?: ENTRIES.first()
}

package com.example.data

import com.example.model.ConditionCategory
import com.example.model.PlantCondition
import com.example.model.PlantType

object PlantConditionRegistry {

    val CONDITIONS: Map<String, PlantCondition> = listOf(
        // ==================== MONEY PLANT (3 classes) ====================
        PlantCondition(
            id = "money_plant_healthy",
            plantType = PlantType.MONEY_PLANT,
            classKey = "MoneyPlant_Healthy",
            displayName = "Healthy Money Plant Foliage",
            category = ConditionCategory.HEALTHY,
            description = "Foliage exhibits vibrant emerald or golden-marbled coloration with smooth leaf margins, glossy cuticle, and firm petiole attachment.",
            symptoms = listOf(
                "Uniform green or natural cream/yellow variegation",
                "Firm leaf turgor with no limp drooping",
                "Clean blade surface free of necrotic lesions or dark spots"
            ),
            whatToDo = "Keep plant in bright, indirect sunlight. Water thoroughly only when the top 2 inches of soil feel dry to the touch. Occasionally wipe leaves with a damp cloth to remove household dust.",
            prevention = listOf(
                "Ensure pot has drainage holes to prevent root waterlogging",
                "Avoid letting water sit in the saucer below the pot",
                "Rotate pot periodically so all vines receive balanced light"
            ),
            precautions = listOf(
                "Money plant sap contains insoluble calcium oxalate crystals; keep out of reach of household pets (cats and dogs).",
                "Wash hands after pruning vines."
            ),
            sources = listOf(
                "University of Florida IFAS Extension: Cultural Care of Golden Pothos",
                "Royal Horticultural Society: Epipremnum Houseplant Profile",
                "ASPCA Animal Poison Control Database: Pothos Toxic Principles"
            )
        ),

        PlantCondition(
            id = "money_plant_bacterial_wilt",
            plantType = PlantType.MONEY_PLANT,
            classKey = "MoneyPlant_BacterialWilt",
            displayName = "Bacterial Wilt / Stem & Leaf Rot",
            category = ConditionCategory.BACTERIAL,
            description = "A destructive vascular condition caused by bacterial pathogens (such as Ralstonia or Pectobacterium) flourishing in warm, poorly aerated, or waterlogged media.",
            symptoms = listOf(
                "Dark water-soaked, translucent lesions near petiole base",
                "Rapid sudden wilting or collapse of individual leaves even in damp soil",
                "Soft, slimy texture on decaying leaf stems, occasionally with a sour odor"
            ),
            whatToDo = "Immediately isolate the plant from other foliage. Prune off affected leaves and rotten petioles with clean shears sterilized with 70% isopropyl alcohol. Allow potting mix to dry out significantly, or refresh with sterile well-draining aroid soil.",
            prevention = listOf(
                "Never leave water standing stagnant in hydroponic jars; refresh water weekly",
                "Avoid overhead misting that leaves wet film on petiole junctions",
                "Use perlite and orchid bark to ensure porous root aeration"
            ),
            precautions = listOf(
                "Do not compost infected foliage; discard in household trash to avoid contaminating garden soil.",
                "Sterilize cutting tools before and after each use."
            ),
            sources = listOf(
                "Penn State Extension: Bacterial Diseases of Foliage Plants",
                "University of California IPM: Houseplant Diseases and Environmental Disorders"
            )
        ),

        PlantCondition(
            id = "money_plant_manganese_toxicity",
            plantType = PlantType.MONEY_PLANT,
            classKey = "MoneyPlant_ManganeseToxicity",
            displayName = "Manganese / Micronutrient Toxicity",
            category = ConditionCategory.ABIOTIC,
            description = "Foliar stress symptom resulting from excessive accumulation of manganese or soluble mineral salts, frequently triggered by overly acidic potting soil (pH < 5.0) or over-fertilization.",
            symptoms = listOf(
                "Interveinal chlorosis (yellowing between primary leaf veins) on mature foliage",
                "Scattered minute dark brown, purplish, or black necrotic pinpoint specks",
                "Marginal leaf scorch and premature drop of older lower leaves"
            ),
            whatToDo = "Thoroughly flush the potting soil with generous amounts of clean, room-temperature distilled or filtered water to leach out accumulated fertilizer salts. Suspend all synthetic fertilizer applications for at least 4 to 6 weeks.",
            prevention = listOf(
                "Dilute indoor fertilizers to one-quarter or half the manufacturer recommendation",
                "Avoid using excessively acidic potting amendments",
                "Repot every 18-24 months in fresh balanced indoor potting soil (pH 6.0–6.5)"
            ),
            precautions = listOf(
                "Avoid applying lime or drastic pH adjusters without testing soil pH first.",
                "Do not mistake micronutrient toxicity for fungal spot; chemical fungicides will not resolve mineral imbalance."
            ),
            sources = listOf(
                "University of Maryland Extension: Plant Nutrient Deficiencies and Toxicities",
                "North Carolina State University Extension: Mineral Nutrition in Ornamental Houseplants"
            )
        ),

        // ==================== SNAKE PLANT (3 classes) ====================
        PlantCondition(
            id = "snake_plant_healthy",
            plantType = PlantType.SNAKE_PLANT,
            classKey = "SnakePlant_Healthy",
            displayName = "Healthy Snake Plant Blade",
            category = ConditionCategory.HEALTHY,
            description = "Stiff, erect, sword-shaped succulent foliage with distinctive cross-banded variegated patterns, firm leathery texture, and intact pointed tip.",
            symptoms = listOf(
                "Sturdy upright posture without leaning or bending",
                "Firm, crisp succulent blade that resists bending",
                "Clean green surface with sharp, defined yellow or silver marbling"
            ),
            whatToDo = "Provide bright indirect to moderate low sunlight. Water sparingly—allow the potting mix to dry completely through the bottom of the container between waterings (typically every 2 to 4 weeks depending on ambient humidity).",
            prevention = listOf(
                "Use gritty cactus or succulent potting mix with pumice or coarse sand",
                "Keep water out of the central crown rosette to prevent crown rot",
                "Ensure container has a functional bottom drainage hole"
            ),
            precautions = listOf(
                "Toxic to dogs and cats if chewed or ingested (saponins).",
                "Cold sensitive: keep above 10°C (50°F) in winter."
            ),
            sources = listOf(
                "Missouri Botanical Garden Plant Finder: Dracaena trifasciata",
                "Royal Horticultural Society: Sansevieria Cultivation Guide"
            )
        ),

        PlantCondition(
            id = "snake_plant_anthracnose",
            plantType = PlantType.SNAKE_PLANT,
            classKey = "SnakePlant_Anthracnose",
            displayName = "Anthracnose Leaf Spot",
            category = ConditionCategory.FUNGAL,
            description = "Fungal infection (commonly Colletotrichum sansevieriae) that attacks succulent foliage during periods of prolonged surface moisture and poor ventilation.",
            symptoms = listOf(
                "Sunken, circular to oblong reddish-brown or dark lesions on the blade",
                "Lesions often surrounded by a noticeable pale yellow chlorotic border",
                "Affected areas turn dry, papery, or develop tiny dark fungal spore cushions"
            ),
            whatToDo = "Isolate the plant. Carefully excise infected leaf segments using sharp, sterilized shears cut down into healthy tissue. Keep the remaining leaf blade strictly dry, and increase room air circulation.",
            prevention = listOf(
                "Never mist or spray water directly onto snake plant leaves",
                "Always water directly into the soil perimeter, keeping blades dry",
                "Maintain adequate spacing between indoor pots"
            ),
            precautions = listOf(
                "Disinfect shears between each cut with rubbing alcohol to avoid transferring fungal spores.",
                "Discard excised leaf pieces in the garbage, not compost."
            ),
            sources = listOf(
                "University of Florida IFAS Extension: Anthracnose on Sansevieria",
                "Clemson University Cooperative Extension: Houseplant Fungal Leaf Spots"
            )
        ),

        PlantCondition(
            id = "snake_plant_leaf_withering",
            plantType = PlantType.SNAKE_PLANT,
            classKey = "SnakePlant_LeafWithering",
            displayName = "Leaf Withering / Dehydration or Root Rot",
            category = ConditionCategory.ABIOTIC,
            description = "Loss of cellular turgidity in the succulent leaf blade leading to shriveling, puckering, thinning, or leaning over.",
            symptoms = listOf(
                "Thinning, wrinkly or puckered vertical lines along the leaf blade",
                "Blades feel rubbery, soft, or bend under their own weight",
                "Tips curl inward and lose their rigid upright structure"
            ),
            whatToDo = "Inspect the root system: (A) If roots are white/firm and soil is dry, the plant is dehydrated—give a thorough bottom soak. (B) If roots are brown/mushy and soil is wet, the plant has root rot from overwatering—trim rotting roots, let rhizome dry in open air for 24 hours, and repot in fresh dry gritty succulent mix.",
            prevention = listOf(
                "Strictly follow the soak-and-dry method for succulents",
                "Never water on a rigid calendar schedule; always check soil moisture first",
                "Use unglazed terracotta pots which promote moisture evaporation"
            ),
            precautions = listOf(
                "Never apply fertilizer to a withering plant; fertilizer salts will burn damaged roots.",
                "Be patient—succulents rehydrate slowly over several weeks."
            ),
            sources = listOf(
                "Iowa State University Extension: Diagnosing Houseplant Leaf Withering and Rot",
                "University of Illinois Extension: Sansevieria Culture and Problems"
            )
        ),

        // ==================== SPIDER PLANT (3 classes) ====================
        PlantCondition(
            id = "spider_plant_healthy",
            plantType = PlantType.SPIDER_PLANT,
            classKey = "SpiderPlant_Healthy",
            displayName = "Healthy Spider Plant Foliage",
            category = ConditionCategory.HEALTHY,
            description = "Gracefully arching, linear ribbon-like leaves exhibiting crisp cream/white variegation bordered by lively green, firm turgor, and clean margins.",
            symptoms = listOf(
                "Supple arching foliage without wilting or curling",
                "Clean leaf tips without dark brown necrotic burn",
                "Active production of stolons (runners) with baby plantlets ('spiderettes')"
            ),
            whatToDo = "Place in bright indirect light or gentle morning sun. Maintain evenly moist soil during spring and summer, tapering off slightly in winter. Mist occasionally if ambient air is very dry.",
            prevention = listOf(
                "Use filtered, distilled, or rested rainwater if tap water has high mineral content",
                "Fertilize with half-strength balanced houseplant food monthly during growing season"
            ),
            precautions = listOf(
                "Completely non-toxic and pet-safe for cats and dogs (certified safe by ASPCA).",
                "Cats are sometimes attracted to spider plant foliage due to mild hallucinogenic compounds similar to catnip."
            ),
            sources = listOf(
                "University of Minnesota Extension: Growing Spider Plants Indoors",
                "ASPCA Non-Toxic Plants List: Chlorophytum comosum"
            )
        ),

        PlantCondition(
            id = "spider_plant_fungal_leaf_spot",
            plantType = PlantType.SPIDER_PLANT,
            classKey = "SpiderPlant_FungalLeafSpot",
            displayName = "Fungal Leaf Spot",
            category = ConditionCategory.FUNGAL,
            description = "Localized foliar lesions caused by opportunistic fungal spores (such as Alternaria, Cercospora, or Phoma) settling on wet foliage under stagnant indoor air.",
            symptoms = listOf(
                "Small round to elliptical grayish-tan or brown spots along the leaf blade",
                "Dark reddish-brown to blackish perimeter ring surrounding each spot",
                "Surrounding leaf tissue may display a yellow chlorotic halo"
            ),
            whatToDo = "Snip off heavily spotted leaves at the crown base using clean shears. Move the plant to an area with improved air circulation and lower relative humidity. Water strictly into the soil, keeping the foliage dry.",
            prevention = listOf(
                "Avoid wetting foliage when watering; use a narrow-spout watering can",
                "Do not overcrowd potted plants on windowsills",
                "Water early in the day so accidental droplets evaporate before night"
            ),
            precautions = listOf(
                "Avoid indoor chemical fungicide sprays; physical sanitation and cultural airflow control are safer and more effective for home plants.",
                "Discard trimmed leaves."
            ),
            sources = listOf(
                "Purdue University Plant and Pest Diagnostic Laboratory: Spider Plant Leaf Spots",
                "University of Wisconsin-Madison Extension: Fungal Leaf Spot Management in Houseplants"
            )
        ),

        PlantCondition(
            id = "spider_plant_leaf_tip_necrosis",
            plantType = PlantType.SPIDER_PLANT,
            classKey = "SpiderPlant_LeafTipNecrosis",
            displayName = "Leaf Tip Necrosis / Tip Burn",
            category = ConditionCategory.ABIOTIC,
            description = "Classic tip browning in spider plants caused by accumulated fluoride, chlorine, sodium, or soluble fertilizer salts translocated to leaf extremities.",
            symptoms = listOf(
                "Dark brown, crispy, dry tips on the ends of arching leaves",
                "Sharp demarcation between dead brown tip and live green leaf tissue",
                "Absence of spreading circular fungal rings or fungal spore bodies"
            ),
            whatToDo = "Neatly trim away dry brown tips with clean scissors, angling the cuts to mimic the natural pointed shape of the leaf. Switch to filtered water, distilled water, or collected rainwater. Flush the pot with clean water to rinse accumulated salts.",
            prevention = listOf(
                "Avoid city tap water with high fluoride or chlorine additives",
                "Avoid potting soils that incorporate fluoridated perlite or high-phosphate fertilizers",
                "Keep soil consistently slightly moist rather than alternating between bone dry and waterlogged"
            ),
            precautions = listOf(
                "When trimming brown tips, leave a tiny 1-millimeter sliver of brown to avoid cutting into live green tissue, which can prompt new tip browning.",
                "Trimming is cosmetic and does not harm the plant."
            ),
            sources = listOf(
                "University of Maryland Extension: Diagnosing Leaf Tip Burn on Indoor Spider Plants",
                "North Carolina State University Extension: Houseplant Fluoride and Salt Sensitivity"
            )
        ),

        // ==================== ROSE (4 classes) ====================
        PlantCondition(
            id = "rose_healthy",
            plantType = PlantType.ROSE,
            classKey = "Rose_Healthy",
            displayName = "Healthy Rose Foliage",
            category = ConditionCategory.HEALTHY,
            description = "Pinnately compound, dark green serrated leaflets displaying smooth epidermal luster, firm turgor, and sturdy attachment to thorny canes.",
            symptoms = listOf(
                "Deep green glossy leaflets with well-defined serrated margins",
                "Absence of powdery coatings, black spots, or viral chlorotic zig-zags",
                "Active vegetative shoots and healthy flower bud formation"
            ),
            whatToDo = "Ensure rose bush receives at least 6 hours of direct sunlight daily. Water deeply at the soil base in the morning. Apply a 2-inch layer of organic mulch around the root zone, keeping mulch 3 inches away from the main cane.",
            prevention = listOf(
                "Prune deadwood and thin out center branches each spring to promote airflow",
                "Irrigate soil at the root line, never wet leaves with overhead sprinklers"
            ),
            precautions = listOf(
                "Wear thick gardening gloves when pruning to protect hands from thorns.",
                "Clean pruning shears between rose bushes."
            ),
            sources = listOf(
                "American Rose Society: Fundamentals of Rose Care and Culture",
                "Cornell University Cooperative Extension: Rose Gardening Guide"
            )
        ),

        PlantCondition(
            id = "rose_mosaic_virus",
            plantType = PlantType.ROSE,
            classKey = "Rose_MosaicVirus",
            displayName = "Rose Mosaic Virus",
            category = ConditionCategory.VIRAL,
            description = "A systemic viral infection (PNRSV / ApMV complex) transmitted during propagation via infected rootstock or budding, resulting in distinctive foliar variegation patterns.",
            symptoms = listOf(
                "Wavy bright yellow lines, rings, zig-zag patterns, or 'oak-leaf' chlorosis",
                "Symptoms typically appear on spring flush and may fade in hot summer heat",
                "Affected leaflets generally retain their shape without severe distortion"
            ),
            whatToDo = "There is no chemical cure for rose mosaic virus. The infected bush will continue to live and bloom, though its vigor and winter hardiness may be mildly decreased. Maintain good cultural care (watering, balanced feeding). Do not take cuttings from this plant for propagation.",
            prevention = listOf(
                "Purchase only certified virus-indexed rose cultivars",
                "Sterilize pruning tools between bushes to maintain high hygiene"
            ),
            precautions = listOf(
                "Does NOT spread through casual garden contact, pruning shears, or insect pests; spreads almost exclusively through vegetative grafting.",
                "No need to destroy the bush if its landscape appearance remains pleasing."
            ),
            sources = listOf(
                "University of California Statewide IPM Program: Rose Mosaic Disease",
                "Texas A&M AgriLife Extension: Plant Pathology on Rose Viruses"
            )
        ),

        PlantCondition(
            id = "rose_pest_damage",
            plantType = PlantType.ROSE,
            classKey = "Rose_PestDamage",
            displayName = "Pest Damage (Sawflies, Aphids, Thrips)",
            category = ConditionCategory.PEST,
            description = "Foliar damage caused by common rose pests, most notably rose slugs (sawfly larvae) skeletonizing foliage or aphids sucking tender sap from new shoots.",
            symptoms = listOf(
                "'Windowpane' skeletonized leaves where epidermal tissue is eaten, leaving only translucent veins",
                "Clusters of soft-bodied green or pink aphids on tender shoots and buds",
                "Curled, puckered new leaves and sticky honeydew film on lower foliage"
            ),
            whatToDo = "Inspect the underside of leaves and dislodge sawfly larvae by hand or with a firm water spray. Apply organic insecticidal soap or diluted cold-pressed neem oil spray in the late afternoon to target active pests.",
            prevention = listOf(
                "Encourage beneficial predators such as ladybugs, hoverflies, and lacewings",
                "Check new spring growth twice weekly to catch pest colonies early"
            ),
            precautions = listOf(
                "Never spray horticultural oil or soap in the direct heat of midday sun (causes foliar leaf burn).",
                "Avoid broad-spectrum synthetic insecticides which destroy beneficial pollinating insects."
            ),
            sources = listOf(
                "University of Wisconsin-Madison Extension: Rose Slug Sawfly Management",
                "Missouri Botanical Garden IPM: Aphids and Insect Pests on Roses"
            )
        ),

        PlantCondition(
            id = "rose_botrytis_blight",
            plantType = PlantType.ROSE,
            classKey = "Rose_BotrytisBlight",
            displayName = "Botrytis Blight / Gray Mold",
            category = ConditionCategory.FUNGAL,
            description = "An aggressive fungal disease caused by Botrytis cinerea that flourishes during cool, overcast, damp weather, attacking flower buds, blooms, and tender foliage.",
            symptoms = listOf(
                "Fuzzy grayish-brown mold growth on developing flower buds and petals",
                "Flower buds fail to open, turn brown, and rot ('balling')",
                "Water-soaked brown spots on petals and tender leaves with stem cankers"
            ),
            whatToDo = "Promptly prune off and bag all infected fuzzy flower buds, spent blooms, and diseased shoots. Sanitize shears between cuts. Increase air circulation through the canopy by thinning crowded internal branches.",
            prevention = listOf(
                "Always water roses at ground level early in the morning so foliage dries quickly",
                "Regularly remove spent blossoms (deadheading) before petals drop and decay",
                "Space rose bushes generously to ensure swift wind penetration"
            ),
            precautions = listOf(
                "Seal infected cuttings inside a trash bag immediately; shaking infected branches releases clouds of airborne fungal spores.",
                "Do not add Botrytis-infected debris to compost piles."
            ),
            sources = listOf(
                "Ohio State University Extension: Botrytis Blight of Ornamental Plants",
                "University of Kentucky Plant Pathology: Botrytis Blight of Rose"
            )
        ),

        // ==================== MARIGOLD (3 classes) ====================
        PlantCondition(
            id = "marigold_healthy",
            plantType = PlantType.MARIGOLD,
            classKey = "Marigold_Healthy",
            displayName = "Healthy Marigold Foliage",
            category = ConditionCategory.HEALTHY,
            description = "Finely pinnatisect (feather-like) dark green foliage with pungent natural aromatic oils, clean leaf edges, and sturdy branching stems.",
            symptoms = listOf(
                "Deep green dissected leaflets with crisp margins",
                "Firm upright stems with active flower bud formation",
                "Characteristic aromatic scent when foliage is gently brushed"
            ),
            whatToDo = "Provide full sun (at least 6 hours of direct sunlight daily). Water deeply once or twice a week when top inch of soil is dry. Regularly pinch off faded blossoms to stimulate continuous flowering.",
            prevention = listOf(
                "Plant in well-draining garden soil or containers with drainage holes",
                "Space plants 8 to 12 inches apart to prevent stagnant humidity pockets"
            ),
            precautions = listOf(
                "Marigold foliage sap can cause mild contact skin irritation (dermatitis) in sensitive individuals; wash hands after pruning.",
                "Non-toxic to pets according to ASPCA."
            ),
            sources = listOf(
                "National Garden Bureau: Year of the Marigold Cultivation Guide",
                "Clemson University Cooperative Extension: Marigold Plant Care"
            )
        ),

        PlantCondition(
            id = "marigold_alternaria_leaf_blight",
            plantType = PlantType.MARIGOLD,
            classKey = "Marigold_AlternariaLeafBlight",
            displayName = "Alternaria Leaf Blight",
            category = ConditionCategory.FUNGAL,
            description = "A widespread foliar fungal disease (Alternaria tagetica) that spreads rapidly during warm, wet, rainy weather, causing extensive blighting and defoliation.",
            symptoms = listOf(
                "Small dark brown to black circular lesions on lower leaves first",
                "Lesions expand and develop concentric target-like rings with yellow halos",
                "Heavily infected leaflets turn brown, wither, and collapse along the stem"
            ),
            whatToDo = "Strip off heavily spotted lower leaves immediately. Ensure foliage remains completely dry when irrigating. If damp weather persists, spray preventative copper biofungicide according to label instructions.",
            prevention = listOf(
                "Water strictly at the soil line using a watering can or soaker hose",
                "Space marigolds 10-12 inches apart to encourage rapid leaf drying after rain",
                "Rotate planting beds each year and remove all dead annual debris at season end"
            ),
            precautions = listOf(
                "Do not leave dead blighted leaves on the soil surface; fungal spores overwinter in plant debris.",
                "Clean garden tools after working with blighted plants."
            ),
            sources = listOf(
                "University of Massachusetts Amherst Greenhouse Crops: Alternaria Blight on Tagetes",
                "University of Georgia Extension: Commercial and Garden Marigold Diseases"
            )
        ),

        PlantCondition(
            id = "marigold_pest_damage",
            plantType = PlantType.MARIGOLD,
            classKey = "Marigold_PestDamage",
            displayName = "Pest Damage (Spider Mites, Slugs, Thrips)",
            category = ConditionCategory.PEST,
            description = "Damage from common garden pests, especially two-spotted spider mites during hot dry weather, or nocturnal slugs chewing ragged holes.",
            symptoms = listOf(
                "Fine yellow or pale speckled stippling across upper leaf surfaces",
                "Delicate silky micro-webbing in leaf axils and beneath flower heads",
                "Irregular ragged chew holes on leaves and petals from caterpillars or slugs"
            ),
            whatToDo = "Blast foliage with a focused water spray (especially leaf undersides) to dislodge spider mites. Apply organic insecticidal soap or diluted cold-pressed neem oil in the cool evening. For slugs, handpick at night or place shallow beer traps.",
            prevention = listOf(
                "Keep plants well-watered during summer heat (drought stress invites spider mites)",
                "Avoid over-fertilizing with high-nitrogen fertilizers which creates succulent pest-prone growth"
            ),
            precautions = listOf(
                "Always test any horticultural soap spray on one lower leaf 24 hours prior to full application.",
                "Avoid spraying blooms to protect pollinating bees."
            ),
            sources = listOf(
                "Texas A&M AgriLife Extension: Managing Spider Mites on Garden Ornamentals",
                "University of California IPM: Tagetes Insect and Mite Management"
            )
        )
    ).associateBy { it.classKey }

    fun getCondition(classKey: String): PlantCondition? = CONDITIONS[classKey]

    fun getConditionsForPlant(plantType: PlantType): List<PlantCondition> =
        CONDITIONS.values.filter { it.plantType == plantType }
}

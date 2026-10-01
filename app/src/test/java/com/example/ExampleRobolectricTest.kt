package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.PlantConditionRegistry
import com.example.data.PlantGuideData
import com.example.ml.EcoLeafModelManager
import com.example.model.PlantType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("EcoLeaf", appName)
  }

  @Test
  fun `verify five separate ornamental plant models defined`() {
    val plants = PlantType.entries
    assertEquals(5, plants.size)
    assertTrue(plants.any { it == PlantType.MONEY_PLANT })
    assertTrue(plants.any { it == PlantType.SNAKE_PLANT })
    assertTrue(plants.any { it == PlantType.SPIDER_PLANT })
    assertTrue(plants.any { it == PlantType.ROSE })
    assertTrue(plants.any { it == PlantType.MARIGOLD })
  }

  @Test
  fun `condition registry covers all 16 classes of all 5 plants`() {
    PlantType.entries.forEach { plant ->
      val conditions = PlantConditionRegistry.getConditionsForPlant(plant)
      assertTrue("Plant ${plant.commonName} should have conditions", conditions.isNotEmpty())
      plant.defaultClasses.forEach { classKey ->
        val cond = PlantConditionRegistry.getCondition(classKey)
        assertNotNull("Condition for classKey $classKey should exist", cond)
        assertTrue(cond!!.symptoms.isNotEmpty())
        assertTrue(cond.whatToDo.isNotBlank())
        assertTrue(cond.prevention.isNotEmpty())
        assertTrue(cond.precautions.isNotEmpty())
        assertTrue(cond.sources.isNotEmpty())
      }
    }
  }

  @Test
  fun `plant guide data contains educational profiles for all 5 plants`() {
    PlantType.entries.forEach { plant ->
      val guide = PlantGuideData.getGuide(plant)
      assertNotNull("Guide for ${plant.commonName} must exist", guide)
      assertTrue(guide.wateringGuide.isNotBlank())
      assertTrue(guide.lightRequirement.isNotBlank())
      assertTrue(guide.soilAndPotting.isNotBlank())
      assertTrue(guide.temperatureRange.isNotBlank())
      assertTrue(guide.toxicityInfo.isNotBlank())
      assertTrue(guide.commonMistakes.isNotEmpty())
    }
  }

  @Test
  fun `model manager reports status for each plant model`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val modelManager = EcoLeafModelManager(context)
    PlantType.entries.forEach { plant ->
      val status = modelManager.getModelStatus(plant)
      assertNotNull(status)
      assertEquals(plant, status.plantType)
      assertTrue(status.labels.isNotEmpty())
    }
  }
}

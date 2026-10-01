package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.model.PlantType
import com.example.sprig.engine.SprigEngine
import com.example.sprig.model.SprigScanContext
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SprigEngineTest {

    private lateinit var context: Application
    private lateinit var engine: SprigEngine

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        engine = SprigEngine(context)
    }

    @Test
    fun testInitializationAndStatus() = runBlocking {
        val status = engine.initialize()
        assertTrue(status.isModelAvailable)
        assertTrue(status.isInitialized)
        assertTrue(status.verifiedFactsCount > 0)
    }

    @Test
    fun testExplainScanResultGrounded() = runBlocking {
        val scanContext = SprigScanContext(
            plantType = PlantType.MONEY_PLANT,
            conditionKey = "MoneyPlant_BacterialWilt",
            conditionDisplayName = "Bacterial Wilt",
            confidence = 0.91f,
            symptoms = listOf("Water-soaked lesions", "Leaf yellowing"),
            whatToDo = "Prune infected leaves and reduce watering."
        )

        val response = engine.generateResponse(
            userMessage = "What does my scan result mean?",
            conversationHistory = emptyList(),
            scanContext = scanContext
        )

        assertNotNull(response.text)
        // Must explain match without claiming absolute definitive clinical diagnosis
        assertTrue(response.text.contains("most closely matched", ignoreCase = true))
        assertTrue(response.text.contains("Bacterial Wilt", ignoreCase = true))
        assertFalse(response.text.contains("definitely has", ignoreCase = true))
    }

    @Test
    fun testWateringQueryGrounded() = runBlocking {
        val response = engine.generateResponse(
            userMessage = "How often should I water my Snake Plant?",
            conversationHistory = emptyList(),
            scanContext = null
        )

        assertNotNull(response.text)
        assertTrue(response.text.contains("Water", ignoreCase = true))
        assertTrue(response.text.contains("Snake Plant", ignoreCase = true))
    }
}

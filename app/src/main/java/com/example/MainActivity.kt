package com.example

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.PlantType
import com.example.ui.components.FloatingPillNavBar
import com.example.ui.screens.CapturePreviewScreen
import com.example.ui.screens.DiagnosisResultScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PlantGuideScreen
import com.example.ui.screens.PlantSelectionScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SprigChatScreen
import com.example.ui.theme.EcoLeafTheme
import com.example.viewmodel.EcoLeafScreen
import com.example.viewmodel.EcoLeafViewModel
import java.io.File

class MainActivity : ComponentActivity() {

    private val viewModel: EcoLeafViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoLeafTheme {
                EcoLeafApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun EcoLeafApp(viewModel: EcoLeafViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val selectedPlant by viewModel.selectedPlant.collectAsStateWithLifecycle()
    val currentBitmap by viewModel.currentBitmap.collectAsStateWithLifecycle()
    val qualityResult by viewModel.qualityResult.collectAsStateWithLifecycle()
    val isAnalyzing by viewModel.isAnalyzing.collectAsStateWithLifecycle()
    val predictionResult by viewModel.predictionResult.collectAsStateWithLifecycle()
    val conditionDetail by viewModel.currentConditionDetail.collectAsStateWithLifecycle()
    val confidenceThreshold by viewModel.confidenceThreshold.collectAsStateWithLifecycle()
    val savedRecordId by viewModel.currentSavedRecordId.collectAsStateWithLifecycle()
    val recentScans by viewModel.scanHistory.collectAsStateWithLifecycle()
    val modelStatuses by viewModel.modelStatuses.collectAsStateWithLifecycle()

    val sprigMessages by viewModel.sprigMessages.collectAsStateWithLifecycle()
    val isSprigThinking by viewModel.isSprigThinking.collectAsStateWithLifecycle()
    val sprigScanContext by viewModel.sprigScanContext.collectAsStateWithLifecycle()
    val sprigEngineStatus by viewModel.sprigEngineStatus.collectAsStateWithLifecycle()

    val showFloatingBar = currentScreen == EcoLeafScreen.HOME ||
            currentScreen == EcoLeafScreen.PLANT_SELECT ||
            currentScreen == EcoLeafScreen.SPRIG ||
            currentScreen == EcoLeafScreen.HISTORY

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    EcoLeafScreen.HOME -> {
                        HomeScreen(
                            recentScans = recentScans,
                            onStartScan = {
                                viewModel.navigateTo(EcoLeafScreen.PLANT_SELECT)
                            },
                            onUploadImage = {
                                viewModel.navigateTo(EcoLeafScreen.PLANT_SELECT)
                            },
                            onSelectPlant = { plant ->
                                viewModel.selectPlant(plant)
                            },
                            onOpenGuide = { plant ->
                                viewModel.openPlantGuide(plant)
                            },
                            onNavigate = { targetScreen ->
                                if (targetScreen == EcoLeafScreen.SPRIG) {
                                    viewModel.openSprigTab()
                                } else {
                                    viewModel.navigateTo(targetScreen)
                                }
                            },
                            onSelectScan = { record ->
                                val file = File(record.imageUri)
                                if (file.exists()) {
                                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                    if (bitmap != null) {
                                        val plantType = PlantType.fromId(record.plantId)
                                        viewModel.selectPlant(plantType)
                                        viewModel.onImageSelected(bitmap)
                                    }
                                }
                            }
                        )
                    }
                    EcoLeafScreen.PLANT_SELECT -> {
                        PlantSelectionScreen(
                            onBack = {
                                viewModel.navigateTo(EcoLeafScreen.HOME)
                            },
                            onPlantSelected = { plant ->
                                viewModel.selectPlant(plant)
                            }
                        )
                    }
                    EcoLeafScreen.CAPTURE_PREVIEW -> {
                        val activePlant = selectedPlant ?: PlantType.MONEY_PLANT
                        CapturePreviewScreen(
                            plantType = activePlant,
                            bitmap = currentBitmap,
                            qualityResult = qualityResult,
                            onBack = {
                                viewModel.navigateTo(EcoLeafScreen.PLANT_SELECT)
                            },
                            onCapturePhoto = { bitmap ->
                                viewModel.onImageSelected(bitmap)
                            },
                            onPickImageUri = { uri ->
                                viewModel.onUriSelected(uri)
                            },
                            onAnalyze = {
                                viewModel.analyzeLeaf()
                            },
                            onRetake = {
                                viewModel.retakePhoto()
                            },
                            onChooseAnotherPlant = {
                                viewModel.chooseAnotherPlant()
                            }
                        )
                    }
                    EcoLeafScreen.DIAGNOSIS_RESULT -> {
                        val activePlant = selectedPlant ?: PlantType.MONEY_PLANT
                        DiagnosisResultScreen(
                            plantType = activePlant,
                            bitmap = currentBitmap,
                            isAnalyzing = isAnalyzing,
                            predictionResult = predictionResult,
                            conditionDetail = conditionDetail,
                            confidenceThreshold = confidenceThreshold,
                            savedRecordId = savedRecordId,
                            onBack = {
                                viewModel.navigateTo(EcoLeafScreen.HOME)
                            },
                            onSaveNotes = { notes ->
                                viewModel.saveNotes(notes)
                            },
                            onOpenGuide = { plant ->
                                viewModel.openPlantGuide(plant)
                            },
                            onAskSprig = {
                                viewModel.openSprigWithScanContext(
                                    activePlant,
                                    predictionResult,
                                    conditionDetail
                                )
                            },
                            onScanAgain = {
                                viewModel.retakePhoto()
                            }
                        )
                    }
                    EcoLeafScreen.SPRIG -> {
                        SprigChatScreen(
                            messages = sprigMessages,
                            isThinking = isSprigThinking,
                            scanContext = sprigScanContext,
                            onSendMessage = { text ->
                                viewModel.sendSprigMessage(text)
                            },
                            onClearContext = {
                                viewModel.clearSprigScanContext()
                            },
                            onNewChat = {
                                viewModel.resetSprigChat()
                            },
                            onBack = {
                                viewModel.navigateTo(EcoLeafScreen.HOME)
                            }
                        )
                    }
                    EcoLeafScreen.PLANT_GUIDE -> {
                        PlantGuideScreen(
                            initialPlant = selectedPlant,
                            onBack = {
                                viewModel.navigateTo(EcoLeafScreen.HOME)
                            },
                            onStartScanForPlant = { plant ->
                                viewModel.selectPlant(plant)
                            }
                        )
                    }
                    EcoLeafScreen.HISTORY -> {
                        HistoryScreen(
                            scans = recentScans,
                            onBack = {
                                viewModel.navigateTo(EcoLeafScreen.HOME)
                            },
                            onSelectScan = { record ->
                                val file = File(record.imageUri)
                                if (file.exists()) {
                                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                    if (bitmap != null) {
                                        val plantType = PlantType.fromId(record.plantId)
                                        viewModel.selectPlant(plantType)
                                        viewModel.onImageSelected(bitmap)
                                    }
                                }
                            },
                            onToggleFavorite = { record ->
                                viewModel.toggleFavorite(record)
                            },
                            onDeleteScan = { record ->
                                viewModel.deleteScan(record)
                            },
                            onClearAll = {
                                viewModel.clearAllHistory()
                            }
                        )
                    }
                    EcoLeafScreen.SETTINGS, EcoLeafScreen.MODEL_STATUS -> {
                        SettingsScreen(
                            confidenceThreshold = confidenceThreshold,
                            modelStatuses = modelStatuses,
                            sprigStatus = sprigEngineStatus,
                            onThresholdChange = { threshold ->
                                viewModel.setConfidenceThreshold(threshold)
                            },
                            onClearAllHistory = {
                                viewModel.clearAllHistory()
                            },
                            onImportModel = { plant, tfliteUri, labelsUri ->
                                viewModel.importCustomModel(plant, tfliteUri, labelsUri)
                            },
                            onBack = {
                                viewModel.navigateTo(EcoLeafScreen.HOME)
                            }
                        )
                    }
                }
            }

            // Signature Floating Pill Navigation Bar
            if (showFloatingBar) {
                FloatingPillNavBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        if (screen == EcoLeafScreen.SPRIG) {
                            viewModel.openSprigTab()
                        } else {
                            viewModel.navigateTo(screen)
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

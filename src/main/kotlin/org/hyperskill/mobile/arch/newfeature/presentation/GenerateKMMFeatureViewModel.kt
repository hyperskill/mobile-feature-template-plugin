package org.hyperskill.mobile.arch.newfeature.presentation

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.util.application
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.hyperskill.mobile.arch.newfeature.domain.FeatureParams
import org.hyperskill.mobile.arch.newfeature.domain.GenerateKMMFeatureInteractor

class GenerateKMMFeatureViewModel(
    private val generateKMMFeatureInteractor: GenerateKMMFeatureInteractor,
    private val editorManager: FileEditorManager,
    private val scope: CoroutineScope
) {

    var featureName: String = ""
    var useViewState: Boolean = true
    var supportAndroidMain: Boolean = false
    var supportCompose: Boolean = false
    var createFeaturePackage: Boolean = true

    val successFlow = MutableSharedFlow<Unit>()

    fun onOkButtonClick() {
        val featureParams = FeatureParams(
            featureName = prepareFeatureName(featureName),
            useViewState = useViewState,
            supportAndroidMain = supportAndroidMain,
            supportCompose = supportCompose
        )
        application.runWriteAction {
            val featureFile = generateKMMFeatureInteractor.generateFeature(
                featureParams = featureParams,
                createFeaturePackage = createFeaturePackage
            )
            editorManager.openFile(featureFile.virtualFile, true)
            scope.launch {
                successFlow.emit(Unit)
            }
        }
    }

    private fun prepareFeatureName(inputFeatureName: String): String =
        inputFeatureName
            .replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase(Locale.getDefault())
                } else {
                    it.toString()
                }
            }
}
package org.hyperskill.mobile.arch.newfeature.ui

import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.DEFAULT_COMMENT_WIDTH
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.mobile.arch.newfeature.presentation.GenerateKMMFeatureViewModel

class GenerateKMMFeatureDialog(
    private val viewModel: GenerateKMMFeatureViewModel,
    private val scope: CoroutineScope
) : DialogWrapper(true) {

    init {
        init()
        viewModel
            .successFlow
            .onEach { close(0) }
            .launchIn(scope)
    }

    private var panel: DialogPanel? = null

    override fun createCenterPanel(): JComponent {
        panel = createPanel()
        val panel = createPanel()
        this.panel = panel
        return panel
    }

    private fun createPanel(): DialogPanel {
        return panel {
            row { label("New KMM Feature") }
            row {
                textField()
                    .focused()
                    .bindText(viewModel::featureName)
                    .align(Align.FILL)
            }
            row {
                text(
                    "Creates a set of files for the new KMM Feature",
                    maxLineLength = DEFAULT_COMMENT_WIDTH
                )
            }
            group("Options") {
                row {
                    checkBox("Create package for the feature")
                        .bindSelected(viewModel::createFeaturePackage)

                }
                row {
                    checkBox("Use viewState")
                        .bindSelected(viewModel::useViewState)
                }
                /*group("Android") {
                    row {
                        checkBox("Support Android")
                            .bindSelected(viewModel::supportAndroidMain)
                        checkBox("Support compose")
                            .bindSelected(viewModel::supportCompose)
                    }
                }*/
            }
        }
    }

    override fun doOKAction() {
        panel?.apply()
        viewModel.onOkButtonClick()
    }

    override fun dispose() {
        super.dispose()
        scope.cancel()
    }
}
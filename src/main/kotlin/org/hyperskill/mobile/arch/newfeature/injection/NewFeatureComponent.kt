package org.hyperskill.mobile.arch.newfeature.injection

import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.swing.Swing
import org.hyperskill.mobile.arch.core.TemplateGenerator
import org.hyperskill.mobile.arch.newfeature.domain.GenerateKMMFeatureInteractor
import org.hyperskill.mobile.arch.newfeature.presentation.GenerateKMMFeatureViewModel

class GenerateKMMFeatureComponent(
    project: Project,
    directory: PsiDirectory
) {

    private val generateKMMFeatureInteractor: GenerateKMMFeatureInteractor =
        GenerateKMMFeatureInteractor(
            directory = directory,
            generator = TemplateGenerator(project = project)
        )

    private val editorManager: FileEditorManager = FileEditorManager.getInstance(project)

    private val vmScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val uiScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Swing)

    val generateKMMFeatureViewModel: GenerateKMMFeatureViewModel =
        GenerateKMMFeatureViewModel(
            generateKMMFeatureInteractor = generateKMMFeatureInteractor,
            editorManager = editorManager,
            scope = vmScope
        )
}
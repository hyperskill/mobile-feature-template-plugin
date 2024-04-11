package org.hyperskill.mobile.arch.newfeature

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiDirectory
import org.hyperskill.mobile.arch.newfeature.injection.GenerateKMMFeatureComponent
import org.hyperskill.mobile.arch.newfeature.ui.GenerateKMMFeatureDialog

class ActionGenerateKMMFeature : AnAction() {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        val generateKMMFeatureComponent = GenerateKMMFeatureComponent(
            project = requireNotNull(actionEvent.project),
            directory = actionEvent.getData(CommonDataKeys.PSI_ELEMENT) as PsiDirectory
        )
        
        GenerateKMMFeatureDialog(
            viewModel = generateKMMFeatureComponent.generateKMMFeatureViewModel,
            scope = generateKMMFeatureComponent.uiScope
        ).show()
    }
}
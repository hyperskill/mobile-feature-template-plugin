package org.hyperskill.mobile.arch.core

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import java.util.*

class TemplateGenerator(private val project: Project) {
    fun generateKt(
        templateName: String,
        fileName: String,
        directory: PsiDirectory,
        properties: Properties
    ): PsiFile {

        val existing = directory.findFile("${fileName}.kt")
        if (existing != null) return existing

        val template = FileTemplateManager
            .getInstance(project)
            .getInternalTemplate(templateName)

        val packageName = requireNotNull(directory.getPackageName())
        properties.setProperty(PropertyKeys.PACKAGE_NAME, packageName)

        return FileTemplateUtil.createFromTemplate(
            /* template = */ template,
            /* fileName = */ "${fileName}.kt",
            /* props = */ properties,
            /* directory = */ directory
        ) as PsiFile
    }
}
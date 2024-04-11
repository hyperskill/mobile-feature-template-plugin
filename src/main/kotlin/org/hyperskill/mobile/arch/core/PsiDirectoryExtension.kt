package org.hyperskill.mobile.arch.core

import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiDirectory

fun PsiDirectory.getPackageName(): String =
    requireNotNull(
        ProjectRootManager.getInstance(project)
        .fileIndex
        .getPackageNameByDirectory(virtualFile)
    )
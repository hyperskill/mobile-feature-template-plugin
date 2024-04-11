package org.hyperskill.mobile.arch.newfeature.domain

import com.intellij.openapi.application.Application
import com.intellij.openapi.util.Computable
import com.intellij.openapi.util.ThrowableComputable
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import java.util.Properties
import org.hyperskill.mobile.arch.core.Directories
import org.hyperskill.mobile.arch.core.TemplateGenerator
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GenerateKMMFeatureInteractor(
    private val directory: PsiDirectory,
    private val generator: TemplateGenerator
) {
    /**
     * returns Feature file
     */
    fun generateFeature(
        featureParams: FeatureParams,
        createFeaturePackage: Boolean,
    ): PsiFile =
        generateFeatureInternal(directory, createFeaturePackage, featureParams)

    private fun generateFeatureInternal(
        directory: PsiDirectory,
        createFeaturePackage: Boolean,
        featureParams: FeatureParams
    ): PsiFile {
        val commonMainFeatureDirectory =
            if (createFeaturePackage) {
                directory.createSubdirectorySafe(
                    featureParams.featureName.toSnackCase()
                )
            } else {
                directory
            }

        val presentationDirectory = commonMainFeatureDirectory.createSubdirectorySafe(Directories.PRESENTATION)
        val viewDirectory = if (featureParams.useViewState) {
            commonMainFeatureDirectory.createSubdirectorySafe(Directories.VIEW)
        } else {
            null
        }

        val properties = featureParams.toProperties(
            commonMainPresentationDirectory = presentationDirectory,
            commonMainViewDirectory = viewDirectory,
            androidMainPresentationDirectory = null /*androidMainDirectory*/
        )

        val featureFile = generateCommonPresentationFiles(
            presentationDirectory = presentationDirectory,
            viewDirectory = viewDirectory,
            generator = generator,
            featureParams = featureParams,
            properties = properties
        )

        generateCommonInjectionFiles(
            directory = commonMainFeatureDirectory,
            generator = generator,
            featureParams = featureParams,
            properties = properties
        )

        return featureFile
    }

    /**
     * returns Feature file
     */
    private fun generateCommonPresentationFiles(
        presentationDirectory: PsiDirectory,
        viewDirectory: PsiDirectory?,
        generator: TemplateGenerator,
        featureParams: FeatureParams,
        properties: Properties
    ): PsiFile {
        with (generator) {
            val featureFile = generateKt(
                templateName = "KMMFeature",
                fileName = featureParams.getFeatureNameWithSuffix(),
                directory = presentationDirectory,
                properties = properties
            )
            generateKt(
                templateName = "KMMReducer",
                fileName = "${featureParams.getFeatureNameWithoutSuffix()}Reducer",
                directory = presentationDirectory,
                properties = properties
            )
            generateKt(
                templateName = "KMMActionDispatcher",
                fileName = "${featureParams.getFeatureNameWithoutSuffix()}ActionDispatcher",
                directory = presentationDirectory,
                properties = properties
            )
            if (viewDirectory != null) {
                generateKt(
                    templateName = "KMMViewStateMapper",
                    fileName = "${featureParams.getFeatureNameWithoutSuffix()}ViewStateMapper",
                    directory = viewDirectory,
                    properties = properties
                )
            }
            return featureFile
        }
    }

    private fun generateCommonInjectionFiles(
        directory: PsiDirectory,
        generator: TemplateGenerator,
        featureParams: FeatureParams,
        properties: Properties
    ) {
        val injectionDirectory = directory.createSubdirectorySafe(Directories.INJECTION)
        with (generator) {
            generateKt(
                templateName = "KMMFeatureBuilder",
                fileName = "${featureParams.getFeatureNameWithSuffix()}Builder",
                directory = injectionDirectory,
                properties = properties
            )
            generateKt(
                templateName = "KMMFeatureComponent",
                fileName = "${featureParams.getFeatureNameWithoutSuffix()}Component",
                directory = injectionDirectory,
                properties = properties
            )
            generateKt(
                templateName = "KMMFeatureComponentImpl",
                fileName = "${featureParams.getFeatureNameWithoutSuffix()}ComponentImpl",
                directory = injectionDirectory,
                properties = properties
            )
        }
    }
}

private fun PsiDirectory.createSubdirectorySafe(name: String): PsiDirectory =
    findSubdirectory(name) ?: createSubdirectory(name)

private val snackCasePattern = "(?<=.)[A-Z]".toRegex()
private fun String.toSnackCase(): String =
    this.replace(snackCasePattern, "_$0").lowercase()
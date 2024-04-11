package org.hyperskill.mobile.arch.newfeature.domain

import com.intellij.psi.PsiDirectory
import java.util.*
import org.hyperskill.mobile.arch.core.PropertyKeys
import org.hyperskill.mobile.arch.core.getPackageName

data class FeatureParams(
    val featureName: String,
    val useViewState: Boolean,
    val supportAndroidMain: Boolean,
    val supportCompose: Boolean
) {
    companion object {
        const val FEATURE_NAME_SUFFIX = "Feature"
    }

    private val hasFeatureNameSuffix: Boolean =
        featureName.endsWith(FEATURE_NAME_SUFFIX)

    fun getFeatureNameWithSuffix(): String =
        if (hasFeatureNameSuffix) {
            featureName
        } else {
            featureName + FEATURE_NAME_SUFFIX
        }

    fun getFeatureNameWithoutSuffix(): String =
        if (hasFeatureNameSuffix) {
            featureName.removeSuffix(FEATURE_NAME_SUFFIX)
        } else {
            featureName
        }
}

fun FeatureParams.toProperties(
    commonMainPresentationDirectory: PsiDirectory,
    commonMainViewDirectory: PsiDirectory?,
    androidMainPresentationDirectory: PsiDirectory?
): Properties =
    Properties().apply {
        setProperty(
            PropertyKeys.COMMON_MAIN_PRESENTATION_PACKAGE_NAME,
            requireNotNull(commonMainPresentationDirectory.getPackageName())
        )
        if (commonMainViewDirectory != null) {
            setProperty(
                PropertyKeys.COMMON_MAIN_VIEW_PACKAGE_NAME,
                requireNotNull(commonMainViewDirectory.getPackageName())
            )
        }
        if (androidMainPresentationDirectory != null) {
            setProperty(
                PropertyKeys.ANDROID_MAIN_PRESENTATION_PACKAGE_NAME,
                requireNotNull(androidMainPresentationDirectory.getPackageName())
            )
        }
        setProperty(PropertyKeys.FULL_FEATURE_NAME, getFeatureNameWithSuffix())
        setProperty(PropertyKeys.SHORT_FEATURE_NAME, getFeatureNameWithoutSuffix())
        setProperty(PropertyKeys.USE_VIEW_STATE, useViewState.toString())
        setProperty(PropertyKeys.STATE_TYPE, if (useViewState) "ViewState" else "State")
        setProperty(PropertyKeys.SUPPORT_COMPOSE, supportCompose.toString())
    }
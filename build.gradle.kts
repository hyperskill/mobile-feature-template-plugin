plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.intellijPlugin)
}

group = "org.hyperskill.mobile.arch"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.coroutines.swing)
}

kotlin {
    jvmToolchain(17)
}

intellij {
    version.set("2023.3.1")
}

tasks {
    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }
}
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":applewidgets-ui"))
    implementation(project(":oink"))

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
}

compose.desktop {
    application {
        mainClass = "zhiqiu.app.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "zhiqiu.app"
            packageVersion = "1.0.0"
        }
    }
}

tasks.register<JavaExec>("renderOink") {
    group = "verification"
    description = "Headless-render the Oink showcase to PNG screenshots."
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("zhiqiu.app.RenderKt")
    systemProperty("java.awt.headless", "true")
}
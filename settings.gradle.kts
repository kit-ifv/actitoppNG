pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "2.3.20"
    }
}

rootProject.name = "actitoppNG"


val dcmPath =  "../discretechoicemodelling"
if (File(dcmPath).exists()) {
    println("Include local build of discrete-choice")
    includeBuild(dcmPath)
}
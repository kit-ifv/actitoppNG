pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "2.1.20"
    }
}

rootProject.name = "actitoppNG"

val localDiscreteChoice = file("../discretechoicemodelling")
if (localDiscreteChoice.exists()) {
    println("Using local DCM project")
    includeBuild(localDiscreteChoice)
} else {
    println("Using remote DCM project.")
}
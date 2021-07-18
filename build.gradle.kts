buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0-alpha03")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.20")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.37")
        classpath ("android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
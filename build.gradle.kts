// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
}

buildscript{
    val objectboxVersion by extra("4.2.0")
    repositories{
        google()
        mavenCentral()
    }
    dependencies{
        classpath("io.objectbox:objectbox-gradle-plugin:$objectboxVersion")
    }
}
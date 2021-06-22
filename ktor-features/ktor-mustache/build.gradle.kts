
kotlin.sourceSets {
    val jvmMain by getting {
        dependencies {
            api("com.github.spullara.mustache.java:compiler:0.9.10")
        }
    }
    val jvmTest by getting {
        dependencies {
            api(project(":ktor-features:ktor-compression"))
            api(project(":ktor-features:ktor-conditional-headers"))
        }
    }
}

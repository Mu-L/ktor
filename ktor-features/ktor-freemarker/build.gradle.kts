kotlin.sourceSets {
    val jvmMain by getting {
        dependencies {
            api("org.freemarker:freemarker:[2.3.20, 2.4)")
        }
    }
    val jvmTest by getting {
        dependencies {
            api(project(":ktor-features:ktor-status-pages"))
            api(project(":ktor-features:ktor-compression"))
            api(project(":ktor-features:ktor-conditional-headers"))
        }
    }
}

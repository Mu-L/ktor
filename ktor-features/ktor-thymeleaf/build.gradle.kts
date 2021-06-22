
kotlin.sourceSets {
    val jvmMain by getting {
        dependencies {
            api("org.thymeleaf:thymeleaf:[3.0.11.RELEASE, 3.1)")
        }
    }
    val jvmTest by getting {
        dependencies {
            api(project(":ktor-features:ktor-conditional-headers"))
            api(project(":ktor-features:ktor-compression"))
        }
    }
}

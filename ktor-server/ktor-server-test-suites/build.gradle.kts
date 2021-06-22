description = ""

val coroutines_version: String by extra

kotlin.sourceSets {
    val jvmMain by getting {
        dependencies {
            implementation(project(":ktor-server:ktor-server-test-host"))
            implementation(project(":ktor-features:ktor-compression"))
            implementation(project(":ktor-features:ktor-partial-content"))
            implementation(project(":ktor-features:ktor-status-pages"))
            implementation(project(":ktor-features:ktor-conditional-headers"))
            implementation(project(":ktor-features:ktor-forwarded-header"))
            implementation(project(":ktor-features:ktor-auto-head-response"))

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:$coroutines_version")
        }
    }
    val jvmTest by getting {
        dependencies {
            api(project(":ktor-server:ktor-server", configuration = "testOutput"))
        }
    }
}

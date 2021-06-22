kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                api(project(":ktor-features:ktor-sessions"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(project(":ktor-server:ktor-server-netty"))
            }
        }
    }
}

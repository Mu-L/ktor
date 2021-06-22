description = ""

kotlin.sourceSets {
    val jvmMain by getting {
        dependencies {
            api(project(":ktor-server:ktor-server"))
            api(project(":ktor-features:ktor-auto-head-response"))
            api(project(":ktor-features:ktor-caching-headers"))
            api(project(":ktor-features:ktor-call-id"))
            api(project(":ktor-features:ktor-call-logging"))
            api(project(":ktor-features:ktor-compression"))
            api(project(":ktor-features:ktor-conditional-headers"))
            api(project(":ktor-features:ktor-content-negotiation"))
            api(project(":ktor-features:ktor-cors"))
            api(project(":ktor-features:ktor-data-conversion"))
            api(project(":ktor-features:ktor-default-headers"))
            api(project(":ktor-features:ktor-double-receive"))
            api(project(":ktor-features:ktor-forwarded-header"))
            api(project(":ktor-features:ktor-hsts"))
            api(project(":ktor-features:ktor-http-redirect"))
            api(project(":ktor-features:ktor-partial-content"))
            api(project(":ktor-features:ktor-sessions"))
            api(project(":ktor-features:ktor-status-pages"))
        }
    }
}

artifacts {
    val jarTest by tasks
    add("testOutput", jarTest)
}

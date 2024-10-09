dependencies {
    implementation(project(":api-core"))
    implementation(project(":common"))
    implementation(project(":mysql"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
}
dependencies {
    implementation(project(":api-core"))
    implementation(project(":common"))
    implementation(project(":http"))
    implementation(project(":mysql"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.nimbusds:nimbus-jose-jwt:9.40")  // Apple OAuth2 IdToken Verification을 위한 의존성
    implementation("com.google.api-client:google-api-client:2.2.0")  // Google OAuth2 IdToken Verification을 위한 의존성

    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0")
}
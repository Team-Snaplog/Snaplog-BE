import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

dependencies {
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("com.github.jasync-sql:jasync-r2dbc-mysql:2.2.4")
}
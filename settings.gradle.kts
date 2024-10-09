rootProject.name = "Snaplog-BE"

// libs 하위 모듈 세팅
listOf(
    "common",
).forEach {
    include(it)
    project(":$it").projectDir = File("$rootDir/libs/$it")
}
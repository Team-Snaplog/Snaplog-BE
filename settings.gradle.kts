rootProject.name = "Snaplog-BE"

// libs 하위 모듈 세팅
listOf(
    "common",
).forEach {
    include(it)
    project(":$it").projectDir = File("$rootDir/libs/$it")
}

// libs/infra 하위 모듈 세팅
listOf(
    "mysql"
).forEach {
    include(it)
    project(":$it").projectDir = File("$rootDir/libs/infra/$it")
}
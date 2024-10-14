rootProject.name = "Snaplog-BE"

// libs 하위 모듈 세팅
listOf(
    "common",
    "api-core",
).forEach {
    include(it)
    project(":$it").projectDir = File("$rootDir/libs/$it")
}

// libs/infra 하위 모듈 세팅
listOf(
    "mysql",
    "redis",
).forEach {
    include(it)
    project(":$it").projectDir = File("$rootDir/libs/infra/$it")
}

// apps/api 하위 모듈 세팅
listOf(
    "snaplog-api",
).forEach {
    include(it)
    project(":$it").projectDir = File("$rootDir/apps/api/$it")
}
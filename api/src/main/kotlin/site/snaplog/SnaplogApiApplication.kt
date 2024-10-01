package site.snaplog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SnaplogApiApplication

fun main(args: Array<String>) {
    runApplication<SnaplogApiApplication>(*args)
}
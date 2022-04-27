package io.kio.websocketdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebSocketDemoApplication

fun main(args: Array<String>) {
    runApplication<WebSocketDemoApplication>(*args)
}

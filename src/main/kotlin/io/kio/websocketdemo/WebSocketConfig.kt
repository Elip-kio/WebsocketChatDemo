package io.kio.websocketdemo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.server.standard.ServerEndpointExporter

@Configuration
class WebSocketConfig {

    @Bean
    public fun endpoint(): ServerEndpointExporter {
        return ServerEndpointExporter()
    }

}
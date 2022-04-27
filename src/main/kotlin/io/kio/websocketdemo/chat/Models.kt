package io.kio.websocketdemo.chat

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MessageTypeWrapper(val type: String)

data class ChatMessage(val user: String, val content: String, val date: String)
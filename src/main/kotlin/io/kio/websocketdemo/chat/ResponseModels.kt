package io.kio.websocketdemo.chat

sealed class ResponseModel(val type: String)

data class Init(
    val currentUser: String,
    val users: List<String>,
    val messages: List<ChatMessage>
) : ResponseModel("init")

data class NewMessage(val message: ChatMessage) : ResponseModel("newMessage")

data class UserEnter(val user: String) : ResponseModel("userEnter")

data class UserLeave(val user: String) : ResponseModel("userLeave")
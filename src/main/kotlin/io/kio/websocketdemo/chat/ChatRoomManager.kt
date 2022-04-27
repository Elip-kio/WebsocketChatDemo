package io.kio.websocketdemo.chat

import java.text.SimpleDateFormat
import java.util.*

object ChatRoomManager {
    private val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
    private val mUsers = mutableListOf<String>()
    val users get() = mUsers.toList()

    private val mMessages = mutableListOf<ChatMessage>()
    val messages get() = mMessages.toList()

    @Synchronized
    fun userEnter(user: String) {
        mUsers.add(user)
    }

    @Synchronized
    fun userLeave(user: String) {
        mUsers.remove(user)
    }

    @Synchronized
    fun sendMessage(user: String, content: String):ChatMessage {
        val msg = ChatMessage(user, content, format.format(System.currentTimeMillis()))
        mMessages.add(msg)
        return msg
    }

}
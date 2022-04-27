package io.kio.websocketdemo.chat

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.CopyOnWriteArraySet
import javax.websocket.*
import javax.websocket.server.ServerEndpoint

@ServerEndpoint("/chat-service")
@Service
class ChatService {

    private val mapper = jacksonObjectMapper()

    companion object {
        private val sockets = CopyOnWriteArraySet<ChatService>()
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
    private lateinit var session: Session
    private lateinit var user: String

    @OnOpen
    fun onOpen(session: Session) {
        sockets.add(this)
        this.session = session
        logger.info("Open")
    }

    @OnClose
    fun onClose() {
        sockets.remove(this)
        logger.info("Close")

        if (!this::user.isInitialized) return
        ChatRoomManager.userLeave(user)
        sockets.forEach { it.sendMessage(UserLeave(user)) }
    }

    @OnMessage
    fun onMessage(message: String, session: Session) {
        logger.info("message:$message")
        val typeWrapper: MessageTypeWrapper = mapper.readValue(message, MessageTypeWrapper::class.java)
        when (typeWrapper.type) {
            "setNickname" -> {
                this.user = mapper.readValue(message, SetNickName::class.java).nickname
                ChatRoomManager.userEnter(user)
                sendMessage(Init(user, ChatRoomManager.users, ChatRoomManager.messages))
                sockets.filter { it.user != user }.forEach {
                    it.sendMessage(UserEnter(user))
                }
            }
            "sendMessage" -> {
                if (!this::user.isInitialized) return
                val content = mapper.readValue(message, SendMessage::class.java)
                val msg = ChatRoomManager.sendMessage(user, content.message)
                sockets.forEach { it.sendMessage(NewMessage(msg)) }
            }
            else -> {}
        }
    }

    @OnError
    fun onError(session: Session, e: Throwable) {
        logger.info("error$e")
    }

    private fun sendMessage(message: Any) {
        if (!this::session.isInitialized) return
        if (!this.session.isOpen) return
        val text = mapper.writeValueAsString(message)
        session.basicRemote.sendText(text)
        logger.info(text)
    }

}
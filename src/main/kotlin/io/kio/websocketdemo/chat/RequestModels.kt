package io.kio.websocketdemo.chat

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
sealed class RequestModel

data class SetNickName(val nickname: String) : RequestModel()
data class SendMessage(val message: String) : RequestModel()
package com.juniverse.wheelchat.model

open class Chat{
    fun identifier() = this::class.java.name.hashCode()
}

data class ChatMessage(
    val uid:String = "",
    val text:String = "",
    val fromId:String ="",
    val toId:String ="",
    val timestamp: Long = -1
)

class ChatFromItem(val message: ChatMessage, val user:User): Chat()

class ChatToItem(val message: ChatMessage): Chat()

class LastestMessageItem(val message: ChatMessage,val user:User): Chat()

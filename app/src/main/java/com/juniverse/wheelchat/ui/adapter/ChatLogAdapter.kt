package com.juniverse.wheelchat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.juniverse.wheelchat.R
import com.juniverse.wheelchat.databinding.ItemChatFromBinding
import com.juniverse.wheelchat.databinding.ItemChatToBinding
import com.juniverse.wheelchat.model.*
import java.lang.IllegalArgumentException

class ChatLogAdapter(private val list:MutableList<Chat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = list[position].identifier()
    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType){
            ChatToItem::class.java.name.hashCode()->{
                val binding = ItemChatToBinding.inflate(inflater, parent, false)
                ChatToViewHolder(binding)
            }
            ChatFromItem::class.java.name.hashCode() -> {
                val binding = ItemChatFromBinding.inflate(inflater, parent, false)
                ChatFromViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unsupported layout")
        }
    }

    fun setData(list:List<Chat>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun add(item:Chat){
        this.list.add(item)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = list[holder.adapterPosition]){
            is ChatToItem -> (holder as ChatToViewHolder).bind(item.message)
            is ChatFromItem -> (holder as ChatFromViewHolder).bind(item.message,item.user)
        }
    }

}

class ChatToViewHolder(private val binding: ItemChatToBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(message: ChatMessage){
        binding.apply {
            this.message = message
            executePendingBindings()
        }
    }

}

class ChatFromViewHolder(private val binding: ItemChatFromBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(message: ChatMessage,user:User){
        binding.apply {
            this.message = message
            this.user = user
            executePendingBindings()
        }
    }

}

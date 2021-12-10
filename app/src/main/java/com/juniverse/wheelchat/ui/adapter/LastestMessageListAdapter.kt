package com.juniverse.wheelchat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.juniverse.wheelchat.databinding.ItemLastestMessageListBinding
import com.juniverse.wheelchat.databinding.ItemUserListBinding
import com.juniverse.wheelchat.model.Chat
import com.juniverse.wheelchat.model.ChatMessage
import com.juniverse.wheelchat.model.LastestMessageItem
import com.juniverse.wheelchat.model.User


class LastestMessageListAdapter(private val list: MutableList<LastestMessageItem>) :
    RecyclerView.Adapter<LastestMessageListViewHolder>() {

    var userFilterList : MutableList<LastestMessageItem> = mutableListOf()

    init {
        userFilterList = list
    }

    private var onClickListener : ((User)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastestMessageListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLastestMessageListBinding.inflate(inflater, parent, false)
        return LastestMessageListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LastestMessageListViewHolder, position: Int) {
        holder.bind(list[position].message,list[position].user)
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(userFilterList[position].user)
        }
    }

    override fun getItemCount(): Int = list.size

    fun setData(list:ArrayList<LastestMessageItem>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun add(item: LastestMessageItem){
        this.list.add(item)
        notifyDataSetChanged()
    }

    fun clear(){
        this.list.clear()
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener:(User)->Unit){
        this.onClickListener = listener
    }

}

class LastestMessageListViewHolder(private val binding: ItemLastestMessageListBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(chatMessage: ChatMessage,data:User){
        binding.apply {
            this.user = data
            this.message = chatMessage
            executePendingBindings()
        }
    }

}
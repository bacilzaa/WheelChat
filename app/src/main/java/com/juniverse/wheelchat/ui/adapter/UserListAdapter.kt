package com.juniverse.wheelchat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.juniverse.wheelchat.databinding.ItemUserListBinding
import com.juniverse.wheelchat.model.Chat
import com.juniverse.wheelchat.model.User


class UserListAdapter(private val list: MutableList<User>) :
    RecyclerView.Adapter<UserListViewHolder>() {

    var userFilterList : MutableList<User> = mutableListOf()

    init {
        userFilterList = list
    }

    private var onClickListener : ((User)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserListBinding.inflate(inflater, parent, false)
        return UserListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(userFilterList[position])
        }
    }

    override fun getItemCount(): Int = list.size

    fun setData(list:ArrayList<User>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun add(item: User){
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

class UserListViewHolder(private val binding: ItemUserListBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(data: User){
        binding.apply {
            this.data = data
            executePendingBindings()
        }
    }

}
package com.juniverse.wheelchat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.juniverse.wheelchat.databinding.ItemSettingListBinding


class SettingListAdapter(private val list: MutableList<SettingItem>) :
    RecyclerView.Adapter<SettingListViewHolder>() {

    var settingFilterList : MutableList<SettingItem> = mutableListOf()

    init {
        settingFilterList = list
    }

    private var onClickListener : ((Int)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSettingListBinding.inflate(inflater, parent, false)
        return SettingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingListViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int = list.size

    fun setData(list:List<SettingItem>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun add(item: SettingItem){
        this.list.add(item)
        notifyDataSetChanged()
    }

    fun clear(){
        this.list.clear()
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener:(Int)->Unit){
        this.onClickListener = listener
    }

}

class SettingListViewHolder(private val binding: ItemSettingListBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(data: SettingItem){
        binding.apply {
            this.data = data
            executePendingBindings()
        }
    }
}

data class SettingItem(val name:String = "", val icon: Int = 0)
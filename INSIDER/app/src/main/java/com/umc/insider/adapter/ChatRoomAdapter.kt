package com.umc.insider.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.R
import com.umc.insider.databinding.ChatRoomItemBinding
import com.umc.insider.model.ChatRoomItem

class ChatRoomAdapter : ListAdapter<ChatRoomItem,ChatRoomAdapter.ChatRoomViewHolder>(DiffCallback)  {

    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<ChatRoomItem>(){
            override fun areItemsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            override fun areContentsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ChatRoomViewHolder(private val binding : ChatRoomItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(chatRoomItem : ChatRoomItem){

            binding.message.text = chatRoomItem.message
            if(chatRoomItem.sender=="me"){
                binding.chatLayout.gravity = Gravity.END
                binding.message.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.lightMain))
            }else{
                binding.chatLayout.gravity = Gravity.START
                binding.message.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val binding = ChatRoomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
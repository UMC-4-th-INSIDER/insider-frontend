package com.umc.insider.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.R
import com.umc.insider.auth.UserManager
import com.umc.insider.databinding.ChatRoomItemBinding
import com.umc.insider.model.ChatRoomItem
import com.umc.insider.retrofit.model.MessageGetRes

class ChatRoomAdapter(context : Context) : ListAdapter<MessageGetRes,ChatRoomAdapter.ChatRoomViewHolder>(DiffCallback)  {

    private val my_id = UserManager.getUserIdx(context)!!.toLong()
    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<MessageGetRes>(){
            override fun areItemsTheSame(oldItem: MessageGetRes, newItem: MessageGetRes): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MessageGetRes, newItem: MessageGetRes): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ChatRoomViewHolder(private val binding : ChatRoomItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(messageGetRes: MessageGetRes) {
            // 너비와 높이를 동적으로 설정하기 위한 onPreDrawListener
            binding.message.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    binding.message.viewTreeObserver.removeOnPreDrawListener(this)

                    // 텍스트의 너비
                    val textWidth = binding.message.paint.measureText(messageGetRes.content)

                    // 너비와 높이 설정
                    val layoutParams = binding.message.layoutParams
                    layoutParams.width = (textWidth + binding.message.paddingLeft + binding.message.paddingRight).toInt()
                    binding.message.layoutParams = layoutParams

                    return true
                }
            })

            binding.message.text = messageGetRes.content

            if (messageGetRes.senderId.id == my_id) {
                binding.chatLayout.gravity = Gravity.END
                binding.message.setBackgroundResource(R.drawable.my_chat)
            } else {
                binding.chatLayout.gravity = Gravity.START
                binding.message.setBackgroundResource(R.drawable.opponent_chat)
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
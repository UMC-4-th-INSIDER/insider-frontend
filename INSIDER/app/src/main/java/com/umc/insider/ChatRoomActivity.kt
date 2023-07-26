package com.umc.insider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.insider.adapter.ChatRoomAdapter
import com.umc.insider.databinding.ActivityChatRoomBinding
import com.umc.insider.model.ChatRoomItem

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatRoomBinding
    private val adapter = ChatRoomAdapter()
    private var chatRoomItems = arrayListOf<ChatRoomItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatRoomItems = DummyDate()

        initView()

    }

    private fun initView(){

        with(binding){
            chatRV.adapter = adapter
            chatRV.layoutManager = LinearLayoutManager(this@ChatRoomActivity)
            adapter.submitList(chatRoomItems)

            sendBtn.setOnClickListener {
                if(binding.chatEditTextView.text.isNullOrBlank()) return@setOnClickListener

                val idDummy = (chatRoomItems.size + 1).toString()
                val message = binding.chatEditTextView.text.toString()
                val myChat = ChatRoomItem(idDummy, "me", message)


                val newChatRoomItems = ArrayList(chatRoomItems)
                newChatRoomItems.add(myChat)
                chatRoomItems = newChatRoomItems
                adapter.submitList(newChatRoomItems)
                binding.chatEditTextView.text.clear()
                binding.chatRV.smoothScrollToPosition(chatRoomItems.size - 1)
            }
        }
    }

    private fun DummyDate() : ArrayList<ChatRoomItem>{
        val dummy1 = ChatRoomItem("1", "me", "안녕하세요. 교환 가능 하실까요?")
        val dummy2 = ChatRoomItem("2", "you", "네, 가능합니다. 어떤 장소에서 교환하면 될까요?")

        val arr = ArrayList<ChatRoomItem>()
        arr.add(dummy1)
        arr.add(dummy2)

        return arr
    }

}
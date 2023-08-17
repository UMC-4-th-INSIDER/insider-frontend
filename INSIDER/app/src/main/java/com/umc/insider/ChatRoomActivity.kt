package com.umc.insider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.insider.adapter.ChatRoomAdapter
import com.umc.insider.auth.UserManager
import com.umc.insider.databinding.ActivityChatRoomBinding
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.MessageInterface
import com.umc.insider.retrofit.model.MessageGetRes
import com.umc.insider.retrofit.model.MessagePostReq
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatRoomBinding
    private lateinit var adapter : ChatRoomAdapter
    private var chatRoom_id : Long? = null
    private var sender_id : Long? = null
    private val messageAPI = RetrofitInstance.getInstance().create(MessageInterface::class.java)
    private var pollingJob: Job? = null
    private var currentChatList: List<MessageGetRes> = emptyList()
    private var first = true
    override fun onResume() {
        super.onResume()
        chatRoom_id = intent.getStringExtra("chatRoom_id")!!.toLong()
        sender_id = UserManager.getUserIdx(this)!!.toLong()
        startPolling()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initView()

    }

    private fun initView(){

        with(binding){
            adapter = ChatRoomAdapter(this@ChatRoomActivity)
            chatRV.adapter = adapter
            chatRV.layoutManager = LinearLayoutManager(this@ChatRoomActivity)

            binding.root.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                val layoutManager = binding.chatRV.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (bottom < oldBottom) {
                    // 키보드가 오픈됐을 때
                    if (currentChatList.isNotEmpty() && lastVisibleItemPosition == adapter.itemCount - 1) {
                        binding.chatRV.post {
                            binding.chatRV.smoothScrollToPosition(adapter.itemCount - 1)
                        }
                    }
                } else if (bottom > oldBottom) {
                    if (currentChatList.isNotEmpty() && lastVisibleItemPosition == adapter.itemCount - 1) {
                        binding.chatRV.post {
                            binding.chatRV.smoothScrollToPosition(adapter.itemCount - 1)
                        }
                    }
                }
            }
            sendBtn.setOnClickListener {

                if(binding.chatEditTextView.text.isNullOrBlank()) return@setOnClickListener

                lifecycleScope.launch {

                    val content = chatEditTextView.text.toString()
                    val messagePostReq = MessagePostReq(chatRoom_id!!,sender_id!!,content)

                    try {
                        val response = withContext(Dispatchers.IO){
                            messageAPI.createMessage(messagePostReq)
                        }
                        if (response.isSuccessful){
                            chatEditTextView.text = null
                            binding.chatRV.smoothScrollToPosition(adapter.itemCount - 1)
                        }else{ }
                    }catch (e : Exception){

                    }
                }
            }
        }
    }

    private fun startPolling() {
        pollingJob = lifecycleScope.launch {
            while (isActive) {  // 코루틴이 active한 동안 계속 실행
                try {
                    val response = withContext(Dispatchers.IO) {
                        messageAPI.getMessageesInChatRoom(chatRoom_id!!)
                    }
                    if (response.isSuccessful) {
                        val chatList = response.body()

                        withContext(Dispatchers.Main) {
                            adapter.submitList(chatList)
//                            if (currentChatList.isNotEmpty()){
//                                binding.chatRV.smoothScrollToPosition(adapter.itemCount - 1)
//                            }
                            if (first){
                                binding.chatRV.smoothScrollToPosition(adapter.itemCount - 1)
                                first = false
                            }
                        }
                        currentChatList = chatList!!
                    } else { }
                } catch (e: Exception) {
                }
                delay(1000L)
            }
        }
    }

    private fun stopPolling() {
        pollingJob?.cancel()
    }


    override fun onPause() {
        super.onPause()
        stopPolling()
    }

}
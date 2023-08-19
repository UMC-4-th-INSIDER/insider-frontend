package com.umc.insider

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.insider.adapter.ChatRoomAdapter
import com.umc.insider.auth.UserManager
import com.umc.insider.databinding.ActivityChatRoomBinding
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.MessageInterface
import com.umc.insider.retrofit.model.MessageGetRes
import com.umc.insider.retrofit.model.MessagePostReq
import com.umc.insider.viewmodel.ChatRoomViewModel
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
    private lateinit var viewModel : ChatRoomViewModel
    private var originalScrollY = 0
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

        val goods_id = intent.getStringExtra("goods_id")!!.toLong()
        //Toast.makeText(this, goods_id.toString(), Toast.LENGTH_SHORT).show()


        initView()
        observeKeyboardChanges()
    }

    private fun initView(){

        with(binding){
            adapter = ChatRoomAdapter(this@ChatRoomActivity)
            chatRV.adapter = adapter
            chatRV.layoutManager = LinearLayoutManager(this@ChatRoomActivity)

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
                            try {
                                val response = withContext(Dispatchers.IO) {
                                    messageAPI.getMessageesInChatRoom(chatRoom_id!!)
                                }
                                if (response.isSuccessful) {
                                    val chatList = response.body()

                                    withContext(Dispatchers.Main) {
                                        adapter.submitList(chatList) {
                                            binding.scrollView.post {
                                                val bottomPosition = binding.chatRV.bottom // RecyclerView의 아래쪽 위치를 가져옵니다.
                                                originalScrollY = bottomPosition
                                                binding.scrollView.smoothScrollTo(0, bottomPosition)
                                            }
                                        }
                                    }
                                    currentChatList = chatList!!
                                } else { }
                            } catch (e: Exception) { }

                        }else{ }
                    }catch (e : Exception){

                    }
                }
                chatEditTextView.requestFocus()
            }
        }
    }

    private fun getKeyboardHeight(rootView: View): Int {
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val screenHeight = rootView.height
        return screenHeight - r.bottom
    }
    private fun observeKeyboardChanges() {
        val rootView = window.decorView.rootView
        var isKeyboardUp = false

        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val keyboardHeight = getKeyboardHeight(rootView)

                if (keyboardHeight > rootView.height * 0.15) {  // 키보드가 올라왔을 때
                    if (!isKeyboardUp) {
                        originalScrollY = binding.scrollView.scrollY  // 현재 스크롤 위치 저장
                        val adjustScroll = originalScrollY + keyboardHeight - 100
                        binding.scrollView.scrollTo(0, adjustScroll)
                        isKeyboardUp = true
                    }
                } else if (isKeyboardUp) {  // 키보드가 내려갔을 때
                    binding.scrollView.scrollTo(0, originalScrollY)  // 원래 스크롤 위치로 복원
                    isKeyboardUp = false
                }
            }
        })
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
                            adapter.submitList(chatList){}
                            if (first){
                                binding.root.post{
                                    binding.scrollView.fullScroll(View.FOCUS_DOWN)
                                }
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
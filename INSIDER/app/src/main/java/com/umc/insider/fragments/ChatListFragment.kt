package com.umc.insider.fragments

import ChatListAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.umc.insider.ChatRoomActivity
import com.umc.insider.DeleteChatActivity
import com.umc.insider.EditProfileActivity
import com.umc.insider.R
import com.umc.insider.auth.UserManager
import com.umc.insider.databinding.FragmentChatListBinding
import com.umc.insider.model.ChatListItem
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.ChattingInterface
import com.umc.insider.retrofit.api.GoodsInterface
import com.umc.insider.utils.ChatListClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChatListFragment : Fragment(), ChatListClickListener {


    private var _binding : FragmentChatListBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatListAdapter : ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentChatListBinding.inflate(inflater, container, false)


        chatListAdapter = ChatListAdapter(this)

        binding.deleteTextView.setOnClickListener{
            startActivity(Intent(activity, DeleteChatActivity::class.java))
        }

        // Set LayoutManager and Adapter for RecyclerView
        binding.chatList.layoutManager = LinearLayoutManager(requireContext())
        binding.chatList.adapter = chatListAdapter

        val chattingAPI = RetrofitInstance.getInstance().create(ChattingInterface::class.java)

        lifecycleScope.launch {

            val user_id = UserManager.getUserIdx(requireContext())!!.toLong()

            try {
                val response = withContext(Dispatchers.IO){
                    chattingAPI.getChatRoomByUser(user_id)
                }
                if (response.isSuccessful){
                    val chatRoomList = response.body()
                    withContext(Dispatchers.Main) {
                        chatListAdapter.submitList(chatRoomList)
                    }
                }else{
                }
            }catch (e : Exception){

            }
        }

        return binding.root
    }

    // Sample data for ChatList (Replace with your actual data)



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun ChatListItemClick(chatRoomId : Long) {
        val intent = Intent(requireContext(), ChatRoomActivity::class.java)
        intent.putExtra("chatRoom_id", chatRoomId.toString())
        startActivity(intent)
    }

}
package com.umc.insider.fragments

import ChatListAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.umc.insider.ChatRoomActivity
import com.umc.insider.DeleteChatActivity
import com.umc.insider.EditProfileActivity
import com.umc.insider.R
import com.umc.insider.databinding.FragmentChatListBinding
import com.umc.insider.model.ChatListItem
import com.umc.insider.utils.ChatListClickListener


class ChatListFragment : Fragment(), ChatListClickListener {


    private var _binding : FragmentChatListBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatListAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

        _binding = FragmentChatListBinding.inflate(inflater, container, false)

        // Sample data for ChatList (Replace with your actual data)
        val chatListData = createSampleChatList()

        // Initialize RecyclerView and ChatListAdapter

        chatListAdapter = ChatListAdapter(chatListData, this)

        binding.deleteTextView.setOnClickListener{
            Toast.makeText(requireContext(), "Hello World", Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity, DeleteChatActivity::class.java))
        }

        // Set LayoutManager and Adapter for RecyclerView
        binding.chatList.layoutManager = LinearLayoutManager(requireContext())
        binding.chatList.adapter = chatListAdapter


        return binding.root
    }

    // Sample data for ChatList (Replace with your actual data)
    private fun createSampleChatList(): List<ChatListItem> {
        val chatList = ArrayList<ChatListItem>()
        chatList.add(ChatListItem("1","User1", "안녕하세요. 교환 가능 하실까요?"))
        chatList.add(ChatListItem("2","User2", "네, 가능합니다. 어떤 장소에서 교환하면 될까요?"))
        chatList.add(ChatListItem("3","User3", "오늘은 어떤 시간이 가능하신가요?"))
        // Add more chat items as needed
        return chatList
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun ChatListItemClick() {
        startActivity(Intent(requireContext(), ChatRoomActivity::class.java))
    }

}
package com.umc.insider.fragments

import ChatListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.R
import com.umc.insider.model.ChatItem

class ChatListFragment : Fragment() {

    private lateinit var chatListAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_chat_list, container, false)

        // Sample data for ChatList (Replace with your actual data)
        val chatListData = createSampleChatList()

        // Initialize RecyclerView and ChatListAdapter
        val recyclerView: RecyclerView = rootView.findViewById(R.id.chatList)
        chatListAdapter = ChatListAdapter(chatListData)

        // Set LayoutManager and Adapter for RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = chatListAdapter

        return rootView
    }

    // Sample data for ChatList (Replace with your actual data)
    private fun createSampleChatList(): List<ChatItem> {
        val chatList = ArrayList<ChatItem>()
        chatList.add(ChatItem("User1", "안녕하세요. 교환 가능 하실까요?"))
        chatList.add(ChatItem("User2", "네, 가능합니다. 어떤 장소에서 교환하면 될까요?"))
        chatList.add(ChatItem("User3", "오늘은 어떤 시간이 가능하신가요?"))
        // Add more chat items as needed
        return chatList
    }
}

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.databinding.ChatRoomItemBinding
import com.umc.insider.model.ChatItem

class ChatListAdapter(private val chatList: List<ChatItem>) : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val binding = ChatRoomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val currentChatItem = chatList[position]
        holder.bindData(currentChatItem)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    inner class ChatListViewHolder(private val binding: ChatRoomItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(chatItem: ChatItem) {
            binding.interlocutor.text = chatItem.chatId
            binding.recentMessageTextView.text = chatItem.recentMessage
            // You can set other views in the ViewHolder as needed
        }
    }
}

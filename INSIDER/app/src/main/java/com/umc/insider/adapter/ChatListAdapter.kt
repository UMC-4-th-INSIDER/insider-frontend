import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.databinding.ChatListItemBinding
import com.umc.insider.model.ChatListItem
import com.umc.insider.utils.ChatListClickListener

class ChatListAdapter(private val chatList: List<ChatListItem>, private val listener : ChatListClickListener)
    : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val binding = ChatListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val currentChatItem = chatList[position]
        holder.bindData(currentChatItem)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    inner class ChatListViewHolder(private val binding : ChatListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(chatItem: ChatListItem) {
            binding.interlocutor.text = chatItem.interlocutor
            binding.recentMessageTextView.text = chatItem.recentMessage
            // You can set other views in the ViewHolder as needed

            binding.root.setOnClickListener {
                listener.ChatListItemClick()
            }
        }
    }
}

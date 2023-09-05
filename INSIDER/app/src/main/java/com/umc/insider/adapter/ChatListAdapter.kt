import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.insider.databinding.ChatListItemBinding
import com.umc.insider.model.ChatListItem
import com.umc.insider.retrofit.model.ChatRoomsListRes
import com.umc.insider.retrofit.model.GoodsGetRes
import com.umc.insider.utils.ChatListClickListener
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import com.umc.insider.utils.dpToPx

class ChatListAdapter(private val listener : ChatListClickListener)
    : ListAdapter<ChatRoomsListRes,ChatListAdapter.ChatListViewHolder>(DiffCallback) {

    val roundedCornersTransformation = RoundedCornersTransformation(18.dpToPx(), 0)

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<ChatRoomsListRes>(){
            override fun areItemsTheSame(oldItem: ChatRoomsListRes, newItem: ChatRoomsListRes): Boolean {
                return oldItem.chatRoomId == newItem.chatRoomId
                //return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChatRoomsListRes, newItem: ChatRoomsListRes): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ChatListViewHolder(private val binding : ChatListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatRoomslistRes : ChatRoomsListRes) {
            binding.interlocutor.text = chatRoomslistRes.otherNickName
            binding.recentMessageTextView.text = chatRoomslistRes.lastMessage

            Glide.with(binding.profileImg.context)
                .load(chatRoomslistRes.otherImgUrl)
                .transform(roundedCornersTransformation)
                .placeholder(null)
                .into(binding.profileImg)

            binding.root.setOnClickListener {
                listener.ChatListItemClick(chatRoomslistRes.chatRoomId, chatRoomslistRes.goodsId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val binding = ChatListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

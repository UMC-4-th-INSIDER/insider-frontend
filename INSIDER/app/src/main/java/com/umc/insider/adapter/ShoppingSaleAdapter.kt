package com.umc.insider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.databinding.ShoppingSaleListItemBinding
import com.umc.insider.model.SearchItem

class ShoppingSaleAdapter : ListAdapter<SearchItem, ShoppingSaleAdapter.ShoppingSaleViewHolder>(
    DiffCallback){

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<SearchItem>(){
            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ShoppingSaleViewHolder(private val binding : ShoppingSaleListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(listItem : SearchItem){
            binding.itemName.text = listItem.itemName
            binding.itemPrice.text = listItem.itemPrice
            binding.itemWeightOrRest.text = "("+listItem.itemWeight+")"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingSaleViewHolder {
        val binding = ShoppingSaleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingSaleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingSaleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
package com.umc.insider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.databinding.ExchangeEndListItemBinding
import com.umc.insider.databinding.ExchangingListItemBinding
import com.umc.insider.model.ExchangeItem

class ExchangeEndAdapter : ListAdapter<ExchangeItem, ExchangeEndAdapter.ExchangeEndViewHolder>(
    DiffCallback){

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<ExchangeItem>(){
            override fun areItemsTheSame(oldItem: ExchangeItem, newItem: ExchangeItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ExchangeItem, newItem: ExchangeItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ExchangeEndViewHolder(private val binding : ExchangeEndListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(listItem : ExchangeItem){
            binding.itemName.text = listItem.itemName
            binding.itemAmount.text = "("+listItem.itemAmount+")"
            binding.itemExchange.text = listItem.itemExchange
            binding.itemExchangeAmount.text = "("+listItem.itemExchangeAmount+")"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeEndViewHolder {
        val binding = ExchangeEndListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExchangeEndViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExchangeEndAdapter.ExchangeEndViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
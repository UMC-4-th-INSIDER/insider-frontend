package com.umc.insider.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.insider.OnNoteListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.databinding.ExchangeItemBinding
import com.umc.insider.model.ExchangeItem
import okhttp3.internal.connection.Exchange

class ExchangeAdapter(private val onNoteListener: OnNoteListener) :
    ListAdapter<ExchangeItem, ExchangeAdapter.ExchangeViewHolder>(DiffCallback){

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

    inner class ExchangeViewHolder(private val binding : ExchangeItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

            init{
                itemView.setOnClickListener(this)
            }

            fun bind(exchangeItem : ExchangeItem){
                binding.itemName.text = exchangeItem.itemName
                binding.itemAmount.text = "(" + exchangeItem.itemAmount + ")"
                binding.itemExchange.text = exchangeItem.itemExchange
            }


            override fun onClick(v: View?) {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    onNoteListener.onNotePurchaseDetail(position)
                }
            }


        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val binding = ExchangeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExchangeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItemAtPosition(position: Int): ExchangeItem {
        return getItem(position)
    }

}
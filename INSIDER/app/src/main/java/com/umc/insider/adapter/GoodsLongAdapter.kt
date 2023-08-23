package com.umc.insider.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.ListAdapter
import com.umc.insider.OnNoteListener
import com.umc.insider.databinding.GoodsLongItemBinding
import com.umc.insider.retrofit.model.GoodsGetRes

class GoodsLongAdapter(private val listener : OnNoteListener) : ListAdapter<GoodsGetRes, GoodsLongAdapter.GoodsLongViewHolder>(DiffCallback) {

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<GoodsGetRes>(){
            override fun areItemsTheSame(oldItem: GoodsGetRes, newItem: GoodsGetRes): Boolean {
                return oldItem.title == newItem.title
                //return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GoodsGetRes, newItem: GoodsGetRes): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class GoodsLongViewHolder(private val binding: GoodsLongItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(goods : GoodsGetRes){

                binding.itemName.text = goods.title
                binding.itemPrice.text = goods.price
                if(goods.sale_price == null){
                    binding.salePrice.visibility = View.INVISIBLE
                    binding.arrowImg.visibility = View.INVISIBLE
                    binding.itemDiscountRate.visibility = View.INVISIBLE
                }else{
                    binding.salePrice.visibility = View.VISIBLE
                    binding.arrowImg.visibility = View.VISIBLE
                    binding.itemDiscountRate.visibility = View.VISIBLE
                    binding.salePrice.text = "${goods.sale_price}원"
                    binding.itemDiscountRate.text = "${goods.sale_percent}%"
                }


                if(goods.weight.isNullOrBlank()){
                    binding.itemWeightOrRest.text = "(${goods.rest}개)"
                    binding.unit.text = "(개당)"
                }else{
                    binding.itemWeightOrRest.text = "(${goods.weight})"
                    binding.unit.text = "(100g당)"
                }


                Glide.with(binding.goodsImg.context)
                    .load(goods.img_url)
                    .placeholder(null)
                    .into(binding.goodsImg)

                binding.goods.setOnClickListener {
                    listener.onNotePurchaseDetail(goods.goods_id)
                }
            }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsLongViewHolder {
        val binding =
            GoodsLongItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoodsLongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoodsLongViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
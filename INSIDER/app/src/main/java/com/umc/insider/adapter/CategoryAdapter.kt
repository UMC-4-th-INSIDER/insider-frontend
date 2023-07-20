package com.umc.insider.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.R

class CategoryAdapter(
    private val imageArray: IntArray,
    private val clickedImageArray: IntArray
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.categoryImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.imageView.setImageResource(imageArray[position])

        holder.imageView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    holder.imageView.setImageResource(clickedImageArray[position])
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    holder.imageView.setImageResource(imageArray[position])
                    true
                }
                else -> false
            }
        }
    }

    override fun getItemCount() = imageArray.size
}
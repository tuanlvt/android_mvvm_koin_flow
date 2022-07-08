package com.example.flowmvvm.base.recyclerView

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.example.flowmvvm.data.model.BaseModel

class BaseDiffCallback<T>
constructor(
        @Nullable private var olds: MutableList<T>,
        @Nullable private var news: MutableList<T>) : DiffUtil.Callback() {
    
    override fun getOldListSize(): Int {
        return olds.size
    }
    
    override fun getNewListSize(): Int {
        return news.size
    }
    
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: BaseModel<*>? = olds[oldItemPosition] as BaseModel<*>?
        val newItem: BaseModel<*>? = news[newItemPosition] as BaseModel<*>?
        return oldItem?.id == newItem?.id
    }
    
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: BaseModel<*>? = olds[oldItemPosition] as BaseModel<*>?
        val newItem: BaseModel<*>? = news[newItemPosition] as BaseModel<*>?
        return oldItem == newItem
    }
}
package com.example.flowmvvm.base.recyclerView

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * BaseItemVH
 *
 * @param <T> is Object
 *
 */

abstract class BaseItemVH<T>
constructor(
        private val view: ViewBinding,
        private val listener: OnItemClickListener<T>? = null) : RecyclerView.ViewHolder(view.root) {
    
    fun bindModel(data: T) {
        bindView(data)
    
        view.root.setOnClickListener {
            listener?.onItemClick(data, bindingAdapterPosition, it)
        }
    }
    
    abstract fun bindView(data: T)
}
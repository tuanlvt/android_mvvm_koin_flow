package com.example.flowmvvm.base.recyclerView

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * Base Adapter.
 *
 * @param <V> is a type extend from {@link RecyclerView.ViewHolder}
 * @param <T> is a Object
 */

abstract class BaseRecyclerViewAdapter<T, V : RecyclerView.ViewHolder>
constructor(protected var dataList: MutableList<T> = mutableListOf()) : RecyclerView.Adapter<V>() {
    
    protected var itemClickListener: OnItemClickListener<T>? = null
    
    override fun getItemCount(): Int {
        return dataList.size
    }
    
    fun getData(): MutableList<T> {
        return dataList
    }
    
    fun setData(newData: MutableList<T>?) {
        if (newData == null) return
        dataList = newData
        notifyDataSetChanged()
    }
    
    fun updateData(newData: MutableList<T>?) {
        if (newData == null) return
        val callBack = BaseDiffCallback(dataList, newData)
        val diffResult = DiffUtil.calculateDiff(callBack)
        dataList = newData
        diffResult.dispatchUpdatesTo(this)
    }
    
    fun addData(newData: MutableList<T>?) {
        if (newData.isNullOrEmpty()) return
        val positionStart = itemCount
        val addMoreItemCount = newData.size
        dataList.addAll(newData)
        notifyItemRangeInserted(positionStart, addMoreItemCount)
    }
    
    fun clearData(isNotify: Boolean = true) {
        dataList.clear()
        if (isNotify) notifyDataSetChanged()
    }
    
    fun getItem(position: Int): T? {
        return if (position < 0 || position >= dataList.size) {
            null
        } else dataList[position]
    }
    
    fun addItem(data: T, position: Int) {
        dataList.add(position, data)
        notifyItemInserted(position)
    }
    
    fun removeItem(position: Int) {
        if (position < 0 || position >= dataList.size) {
            return
        }
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }
    
    fun replaceItem(item: T, position: Int) {
        if (position < 0 || position >= dataList.size) {
            return
        }
        dataList[position] = item
        notifyItemChanged(position)
    }
    
    fun registerItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        itemClickListener = onItemClickListener
    }
    
    fun unRegisterItemClickListener() {
        itemClickListener = null
    }
    
    companion object {
        private const val TAG = "BaseRecyclerViewAdapter"
    }
    
}
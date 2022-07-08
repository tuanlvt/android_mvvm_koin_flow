package com.example.flowmvvm.base.recyclerView

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flowmvvm.base.paging.NetworkState

@Suppress("UNCHECKED_CAST")
abstract class LoadMoreAdapter<T> : BaseRecyclerViewAdapter<T, RecyclerView.ViewHolder>() {
    
    private var networkState: NetworkState<Any>? = null
    
    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == networkStatePosition()) {
            ItemLoadMoreVH.TYPE_LOAD_MORE
        } else {
            getViewType(position)
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemLoadMoreVH.TYPE_LOAD_MORE -> ItemLoadMoreVH.create(parent)
            else -> onCreateVH(parent, viewType)
        }
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemLoadMoreVH) {
            holder.bindTo(networkState = networkState)
            return
        }
        onBindVH(holder, position)
    }
    
    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }
    
    private fun networkStatePosition(): Int = itemCount - 1
    
    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState == NetworkState.LOAD_MORE
    }
    
    /**
     * Set the current network state to the adapter
     * but this work only after the initial load
     * and the adapter already have list to add new loading raw to it
     * so the initial loading state the activity responsible for handle it
     *
     * @param newNetworkState the new network state
     */
    fun setNetworkState(newNetworkState: NetworkState<Any>?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else {
            if (hasExtraRow && previousState !== newNetworkState) {
                notifyItemChanged(networkStatePosition())
            }
        }
    }
    
    protected abstract fun onCreateVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    
    protected abstract fun onBindVH(holder: RecyclerView.ViewHolder, position: Int)
    
    protected abstract fun getViewType(position: Int): Int
}
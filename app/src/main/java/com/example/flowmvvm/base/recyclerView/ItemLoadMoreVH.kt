package com.example.flowmvvm.base.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flowmvvm.base.paging.NetworkState
import com.example.flowmvvm.databinding.ItemLoadMoreBinding

class ItemLoadMoreVH
constructor(private val binding: ItemLoadMoreBinding) : BaseItemVH<Boolean>(binding) {
    
    fun bindTo(networkState: NetworkState<Any>?) {
        binding.progress.visibility = toVisibility(networkState == NetworkState.LOAD_MORE)
    }
    
    private fun toVisibility(constraint: Boolean): Int {
        return if (constraint) View.VISIBLE else View.GONE
    }
    
    companion object {
        const val TYPE_LOAD_MORE = 0xFFFF
        
        fun create(parent: ViewGroup): ItemLoadMoreVH {
            val inflater = LayoutInflater.from(parent.context)
            return ItemLoadMoreVH(ItemLoadMoreBinding.inflate(inflater, parent, false))
        }
    }
    
    override fun bindView(data: Boolean) {
    
    }
}
package com.example.flowmvvm.screen.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.flowmvvm.base.paging.BasePagingDataAdapter
import com.example.flowmvvm.base.recyclerView.BaseItemVH
import com.example.flowmvvm.base.recyclerView.OnItemClickListener
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.databinding.ItemUserBinding
import com.example.flowmvvm.utils.extension.loadImageCircleUrl
import com.example.flowmvvm.utils.extension.notNull

class PagingUserAdapter(private val listener: OnItemClickListener<User>)
    : BasePagingDataAdapter<User>(DIFF_CALLBACK) {
    
    override fun onCreateVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding, listener)
    }
    
    override fun onBindVH(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            getItem(position).notNull { holder.bindModel(it) }
        }
    }
    
    override fun getViewType(position: Int): Int = 0
    
    companion object {
        
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            
            override fun areItemsTheSame(oldConcert: User, newConcert: User) =
                    oldConcert.id == newConcert.id
            
            override fun areContentsTheSame(oldConcert: User, newConcert: User) =
                    oldConcert == newConcert
        }
        
        class ItemViewHolder(
                private val binding: ItemUserBinding,
                listener: OnItemClickListener<User>?) : BaseItemVH<User>(binding, listener) {
            
            override fun bindView(data: User) {
                with(binding) {
                    txtName.text = data.name.toString()
                    txtSubName.text = data.fullName.toString()
                    imgAvatar.loadImageCircleUrl(data.owner?.avatarUrl)
                }
            }
        }
    }
}
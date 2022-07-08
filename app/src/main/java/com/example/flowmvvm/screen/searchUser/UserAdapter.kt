package com.example.flowmvvm.screen.searchUser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flowmvvm.base.recyclerView.BaseItemVH
import com.example.flowmvvm.base.recyclerView.LoadMoreAdapter
import com.example.flowmvvm.base.recyclerView.OnItemClickListener
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.databinding.ItemUserBinding
import com.example.flowmvvm.utils.extension.loadImageCircleUrl
import com.example.flowmvvm.utils.extension.notNull

class UserAdapter : LoadMoreAdapter<User>() {
    
    override fun onCreateVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding, itemClickListener)
    }
    
    override fun onBindVH(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            getItem(position).notNull { holder.bindModel(it) }
        }
    }
    
    override fun getViewType(position: Int): Int {
        return 0
    }
    
    companion object {
        
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
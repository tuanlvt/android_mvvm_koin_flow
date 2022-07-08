package com.example.flowmvvm.screen.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flowmvvm.base.BaseFragment
import com.example.flowmvvm.base.recyclerView.OnItemClickListener
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.databinding.FragmentFavoriteUserBinding
import com.example.flowmvvm.screen.searchUser.UserAdapter
import com.example.flowmvvm.utils.liveData.autoCleared
import kotlin.reflect.KClass

class FavoriteUserFragment : BaseFragment<FragmentFavoriteUserBinding, FavoriteUserViewModel>() {
    
    override val viewModelClass: KClass<FavoriteUserViewModel>
        get() = FavoriteUserViewModel::class
    
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFavoriteUserBinding
        get() = FragmentFavoriteUserBinding::inflate
    
    private var adapter by autoCleared<UserAdapter>()
    
    override fun setupView() {
        adapter = UserAdapter().apply {
            registerItemClickListener(object : OnItemClickListener<User> {
                override fun onItemClick(item: User, position: Int, view: View?) {
                    viewModel.deleteUser(item)
                }
            })
        }
        
        with(binding.recyclerView) {
            adapter = this@FavoriteUserFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }
    
    override fun bindView() {
        viewModel.users.observe(viewLifecycleOwner) {
            adapter.updateData(it.toMutableList())
        }
    }
    
    companion object {
        private const val TAG = "FavoriteUserFragment"
        fun newInstance() = FavoriteUserFragment()
    }
}
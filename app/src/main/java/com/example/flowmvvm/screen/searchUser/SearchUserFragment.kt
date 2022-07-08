package com.example.flowmvvm.screen.searchUser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flowmvvm.base.BaseFragment
import com.example.flowmvvm.base.paging.NetworkState
import com.example.flowmvvm.base.recyclerView.EndlessRecyclerOnScrollListener
import com.example.flowmvvm.base.recyclerView.OnItemClickListener
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.databinding.FragmentSearchUserBinding
import com.example.flowmvvm.screen.MainViewModel
import com.example.flowmvvm.utils.LogUtils
import com.example.flowmvvm.utils.extension.*
import com.example.flowmvvm.utils.liveData.autoCleared
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.reflect.KClass

class SearchUserFragment : BaseFragment<FragmentSearchUserBinding, SearchUserViewModel>() {
    
    override val viewModelClass: KClass<SearchUserViewModel>
        get() = SearchUserViewModel::class
    
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchUserBinding
        get() = FragmentSearchUserBinding::inflate
    
    private var adapter by autoCleared<UserAdapter>()
    
    private var scrollListener by autoCleared<EndlessRecyclerOnScrollListener>()
    
    private val mainViewModel by sharedViewModel<MainViewModel>()
    
    override fun setupView() {
        
        binding.menu.clicks()
                .throttleFirst(3000)
                .onEach {
                    LogUtils.d("clicks", "TEST")
                }
                .launchIn(coroutineScope)
        
        adapter = UserAdapter().apply {
            registerItemClickListener(object : OnItemClickListener<User> {
                override fun onItemClick(item: User, position: Int, view: View?) {
                    viewModel.insertUser(item)
        
                    coroutineScope.launch { mainViewModel.testBehaviorRelay.onNext(value = item) }
                }
            })
        }
        
        with(binding.swipeRefreshLayout) {
            setOnRefreshListener {
                isRefreshing = true
                adapter.clearData()
                viewModel.searchUser(binding.edtSearch.text.toString())
            }
        }
        
        with(binding.recyclerView) {
            adapter = this@SearchUserFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            
            scrollListener = object : EndlessRecyclerOnScrollListener(layoutManager) {
                override fun onLoadMore(currentPage: Int) {
                    viewModel.searchUser(binding.edtSearch.text.toString(), currentPage)
                }
            }
            addOnScrollListener(scrollListener)
        }
        
        viewModel.initSearch(stateFlow = binding.edtSearch.searchTextChanged())
    }
    
    override fun bindView() {
        viewModel.networkState.observe(this, { state ->
            LogUtils.thread()
            with(binding) {
                swipeRefreshLayout.isRefreshing = false
                progress.hide()
                recyclerView.postDelayed({ adapter.setNetworkState(state) }, 100)
            }
    
            when (state) {
                is NetworkState.FETCH -> {
                    scrollListener.reset()
                    binding.progress.show()
                }
                is NetworkState.SUCCESS -> {
                    if (state.isLoadMore) {
                        adapter.addData(state.data)
                    } else {
                        adapter.updateData(state.data)
                    }
                }
                is NetworkState.ERROR -> {
                    LogUtils.thread()
                    onHandleError(state.throwable)
                }
                else -> {
                }
            }
        })
    }
    
    companion object {
        private const val TAG = "SearchUserFragment"
        fun newInstance() = SearchUserFragment()
    }
}
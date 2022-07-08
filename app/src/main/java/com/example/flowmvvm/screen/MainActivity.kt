package com.example.flowmvvm.screen

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Toast
import com.example.flowmvvm.R
import com.example.flowmvvm.base.BaseActivity
import com.example.flowmvvm.databinding.ActivityMainBinding
import com.example.flowmvvm.screen.favorite.FavoriteUserFragment
import com.example.flowmvvm.screen.paging.PagingUserFragment
import com.example.flowmvvm.screen.searchUser.SearchUserFragment
import com.example.flowmvvm.widgets.navigation.NavHelper
import com.example.flowmvvm.widgets.navigation.NavHelperImpl
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.reflect.KClass

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModelClass: KClass<MainViewModel>
        get() = MainViewModel::class

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    private val searchUserFragment by lazy { SearchUserFragment.newInstance() }
    private val favoriteUserFragment by lazy { FavoriteUserFragment.newInstance() }
    private val pagingUserFragment by lazy { PagingUserFragment.newInstance() }

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var isDoubleTapBack = false

    val navHelper: NavHelper by lazy {
        NavHelperImpl.createBuilder()
            .tabs(numberOfTab = 3)
            .mainFraManager(fragManager = supportFragmentManager)
            .mainContainerViewId(resLayoutID = R.id.mainContainer)
            .setup()
    }

    private val onNavigationItemSelectedListener by lazy {
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab1 -> navHelper.switchTab(tab = TAB1, fragment = searchUserFragment)
                R.id.tab2 -> navHelper.switchTab(tab = TAB2, fragment = favoriteUserFragment)
                R.id.tab3 -> navHelper.switchTab(tab = TAB3, fragment = pagingUserFragment)
            }
            return@OnNavigationItemSelectedListener true
        }
    }

    override fun setupView() {
        navHelper.addOrReplaceFrag(fragment = searchUserFragment)

        handler = Handler(Looper.getMainLooper())
        runnable = Runnable { isDoubleTapBack = false }

        binding.bottomNav.selectedItemId = R.id.tab1
        binding.bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun bindView() {
        // No-Op
    }

    override fun onBackPressed() {
        if (navHelper.isCanPopBack()) {
            return
        }
        if (navHelper.getCurrentTab() != TAB1) {
            binding.bottomNav.selectedItemId = R.id.tab1
            navHelper.switchTab(TAB1, searchUserFragment)
            return
        }
        if (isDoubleTapBack) {
            finish()
            return
        }
        isDoubleTapBack = true
        Toast.makeText(this, "please click again to exit", Toast.LENGTH_SHORT).show()
        handler.postDelayed(runnable, DELAY_TIME_TWO_TAP_BACK_BUTTON)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        binding.bottomNav.setOnNavigationItemReselectedListener(null)
        super.onDestroy()
    }

    companion object {
        fun getInstance(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }

        const val DELAY_TIME_TWO_TAP_BACK_BUTTON: Long = 2000
        const val TAB1 = 0
        const val TAB2 = 1
        const val TAB3 = 2
    }
}
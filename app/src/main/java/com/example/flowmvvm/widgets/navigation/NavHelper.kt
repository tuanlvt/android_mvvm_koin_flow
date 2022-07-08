package com.example.flowmvvm.widgets.navigation

import androidx.fragment.app.Fragment

interface NavHelper {

    fun addOrReplaceFrag(
        fragment: Fragment,
        isAddFrag: Boolean = false,
        addToBackStack: Boolean = true,
        animateType: NavAnimateType = NavAnimateType.NONE,
        tag: String = fragment::class.java.simpleName
    )

    fun switchTab(
        tab: Int,
        fragment: Fragment,
        isAddFrag: Boolean = false,
        addToBackStack: Boolean = true,
        animateType: NavAnimateType = NavAnimateType.NONE,
        tag: String = fragment::class.java.simpleName
    )

    fun getFragment(position: Int): Fragment?

    fun getCurrentFrag(): Fragment

    fun getCurrentTab(): Int

    fun popBack(isToRoot: Boolean = false)

    fun isCanPopBack(): Boolean
}
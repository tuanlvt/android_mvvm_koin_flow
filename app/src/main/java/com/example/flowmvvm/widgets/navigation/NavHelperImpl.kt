package com.example.flowmvvm.widgets.navigation

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.flowmvvm.utils.LogUtils
import com.example.flowmvvm.utils.extension.generateTag
import com.example.flowmvvm.utils.extension.notNull
import com.example.flowmvvm.utils.extension.setAnimations
import com.example.flowmvvm.utils.extension.transact

open class NavHelperImpl
constructor(
    private val tabs: List<Int>?,
    private val mainFraManager: FragmentManager?,
    private val mainContainerViewId: Int?,
    private val fragListRoot: MutableList<ContainerFragment> = mutableListOf()
) : NavHelper {

    private var curFragRoot: ContainerFragment

    private val handler = Handler(Looper.getMainLooper())

    private var curTab = -1
        set(value) {
            value.notNull {
                field = it
                LogUtils.d(TAG, "Current Tab: $it")
            }
        }

    init {
        if (tabs == null) {
            throw IllegalArgumentException("tabs is null")
        }
        if (tabs.size <= MIN_NUM_TABS) {
            throw IllegalArgumentException("Number of tabs cannot be less than $MIN_NUM_TABS")
        }
        if (mainFraManager == null) {
            throw IllegalArgumentException("mainFraManager is null")
        }
        if (mainContainerViewId == null) {
            throw IllegalArgumentException("mainContainerViewId is null")
        }

        mainFraManager.transact {

            tabs.forEachIndexed { index, tab ->
                val fragment = ContainerFragment.newInstance(tab = tab)
                fragListRoot.add(index, fragment)

                val tag = fragment.generateTag()
                addToBackStack(tag)
                add(mainContainerViewId, fragment, tag)
            }
        }

        curTab = tabs.first()
        curFragRoot = fragListRoot.first()
    }

    override fun addOrReplaceFrag(
        fragment: Fragment,
        isAddFrag: Boolean,
        addToBackStack: Boolean,
        animateType: NavAnimateType,
        tag: String
    ) {
        handler.post {
            curFragRoot.addOrReplaceFrag(
                fragment = fragment,
                isAddFrag = isAddFrag,
                addToBackStack = addToBackStack,
                animateType = animateType,
                tag = tag
            )
        }
    }

    override fun switchTab(
        tab: Int,
        fragment: Fragment,
        isAddFrag: Boolean,
        addToBackStack: Boolean,
        animateType: NavAnimateType,
        tag: String
    ) {

        if (curTab == tab) return

        val switchFragRoot = getFragRoot(tab = tab) ?: return

        mainFraManager?.transact {
            setAnimations(animateType = animateType)
            hide(curFragRoot).show(switchFragRoot)
        }

        switchFragRoot.addOrReplaceFrag(
            fragment = fragment,
            isAddFrag = isAddFrag,
            addToBackStack = addToBackStack,
            animateType = animateType,
            tag = tag
        )

        curTab = tab
        curFragRoot = switchFragRoot
    }

    override fun getFragment(position: Int): Fragment? {
        return try {
            fragListRoot[position]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    override fun getCurrentFrag(): Fragment = curFragRoot

    override fun getCurrentTab(): Int = curTab

    override fun popBack(isToRoot: Boolean) = curFragRoot.popBack(isToRoot = isToRoot)

    override fun isCanPopBack(): Boolean {
        return curFragRoot.isCanPopBack()
    }

    private fun getFragRoot(tab: Int): ContainerFragment? {
        tabs?.forEachIndexed { index, tabRoot ->
            if (tab == tabRoot) {
                return fragListRoot[index]
            }
        }
        return null
    }

    companion object {
        private const val TAG = "NavHelperImpl"

        private const val MIN_NUM_TABS = 1

        fun createBuilder() = Builder()

        data class Builder(
            private var tabs: List<Int>? = null,
            private var mainFraManager: FragmentManager? = null,
            private var mainContainerViewId: Int? = null
        ) {

            fun setup() = NavHelperImpl(
                tabs = tabs,
                mainFraManager = mainFraManager,
                mainContainerViewId = mainContainerViewId
            )

            fun tabs(numberOfTab: Int) = apply {
                val tabs = arrayListOf<Int>()
                for (i in 0 until numberOfTab) {
                    tabs.add(i)
                }
                this.tabs = tabs
            }

            fun mainFraManager(
                fragManager: FragmentManager
            ) = apply { this.mainFraManager = fragManager }

            fun mainContainerViewId(
                resLayoutID: Int
            ) = apply { this.mainContainerViewId = resLayoutID }

        }

    }
}

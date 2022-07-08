package com.example.flowmvvm.utils.extension

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.flowmvvm.R
import com.example.flowmvvm.widgets.navigation.NavAnimateType

fun Fragment.addOrReplaceFragment(
        @IdRes containerId: Int,
        fragmentManager: FragmentManager? = parentFragmentManager,
        fragment: Fragment,
        isAddFrag: Boolean,
        addToBackStack: Boolean = true,
        animateType: NavAnimateType,
        tag: String = fragment::class.java.simpleName
) {
    fragmentManager?.transact {
        setAnimations(animateType = animateType)
        
        if (addToBackStack) {
            addToBackStack(tag)
        }
        
        if (isAddFrag) {
            add(containerId, fragment, tag)
        } else {
            replace(containerId, fragment, tag)
        }
    }
}

fun Fragment.popBackFragment(): Boolean {
    with(parentFragmentManager) {
        val isShowPreviousPage = this.backStackEntryCount > 0
        if (isShowPreviousPage) {
            this.popBackStackImmediate()
        }
        return isShowPreviousPage
    }
}

fun Fragment.generateTag(): String {
    return this::class.java.simpleName
}

fun FragmentManager.isExitFragment(tag: String): Boolean {
    return this.findFragmentByTag(tag) != null
}

fun FragmentTransaction.setAnimations(animateType: NavAnimateType) {
    when (animateType) {
        NavAnimateType.FADE -> {
            setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
        }
        NavAnimateType.SLIDE_DOWN -> {
            setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
        }
        NavAnimateType.SLIDE_UP -> {
            setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
        }
        NavAnimateType.SLIDE_LEFT -> {
            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, 0, 0)
        }
        NavAnimateType.SLIDE_RIGHT -> {
            setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_right)
        }
        else -> {
        }
    }
}

/**
 * Runs a FragmentTransaction, then calls commitAllowingStateLoss().
 */
inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}





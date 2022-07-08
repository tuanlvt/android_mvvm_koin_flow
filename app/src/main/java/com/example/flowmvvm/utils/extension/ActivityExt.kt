package com.example.flowmvvm.utils.extension

import android.content.Intent
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flowmvvm.widgets.navigation.NavAnimateType

/**
 * Various extension functions for AppCompatActivity.
 */

fun AppCompatActivity.startActivity(@NonNull intent: Intent, flags: Int? = null) {
    flags.notNull { intent.flags = it }
    startActivity(intent)
}

fun AppCompatActivity.finishActivity(requestCode: Int? = null) {
    requestCode?.let {
        finishActivity(it)
    } ?: kotlin.run {
        finish()
    }
}


fun AppCompatActivity.addOrReplaceFragment(
        @IdRes containerId: Int,
        fragment: Fragment,
        isAddFrag: Boolean,
        addToBackStack: Boolean = true,
        animateType: NavAnimateType,
        tag: String = fragment::class.java.simpleName
) {
    supportFragmentManager.transact {
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

fun AppCompatActivity.popBackFragment(): Boolean {
    val isShowPreviousPage = supportFragmentManager.backStackEntryCount > 0
    if (isShowPreviousPage) {
        supportFragmentManager.popBackStackImmediate()
    }
    return isShowPreviousPage
}

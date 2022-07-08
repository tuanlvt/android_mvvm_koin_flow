package com.example.flowmvvm.utils.extension

import android.content.Context
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun Context.showToast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
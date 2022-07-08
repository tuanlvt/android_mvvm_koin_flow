package com.example.flowmvvm.utils.extension

import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.flowmvvm.utils.GlideApp

fun ImageView.loadImageUrl(url: String?) {
    if (url.isNullOrEmpty()) return
    GlideApp.with(context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
}

fun ImageView.loadImageCircleUrl(url: String?) {
    if (url.isNullOrEmpty()) return
    GlideApp.with(context)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(this)
}
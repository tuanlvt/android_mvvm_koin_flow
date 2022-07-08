package com.example.flowmvvm.base.recyclerView

import android.view.View
import com.example.flowmvvm.utils.Constants.POSITION_DEFAULT

/**
 * OnItemClickListener
 *
 * @param <T> Data from item click
</T> */

interface OnItemClickListener<T> {
    fun onItemClick(item: T, position: Int = POSITION_DEFAULT, view: View? = null)
}
package com.woowacourse.ody.presentation.common.listener

import android.view.View

fun View.setOnSingleClickListener(onSingleClick: (View) -> Unit) {
    val singleClickListener = SingleClickListener { onSingleClick(this) }
    this.setOnClickListener(singleClickListener)
}

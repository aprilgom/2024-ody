package com.woowacourse.ody.presentation.common.listener

import android.view.View

class SingleClickListener(
    private val interval: Long = SINGLE_CLICK_INTERVAL,
    private val onSingleClick: (View) -> Unit,
) : View.OnClickListener {
    private var lastClickTime = 0L

    override fun onClick(view: View) {
        val now = System.currentTimeMillis()
        if (now - lastClickTime < interval) {
            return
        }
        lastClickTime = now
        onSingleClick(view)
    }

    companion object {
        private const val SINGLE_CLICK_INTERVAL = 600L
    }
}

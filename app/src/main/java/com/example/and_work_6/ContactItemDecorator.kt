package com.example.and_work_6

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ContactItemDecorator(private val offsetInDp: Float, private val dividerId: Int) :
    RecyclerView.ItemDecoration() {

    private var divider: Drawable? = null

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val offsetInPx = getDimenInPx(parent.resources, offsetInDp)
        outRect.top = offsetInPx
        outRect.bottom = offsetInPx
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        if (childCount == 0) return
        divider = ContextCompat.getDrawable(parent.context, dividerId)
        val offsetInPx = getDimenInPx(parent.resources, offsetInDp)
        for (i in 0..childCount) {
            val child: View? = parent.getChildAt(i)
            child?.let {
                val params = it.layoutParams as RecyclerView.LayoutParams

                val left = it.left - params.leftMargin + offsetInPx
                val right = it.right + params.rightMargin - offsetInPx
                val top = it.bottom + params.bottomMargin + offsetInPx
                val bottom = top + (divider?.intrinsicHeight ?: 0)

                divider?.setBounds(left, top, right, bottom)
                divider?.draw(c)
            }
        }
    }

    private fun getDimenInPx(resources: Resources, dimenInDp: Float): Int {
        return (dimenInDp * resources.displayMetrics.density).toInt();
    }
}
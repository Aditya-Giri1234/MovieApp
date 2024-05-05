package com.aditya.movieapp.util.other

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class FooterDecoration (private val footerView: View) : RecyclerView.ItemDecoration() {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        footerView.layout(parent.left, 0, parent.right, footerView.measuredHeight)
        val count = parent.adapter!!.itemCount

    parent.getChildAt(parent.childCount - 1)?.let {view ->
        if (parent.getChildAdapterPosition(view) == count - 1) {
            c.save()

            val rect = Rect()
            view.getGlobalVisibleRect(rect)
            c.translate(0f, (rect.top).toFloat())

            footerView.draw(c)
            c.restore()
        }
        }

    }
}
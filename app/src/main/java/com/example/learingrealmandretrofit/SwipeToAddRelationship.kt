package com.example.learingrealmandretrofit

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToAddRelationship(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){

    private val addRelations = ContextCompat.getDrawable(context, R.drawable.ic_list_add)!!
    private val intrinsicWidth = addRelations.intrinsicWidth
    private val intrinsicHeight = addRelations.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.GREEN
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(canvas, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Draw the green background
        background.color = backgroundColor
        background.setBounds(itemView.left + dX.toInt(), itemView.top, itemView.left, itemView.bottom)
        background.draw(canvas)

        // Calculate position of addRelations icon
        val addRelationsIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val addRelationsIconMargin = (itemHeight - intrinsicHeight) / 2
        val addRelationsIconLeft = itemView.left + addRelationsIconMargin
        val addRelationsIconRight = addRelationsIconLeft + intrinsicWidth
        val addRelationsIconBottom = addRelationsIconTop + intrinsicHeight

        // Draw the addRelations icon
        addRelations.setBounds(addRelationsIconLeft, addRelationsIconTop, addRelationsIconRight, addRelationsIconBottom)
        addRelations.draw(canvas)

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int
    ) {
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}

package com.example.eppdraft1.main.utils

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.eppdraft1.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class SwipeGesture (val context : Context) : ItemTouchHelper.SimpleCallback(0,
    ItemTouchHelper.LEFT) {


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    /*
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
     */

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {

        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.white))
            .addSwipeLeftCornerRadius(3, 7f)
            .addSwipeLeftPadding(3, 5f, 0f, 5f)
            .addSwipeLeftLabel(context.resources.getString(R.string.update_reservation))
            .addSwipeLeftActionIcon(R.drawable.blue_add_workout_36)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
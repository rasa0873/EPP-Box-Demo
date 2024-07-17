package com.example.eppdraft1.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eppdraft1.R
import com.example.eppdraft1.main.activities.BaseActivity
import com.example.eppdraft1.main.models.Workout
import kotlinx.android.synthetic.main.item_workout.view.*
import kotlinx.android.synthetic.main.item_workout.view.cv_workout_item
import kotlinx.android.synthetic.main.item_workout_week.view.*

open class WeekWorkoutListAdapter (private val context : Context,
                                   private val list: ArrayList<Workout>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener : OnClickListener? = null
    private lateinit var imageViewReservedIcon: ImageView

    private var workoutReserved : Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
            .inflate(R.layout.item_workout_week, parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = list[position] // model = Workout instance

        if (holder is MyViewHolder){

            holder.itemView.tv_item_week_workout_description.text = model.description
            if (model.athleteAttendees.size >0){
                holder.itemView.tv_item_week_workout_vacancies.text = model.athleteAttendees.size.toString()
            } else {
                holder.itemView.tv_item_week_workout_vacancies.text = ""
            }

            val time = model.dateTime

            holder.itemView.tv_week_workout_time.text = time

            holder.itemView.tv_item_week_workout_trainer.text = model.trainerName

            holder.itemView.cv_week_workout_item.setOnClickListener {
                if (onClickListener!=null){
                    onClickListener!!.onClick(model.timeCreatedMillis)
                }
            }

            if (model.active == "No"){
                holder.itemView.tv_week_workout_time.setTextColor(ContextCompat.getColor(context, R.color.dark_gray1))
            }

            workoutReserved = false // reset to false per item analysis
            for (user in model.athleteAttendees){
                if (user.id == BaseActivity().getCurrentUserId()){
                    workoutReserved = true
                }
            }
            if (workoutReserved) {
                holder.itemView.iv_item_week_workout_reserved.visibility = View.VISIBLE
            } else {
                holder.itemView.iv_item_week_workout_reserved.visibility = View.INVISIBLE
            }

            holder.itemView.cv_week_workout_item.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_transition)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }



    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)


    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener{
        fun onClick(timeDateInMillis: String)
    }

}
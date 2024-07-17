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
import kotlinx.android.synthetic.main.item_my_workout.view.*
import kotlinx.android.synthetic.main.item_workout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

open class MyWorkoutListAdapter(private val context : Context,
                                private val list: ArrayList<Workout>,
                                private val currentTimeMillis: Long = 0)
                : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private var onClickListener : OnClickListener? = null
    private var attendedToWorkout : Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_my_workout, parent, false)
       //return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_workout, parent, false))
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position] // model = Workout instance

        if (holder is MyViewHolder){

            holder.itemView.tv_item_my_workout_description.text = model.description
            if (model.athleteAttendees.size > 0){
                holder.itemView.tv_item_my_workout_vacancies.text = model.athleteAttendees.size.toString()
            } else {
                holder.itemView.tv_item_my_workout_vacancies.text = ""
            }

            val day = model.dateDay
            val monthNumber = model.dateMonth
            val time = model.dateTime

            holder.itemView.cv_my_workout_item.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_transition)

            holder.itemView.tv_my_workout_day.text = day
            holder.itemView.tv_my_workout_month.text = monthConverter(monthNumber)
            holder.itemView.tv_my_workout_time.text = time

            holder.itemView.tv_item_my_workout_trainer.text = model.trainerName
            holder.itemView.tv_item_my_workout_year.text = model.dateYear

            holder.itemView.cv_my_workout_item.setOnClickListener {
                if (onClickListener!=null){
                    onClickListener!!.onClick(model.timeCreatedMillis)
                }
            }

            if (model.active == "No"){
                holder.itemView.tv_my_workout_day.setTextColor(ContextCompat.getColor(  context, R.color.dark_gray1))
                holder.itemView.tv_my_workout_month.setTextColor(ContextCompat.getColor(context, R.color.dark_gray1))
                holder.itemView.tv_my_workout_time.setTextColor(ContextCompat.getColor(context, R.color.dark_gray1))
            }

            for (attendee in model.athleteAttendees){
                if (attendee.attended == "Yes"){
                    attendedToWorkout = true
                }
            }
            if (attendedToWorkout){
                holder.itemView.iv_item_my_workout_attended.setImageResource(R.drawable.blue_check_circle_24)
            } else {
                holder.itemView.iv_item_my_workout_attended.setImageResource(R.drawable.dark_round_cancel_24)
            }
            holder.itemView.iv_item_my_workout_attended.alpha = 0.5f

            if (currentTimeMillis > 0) {
                if (checkTimePassed(currentTimeMillis, model)) {
                    holder.itemView.iv_item_my_workout_attended.visibility = View.VISIBLE
                } else {
                    holder.itemView.iv_item_my_workout_attended.visibility = View.INVISIBLE
                }
            }


        }
    }

    override fun getItemCount(): Int {
       return list.size
    }



    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private fun monthConverter(monthNumber: String): String {
        val monthText: String = when(monthNumber){
            "1" -> context.resources.getString(R.string.january)
            "2" -> context.resources.getString(R.string.february)
            "3" -> context.resources.getString(R.string.march)
            "4" -> context.resources.getString(R.string.april)
            "5" -> context.resources.getString(R.string.may)
            "6" -> context.resources.getString(R.string.june)
            "7" -> context.resources.getString(R.string.july)
            "8" -> context.resources.getString(R.string.august)
            "9" -> context.resources.getString(R.string.september)
            "10" -> context.resources.getString(R.string.october)
            "11" -> context.resources.getString(R.string.november)
            "12" -> context.resources.getString(R.string.december)
            else -> ""
        }

        return monthText
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener{
        fun onClick(timeDateInMillis: String)
    }

    private fun checkTimePassed(currentTimeMillis: Long, workout: Workout): Boolean {
        // Check if time and date passed
        val timeCreatedMillisHourGap: Long = workout.timeCreatedMillis.toLong() - 3600000
        return currentTimeMillis >= timeCreatedMillisHourGap
    }

}
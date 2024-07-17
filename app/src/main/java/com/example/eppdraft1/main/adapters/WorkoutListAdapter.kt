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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

open class WorkoutListAdapter(private val context : Context,
                              private val list: ArrayList<Workout>,
                              private val day: String = "")
                : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private var onClickListener : OnClickListener? = null
    private var reservedWorkout : Boolean = false
    private lateinit var imageViewReservedIcon: ImageView



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return MyViewHolder(LayoutInflater.from(context)
           .inflate(R.layout.item_workout, parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position] // model = Workout instance

        if (holder is MyViewHolder){

            holder.itemView.tv_item_workout_description.text = model.description
            if (model.athleteAttendees.size > 0){
                holder.itemView.tv_item_workout_vacancies.text = model.athleteAttendees.size.toString()
            } else {
                holder.itemView.tv_item_workout_vacancies.text = ""
            }

            val day = model.dateDay
            val monthNumber = model.dateMonth
            val time = model.dateTime

            holder.itemView.cv_workout_item.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_transition)

            holder.itemView.tv_workout_day.text = day
            holder.itemView.tv_workout_month.text = monthConverter(monthNumber)
            holder.itemView.tv_workout_time.text = time

            holder.itemView.tv_item_workout_trainer.text = model.trainerName
            holder.itemView.tv_item_workout_year.text = model.dateYear

            holder.itemView.cv_workout_item.setOnClickListener {
                if (onClickListener!=null){
                    onClickListener!!.onClick(model.timeCreatedMillis)
                }
            }

            if (model.active == "No"){
                holder.itemView.tv_workout_day.setTextColor(ContextCompat.getColor(context, R.color.dark_gray1))
                holder.itemView.tv_workout_month.setTextColor(ContextCompat.getColor(context, R.color.dark_gray1))
                holder.itemView.tv_workout_time.setTextColor(ContextCompat.getColor(context, R.color.dark_gray1))
            }

            for (user in model.athleteAttendees){
                if (user.id == BaseActivity().getCurrentUserId()){
                    reservedWorkout = true
                }
            }
            if (reservedWorkout){
                holder.itemView.iv_item_workout_reserved.visibility = View.VISIBLE
            } else {
                holder.itemView.iv_item_workout_reserved.visibility = View.INVISIBLE
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

    fun removeWorkout(i: Int){
        list.removeAt(i)
        notifyDataSetChanged()
    }

}
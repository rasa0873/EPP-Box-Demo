package com.example.eppdraft1.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
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

class MyWorkoutListAdapterForFragment(private val activityContext: Context,
                                      private val list: ArrayList<Workout>,
                                      private val currentTimeMillis: Long = 0)
                : RecyclerView.Adapter<MyWorkoutListAdapterForFragment.MyViewHolder>()  {

    private var onClickListener : OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate the layout for each item and return a new ViewHolder object
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_my_workout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position] // model = Workout instance

        var attendedToWorkout : Boolean

        holder.workoutName.text = model.description

        if (model.athleteAttendees.size > 0){
            holder.numberOfAttendees.text = model.athleteAttendees.size.toString()
        } else {
            holder.numberOfAttendees.text = ""
        }

        val day = model.dateDay
        val monthNumber = model.dateMonth
        val time = model.dateTime

        holder.workoutCardView.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_transition)

        holder.workoutDay.text = day
        holder.workoutMonth.text = monthConverter(monthNumber)
        holder.workoutTime.text = time
        holder.workoutTrainer.text = model.trainerName
        holder.workoutYear.text = model.dateYear

        holder.workoutCardView.setOnClickListener {
            if (onClickListener!=null){
                onClickListener!!.onClick(model.timeCreatedMillis, position)
            }
        }

            if (model.active == "No"){
                holder.workoutDay.setTextColor(ContextCompat.getColor( activityContext, R.color.dark_gray1))
                holder.workoutMonth.setTextColor(ContextCompat.getColor(activityContext, R.color.dark_gray1))
                holder.workoutTime.setTextColor(ContextCompat.getColor(activityContext, R.color.dark_gray1))
            }

        attendedToWorkout = false
        for (attendee in model.athleteAttendees) {
            if (attendee.attended == "Yes"){
                attendedToWorkout = true
            }
        }

        if (attendedToWorkout){
            holder.workoutAttended.setImageResource(R.drawable.blue_check_circle_24)
        } else {
            holder.workoutAttended.setImageResource(R.drawable.dark_round_cancel_24)
        }
        holder.workoutAttended.alpha = 0.5f

        if (currentTimeMillis > 0) {
            if (checkTimePassed(currentTimeMillis, model)) {
                holder.workoutAttended.visibility = View.VISIBLE
            } else {
                holder.workoutAttended.visibility = View.INVISIBLE
            }
        }

    }

    override fun getItemCount(): Int {
       return list.size
    }


    private fun monthConverter(monthNumber: String): String {
        val monthText: String = when(monthNumber){
            "1" -> activityContext.resources.getString(R.string.january)
            "2" -> activityContext.resources.getString(R.string.february)
            "3" -> activityContext.resources.getString(R.string.march)
            "4" -> activityContext.resources.getString(R.string.april)
            "5" -> activityContext.resources.getString(R.string.may)
            "6" -> activityContext.resources.getString(R.string.june)
            "7" -> activityContext.resources.getString(R.string.july)
            "8" -> activityContext.resources.getString(R.string.august)
            "9" -> activityContext.resources.getString(R.string.september)
            "10" -> activityContext.resources.getString(R.string.october)
            "11" -> activityContext.resources.getString(R.string.november)
            "12" -> activityContext.resources.getString(R.string.december)
            else -> ""
        }

        return monthText
    }

    // Interface definition and initialization
    interface OnClickListener {
        fun onClick(timeDateInMillis: String, posFromAdapter: Int = 0)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }



    private fun checkTimePassed(currentTimeMillis: Long, workout: Workout): Boolean {
        // Check if time and date passed
        val timeCreatedMillisHourGap: Long = workout.timeCreatedMillis.toLong() - 3600000
        return currentTimeMillis >= timeCreatedMillisHourGap
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val workoutCardView: CardView = itemView.findViewById(R.id.cv_my_workout_item)
        val workoutName: TextView = itemView.findViewById(R.id.tv_item_my_workout_description)
        val workoutTime : TextView = itemView.findViewById(R.id.tv_my_workout_time)
        val workoutDay : TextView = itemView.findViewById(R.id.tv_my_workout_day)
        val workoutMonth : TextView = itemView.findViewById(R.id.tv_my_workout_month)
        val workoutYear : TextView = itemView.findViewById(R.id.tv_item_my_workout_year)
        val workoutTrainer: TextView = itemView.findViewById(R.id.tv_item_my_workout_trainer)

        val numberOfAttendees: TextView = itemView.findViewById(R.id.tv_item_my_workout_vacancies)

        val workoutAttended: ImageView = itemView.findViewById(R.id.iv_item_my_workout_attended)
    }

}
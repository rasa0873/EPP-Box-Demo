package com.example.eppdraft1.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eppdraft1.R
import com.example.eppdraft1.main.activities.ViewWorkoutActivity
import com.example.eppdraft1.main.models.User
import com.example.eppdraft1.main.models.UserAttendee
import kotlinx.android.synthetic.main.item_athlete.view.*
import kotlinx.android.synthetic.main.item_athlete_reservation.view.*
import kotlinx.android.synthetic.main.portal_main_layout.view.*

open class AthleteReservationListAdapter(private val context: Context,
                                         private val list: ArrayList<UserAttendee>,
                                         private val showAttendeeCheck: Boolean = false,
                        private val attendeeSaveMode: Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return MyViewHolder(LayoutInflater.from(context)
           .inflate(R.layout.item_athlete_reservation, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.person_128)
                .into(holder.itemView.iv_athlete_reservation_image)

            holder.itemView.iv_athlete_reservation_image.setOnClickListener {
                onClickListener?.onClickUser(model)
            }

            holder.itemView.tv_item_athlete_reservation_name.text = model.name
            holder.itemView.tv_item_athlete_reservation_name.setOnClickListener {
                onClickListener?.onClickUser(model)
            }

            if (showAttendeeCheck) { // if Manager or Trainer is seeing
                holder.itemView.iv_workout_attendee_check.visibility = View.VISIBLE
            } else {
                holder.itemView.iv_workout_attendee_check.visibility = View.INVISIBLE
            }
            when (model.attended) {
                "Yes" -> {
                    holder.itemView.iv_workout_attendee_check.setImageResource(R.drawable.blue_check_circle_24)
                }
                "No" -> {
                    holder.itemView.iv_workout_attendee_check.setImageResource(R.drawable.dark_round_cancel_24)
                }
                else -> {
                    holder.itemView.iv_workout_attendee_check.setImageResource(R.drawable.gray_question_mark_24)
                }
            }
            if (attendeeSaveMode){
                holder.itemView.iv_workout_attendee_check.alpha = 1.0f
            } else {
                holder.itemView.iv_workout_attendee_check.alpha = 0.5f
            }

            holder.itemView.iv_workout_attendee_check.setOnClickListener {
                if (ViewWorkoutActivity.attendeeSaveMode) {
                    if (model.attended == "Yes") {
                        // Switch it to "No"
                        model.attended = "No"
                        list[position].attended = "No"
                        holder.itemView.iv_workout_attendee_check.setImageResource(R.drawable.dark_round_cancel_24)
                    } else {
                        // Switch it to "Yes"
                        model.attended = "Yes"
                        list[position].attended = "Yes"
                        holder.itemView.iv_workout_attendee_check.setImageResource(R.drawable.blue_check_circle_24)
                    }
                    onClickListener?.onClickAttending(position, model.attended)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }


    interface OnClickListener{
        fun onClickUser(userDataAttendee: UserAttendee)
        fun onClickAttending(position: Int, attended: String)
    }




    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
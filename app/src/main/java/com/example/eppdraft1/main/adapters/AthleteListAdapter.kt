package com.example.eppdraft1.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eppdraft1.R
import com.example.eppdraft1.main.models.User
import kotlinx.android.synthetic.main.item_athlete.view.*
import kotlinx.android.synthetic.main.portal_main_layout.view.*

open class AthleteListAdapter(private val context: Context,
                            private val list: ArrayList<User>,
                            private val isManager: Boolean = true) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return MyViewHolder(LayoutInflater.from(context)
           .inflate(R.layout.item_athlete, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.person_128)
                .into(holder.itemView.iv_athlete_image)

            holder.itemView.tv_item_athlete_name.text = model.name
            holder.itemView.tv_item_athlete_email.text = model.email

            holder.itemView.cv_athlete_item.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_transition)

            holder.itemView.cv_athlete_item.setOnClickListener {
                onClickListener!!.onClick(model)
            }

            if (model.status == "active" && isManager){
                holder.itemView.iv_item_user_check.visibility = View.VISIBLE
            } else {
                holder.itemView.iv_item_user_check.visibility = View.INVISIBLE
            }

            holder.itemView.iv_item_user_check.setOnClickListener {
                onClickListener!!.onClick2(model)
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
        fun onClick(userData: User)

        fun onClick2(userData: User)
    }


    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
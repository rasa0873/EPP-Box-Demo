package com.example.eppdraft1.main.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eppdraft1.R;
import com.example.eppdraft1.main.utils.CalendarUtils;
import org.threeten.bp.LocalDate;
import java.util.ArrayList;


// Adapter for Week based Calendar
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {


    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    private Context context;


    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener, Context context) {
        this.days = days;
        this.onItemListener = onItemListener;
        this.context = context;

    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (days.size() > 15)  // Month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        else // Week view
            layoutParams.height = (int) parent.getHeight();

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);
        if (date == null)
            holder.dayOfMonth.setText("");
        else {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            switch (date.getDayOfWeek().toString()){
                case "SUNDAY":
                    holder.dayOfWeek.setText(context.getString(R.string.sun));
                    break;
                case "MONDAY":
                    holder.dayOfWeek.setText(context.getString(R.string.mon));
                    break;
                case "TUESDAY":
                    holder.dayOfWeek.setText(context.getString(R.string.tue));
                    break;
                case "WEDNESDAY":
                    holder.dayOfWeek.setText(context.getString(R.string.wed));
                    break;
                case "THURSDAY":
                    holder.dayOfWeek.setText(context.getString(R.string.thu));
                    break;
                case "FRIDAY":
                    holder.dayOfWeek.setText(context.getString(R.string.fri));
                    break;
                case "SATURDAY":
                    holder.dayOfWeek.setText(context.getString(R.string.sat));
                    break;
            }


            if (date.equals(CalendarUtils.selectedDate)) {
                holder.dayOfMonth.setTextColor(Color.BLUE);
                holder.dayOfMonth.setAlpha(1.0f);

                holder.dayOfWeek.setTextColor(Color.BLUE);
                holder.dayOfWeek.setAlpha(1.0f);
            }
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, LocalDate date);
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ArrayList<LocalDate> days;
        public final View parentView;
        public final TextView dayOfMonth;
        public final TextView dayOfWeek;
        private final CalendarAdapter.OnItemListener onItemListener;


        public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener
                , ArrayList<LocalDate> days) {

            super(itemView);
            parentView = itemView.findViewById(R.id.parentView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            dayOfWeek = itemView.findViewById(R.id.cellWeekText);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
            this.days = days;
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getBindingAdapterPosition(), days.get(getBindingAdapterPosition()));

        }

    }
}

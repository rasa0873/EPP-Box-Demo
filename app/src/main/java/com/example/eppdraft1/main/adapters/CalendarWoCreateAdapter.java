package com.example.eppdraft1.main.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eppdraft1.R;

import java.util.ArrayList;

public class CalendarWoCreateAdapter extends RecyclerView.Adapter<CalendarWoCreateAdapter.CalendarViewHolder2> {

    private final ArrayList<String> dayOfMonth;
    private final OnItemListener onItemListener;

    private final ArrayList<Boolean> pickedDaysList;

    private final Context context;

    public CalendarWoCreateAdapter(ArrayList<String> dayOfMonth, ArrayList<Boolean> pickedDaysList,
                                   OnItemListener onItemListener, Context context) {
        this.dayOfMonth = dayOfMonth;
        this.pickedDaysList = pickedDaysList;
        this.onItemListener = onItemListener;
        this.context = context;
    }


    @NonNull
    @Override
    public CalendarViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell_create_wo, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder2(view, onItemListener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder2 holder, int position) {
        holder.dayOfMonth.setText( dayOfMonth.get(position));

        if (pickedDaysList.get(position)){
            holder.dayOfMonth.setTextColor(Color.WHITE);
            holder.cellHolderLayout.setBackgroundResource(R.drawable.rounded_corners_blue);
        } else {
            holder.dayOfMonth.setTextColor(Color.BLACK);
            holder.cellHolderLayout.setBackgroundResource(R.drawable.rounded_corners_white);
        }
        holder.cellHolderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First, verify if picked a position with text
                if (holder.dayOfMonth.getText() != "") {
                    if (holder.dayOfMonth.getCurrentTextColor() != Color.WHITE) { // not already selected
                        holder.dayOfMonth.setTextColor(Color.WHITE);
                        holder.cellHolderLayout.setBackgroundResource(R.drawable.rounded_corners_blue);
                    } else { // already selected
                        holder.dayOfMonth.setTextColor(Color.BLACK);
                        holder.cellHolderLayout.setBackgroundResource(R.drawable.rounded_corners_white);
                    }

                if (onItemListener != null) {
                    onItemListener.onItemClick(holder.dayOfMonth.getText().toString());
                }
            }
        }
        });

    }

    @Override
    public int getItemCount() {
        return dayOfMonth.size();
    }

    public interface OnItemListener {
        void onItemClick(String dayText);
    }

    static class CalendarViewHolder2 extends RecyclerView.ViewHolder  {
        public final TextView dayOfMonth;
        public final LinearLayout cellHolderLayout;
        private final CalendarWoCreateAdapter.OnItemListener onItemListener;

        private Context context = null;

        public CalendarViewHolder2(@NonNull View itemView, CalendarWoCreateAdapter.OnItemListener onItemListener, Context context) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            cellHolderLayout = itemView.findViewById(R.id.calendar_cell_holder);
            this.onItemListener = onItemListener;
            this.context = context;

        }

    }



}

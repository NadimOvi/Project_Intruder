package com.informatix.intruder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeekDataViewHolder extends RecyclerView.ViewHolder {
    TextView weekName, startDay,endDay;
    public WeekDataViewHolder(@NonNull View itemView) {
        super(itemView);

        weekName = itemView.findViewById(R.id.weekName);
        startDay = itemView.findViewById(R.id.startDay);
        endDay = itemView.findViewById(R.id.endDay);

    }
}

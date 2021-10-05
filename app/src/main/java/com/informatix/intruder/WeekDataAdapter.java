package com.informatix.intruder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.informatix.intruder.Model.MainWeekModels;
import com.informatix.intruder.Model.WeekModels;

import java.util.ArrayList;

public class WeekDataAdapter extends RecyclerView.Adapter<WeekDataViewHolder> {
    ArrayList<WeekModels> itemsList;
    private Context context;

    LayoutInflater inflater;
    View view;


    public WeekDataAdapter(ArrayList<WeekModels> itemsList, Context context) {
        this.itemsList=itemsList;
        this.context=context;

    }


    @NonNull
    @Override
    public WeekDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(R.layout.weekday_view_holder,parent,false);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new WeekDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekDataViewHolder holder, int position) {

        WeekModels cell = itemsList.get(position);

        if(cell.isSectionHeader())
        {
            /*if (cell.getWeek_day()==1) {
                view = inflater.inflate(R.layout.section_header, null);
                view.setClickable(false);
                TextView header = (TextView) v.findViewById(R.id.section_header);
                header.setText("sunday");
            }else if (cell.getWeek_day()==2){
                view = inflater.inflate(R.layout.section_header, null);
                view.setClickable(false);
                TextView header = (TextView) v.findViewById(R.id.section_header);
                header.setText("Monday");
            }*/
            if (cell.getWeek_day()==1){
                holder.weekName.setText("Sunday");
                holder.startDay.setText(cell.getStart_time());
                holder.endDay.setText(cell.getEnd_time());
            }else if (cell.getWeek_day()==2){
            holder.weekName.setText("Monday");
                holder.startDay.setText(cell.getStart_time());
                holder.endDay.setText(cell.getEnd_time());
            }else if (cell.getWeek_day()==3){
                holder.weekName.setText("Tuesday");
                holder.startDay.setText(cell.getStart_time());
                holder.endDay.setText(cell.getEnd_time());
            }else if (cell.getWeek_day()==4){
                holder.weekName.setText("Wednesday");
                holder.startDay.setText(cell.getStart_time());
                holder.endDay.setText(cell.getEnd_time());
            }else if (cell.getWeek_day()==5){
                holder.weekName.setText("Thursday");
                holder.startDay.setText(cell.getStart_time());
                holder.endDay.setText(cell.getEnd_time());
            }else if (cell.getWeek_day()==6){
                holder.weekName.setText("Friday");
                holder.startDay.setText(cell.getStart_time());
                holder.endDay.setText(cell.getEnd_time());
            }
            else if (cell.getWeek_day()==7){
                holder.weekName.setText("Saturday");
                holder.startDay.setText(cell.getStart_time());
                holder.endDay.setText(cell.getEnd_time());
            }
        }
        else
        {

            holder.startDay.setText(cell.getStart_time());
            holder.endDay.setText(cell.getEnd_time());


        }


    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}

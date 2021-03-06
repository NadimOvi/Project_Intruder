package com.informatix.intruder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.informatix.intruder.Api.Service;
import com.informatix.intruder.Model.MainWeekModels;
import com.informatix.intruder.Model.WeekModels;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardActivity extends AppCompatActivity {

    ImageView ClickMenu,ClickClose;
    DrawerLayout drawerLayout;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ListView list;
    Button addButton,deleteButton,editButton,pop_upaddButton,cancel_popup;
    TextView startTime,endTime, ClickWeek, ClickDay, ClickSetting,profileNameID,totalBalance;
    TextView pop_upstartTime,pop_upendTime;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    int startTimeHour,startTimeMinute, endTimeHour,endTimeMinute;
    String popUpStartTime,popUpEndTime;
    String mainDeviceID;
    int device_id, weekDayId, dayId;

    ArrayList<WeekModels> tempList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();
        list = (ListView) findViewById(R.id.listview);
        sharedPreferences = getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
        String userID= sharedPreferences.getString("userID",null);
        String password= sharedPreferences.getString("password",null);
        String name= sharedPreferences.getString("name",null);
        mainDeviceID= sharedPreferences.getString("device_id","");
        String valid_till= sharedPreferences.getString("valid_till",null);


        progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        ClickMenu = findViewById(R.id.ClickMenu);
        ClickClose = findViewById(R.id.ClickClose);
        ClickWeek = findViewById(R.id.ClickWeek);
        ClickDay = findViewById(R.id.ClickDay);
        ClickSetting = findViewById(R.id.ClickSetting);
        drawerLayout = findViewById(R.id.drawer);
        profileNameID = findViewById(R.id.profileNameID);
        totalBalance = findViewById(R.id.totalBalance);
        profileNameID.setText(name);
        totalBalance.setText(valid_till);
        listShow();
        ClickMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        ClickClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(drawerLayout);
            }
        });
        ClickWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this,DashboardActivity.class);
                startActivity(intent);
            }
        });
        ClickDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this,DayListActivity.class);
                startActivity(intent);
            }
        });
        ClickSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

    }
    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
        /*view.setClickable(false);*/
    }

    private void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            ClickClose.setClickable(true);
        }
    }


    private void listShow() {
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://us.infrmtx.com/iot/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service api = retrofit.create(Service.class);
        Call<MainWeekModels> call= api.getModels(Integer.valueOf(mainDeviceID));
        call.enqueue(new Callback<MainWeekModels>() {
            @Override
            public void onResponse(Call<MainWeekModels> call, Response<MainWeekModels> response) {
                progressDialog.dismiss();
                ArrayList<WeekModels> itemsModel = response.body().getWeekdays();

                ArrayList<WeekModels> itemsList = new ArrayList<>();

                itemsList = sortAndAddSections(itemsModel);

               /* WeekDataAdapter weekDataAdapter = new WeekDataAdapter(itemsList,getApplicationContext());
                weekRecyclerView.setAdapter(weekDataAdapter);*/

                ListAdapter adapter = new ListAdapter(DashboardActivity.this, itemsList);
                list.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<MainWeekModels> call, Throwable t) {
            progressDialog.dismiss();
            }
        });
    }

    private ArrayList sortAndAddSections(ArrayList<WeekModels> itemList) {

        tempList = new ArrayList<>();
        //First we sort the array
        Collections.sort(itemList);

        //Loops thorugh the list and add a section before each sectioncell start
        Integer header = 0;
        for(int i=0; i<itemList.size(); i++)
        {
            //If it is the start of a new section we create a new listcell and add it to our array
            if(!(header.equals(itemList.get(i).getWeek_day()))) {
                /*TestModels sectionCell = new TestModels(null, null,null,itemList.get(i).getDate());*/
                WeekModels sectionCell = new WeekModels(null, itemList.get(i).getWeek_day(), null,null,null);
                sectionCell.setToSectionHeader();
                tempList.add(sectionCell);
                header = itemList.get(i).getWeek_day();
            }
            tempList.add(itemList.get(i));
        }

        return tempList;
    }

    public class ListAdapter extends ArrayAdapter {

        LayoutInflater inflater;
        public ListAdapter(Context context, ArrayList items) {
            super(context, 0, items);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            final WeekModels cell = (WeekModels) getItem(position);

            //If the cell is a section header we inflate the header layout
            if(cell.isSectionHeader())
            {
                if (cell.getWeek_day()==1) {
                    v = inflater.inflate(R.layout.section_header, null);
                    v.setClickable(false);
                    TextView header = (TextView) v.findViewById(R.id.section_header);
                    header.setText("Sunday");

                }else if (cell.getWeek_day()==2){
                    v = inflater.inflate(R.layout.section_header, null);
                    v.setClickable(false);
                    TextView header = (TextView) v.findViewById(R.id.section_header);
                    header.setText("Monday");

                }else if (cell.getWeek_day()==3){
                    v = inflater.inflate(R.layout.section_header, null);
                    v.setClickable(false);
                    TextView header = (TextView) v.findViewById(R.id.section_header);
                    header.setText("Tuesday");

                }else if (cell.getWeek_day()==4){
                    v = inflater.inflate(R.layout.section_header, null);
                    v.setClickable(false);
                    TextView header = (TextView) v.findViewById(R.id.section_header);
                    header.setText("Wednesday");

                }else if (cell.getWeek_day()==5){
                    v = inflater.inflate(R.layout.section_header, null);
                    v.setClickable(false);
                    TextView header = (TextView) v.findViewById(R.id.section_header);
                    header.setText("Thursday");

                }else if (cell.getWeek_day()==6){
                    v = inflater.inflate(R.layout.section_header, null);
                    v.setClickable(false);
                    TextView header = (TextView) v.findViewById(R.id.section_header);
                    header.setText("Friday");
                }else if (cell.getWeek_day()==7){
                    v = inflater.inflate(R.layout.section_header, null);
                    v.setClickable(false);
                    TextView header = (TextView) v.findViewById(R.id.section_header);
                    header.setText("Saturday");
                }
            }
            else
            {
                v = inflater.inflate(R.layout.row_item, null);
                startTime = v.findViewById(R.id.startTime);
                endTime =  v.findViewById(R.id.endTime);
                addButton =  v.findViewById(R.id.addButton);
                deleteButton =  v.findViewById(R.id.deleteButton);
                editButton =  v.findViewById(R.id.editButton);

                device_id = cell.getDevice_id();
                weekDayId = cell.getWeek_day();
                dayId = cell.getDay_id();

                startTime.setText(cell.getStart_time());
                endTime.setText(cell.getEnd_time());

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cell.getWeek_day()==1){

                            dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            v = getLayoutInflater().inflate(R.layout.popup_week_add,null);
                            pop_upstartTime = v.findViewById(R.id.pop_upstartTime);
                            pop_upendTime = v.findViewById(R.id.pop_upendTime);
                            pop_upaddButton = v.findViewById(R.id.pop_upaddButton);
                            dialogBuilder.setView(v);
                            dialog = dialogBuilder.create();
                            dialog.show();

                            pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    startTimeHour = hourOfDay;
                                                    startTimeMinute = minute;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,startTimeHour,startTimeMinute);
                                                    popUpStartTime = String.valueOf(DateFormat.format("hh:mm ",calendar));
                                                    pop_upstartTime.setText(popUpStartTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(startTimeHour,startTimeMinute);
                                    timePickerDialog.show();
                                }
                            });

                            pop_upendTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    endTimeHour = hourOfDay;
                                                    endTimeMinute = minute;

                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,endTimeHour,endTimeMinute);

                                                    popUpEndTime = String.valueOf(DateFormat.format("hh:mm",calendar));
                                                    pop_upendTime.setText(popUpEndTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(endTimeHour,endTimeMinute);
                                    timePickerDialog.show();
                                }
                            });



                            pop_upaddButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startTime.setEnabled(true);
                                    endTime.setEnabled(true);
                                    progressDialog.show();
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://us.infrmtx.com/iot/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    Service api = retrofit.create(Service.class);
                                    Call<String> call= api.getAddWeeks(device_id,cell.getWeek_day(),popUpStartTime,popUpEndTime);
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if (response.isSuccessful()){
                                                /*Toast.makeText(DashboardActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/

                                                /*startActivity(getIntent());*/
                                                /*recreate();*/
                                                progressDialog.dismiss();

                                            }else{
                                               /* Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            progressDialog.dismiss();
                                            /*Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                            recreate();
                                        }
                                    });
                                }
                            });
                        }
                        else if (cell.getWeek_day()==2){

                            dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            v = getLayoutInflater().inflate(R.layout.popup_week_add,null);
                            pop_upstartTime = v.findViewById(R.id.pop_upstartTime);
                            pop_upendTime = v.findViewById(R.id.pop_upendTime);
                            pop_upaddButton = v.findViewById(R.id.pop_upaddButton);
                            dialogBuilder.setView(v);
                            dialog = dialogBuilder.create();
                            dialog.show();

                            pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    startTimeHour = hourOfDay;
                                                    startTimeMinute = minute;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,startTimeHour,startTimeMinute);
                                                    popUpStartTime = String.valueOf(DateFormat.format("hh:mm ",calendar));
                                                    pop_upstartTime.setText(popUpStartTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(startTimeHour,startTimeMinute);
                                    timePickerDialog.show();
                                }
                            });

                            pop_upendTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    endTimeHour = hourOfDay;
                                                    endTimeMinute = minute;

                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,endTimeHour,endTimeMinute);

                                                    popUpEndTime = String.valueOf(DateFormat.format("hh:mm",calendar));
                                                    pop_upendTime.setText(popUpEndTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(endTimeHour,endTimeMinute);
                                    timePickerDialog.show();
                                }
                            });



                            pop_upaddButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    progressDialog.show();
                                    startTime.setEnabled(true);
                                    endTime.setEnabled(true);
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://us.infrmtx.com/iot/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    Service api = retrofit.create(Service.class);
                                    Call<String> call= api.getAddWeeks(device_id,cell.getWeek_day(),popUpStartTime,popUpEndTime);
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if (response.isSuccessful()){
                                               /* Toast.makeText(DashboardActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/
                                                progressDialog.dismiss();
                                                /*startActivity(getIntent());*/
                                                /*recreate();*/

                                            }else{
                                               /* Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                                progressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            progressDialog.dismiss();
                                           /* Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                            recreate();
                                        }
                                    });
                                }
                            });
                        }
                        else if (cell.getWeek_day()==3){

                            dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            v = getLayoutInflater().inflate(R.layout.popup_week_add,null);
                            pop_upstartTime = v.findViewById(R.id.pop_upstartTime);
                            pop_upendTime = v.findViewById(R.id.pop_upendTime);
                            pop_upaddButton = v.findViewById(R.id.pop_upaddButton);
                            dialogBuilder.setView(v);
                            dialog = dialogBuilder.create();
                            dialog.show();

                            pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    startTimeHour = hourOfDay;
                                                    startTimeMinute = minute;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,startTimeHour,startTimeMinute);
                                                    popUpStartTime = String.valueOf(DateFormat.format("hh:mm ",calendar));
                                                    pop_upstartTime.setText(popUpStartTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(startTimeHour,startTimeMinute);
                                    timePickerDialog.show();
                                }
                            });

                            pop_upendTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    endTimeHour = hourOfDay;
                                                    endTimeMinute = minute;

                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,endTimeHour,endTimeMinute);

                                                    popUpEndTime = String.valueOf(DateFormat.format("hh:mm",calendar));
                                                    pop_upendTime.setText(popUpEndTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(endTimeHour,endTimeMinute);
                                    timePickerDialog.show();
                                }
                            });



                            pop_upaddButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialog.show();
                                    startTime.setEnabled(true);
                                    endTime.setEnabled(true);
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://us.infrmtx.com/iot/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    Service api = retrofit.create(Service.class);
                                    Call<String> call= api.getAddWeeks(device_id,cell.getWeek_day(),popUpStartTime,popUpEndTime);
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if (response.isSuccessful()){
                                               /* Toast.makeText(DashboardActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/
                                                progressDialog.dismiss();
                                                /*startActivity(getIntent());*/
                                                /*recreate();*/

                                            }else{
                                               /* Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                                progressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            progressDialog.dismiss();
                                           /* Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                            recreate();
                                        }
                                    });
                                }
                            });
                        }
                        else if (cell.getWeek_day()==4){

                            dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            v = getLayoutInflater().inflate(R.layout.popup_week_add,null);
                            pop_upstartTime = v.findViewById(R.id.pop_upstartTime);
                            pop_upendTime = v.findViewById(R.id.pop_upendTime);
                            pop_upaddButton = v.findViewById(R.id.pop_upaddButton);
                            dialogBuilder.setView(v);
                            dialog = dialogBuilder.create();
                            dialog.show();

                            pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    startTimeHour = hourOfDay;
                                                    startTimeMinute = minute;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,startTimeHour,startTimeMinute);
                                                    popUpStartTime = String.valueOf(DateFormat.format("hh:mm ",calendar));
                                                    pop_upstartTime.setText(popUpStartTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(startTimeHour,startTimeMinute);
                                    timePickerDialog.show();
                                }
                            });

                            pop_upendTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    endTimeHour = hourOfDay;
                                                    endTimeMinute = minute;

                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,endTimeHour,endTimeMinute);

                                                    popUpEndTime = String.valueOf(DateFormat.format("hh:mm",calendar));
                                                    pop_upendTime.setText(popUpEndTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(endTimeHour,endTimeMinute);
                                    timePickerDialog.show();
                                }
                            });



                            pop_upaddButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialog.show();
                                    startTime.setEnabled(true);
                                    endTime.setEnabled(true);
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://us.infrmtx.com/iot/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    Service api = retrofit.create(Service.class);
                                    Call<String> call= api.getAddWeeks(device_id,cell.getWeek_day(),popUpStartTime,popUpEndTime);
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if (response.isSuccessful()){
                                               /* Toast.makeText(DashboardActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/
                                                progressDialog.dismiss();
                                                /*startActivity(getIntent());*/
                                                /*recreate();*/

                                            }else{
                                               /* Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            progressDialog.dismiss();
                                           /* Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                            recreate();

                                        }
                                    });
                                }
                            });
                        }
                        else if (cell.getWeek_day()==5){

                            dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            v = getLayoutInflater().inflate(R.layout.popup_week_add,null);
                            pop_upstartTime = v.findViewById(R.id.pop_upstartTime);
                            pop_upendTime = v.findViewById(R.id.pop_upendTime);
                            pop_upaddButton = v.findViewById(R.id.pop_upaddButton);
                            dialogBuilder.setView(v);
                            dialog = dialogBuilder.create();
                            dialog.show();

                            pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    startTimeHour = hourOfDay;
                                                    startTimeMinute = minute;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,startTimeHour,startTimeMinute);
                                                    popUpStartTime = String.valueOf(DateFormat.format("hh:mm ",calendar));
                                                    pop_upstartTime.setText(popUpStartTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(startTimeHour,startTimeMinute);
                                    timePickerDialog.show();
                                }
                            });

                            pop_upendTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    endTimeHour = hourOfDay;
                                                    endTimeMinute = minute;

                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,endTimeHour,endTimeMinute);

                                                    popUpEndTime = String.valueOf(DateFormat.format("hh:mm",calendar));
                                                    pop_upendTime.setText(popUpEndTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(endTimeHour,endTimeMinute);
                                    timePickerDialog.show();
                                }
                            });



                            pop_upaddButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialog.show();
                                    startTime.setEnabled(true);
                                    endTime.setEnabled(true);
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://us.infrmtx.com/iot/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    Service api = retrofit.create(Service.class);
                                    Call<String> call= api.getAddWeeks(device_id,cell.getWeek_day(),popUpStartTime,popUpEndTime);
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if (response.isSuccessful()){
                                                progressDialog.dismiss();
                                              /*  Toast.makeText(DashboardActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/

                                                /*startActivity(getIntent());*/
                                                /*recreate();*/

                                            }else{
                                                progressDialog.dismiss();
                                                /*Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            progressDialog.dismiss();
                                            /*Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                            recreate();
                                        }
                                    });
                                }
                            });
                        }
                        else if (cell.getWeek_day()==6){

                            dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            v = getLayoutInflater().inflate(R.layout.popup_week_add,null);
                            pop_upstartTime = v.findViewById(R.id.pop_upstartTime);
                            pop_upendTime = v.findViewById(R.id.pop_upendTime);
                            pop_upaddButton = v.findViewById(R.id.pop_upaddButton);
                            dialogBuilder.setView(v);
                            dialog = dialogBuilder.create();
                            dialog.show();

                            pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    startTimeHour = hourOfDay;
                                                    startTimeMinute = minute;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,startTimeHour,startTimeMinute);
                                                    popUpStartTime = String.valueOf(DateFormat.format("hh:mm ",calendar));
                                                    pop_upstartTime.setText(popUpStartTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(startTimeHour,startTimeMinute);
                                    timePickerDialog.show();
                                }
                            });

                            pop_upendTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    endTimeHour = hourOfDay;
                                                    endTimeMinute = minute;

                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,endTimeHour,endTimeMinute);

                                                    popUpEndTime = String.valueOf(DateFormat.format("hh:mm",calendar));
                                                    pop_upendTime.setText(popUpEndTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(endTimeHour,endTimeMinute);
                                    timePickerDialog.show();
                                }
                            });



                            pop_upaddButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialog.show();
                                    startTime.setEnabled(true);
                                    endTime.setEnabled(true);
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://us.infrmtx.com/iot/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    Service api = retrofit.create(Service.class);
                                    Call<String> call= api.getAddWeeks(device_id,cell.getWeek_day(),popUpStartTime,popUpEndTime);
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if (response.isSuccessful()){
                                               /* Toast.makeText(DashboardActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/

                                                /*startActivity(getIntent());*/
                                                /*recreate();*/

                                            }else{
                                                /*Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            progressDialog.dismiss();
                                          /*  Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                            recreate();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        tempList.size();
                        Integer cellData = cell.getWeek_day();
                        ArrayList<Integer> list = new ArrayList<>();
                        for (int i=0;i<tempList.size();i++){
                            if (tempList.get(i).getWeek_day().equals(cellData)){

                                list.add(i);
                            }
                        }
                        if (list.size()<=2){
                            new iOSDialogBuilder(getContext())
                                    .setTitle("Sorry!")
                                    .setSubtitle("You did not permission to delete this")
                                    .setBoldPositiveLabel(true)
                                    .setCancelable(false)
                                    .setPositiveListener("Ok",new iOSDialogClickListener() {
                                        @Override
                                        public void onClick(iOSDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    })

                                    .build().show();
                        }else{
                            new iOSDialogBuilder(getContext())
                                .setTitle("Delete!")
                                .setSubtitle("Are you certain you want to delete")
                                .setBoldPositiveLabel(true)
                                .setCancelable(false)
                                .setPositiveListener("Ok",new iOSDialogClickListener() {
                                    @Override
                                    public void onClick(iOSDialog dialog) {
                                        startTime.setEnabled(true);
                                        endTime.setEnabled(true);
                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl("http://us.infrmtx.com/iot/")
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        progressDialog.show();
                                        Service api = retrofit.create(Service.class);
                                        Call<JSONObject> call= api.getDeleteWeeks(cell.getDevice_id(),cell.getDay_id());
                                        call.enqueue(new Callback<JSONObject>() {
                                            @Override
                                            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                                if (response.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    dialog.dismiss();
                                                    recreate();
                                                }else{
                                                    progressDialog.dismiss();

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<JSONObject> call, Throwable t) {
                                                progressDialog.dismiss();
                                                dialog.dismiss();
                                                recreate();
                                            }
                                        });


                                    }
                                })
                                .setNegativeListener("Cancel", new iOSDialogClickListener() {
                                    @Override
                                    public void onClick(iOSDialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .build().show();
                        }

                    }
                });



                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cell.getWeek_day()==1){
                            dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            v = getLayoutInflater().inflate(R.layout.popup_week_add,null);
                            pop_upstartTime = v.findViewById(R.id.pop_upstartTime);
                            pop_upendTime = v.findViewById(R.id.pop_upendTime);
                            pop_upaddButton = v.findViewById(R.id.pop_upaddButton);
                            dialogBuilder.setView(v);
                            dialog = dialogBuilder.create();
                            dialog.show();

                            pop_upstartTime.setText(cell.getStart_time());
                            pop_upendTime.setText(cell.getEnd_time());

                            pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    startTimeHour = hourOfDay;
                                                    startTimeMinute = minute;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,startTimeHour,startTimeMinute);
                                                    popUpStartTime = String.valueOf(DateFormat.format("hh:mm ",calendar));
                                                    pop_upstartTime.setText(popUpStartTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(startTimeHour,startTimeMinute);
                                    timePickerDialog.show();
                                }
                            });

                            pop_upendTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    endTimeHour = hourOfDay;
                                                    endTimeMinute = minute;

                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,endTimeHour,endTimeMinute);

                                                    popUpEndTime = String.valueOf(DateFormat.format("hh:mm",calendar));
                                                    pop_upendTime.setText(popUpEndTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(endTimeHour,endTimeMinute);
                                    timePickerDialog.show();
                                }
                            });



                            pop_upaddButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialog.show();
                                    startTime.setEnabled(true);
                                    endTime.setEnabled(true);
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://us.infrmtx.com/iot/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    Service api = retrofit.create(Service.class);
                                    Call<JSONObject> call= api.getEditWeeks(device_id,cell.getWeek_day(),popUpStartTime,popUpEndTime);
                                    call.enqueue(new Callback<JSONObject>() {
                                        @Override
                                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                            if (response.isSuccessful()){
                                                progressDialog.dismiss();
                                               /* Toast.makeText(DashboardActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/

                                                startActivity(getIntent());
                                                recreate();

                                            }else{
                                                progressDialog.dismiss();
                                              /*  Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONObject> call, Throwable t) {
                                            progressDialog.dismiss();
                                           /* Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                            recreate();
                                        }
                                    });
                                }
                            });
                        }
                        else if (cell.getWeek_day()==2){

                            dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            v = getLayoutInflater().inflate(R.layout.popup_week_edit,null);
                            pop_upstartTime = v.findViewById(R.id.pop_upstartTime);
                            pop_upendTime = v.findViewById(R.id.pop_upendTime);
                            pop_upaddButton = v.findViewById(R.id.pop_upaddButton);
                            dialogBuilder.setView(v);
                            dialog = dialogBuilder.create();
                            dialog.show();

                            pop_upstartTime.setText(cell.getStart_time());
                            pop_upendTime.setText(cell.getEnd_time());

                            pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    startTimeHour = hourOfDay;
                                                    startTimeMinute = minute;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,startTimeHour,startTimeMinute);
                                                    popUpStartTime = String.valueOf(DateFormat.format("hh:mm ",calendar));
                                                    pop_upstartTime.setText(popUpStartTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(startTimeHour,startTimeMinute);
                                    timePickerDialog.show();
                                }
                            });

                            pop_upendTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    endTimeHour = hourOfDay;
                                                    endTimeMinute = minute;

                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,endTimeHour,endTimeMinute);

                                                    popUpEndTime = String.valueOf(DateFormat.format("hh:mm",calendar));
                                                    pop_upendTime.setText(popUpEndTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(endTimeHour,endTimeMinute);
                                    timePickerDialog.show();
                                }
                            });



                            pop_upaddButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startTime.setEnabled(true);
                                    endTime.setEnabled(true);
                                    progressDialog.show();
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://us.infrmtx.com/iot/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    Service api = retrofit.create(Service.class);
                                    Call<JSONObject> call= api.getEditWeeks(device_id,cell.getDay_id(),popUpStartTime,popUpEndTime);
                                    call.enqueue(new Callback<JSONObject>() {
                                        @Override
                                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                            if (response.isSuccessful()){
                                                progressDialog.dismiss();
                                               /* Toast.makeText(DashboardActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/

                                                /*startActivity(getIntent());*/
                                                /*recreate();*/

                                            }else{
                                                progressDialog.dismiss();
                                             /*   Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONObject> call, Throwable t) {
                                            progressDialog.dismiss();
                                           /* Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                            recreate();
                                        }
                                    });
                                }
                            });
                        }
                        else if (cell.getWeek_day()==3){

                            dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            v = getLayoutInflater().inflate(R.layout.popup_week_edit,null);
                            pop_upstartTime = v.findViewById(R.id.pop_upstartTime);
                            pop_upendTime = v.findViewById(R.id.pop_upendTime);
                            pop_upaddButton = v.findViewById(R.id.pop_upaddButton);
                            dialogBuilder.setView(v);
                            dialog = dialogBuilder.create();
                            dialog.show();

                            pop_upstartTime.setText(cell.getStart_time());
                            pop_upendTime.setText(cell.getEnd_time());

                            pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    startTimeHour = hourOfDay;
                                                    startTimeMinute = minute;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,startTimeHour,startTimeMinute);
                                                    popUpStartTime = String.valueOf(DateFormat.format("hh:mm ",calendar));
                                                    pop_upstartTime.setText(popUpStartTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(startTimeHour,startTimeMinute);
                                    timePickerDialog.show();
                                }
                            });

                            pop_upendTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    endTimeHour = hourOfDay;
                                                    endTimeMinute = minute;

                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,endTimeHour,endTimeMinute);

                                                    popUpEndTime = String.valueOf(DateFormat.format("hh:mm",calendar));
                                                    pop_upendTime.setText(popUpEndTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(endTimeHour,endTimeMinute);
                                    timePickerDialog.show();
                                }
                            });



                            pop_upaddButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialog.show();
                                    startTime.setEnabled(true);
                                    endTime.setEnabled(true);
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://us.infrmtx.com/iot/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    Service api = retrofit.create(Service.class);
                                    Call<JSONObject> call= api.getEditWeeks(device_id,cell.getDay_id(),popUpStartTime,popUpEndTime);
                                    call.enqueue(new Callback<JSONObject>() {
                                        @Override
                                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                            if (response.isSuccessful()){
                                                progressDialog.dismiss();
                                              /*  Toast.makeText(DashboardActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/

                                                startActivity(getIntent());
                                                recreate();

                                            }else{
                                                progressDialog.dismiss();
                                              /*  Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONObject> call, Throwable t) {
                                            progressDialog.dismiss();
                                          /*  Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                            recreate();
                                        }
                                    });
                                }
                            });
                        }
                        else if (cell.getWeek_day()==4){
                            dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            v = getLayoutInflater().inflate(R.layout.popup_week_edit,null);
                            pop_upstartTime = v.findViewById(R.id.pop_upstartTime);
                            pop_upendTime = v.findViewById(R.id.pop_upendTime);
                            pop_upaddButton = v.findViewById(R.id.pop_upaddButton);
                            dialogBuilder.setView(v);
                            dialog = dialogBuilder.create();
                            dialog.show();

                            pop_upstartTime.setText(cell.getStart_time());
                            pop_upendTime.setText(cell.getEnd_time());

                            pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    startTimeHour = hourOfDay;
                                                    startTimeMinute = minute;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,startTimeHour,startTimeMinute);
                                                    popUpStartTime = String.valueOf(DateFormat.format("hh:mm ",calendar));
                                                    pop_upstartTime.setText(popUpStartTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(startTimeHour,startTimeMinute);
                                    timePickerDialog.show();
                                }
                            });

                            pop_upendTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    endTimeHour = hourOfDay;
                                                    endTimeMinute = minute;

                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,endTimeHour,endTimeMinute);

                                                    popUpEndTime = String.valueOf(DateFormat.format("hh:mm",calendar));
                                                    pop_upendTime.setText(popUpEndTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(endTimeHour,endTimeMinute);
                                    timePickerDialog.show();
                                }
                            });



                            pop_upaddButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialog.show();
                                    startTime.setEnabled(true);
                                    endTime.setEnabled(true);
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://us.infrmtx.com/iot/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    Service api = retrofit.create(Service.class);
                                    Call<JSONObject> call= api.getEditWeeks(device_id,cell.getDay_id(),popUpStartTime,popUpEndTime);
                                    call.enqueue(new Callback<JSONObject>() {
                                        @Override
                                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                            if (response.isSuccessful()){
                                                progressDialog.dismiss();
                                             /*   Toast.makeText(DashboardActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/

                                                startActivity(getIntent());
                                                recreate();

                                            }else{
                                                progressDialog.dismiss();
                                              /*  Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONObject> call, Throwable t) {
                                            progressDialog.dismiss();
                                           /* Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                            recreate();
                                        }
                                    });
                                }
                            });
                        }
                        else if (cell.getWeek_day()==5){
                            dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            v = getLayoutInflater().inflate(R.layout.popup_week_edit,null);
                            pop_upstartTime = v.findViewById(R.id.pop_upstartTime);
                            pop_upendTime = v.findViewById(R.id.pop_upendTime);
                            pop_upaddButton = v.findViewById(R.id.pop_upaddButton);
                            dialogBuilder.setView(v);
                            dialog = dialogBuilder.create();
                            dialog.show();

                            pop_upstartTime.setText(cell.getStart_time());
                            pop_upendTime.setText(cell.getEnd_time());

                            pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    startTimeHour = hourOfDay;
                                                    startTimeMinute = minute;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,startTimeHour,startTimeMinute);
                                                    popUpStartTime = String.valueOf(DateFormat.format("hh:mm ",calendar));
                                                    pop_upstartTime.setText(popUpStartTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(startTimeHour,startTimeMinute);
                                    timePickerDialog.show();
                                }
                            });

                            pop_upendTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    endTimeHour = hourOfDay;
                                                    endTimeMinute = minute;

                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,endTimeHour,endTimeMinute);

                                                    popUpEndTime = String.valueOf(DateFormat.format("hh:mm",calendar));
                                                    pop_upendTime.setText(popUpEndTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(endTimeHour,endTimeMinute);
                                    timePickerDialog.show();
                                }
                            });



                            pop_upaddButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialog.show();
                                    startTime.setEnabled(true);
                                    endTime.setEnabled(true);
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://us.infrmtx.com/iot/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    Service api = retrofit.create(Service.class);
                                    Call<JSONObject> call= api.getEditWeeks(device_id,cell.getDay_id(),popUpStartTime,popUpEndTime);
                                    call.enqueue(new Callback<JSONObject>() {
                                        @Override
                                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                            if (response.isSuccessful()){
                                                progressDialog.dismiss();
                                               /* Toast.makeText(DashboardActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/

                                                startActivity(getIntent());
                                                recreate();

                                            }else{
                                                progressDialog.dismiss();
                                              /*  Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONObject> call, Throwable t) {
                                            progressDialog.dismiss();
                                            /*Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                            recreate();
                                        }
                                    });
                                }
                            });
                        }
                        else if (cell.getWeek_day()==6){

                            dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            v = getLayoutInflater().inflate(R.layout.popup_week_edit,null);
                            pop_upstartTime = v.findViewById(R.id.pop_upstartTime);
                            pop_upendTime = v.findViewById(R.id.pop_upendTime);
                            pop_upaddButton = v.findViewById(R.id.pop_upaddButton);
                            dialogBuilder.setView(v);
                            dialog = dialogBuilder.create();
                            dialog.show();

                            pop_upstartTime.setText(cell.getStart_time());
                            pop_upendTime.setText(cell.getEnd_time());

                            pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    startTimeHour = hourOfDay;
                                                    startTimeMinute = minute;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,startTimeHour,startTimeMinute);
                                                    popUpStartTime = String.valueOf(DateFormat.format("hh:mm ",calendar));
                                                    pop_upstartTime.setText(popUpStartTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(startTimeHour,startTimeMinute);
                                    timePickerDialog.show();
                                }
                            });

                            pop_upendTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                                            DashboardActivity.this,
                                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    endTimeHour = hourOfDay;
                                                    endTimeMinute = minute;

                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(0,0,0,endTimeHour,endTimeMinute);

                                                    popUpEndTime = String.valueOf(DateFormat.format("hh:mm",calendar));
                                                    pop_upendTime.setText(popUpEndTime);

                                                }
                                            },12,0,false
                                    );

                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    timePickerDialog.updateTime(endTimeHour,endTimeMinute);
                                    timePickerDialog.show();
                                }
                            });



                            pop_upaddButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startTime.setEnabled(true);
                                    endTime.setEnabled(true);
                                    progressDialog.show();
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://us.infrmtx.com/iot/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    Service api = retrofit.create(Service.class);
                                    Call<JSONObject> call= api.getEditWeeks(device_id,cell.getDay_id(),popUpStartTime,popUpEndTime);
                                    call.enqueue(new Callback<JSONObject>() {
                                        @Override
                                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                            if (response.isSuccessful()){
                                                progressDialog.dismiss();
                                               /* Toast.makeText(DashboardActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/

                                                startActivity(getIntent());
                                                recreate();

                                            }else{
                                                progressDialog.dismiss();
                                                /*Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONObject> call, Throwable t) {
                                            progressDialog.dismiss();
                                           /* Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                            recreate();
                                        }
                                    });
                                }
                            });
                        }

                    }
                });
            }
            return v;
        }
    }

    @Override
    public void onBackPressed(){

        new iOSDialogBuilder(this)
                .setTitle("Exit!")
                .setSubtitle("Do you want to exit?")
                .setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener("Ok",new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        finish();
                    }
                })
                .setNegativeListener("Cancel", new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build().show();

    }
}
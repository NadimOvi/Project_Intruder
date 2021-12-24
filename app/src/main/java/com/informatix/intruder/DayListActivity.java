package com.informatix.intruder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.informatix.intruder.Api.Service;
import com.informatix.intruder.Model.DayModels;
import com.informatix.intruder.Model.MainDayModels;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DayListActivity extends AppCompatActivity {

    LinearLayout addLinearButton;
    ImageView ClickMenu,ClickClose;
    DrawerLayout drawerLayout;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    ListView list;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    String mainDeviceID;
    TextView pop_upSelectDate,pop_upstartTime,pop_upendTime;
    TextView profileNameID,totalBalance, ClickWeek, ClickDay, ClickSetting, startTimeShow, endTimeShow;
    Button addDateButton,addButton,deleteButton,editButton,pop_upaddButton,cancel_popup;
    int startTimeHour,startTimeMinute, endTimeHour,endTimeMinute;
    String popUpStartTime,popUpEndTime;
    int device_id;
    String date;
    int date_id;
    String datePostAdd;
    ArrayList<DayModels> tempList;

    ArrayList<String> dateList = new ArrayList<String>();
    /*ArrayList<Integer> countDate = new ArrayList<>();*/
    int countDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);
        getSupportActionBar().hide();
        list = (ListView) findViewById(R.id.listview);
        addLinearButton = findViewById(R.id.addLinearButton);

        sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String userID= sharedPreferences.getString("userID",null);
        String password= sharedPreferences.getString("password",null);
        String name= sharedPreferences.getString("name",null);
        mainDeviceID= sharedPreferences.getString("device_id","");
        String valid_till= sharedPreferences.getString("valid_till",null);


        progressDialog = new ProgressDialog(DayListActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        drawerLayout = findViewById(R.id.drawer);
        addDateButton = findViewById(R.id.addDateButton);
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
                Intent intent = new Intent(DayListActivity.this,DashboardActivity.class);
                startActivity(intent);
            }
        });
        ClickDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DayListActivity.this,DayListActivity.class);
                startActivity(intent);
            }
        });
        ClickSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DayListActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
        addLinearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogBuilder = new AlertDialog.Builder(DayListActivity.this);
                view = getLayoutInflater().inflate(R.layout.popup_add_date,null);
                pop_upSelectDate = view.findViewById(R.id.pop_upSelectDate);
                pop_upstartTime = view.findViewById(R.id.pop_upstartTime);
                pop_upendTime = view.findViewById(R.id.pop_upendTime);
                pop_upaddButton = view.findViewById(R.id.pop_upaddButton);
                dialogBuilder.setView(view);
                dialog = dialogBuilder.create();
                dialog.show();
                pop_upSelectDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar c = Calendar.getInstance();
                        int mYear, mMonth, mDay;
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);


                        DatePickerDialog datePickerDialog = new DatePickerDialog(DayListActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                       /* datePostAdd = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;*/
                                        datePostAdd = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                        pop_upSelectDate.setText(datePostAdd);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });

                pop_upstartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                DayListActivity.this,
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
                                DayListActivity.this,
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
                        startTimeShow.setEnabled(true);
                        endTimeShow.setEnabled(true);
                        progressDialog.show();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://us.infrmtx.com/iot/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        Service api = retrofit.create(Service.class);
                        Call<JSONObject> call= api.getAddDay(Integer.parseInt(mainDeviceID),datePostAdd,popUpStartTime,popUpEndTime);
                        call.enqueue(new Callback<JSONObject>() {
                            @Override
                            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                if (response.isSuccessful()){
                                    /*Toast.makeText(DayListActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/

                                    startActivity(getIntent());
                                    recreate();
                                    progressDialog.dismiss();

                                }else{
                                    /*Toast.makeText(DayListActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                }
                            }

                            @Override
                            public void onFailure(Call<JSONObject> call, Throwable t) {
                                progressDialog.dismiss();
                                recreate();


                            }
                        });
                    }
                });
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
        Call<MainDayModels> call= api.getDayModels(Integer.valueOf(mainDeviceID));
        call.enqueue(new Callback<MainDayModels>() {
            @Override
            public void onResponse(Call<MainDayModels> call, Response<MainDayModels> response) {
                progressDialog.dismiss();
                ArrayList<DayModels> itemsModel = response.body().getDates();

                ArrayList<DayModels> itemsList = new ArrayList<>();
                itemsList = sortAndAddSections(itemsModel);

               /* WeekDataAdapter weekDataAdapter = new WeekDataAdapter(itemsList,getApplicationContext());
                weekRecyclerView.setAdapter(weekDataAdapter);*/

                DayListActivity.ListAdapter adapter = new DayListActivity.ListAdapter(DayListActivity.this, itemsList);
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MainDayModels> call, Throwable t) {

            }
        });
    }
    private ArrayList sortAndAddSections(ArrayList<DayModels> itemList) {

        tempList = new ArrayList<>();
        //First we sort the array
        Collections.sort(itemList);

        //Loops thorugh the list and add a section before each sectioncell start
        String header = "";

        for(int i=0; i<itemList.size(); i++)
        {
            //If it is the start of a new section we create a new listcell and add it to our array
            if(!(header.equals(itemList.get(i).getDate()))) {
                /*TestModels sectionCell = new TestModels(null, null,null,itemList.get(i).getDate());*/
                DayModels sectionCell = new DayModels(0, itemList.get(i).getDate(), null,null,0);
                sectionCell.setToSectionHeader();
                tempList.add(sectionCell);

                header = itemList.get(i).getDate();
                dateList.add(header);

                dateList.size();


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
            final DayModels cell = (DayModels) getItem(position);

            //If the cell is a section header we inflate the header layout
            if(cell.isSectionHeader())
            {
                v = inflater.inflate(R.layout.section_header, null);
                v.setClickable(false);
                TextView header = (TextView) v.findViewById(R.id.section_header);
                header.setText(cell.getDate());
            }
            else
            {


                /*for (int i=0;i<=dateList.size();i++){
                    if (dateList.get(0).contains(dateList.get(i))){
                        Toast.makeText(DayListActivity.this, dateList.get(i), Toast.LENGTH_SHORT).show();
                    }
                }*/
                v = inflater.inflate(R.layout.date_row_item, null);

                startTimeShow = v.findViewById(R.id.startTime);
                endTimeShow =  v.findViewById(R.id.endTime);
                addButton =  v.findViewById(R.id.addButton);
                deleteButton =  v.findViewById(R.id.deleteButton);
                editButton =  v.findViewById(R.id.editButton);

                device_id = cell.getDevice_id();
                date = cell.getDate();
                date_id = cell.getDate_id();

                startTimeShow.setText(cell.getStart_time());
                endTimeShow.setText(cell.getEnd_time());

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder = new AlertDialog.Builder(DayListActivity.this);
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
                                        DayListActivity.this,
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
                                        DayListActivity.this,
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
                                startTimeShow.setEnabled(true);
                                endTimeShow.setEnabled(true);
                                progressDialog.show();
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("http://us.infrmtx.com/iot/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                Service api = retrofit.create(Service.class);
                                Call<JSONObject> call= api.getAddDay(device_id,cell.getDate(),popUpStartTime,popUpEndTime);
                                call.enqueue(new Callback<JSONObject>() {
                                    @Override
                                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                        if (response.isSuccessful()){
                                            /*Toast.makeText(DayListActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/

                                            /*startActivity(getIntent());*/
                                            /*recreate();*/
                                            progressDialog.dismiss();

                                        }else{
                                            /*Toast.makeText(DayListActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JSONObject> call, Throwable t) {
                                        progressDialog.dismiss();
                                       /* Toast.makeText(DayListActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                        recreate();
                                    }
                                });
                            }
                        });

                    }
                });

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogBuilder = new AlertDialog.Builder(DayListActivity.this);
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
                                        DayListActivity.this,
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
                                        DayListActivity.this,
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
                                startTimeShow.setEnabled(true);
                                endTimeShow.setEnabled(true);
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("http://us.infrmtx.com/iot/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                Service api = retrofit.create(Service.class);
                                Call<JSONObject> call= api.getEditDay(device_id,cell.getDate(),popUpStartTime,popUpEndTime,cell.getDate_id());
                                call.enqueue(new Callback<JSONObject>() {
                                    @Override
                                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                        if (response.isSuccessful()){
                                            progressDialog.dismiss();
                                           /* Toast.makeText(DayListActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();*/

                                            startActivity(getIntent());
                                            recreate();

                                        }else{
                                            progressDialog.dismiss();
                                            /*Toast.makeText(DayListActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JSONObject> call, Throwable t) {
                                        progressDialog.dismiss();
                                        /*Toast.makeText(DayListActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
                                        recreate();
                                    }

                                });
                            }
                        });

                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        tempList.size();
                        String cellData = cell.getDate();
                        ArrayList<Integer> list = new ArrayList<>();
                        for (int i=0;i<tempList.size();i++){
                            if (tempList.get(i).getDate().equals(cellData)){

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

                                            startTimeShow.setEnabled(true);
                                            endTimeShow.setEnabled(true);
                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl("http://us.infrmtx.com/iot/")
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();
                                            progressDialog.show();
                                            Service api = retrofit.create(Service.class);
                                            Call<JSONObject> call= api.getDeleteDate(cell.getDevice_id(),cell.getDate_id());
                                            call.enqueue(new Callback<JSONObject>() {
                                                @Override
                                                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                                    if (response.isSuccessful()){
                                                        progressDialog.dismiss();
                                                        /*Toast.makeText(DayListActivity.this, "Delete Done", Toast.LENGTH_SHORT).show();*/
                                                        recreate();

                                                    }else{
                                                        progressDialog.dismiss();
                                                        /*Toast.makeText(DayListActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<JSONObject> call, Throwable t) {
                                                    progressDialog.dismiss();
                                                    dialog.dismiss();
                                                    /*Toast.makeText(DayListActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
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

            }
            return v;
        }
    }
}
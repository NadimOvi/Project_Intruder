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

    ImageView ClickMenu,ClickClose;
    DrawerLayout drawerLayout;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    ListView list;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    String mainDeviceID;
    TextView pop_upstartTime,pop_upendTime;
    TextView profileNameID,totalBalance, ClickWeek, ClickDay, ClickSetting,startTime,endTime;
    Button addButton,deleteButton,editButton,pop_upaddButton,cancel_popup;
    int startTimeHour,startTimeMinute, endTimeHour,endTimeMinute;
    String popUpStartTime,popUpEndTime;
    int device_id;
    String date;
    int date_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);
        getSupportActionBar().hide();
        list = (ListView) findViewById(R.id.listview);
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

        ArrayList<DayModels> tempList = new ArrayList<>();
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
                v = inflater.inflate(R.layout.date_row_item, null);
                startTime = (EditText) v.findViewById(R.id.startTime);
                endTime = (EditText) v.findViewById(R.id.endTime);
                addButton =  v.findViewById(R.id.addButton);
                deleteButton =  v.findViewById(R.id.deleteButton);
                editButton =  v.findViewById(R.id.editButton);

                device_id = cell.getDevice_id();
                date = cell.getDate();
                date_id = cell.getDate_id();

                startTime.setText(cell.getStart_time());
                endTime.setText(cell.getEnd_time());

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
                                startTime.setEnabled(true);
                                endTime.setEnabled(true);
                                progressDialog.show();
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("http://us.infrmtx.com/iot/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                Service api = retrofit.create(Service.class);
                                Call<String> call= api.getAddDay(device_id,cell.getDate(),popUpStartTime,popUpEndTime);
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.isSuccessful()){
                                            Toast.makeText(DayListActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();

                                            /*startActivity(getIntent());*/
                                            /*recreate();*/
                                            progressDialog.dismiss();

                                        }else{
                                            Toast.makeText(DayListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        progressDialog.dismiss();
                                        Toast.makeText(DayListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
                                startTime.setEnabled(true);
                                endTime.setEnabled(true);
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
                                            Toast.makeText(DayListActivity.this, "New Time Added", Toast.LENGTH_SHORT).show();

                                            startActivity(getIntent());
                                            recreate();

                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(DayListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JSONObject> call, Throwable t) {
                                        progressDialog.dismiss();
                                        Toast.makeText(DayListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
                        startTime.setEnabled(true);
                        endTime.setEnabled(true);
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
                                    Toast.makeText(DayListActivity.this, "Delete Done", Toast.LENGTH_SHORT).show();
                                    recreate();
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(DayListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<JSONObject> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(DayListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        });
                    }
                });

            }
            return v;
        }
    }
}
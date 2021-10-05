package com.informatix.intruder;

import com.informatix.intruder.Model.LoginModel;
import com.informatix.intruder.Model.MainWeekModels;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {
    @GET("login.php")
    Call<LoginModel> getLogin(@Query("user") String userName,
                              @Query("pass") String userPassword);

    @GET("list_weekdays.php")
    Call<MainWeekModels> getModels(@Query("device_id") Integer device_id);

    @POST("weekdays_add.php")
    Call<String> getAddWeeks(@Query("device_id") Integer device_id,
                                      @Query("weekday_id") Integer weekday_id,
                                      @Query("start_time") String start_time,
                                      @Query("end_time") String end_time);
    @POST("weekdays_edit.php")
    Call<String> getEditWeeks(@Query("device_id") Integer device_id,
                                      @Query("weekday_id") Integer weekday_id,
                                      @Query("start_time") String start_time,
                                      @Query("end_time") String end_time);

    @Headers({"Accept: application/json"})
    @GET("weekdays_del.php")
    Call<JSONObject> getDeleteWeeks(@Query("device_id") Integer device_id,
                                    @Query("weekday_id") Integer weekday_id);




}

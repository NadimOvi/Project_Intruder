package com.informatix.intruder.Api;

import com.google.gson.JsonObject;
import com.informatix.intruder.Model.LoginModel;
import com.informatix.intruder.Model.MainDayModels;
import com.informatix.intruder.Model.MainWeekModels;
import com.informatix.intruder.Model.SettingsMainModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;
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

    @Headers({"Accept: application/json"})
    @GET("weekdays_add.php")
    Call<String> getAddWeeks(@Query("device_id") Integer device_id,
                                      @Query("weekday_id") Integer weekday_id,
                                      @Query("start_time") String start_time,
                                      @Query("end_time") String end_time);
    @POST("weekdays_edit.php")
    Call<JSONObject> getEditWeeks(@Query("device_id") Integer device_id,
                                      @Query("weekday_id") Integer weekday_id,
                                      @Query("start_time") String start_time,
                                      @Query("end_time") String end_time);

    @Headers({"Accept: application/json"})
    @GET("weekdays_del.php")
    Call<JSONObject> getDeleteWeeks(@Query("device_id") Integer device_id,
                                    @Query("weekday_id") Integer weekday_id);

    @GET("list_dates.php")
    Call<MainDayModels> getDayModels(@Query("device_id") Integer device_id);


    @Headers({"Accept: application/json"})
    @GET("dates_add.php")
    Call<JSONObject> getAddDay(@Query("device_id") Integer device_id,
                             @Query("date") String date,
                             @Query("start_time") String start_time,
                             @Query("end_time") String end_time);



    @POST("dates_edit.php")
    Call<JSONObject> getEditDay(@Query("device_id") Integer device_id,
                                  @Query("date") String date,
                                  @Query("start_time") String start_time,
                                  @Query("end_time") String end_time,
                                  @Query("date_id") Integer date_id);


    @Headers({"Accept: application/json"})
    @GET("dates_del.php")
    Call<JSONObject> getDeleteDate(@Query("device_id") Integer device_id,
                                    @Query("date_id") Integer date_id);


    @GET("settings.php")
    Call<SettingsMainModel> getSettingModels(@Query("device_id") Integer device_id);


    @POST("update_settings.php")
    Call<JsonObject> getEditSettings(@Query("device_id") Integer device_id,
                                     @Query("phone1") String phone1,
                                     @Query("phone2") String phone2,
                                     @Query("phone3") String phone3,
                                     @Query("relay") int relay,
                                     @Query("buzzer") int buzzer);

}

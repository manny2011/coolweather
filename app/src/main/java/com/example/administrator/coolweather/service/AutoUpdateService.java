package com.example.administrator.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.coolweather.receiver.AutoUpdateReceiver;
import com.example.administrator.coolweather.util.HttpCallbackListener;
import com.example.administrator.coolweather.util.HttpUtil;
import com.example.administrator.coolweather.util.Utility;

import java.util.Date;

/**
 * Created by Administrator on 2016/10/25.
 */
public class AutoUpdateService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG",String.valueOf(System.currentTimeMillis())+"\n"+new Date().toString());
        updateWeather();
        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        int eightHour=30*1000;
        long triggerAtTime= SystemClock.elapsedRealtime()+eightHour;
        Intent intent1=new Intent(AutoUpdateService.this, AutoUpdateReceiver.class);
        PendingIntent pi=PendingIntent.getBroadcast(this,0,intent1,0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String weather_code = sharedPreferences.getString("weather_code", "");
        String address="http://www.weather.com.cn/data/cityinfo/"+weather_code+".html";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinished(String response) {
                Utility.handleWeatherResponse(AutoUpdateService.this,response);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}

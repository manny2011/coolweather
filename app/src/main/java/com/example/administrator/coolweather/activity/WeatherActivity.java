package com.example.administrator.coolweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.coolweather.R;
import com.example.administrator.coolweather.util.HttpCallbackListener;
import com.example.administrator.coolweather.util.HttpUtil;
import com.example.administrator.coolweather.util.Utility;

/**
 * Created by Administrator on 2016/10/24.
 */
public class WeatherActivity extends Activity {

    private TextView cityNameText,publishText,weatherDespText,temp1Text,temp2Text,currentDateText;
    private LinearLayout weatherInfoLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        cityNameText= (TextView) findViewById(R.id.city_name);
        publishText= (TextView) findViewById(R.id.publish_text);
        weatherDespText= (TextView) findViewById(R.id.weather_desp);
        temp1Text= (TextView) findViewById(R.id.temp1);
        temp2Text= (TextView) findViewById(R.id.temp2);
        currentDateText= (TextView) findViewById(R.id.current_date);
        weatherInfoLayout= (LinearLayout) findViewById(R.id.weather_info_layout);
        String countyCode=getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)){
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.VISIBLE);
            queryWeatherCode(countyCode);
        }else{
            showWeather();
        }
    }

    private void showWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(preferences.getString("city_name",""));
        publishText.setText(preferences.getString("publish_time",""));
        weatherDespText.setText(preferences.getString("weather_desp",""));
        temp1Text.setText(preferences.getString("temp1",""));
        temp2Text.setText(preferences.getString("temp2",""));
        currentDateText.setText(preferences.getString("current_date",""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);

    }

    private void queryWeatherCode(String countyCode) {
        String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
        queryFromServer(address,"countyCode");
    }

    private void queryFromServer(String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinished(String response) {
                if(type.equals("countyCode")){
                    String[] array = response.split("\\|");
                    String weatherCode=array[1];
                    queryWeatherInfo(weatherCode);
                }else if(type.equals("weatherCode")){
                    Utility.handleWeatherResponse(WeatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
                e.printStackTrace();
            }
        });
    }

    private void queryWeatherInfo(String weatherCode) {
        String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromServer(address,"weatherCode");
    }
}

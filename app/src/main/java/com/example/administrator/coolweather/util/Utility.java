package com.example.administrator.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.administrator.coolweather.model.City;
import com.example.administrator.coolweather.model.CoolWeatherDB;
import com.example.administrator.coolweather.model.County;
import com.example.administrator.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/10/23.
 */
public class Utility {
    /**
     * 解析服务器返回的省级数据
     * */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
       if(!TextUtils.isEmpty(response)){
           String[] allProvinces=response.split(",");
           if(allProvinces.length > 0){
               for(String p:allProvinces){
                   String[] array = p.split("\\|");
                   Province province=new Province();
                   province.setProvinceName(array[1]);
                   province.setProvinceCode(array[0]);
                   coolWeatherDB.saveProvince(province);
               }
               return true;
           }
       }
        return false;

    }
    /**
     * 解析服务器返回的某省所包含的市级信息
     * */
    public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities=response.split(",");
            if(allCities!=null&&allCities.length>0){
                for(String c: allCities){
                    String[] array = c.split("\\|");
                    City city=new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;

    }
    /**
     * 解析服务器返回的某市所包含的所有县级信息
     * */
    public synchronized static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");
            if(allCounties.length>0){
                for(String c:allCounties){
                    String[] array = c.split("\\|");
                    County county=new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }


    public static void handleWeatherResponse(Context context,String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONObject weatherinfo = jsonObject.getJSONObject("weatherinfo");
            String cityName= weatherinfo.getString("city");
            String weatherCode=weatherinfo.getString("cityid");
            String temp1=weatherinfo.getString("temp1");
            String temp2=weatherinfo.getString("temp2");
            String weatherDesp=weatherinfo.getString("weather");
            String publishTime=weatherinfo.getString("ptime");
            saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,String weatherDesp,String publishTime){

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年m月d日", Locale.CHINA);
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name",cityName);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();


    }
}

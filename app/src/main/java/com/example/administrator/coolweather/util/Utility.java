package com.example.administrator.coolweather.util;

import android.text.TextUtils;

import com.example.administrator.coolweather.model.City;
import com.example.administrator.coolweather.model.CoolWeatherDB;
import com.example.administrator.coolweather.model.County;
import com.example.administrator.coolweather.model.Province;

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
}

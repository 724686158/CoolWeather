package com.example.admin.coolweather.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.admin.coolweather.db.CoolWeatherDB;
import com.example.admin.coolweather.model.City;
import com.example.admin.coolweather.model.County;
import com.example.admin.coolweather.model.Province;
import com.example.admin.coolweather.model.Weather;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 2016/9/18.
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response)
    {
        if(!TextUtils.isEmpty(response))
        {
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length > 0)
            {
                for(String p : allProvinces)
                {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //将解析出来的数据存储到Province表
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId)
    {
        if(!TextUtils.isEmpty(response))
        {
            String[] allCities = response.split(",");
            if(allCities != null && allCities.length > 0)
            {
                for(String p : allCities)
                {
                    String[] array = p.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    //将解析出来的数据存储到Province表
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县数据
     */
    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId)
    {
        if(!TextUtils.isEmpty(response))
        {
            String[] allCounties = response.split(",");
            if(allCounties != null && allCounties.length > 0)
            {
                for(String p : allCounties)
                {
                    String[] array = p.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    //将解析出来的数据存储到Province表
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
    public static void handleWeatherResponse(Context context, String response)
    {
        Gson gson = new Gson();
        Weather weather = gson.fromJson(response, new TypeToken<Weather>() {
        }.getType());
        saveWeatherInfo(context, weather);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static void saveWeatherInfo(Context context, Weather weather) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        String w = weather.toString();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", weather.getWeatherinfo().getCity());
        editor.putString("temp1", weather.getWeatherinfo().getTemp1());
        editor.putString("temp2", weather.getWeatherinfo().getTemp2());
        editor.putString("current_weather", weather.getWeatherinfo().getWeather());
        editor.putString("publish_time", weather.getWeatherinfo().getPtime());
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();


    }
}

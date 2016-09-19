package com.example.admin.coolweather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2016/9/18.
 */
public class Weather {

    /**
     * city : 德州
     * cityid : 101120401
     * temp1 : 4℃
     * temp2 : 18℃
     * weather : 晴
     * img1 : n0.gif
     * img2 : d0.gif
     * ptime : 18:00
     */

    private WeatherinfoBean weatherinfo;

    public WeatherinfoBean getWeatherinfo() {
        return weatherinfo;
    }

    public void setWeatherinfo(WeatherinfoBean weatherinfo) {
        this.weatherinfo = weatherinfo;
    }

    public static class WeatherinfoBean implements Parcelable {
        private String city;
        private int cityid;
        private String temp1;
        private String temp2;
        private String weather;
        private String img1;
        private String img2;
        private String ptime;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getCityid() {
            return cityid;
        }

        public void setCityid(int cityid) {
            this.cityid = cityid;
        }

        public String getTemp1() {
            return temp1;
        }

        public void setTemp1(String temp1) {
            this.temp1 = temp1;
        }

        public String getTemp2() {
            return temp2;
        }

        public void setTemp2(String temp2) {
            this.temp2 = temp2;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getImg1() {
            return img1;
        }

        public void setImg1(String img1) {
            this.img1 = img1;
        }

        public String getImg2() {
            return img2;
        }

        public void setImg2(String img2) {
            this.img2 = img2;
        }

        public String getPtime() {
            return ptime;
        }

        public void setPtime(String ptime) {
            this.ptime = ptime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.city);
            dest.writeInt(this.cityid);
            dest.writeString(this.temp1);
            dest.writeString(this.temp2);
            dest.writeString(this.weather);
            dest.writeString(this.img1);
            dest.writeString(this.img2);
            dest.writeString(this.ptime);
        }

        public WeatherinfoBean() {
        }

        protected WeatherinfoBean(Parcel in) {
            this.city = in.readString();
            this.cityid = in.readInt();
            this.temp1 = in.readString();
            this.temp2 = in.readString();
            this.weather = in.readString();
            this.img1 = in.readString();
            this.img2 = in.readString();
            this.ptime = in.readString();
        }

        public static final Parcelable.Creator<WeatherinfoBean> CREATOR = new Parcelable.Creator<WeatherinfoBean>() {
            @Override
            public WeatherinfoBean createFromParcel(Parcel source) {
                return new WeatherinfoBean(source);
            }

            @Override
            public WeatherinfoBean[] newArray(int size) {
                return new WeatherinfoBean[size];
            }
        };

        @Override
        public String toString() {
            return "WeatherinfoBean{" +
                    "city='" + city + '\'' +
                    ", cityid=" + cityid +
                    ", temp1='" + temp1 + '\'' +
                    ", temp2='" + temp2 + '\'' +
                    ", weather='" + weather + '\'' +
                    ", img1='" + img1 + '\'' +
                    ", img2='" + img2 + '\'' +
                    ", ptime='" + ptime + '\'' +
                    '}';
        }
    }
}

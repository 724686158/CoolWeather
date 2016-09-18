package com.example.admin.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.coolweather.R;
import com.example.admin.coolweather.db.CoolWeatherDB;
import com.example.admin.coolweather.model.City;
import com.example.admin.coolweather.model.County;
import com.example.admin.coolweather.model.Province;
import com.example.admin.coolweather.util.HttpCallbackListener;
import com.example.admin.coolweather.util.HttpUtil;
import com.example.admin.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/18.
 */
public class ChooseAreaActivity extends Activity{
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog mProgressDialog;
    private TextView titleText;
    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;
    private CoolWeatherDB mCoolWeatherDB;
    private List<String> dataList = new ArrayList<String>();

    /**
     * 省列表
     */
    private List<Province> mProvinceList;

    /**
     * 市列表
     */
    private List<City>  mCityList;

    /**
     * 县列表
     */
    private List<County> mCountyList;

    /**
     * 选中的省份
     */
    private Province selectedProvince;

    /**
     * 选中的城市
     */
    private City selectedCity;

    /**
     * 当前选中级别
     */
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        mListView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        mListView.setAdapter(mArrayAdapter);
        mCoolWeatherDB = CoolWeatherDB.getInstance(this);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE)
                {
                    selectedProvince = mProvinceList.get(position);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY)
                {
                    selectedCity = mCityList.get(position);
                    queryCounties();
                }
            }
        });
        queryProvinces(); //加载省级资源
    }

    private void queryProvinces() {
        mProvinceList = mCoolWeatherDB.loadProvinces();
        if (mProvinceList.size() > 0)
        {
            dataList.clear();
            for (Province province : mProvinceList)
            {
                dataList.add(province.getProvinceName());
            }
            mArrayAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else {
            queryFromServer(null, "province");
        }
    }



    private void queryCities() {
        mCityList = mCoolWeatherDB.loadCities(selectedProvince.getId());
        if (mCityList.size() > 0)
        {
            dataList.clear();
            for (City city : mCityList)
            {
                dataList.add(city.getCityName());
            }
            mArrayAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else {
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }

    }
    private void queryCounties() {
        mCountyList = mCoolWeatherDB.loadCounties(selectedCity.getId());
        if (mCountyList.size() > 0) {
            dataList.clear();
            for (County county : mCountyList) {
                dataList.add(county.getCountyName());
            }
            mArrayAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        } else {
            queryFromServer(selectedCity.getCityCode(), "county");
        }
    }

        /**
         * 根据传入的代号和类型从服务器上查询省市县数据
         */
        private void queryFromServer(final String code, final String type) {
            String address;
            if (TextUtils.isEmpty(code))
            {
                address = "http://www.weather.com.cn/data/list3/city.xml";
            }else {
                address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
            }
            showProgressDialog();
            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void OnFinish(String response) {
                    boolean result = false;
                    if ("province".equals(type))
                    {
                        result = Utility.handleProvincesResponse(mCoolWeatherDB, response);
                    }else if("city".equals(type))
                    {
                        result = Utility.handleCitiesResponse(mCoolWeatherDB, response, selectedProvince.getId());
                    }else if ("county".equals(type))
                    {
                        result = Utility.handleCountiesResponse(mCoolWeatherDB, response, selectedCity.getId());
                    }
                    if (result)
                    {
                        //通过runOnUiThread()方法回到主线程处理逻辑
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeProgressDialog();
                                if ("province".equals(type))
                                {
                                    queryProvinces();
                                }
                                else if ("city".equals(type))
                                {
                                    queryCities();
                                }
                                else if ("county".equals(type))
                                {
                                    queryCounties();
                                }
                            }
                        });

                    }
                }


                @Override
                public void OnError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (mProgressDialog == null)
        {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if (mProgressDialog != null)
        {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 捕获Back按键，根据当前的级别来判断，此时应当返回市列表、省列表、还是直接退出
     */
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY)
        {
            queryCities();
        }
        else if (currentLevel == LEVEL_CITY)
        {
            queryProvinces();
        }
        else
        {
            finish();
        }
    }
}


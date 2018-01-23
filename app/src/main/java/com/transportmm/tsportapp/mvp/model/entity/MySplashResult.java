package com.transportmm.tsportapp.mvp.model.entity;

/**
 * Created by Administrator on 2017/9/22.
 */

public class MySplashResult {
    private String quName;
    private String cityName;
    private String stateDetailed;//天气状况
    private String tem1;//最高温度
    private String tem2;//最低温度
    private String windState;

    @Override
    public String toString() {
        return "MySplashResult{" +
                "quName='" + quName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", stateDetailed='" + stateDetailed + '\'' +
                ", tem1='" + tem1 + '\'' +
                ", tem2='" + tem2 + '\'' +
                ", windState='" + windState + '\'' +
                '}';
    }

    public MySplashResult() {
    }

    public MySplashResult(String quName, String cityName, String stateDetailed, String tem1, String tem2, String windState) {
        this.quName = quName;
        this.cityName = cityName;
        this.stateDetailed = stateDetailed;
        this.tem1 = tem1;
        this.tem2 = tem2;
        this.windState = windState;
    }

    public String getQuName() {
        return quName;
    }

    public void setQuName(String quName) {
        this.quName = quName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateDetailed() {
        return stateDetailed;
    }

    public void setStateDetailed(String stateDetailed) {
        this.stateDetailed = stateDetailed;
    }

    public String getTem1() {
        return tem1;
    }

    public void setTem1(String tem1) {
        this.tem1 = tem1;
    }

    public String getTem2() {
        return tem2;
    }

    public void setTem2(String tem2) {
        this.tem2 = tem2;
    }

    public String getWindState() {
        return windState;
    }

    public void setWindState(String windState) {
        this.windState = windState;
    }
}

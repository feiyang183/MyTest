package com.example.dell.myscada.bean;

/**
 * Created by 25468 on 2017/7/12.
 */

public class UpdataInfo {
    private String versionName;
    private int versionCode;
    private String description;
    private String url;
    private String msg;

    public UpdataInfo(){
        versionName = "";
        versionCode = 0;
        description = "";
        url = "";
        msg = "";
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

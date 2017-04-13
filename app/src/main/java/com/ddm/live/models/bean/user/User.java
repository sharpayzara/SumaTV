package com.ddm.live.models.bean.user;

/**
 * Created by JiGar on 2016/1/13.
 */
public class User {
    private String username;

    private String phoneNum;

    private boolean isLogin;

    private String sex;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}

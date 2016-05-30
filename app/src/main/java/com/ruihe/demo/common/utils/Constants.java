package com.ruihe.demo.common.utils;

/**
 * 描述：全局常量
 * Created by ruihe on 2016/4/28.
 */
public class Constants {

    public enum MainPage {
        PAGE_FIRST,
        PAGE_SECOND,
        PAGE_THIRD,
        PAGE_FOURTH,
        PAGE_FIVE
    }


    public static String URL_ROOT = getRootUrl();// 请求地址
    public static final String API_LOGIN = "/app/login";//登录接口

    public static String getRootUrl() {
        return "";
    }


}

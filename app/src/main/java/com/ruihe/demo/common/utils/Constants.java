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

    public static String[] userIds = {"1001", "1002"};
    public static String[] userNames = {"张大三", "王小二"};
    public static String[] userPortraitUris = {"http://fitimg.yunimg.cn/avatar/201604/19/1461048379970i448zd5noqeg4a7s.jpg!100",
            "fitimg.yunimg.cn/avatar/201601/14/1452755682800nx8vto5chhhra6tr.jpg!100"};
    public static String[] userTokens = {"URCzmPo52FX+v64HnzWkVbvtwcaQq0EftgCCKcu3Hawf1MMxOljweevM2wHocHPe77rqT+T/f9tq71X4as0haQ==",
            "4jGjZ7AnVhREOM2LyUuEFjzNbe1+FBrEAPK376cggSwkl4XaNNBF/qXPqTSgtzlwec8TJtHv+9VBeZAtA/TU+A=="};

}

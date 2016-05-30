package com.ruihe.demo.test;


import com.ruihe.demo.common.utils.json.JsonParserBase;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemResponseBase extends JsonParserBase {

    public int cn; //请求结果码 0为成功
    public JSONObject data; //相关数据
    public String message;
    public JSONObject notify; //显示全局通知

    /**
     * 将基本的response数据解析为基础item
     */
    public static ItemResponseBase parserBaseResponse(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        ItemResponseBase item = new ItemResponseBase();
        item.cn = object.getInt("cn"); //不用父类方法.如果不是标准格式,直接抛出异常
        item.message = getString(object, "message");
        item.data = getJSONObject(object, "data");
        item.notify = getJSONObject(object, "notify");
        return item;
    }

}

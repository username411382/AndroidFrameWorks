package com.ruihe.demo.common.utils.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;


/**
 * json解析的基类
 */
public class JsonParserBase implements Serializable {

    //是否在log中显示解析时没有得到值的key
    private static boolean SHOW_JSON_PARAM_LOST = false;
    private static String TAG = "JsonParser could not find the key";

    public static String getString(JSONObject object, String key) throws JSONException {
        if (object.has(key)) {
            return object.getString(key);
        } else {
            if (SHOW_JSON_PARAM_LOST) {
                Log.i(TAG, key);
            }
            return null;
        }
    }

    public static int getInt(JSONObject object, String key) throws JSONException {
        if (object.has(key)) {
            return object.getInt(key);
        } else {
            if (SHOW_JSON_PARAM_LOST) {
                Log.i(TAG, key);
            }
            return 0;
        }
    }

    public static long getLong(JSONObject object, String key) throws JSONException {
        if (object.has(key)) {
            return object.getLong(key);
        } else {
            if (SHOW_JSON_PARAM_LOST) {
                Log.i(TAG, key);
            }
            return 0;
        }
    }

    public static double getDouble(JSONObject object, String key) throws JSONException {
        if (object.has(key)) {
            return object.getDouble(key);
        } else {
            if (SHOW_JSON_PARAM_LOST) {
                Log.i(TAG, key);
            }
            return 0;
        }
    }

    public static JSONObject getJSONObject(JSONObject object, String key) throws JSONException {
        if (object.has(key) && isJSONObject(object.get(key).toString())) {
            return object.getJSONObject(key);
        } else {
            if (SHOW_JSON_PARAM_LOST) {
                Log.i(TAG, key);
            }
            return new JSONObject();
        }
    }

    public static JSONArray getJSONArray(JSONObject object, String key) throws JSONException {
        if (object.has(key) && isJSONArray(object.get(key).toString())) {
            return object.getJSONArray(key);
        } else {
            if (SHOW_JSON_PARAM_LOST) {
                Log.i(TAG, key);
            }
            return new JSONArray();
        }
    }

    //是否是Json对象
    public static boolean isJSONObject(String data) throws JSONException {
        Object json = new JSONTokener(data).nextValue();
        return json instanceof JSONObject;
    }

    //是否是Json数组
    public static boolean isJSONArray(String data) throws JSONException {
        Object json = new JSONTokener(data).nextValue();
        return json instanceof JSONArray;
    }

}

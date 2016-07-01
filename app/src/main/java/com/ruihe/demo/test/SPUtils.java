package com.ruihe.demo.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.ruihe.demo.MyApplication;


public class SPUtils {

    private static final String SP_NAME = "YLH";
    public static final String SP_KEY_USE_DEV_SERVICE = "use_dev_service";

    private Context mContext;
    private static SPUtils instance;


    public static final String CURRENT_USER_ID = "user_id";


    /**
     * 所在小区
     */
    public static final String GARDEN_ID = "garden_id";
    public static final String GARDEN_NAME = "garden_name";
    public static final String GARDEN_DESCRIPTION = "garden_description";
    public static final String GARDEN_IMAGE_URL = "garden_image_url";

    /**
     * 所在地址
     */
    public static final String LOCATION_LATITUDE = "location_latitude";
    public static final String LOCATION_LONGITUDE = "location_longitude";
    public static final String LOCATION_ADDRESS = "location_address";
    public static final String LOCATION_COUNTRY = "location_country";
    public static final String LOCATION_PROVINCE = "location_province";
    public static final String LOCATION_CITY = "location_city";
    public static final String LOCATION_DISTRICT = "location_district";
    public static final String LOCATION_STREET = "location_street";
    public static final String USER_CITY = "user_city";

    /**
     * 用户
     */
    public static final String USER_ID = "user_id";
    public static final String USER_ID_NUMBER = "user_id_number";
    public static final String USER_ROLE = "user_role";
    public static final String USER_NAME = "user_name";
    public static final String USER_GENDER = "user_gender";
    public static final String USER_AVATAR = "user_avatar";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_ADDRESS = "user_address";
    public static final String USER_MONEY = "user_money";
    public static final String USER_COIN = "user_coin";
    public static final String USER_REAL_NAME = "user_real_name";
    public static final String USER_OWNER_NAME = "user_owner_name";
    public static final String USER_IS_BOSS = "user_is_boss";
    public static final String SESS = "sess";
    public static final String USER_GETUI_CID = "getui_cid";
    public static final String USER_MINE_MENU = "user_module"; //在我页面需要显示的模块信息

    private SPUtils(Context context) {
        mContext = context;
    }

    public static synchronized SPUtils getInstance() {
        if (instance == null) {
            instance = new SPUtils(MyApplication.getInstance());
        }
        return instance;
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    public void putLong(String key, long value) {
        Editor editor = getSharedPreferences().edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key, long defValue) {
        return getSharedPreferences().getLong(key, defValue);
    }

    public void putInt(String key, int value) {
        Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public void putFloat(String key, float value) {
        Editor editor = getSharedPreferences().edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key, float defValue) {
        return getSharedPreferences().getFloat(key, defValue);
    }

    public void remove(String key) {
        Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        editor.apply();
    }


    /**
     * 保存用户信息至SharedPreference
     */
    public void saveCurrentUser(ItemUser user) {
        Editor editor = getSharedPreferences().edit();
        editor.putString(USER_NAME, user.name);
        editor.putString(USER_PHONE, user.phone);
        editor.putString(USER_AVATAR, user.avatar);
        editor.putInt(USER_GENDER, user.gender);
        editor.putInt(GARDEN_ID, user.gardenId);
        editor.putString(GARDEN_NAME, user.gardenName);
        editor.putString(USER_ADDRESS, user.address);
        editor.putInt(USER_MONEY, user.money);
        editor.putInt(USER_COIN, user.coin);
        editor.putString(USER_REAL_NAME, user.realName);
        editor.putString(USER_OWNER_NAME, user.ownerName);
        editor.putInt(USER_ID, user.id);
        editor.putString(USER_ID_NUMBER, user.idNumber);
        editor.putInt(USER_ROLE, user.role);
        editor.putInt(USER_IS_BOSS, user.isBoss);
        editor.putString(USER_MINE_MENU, user.mineMenu);

        if (!TextUtils.isEmpty(user.sess)) {
            editor.putString(SESS, user.sess);
        }
        user.sess = getString(SESS, "");

        editor.apply();
    }

    /**
     * 从SharedPreference获取当前用户信息
     */
    public ItemUser getCurrentUser() {
        ItemUser user = new ItemUser();
        SharedPreferences preferences = getSharedPreferences();
        user.id = preferences.getInt(USER_ID, 0);
        user.idNumber = preferences.getString(USER_ID_NUMBER, "");
        user.role = preferences.getInt(USER_ROLE, 0);
        user.name = preferences.getString(USER_NAME, "");
        user.gender = preferences.getInt(USER_GENDER, 0);
        user.phone = preferences.getString(USER_PHONE, "");
        user.avatar = preferences.getString(USER_AVATAR, "");
        user.gardenId = preferences.getInt(GARDEN_ID, 0);
        user.gardenName = preferences.getString(GARDEN_NAME, "");
        user.address = preferences.getString(USER_ADDRESS, "");
        user.money = preferences.getInt(USER_MONEY, 0);
        user.coin = preferences.getInt(USER_COIN, 0);
        user.realName = preferences.getString(USER_REAL_NAME, "");
        user.ownerName = preferences.getString(USER_OWNER_NAME, "");
        user.isBoss = preferences.getInt(USER_IS_BOSS, 0);
        user.mineMenu = preferences.getString(USER_MINE_MENU, "");
        return user;
    }

    /**
     * 退出登录，从SharedPreference移除用户信息
     */
    public void removeCurrentUser() {
        Editor editor = getSharedPreferences().edit();
        editor.remove(SESS);

        editor.remove(USER_ID);
        editor.remove(USER_ID_NUMBER);
        editor.remove(USER_ROLE);
        editor.remove(USER_NAME);
        editor.remove(USER_GENDER);
        editor.remove(USER_PHONE);
        editor.remove(USER_AVATAR);
        editor.remove(GARDEN_ID);
        editor.remove(GARDEN_NAME);
        editor.remove(USER_ADDRESS);
        editor.remove(USER_IS_BOSS);
        editor.remove(USER_MINE_MENU);

        editor.remove(USER_MONEY);
        editor.remove(USER_COIN);
        editor.remove(USER_REAL_NAME);
        editor.remove(USER_OWNER_NAME);

        editor.apply();
    }

}

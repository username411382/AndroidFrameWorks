package com.ruihe.demo.test;


import com.ruihe.demo.common.utils.json.JsonParserBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ItemUser extends JsonParserBase implements Serializable {

    private static final long serialVersionUID = 8565786478528682361L;

    public int id;
    public String idNumber;
    public int role;
    public String name;
    public String phone;
    public int gender;
    public String avatar;
    public int gardenId;
    public String gardenName;
    public String address;
    public String sess;
    public String password;
    public int money;
    public int coin;
    public String realName;
    public String ownerName;
    public int isBoss;

    public String mineMenu; //用户自己才有的字段，包含需要显示在 我 页面的菜单模块，是一个JSONArray

    /**
     * 单个用户的json解析
     */
    public static ItemUser parserUser(JSONObject userData) throws JSONException {

        ItemUser user = new ItemUser();
        user.id = getInt(userData, "id");
        user.idNumber = getString(userData, "idCard");
        user.role = getInt(userData, "role");
        user.name = getString(userData, "nickname");
        user.phone = getString(userData, "phone");
        user.avatar = getString(userData, "avatar");
        user.gender = getInt(userData, "gender");
        user.gardenId = getInt(userData, "communityId");
        user.gardenName = getString(userData, "communityName");
        user.address = getString(userData, "address");
        user.sess = getString(userData, "sess");
        user.money = getInt(userData, "money");
        user.coin = getInt(userData, "gold");
        user.realName = getString(userData, "fullName");
        user.ownerName = getString(userData, "ownerRealName");
        user.isBoss = getInt(userData, "isBoss");

        user.mineMenu = getString(userData, "module");
        return user;

    }
}

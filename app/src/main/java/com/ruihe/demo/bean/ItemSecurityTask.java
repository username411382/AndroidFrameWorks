package com.ruihe.demo.bean;

import com.ruihe.demo.common.utils.json.JsonParserBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：安保任务的实体
 * Created by ruihe on 2016/5/17.
 */
public class ItemSecurityTask {

    public String securityTaskName;
    public int lastPatrolTime;
    public int currentMothCount;
    public int merchantId;
    public int serviceId;
    public int taskId;


    public static List<ItemSecurityTask> parserSecurityTask(JSONObject data) throws JSONException {

        if (!data.has("list")) {
            return null;
        }
        ArrayList<ItemSecurityTask> items = new ArrayList<>();
        JSONArray dataArray = data.getJSONArray("list");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject jsonObject = dataArray.getJSONObject(i);
            ItemSecurityTask item = new ItemSecurityTask();
            item.securityTaskName = JsonParserBase.getString(jsonObject, "name");
            item.lastPatrolTime = JsonParserBase.getInt(jsonObject, "lastPatrolTime");
            item.currentMothCount = JsonParserBase.getInt(jsonObject, "toMonthCount");
            item.merchantId = JsonParserBase.getInt(jsonObject, "merchantId");
            item.serviceId = JsonParserBase.getInt(jsonObject, "serviceId");
            item.taskId = JsonParserBase.getInt(jsonObject, "taskId");
            items.add(item);
        }
        return items;
    }


}

package com.ruihe.demo.common.utils;

import android.app.Activity;

import com.ruihe.demo.activity.ActivityMain;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity的容器
 */
public class ActivitiesContainer {

    private ActivitiesContainer() {
    }

    private static ActivitiesContainer instance = new ActivitiesContainer();
    private static List<Activity> activityStack = new ArrayList<>();

    public static ActivitiesContainer getInstance() {
        return instance;
    }

    public void addActivity(Activity activity) {
        if (!activityStack.contains(activity)) {
            activityStack.add(activity);
        }
    }

    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }


    /**
     * 取得最后打开的activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        for (int i = activityStack.size() - 1; i >= 0; i++) {
            if (activityStack.get(i) != null) {
                return activityStack.get(i);
            }
        }
        return null;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivities() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有Activity,直到登录页面
     */
    public void finishAllActivitiesUntilLoginPage() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            /*if (null != activityStack.get(i) && !(activityStack.get(i) instanceof ActivityLogin)) {
                activityStack.get(i).finish();
			}*/
        }
    }

    /**
     * 结束所有Activity,直到首页
     */
    public void finishAllActivitiesUntilHomePage() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i) && !(activityStack.get(i) instanceof ActivityMain)) {
                activityStack.get(i).finish();
            }
        }
    }

    /**
     * 取得activity列表
     *
     * @return
     */
    public List<Activity> getActivityStack() {
        return activityStack;
    }


}

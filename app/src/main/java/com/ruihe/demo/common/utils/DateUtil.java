package com.ruihe.demo.common.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Class containing some static utility methods.
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

    /** 时间日期格式化到年月日时分秒. */
    public static final String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";

    /** 时分. */
    public static final String dateFormatHM = "HH:mm";
    public static final String[] week = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};


    /**
     * 描述：获取指定日期时间的字符串,用于导出想要的格式.
     *
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @param format 输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 转换后的String类型的日期时间
     */
    public static String getStringByFormat(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMDHMS);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /**
     * 描述：获取milliseconds表示的日期时间的字符串.
     *
     * @param milliseconds the milliseconds
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 日期时间字符串
     */
    public static String getStringByFormat(long milliseconds,String format) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    /**
     * 描述：计算两个日期所差的天数.
     *
     * @param milliseconds1 the milliseconds1
     * @param milliseconds2 the milliseconds2
     * @return int 所差的天数
     */
    public static int getOffsetDay(long milliseconds1, long milliseconds2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(milliseconds1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(milliseconds2);
        //先判断是否同年
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = d1 - d2;
        }
        return day;
    }

    /**
     * 描述：计算两个日期所差的小时数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的小时数
     */
    public static int getOffsetHour(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int h = 0;
        int day = getOffsetDay(date1, date2);
        h = h1-h2+day*24;
        return h;
    }

    /**
     * 描述：计算两个日期所差的分钟数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的分钟数
     */
    public static int getOffsetMinutes(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int m1 = calendar1.get(Calendar.MINUTE);
        int m2 = calendar2.get(Calendar.MINUTE);
        int h = getOffsetHour(date1, date2);
        int m = 0;
        m = m1-m2+h*60;
        return m;
    }

    /**
     * 描述：根据时间返回格式化后的时间的描述.
     * 小于1小时显示多少分钟前  大于1小时显示今天＋实际日期，大于今天全部显示实际时间
     *
     * @param strDate the str date
     * @param outFormat the out format
     * @return the string
     */
    public static String formatDateStr2Desc(String strDate,String outFormat) {

        DateFormat df = new SimpleDateFormat(dateFormatYMDHMS);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c2.setTime(df.parse(strDate));
            c1.setTime(new Date());
            int d = getOffsetDay(c1.getTimeInMillis(), c2.getTimeInMillis());
            if(d==0){
                int h = getOffsetHour(c1.getTimeInMillis(), c2.getTimeInMillis());
                if(h>0){
                    return "今天"+getStringByFormat(strDate,dateFormatHM);
                    //return h + "小时前";
                }else if(h<0){
                    //return Math.abs(h) + "小时后";
                }else if(h==0){
                    int m = getOffsetMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
                    if(m>0){
                        return m + "分钟前";
                    }else if(m<0){
                        //return Math.abs(m) + "分钟后";
                    }else{
                        return "刚刚";
                    }
                }

            }else if(d>0){
                return d+"天前发布";
            }else if(d<0){
                if(d == -1){
                    //return "明天"+getStringByFormat(strDate,outFormat);
                }else if(d== -2){
                    //return "后天"+getStringByFormat(strDate,outFormat);
                }else{
                    //return Math.abs(d) + "天后"+getStringByFormat(strDate,outFormat);
                }
            }

            String out = getStringByFormat(strDate,outFormat);
            if (!StringUtil.isEmpty(out)) {
                return out;
            }
        } catch (Exception e) {
        }

        return strDate;
    }

	public static String getTimeByOutput(double publishTime) {
		DecimalFormat df = new DecimalFormat("#.#");
		return formatDateStr2Desc(getStringByFormat(Long.parseLong(df.format(publishTime)) * 1000,
				dateFormatYMDHMS), dateFormatYMDHMS);
	}

	@SuppressLint("SimpleDateFormat")
	public static  String getTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
		Date date = new Date(time);
		String times = sdf.format(date);
		if (getTodayDate().equals(getFormatTimeDate(time, null))) {
			times = "今天 " + getFormatTimeDate(time, "HH:mm");
		} else if (getYesterdayDate().equals(getFormatTimeDate(time, null))) {
			times = "昨天 " + getFormatTimeDate(time, "HH:mm");
		}
		return times;
	}

	private static String getFormatTimeDate(long time, String format) {
		String dateFormat = "yyyy-MM-dd";
		if (!TextUtils.isEmpty(format)) {
			dateFormat = format;
		}
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date(time);
		return sdf.format(date);
	}

    /**
     * Sting或Date转化为时间戳
     * @param time
     * @return
     */
    public static long stingIntoTimestamp(String time){
        SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        long d = 0;
        try {
            Date	date = format.parse(time);
            d = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 获取当前日期是星期几
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static int getWeekOfDate(Date dt) {
        int[] weekDays = {6,0, 1,2, 3, 4, 5};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) -1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }



    public static String getDayOnWeek(int index){
        Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = null;
        switch (index) {
            case 0:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
                dateString = df.format(cal.getTime());
                break;
            case 1:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY); //获取本周一的日期
                dateString = df.format(cal.getTime());
                break;
            case 2:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY); //获取本周一的日期
                dateString = df.format(cal.getTime());
                break;
            case 3:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY); //获取本周一的日期
                dateString = df.format(cal.getTime());
                break;
            case 4:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY); //获取本周一的日期
                dateString = df.format(cal.getTime());
                break;
            case 5:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); //获取本周一的日期
                dateString = df.format(cal.getTime());
                break;
            case 6:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                //增加一个星期，才是我们中国人理解的本周日的日期
                cal.add(Calendar.WEEK_OF_YEAR, 1);
                dateString = df.format(cal.getTime());
        }
        return dateString;
    }


	/**
	 * 得到昨天的日期
	 */
	public static String getYesterdayDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(calendar.getTime());
	}

	/**
	 * 得到今天的日期
	 */
	public static String getTodayDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	/**
	 * 判断当前应用是否在前台
	 */
	public static boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
        return !TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName());
    }

    // 判断时间段
    @SuppressWarnings("deprecation")
    public static int judgeTimeSec(int hour, int min) {
        Calendar c = Calendar.getInstance();
        // 12点
        Date lunchTime = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH), 12, 0);
        // 18点
        Date nightTime = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH), 18, 0);
        // 需要判断的时间
        Date nowTime = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH), hour, min);

        if (nowTime.before(lunchTime)) {
            // 在早上
           // return GetTimeInterface.morning;
        } else if (nowTime.after(lunchTime) && nowTime.before(nightTime)
                || nowTime.getTime() == lunchTime.getTime()) {
            // 在中午12:00也属于中午
          //  return GetTimeInterface.afternoon;
        } else if (nowTime.after(nightTime)
                || nowTime.getTime() == nightTime.getTime()) {
            // 在晚上 在18：0这一刻相等也属于晚上
         //   return GetTimeInterface.night;
        } else {
            throw new RuntimeException("时间错误");
        }
        return 0;
    }
}

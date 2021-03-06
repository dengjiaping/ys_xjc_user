package com.hemaapp.wcpc_user;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.SpannableString;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.hemaapp.hm_FrameWork.emoji.EmojiParser;
import com.hemaapp.hm_FrameWork.emoji.ParseEmojiMsgUtil;
import com.hemaapp.wcpc_user.activity.LoginActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import xtom.frame.XtomActivityManager;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomTimeUtil;
import xtom.frame.util.XtomToastUtil;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.os.Environment.DIRECTORY_PICTURES;


/**
 *
 */
public class BaseUtil {
    private static double EARTH_RADIUS = 6378.137;// 地球半径

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static String transDuration(long duration) {
        String ds = "";
        long min = duration / 60;
        if (min < 60) {
            ds += (min + "分钟");
        } else {
            long hour = min / 60;
            long rm = min % 60;
            if (rm > 0)
                ds += (hour + "小时" + rm + "分钟");
            else
                ds += (hour + "小时");
        }
        return ds;
    }

    /**
     * 判断wifi连接状态
     *
     * @param ctx
     * @return
     */
    public static boolean isWifiAvailable(Context ctx) {
        ConnectivityManager conMan = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (NetworkInfo.State.CONNECTED == wifi) {
            return true;
        } else {
            return false;
        }
    }

    public static String transDistance(float distance) {
        String ds = "";
        if (distance < 1000) {
            ds += (distance + "m");
        } else {
            float km = distance / 1000;
            ds += (String.format(Locale.getDefault(), "%.3f", km));
        }
//        float km = distance;
//        ds += (String.format(Locale.getDefault(), "%.3f", km));
        return ds;
    }

    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public static String getTime2(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String TransTimeHour(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        try {
            Date e = sdf.parse(time);
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return dateFormat.format(e);
        } catch (Exception var5) {
            return null;
        }
    }

    public static int compareTime(String time1, String time2) {
        // TODO Auto-generated method stub
        DateFormat df = new SimpleDateFormat("HH:mm:ss");//创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
        try {
            Date dt1 = df.parse(time1);//将字符串转换为date类型
            Date dt2 = df.parse(time2);
            if (dt1.getTime() >= dt2.getTime())//比较时间大小,如果dt1大于dt2
            {
                return 1;
            } else {
                return -1;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 计算两点间的距离
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static Double GetDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 退出登录
     */
    public static void clientLoginout(Context context) {
        XtomSharedPreferencesUtil.save(context, "password", "");
        XtomSharedPreferencesUtil.save(context, "username", "");
        BaseApplication application = BaseApplication.getInstance();
        application.setUser(null);
        XtomActivityManager.finishAll();
        Intent it = new Intent(context, LoginActivity.class);
        context.startActivity(it);
    }

    /**
     * 隐藏用户名
     *
     * @param nickname
     * @return
     */
    public static String hideNickname(String nickname) {
        int length = nickname.length();
        String first = nickname.substring(0, 1);
        String last = nickname.substring(length - 1, length);
        String x = "";
        for (int i = 0; i < length - 2; i++) {
            x += "*";
        }
        return first + x + last;
    }

    /**
     * 转换时间显示形式(与当前系统时间比较),在显示即时聊天的时间时使用
     *
     * @param time 时间字符串
     * @return String
     */
    public static String transTimeChat(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            String current = XtomTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
            String dian24 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
                    + " 24:00:00";
            String dian00 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
                    + " 00:00:00";

            java.util.Date dt = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);
            calendar.add(Calendar.DAY_OF_YEAR, 1);// 日期加1天
            java.util.Date dt1 = calendar.getTime();
            String dian48 = XtomTimeUtil.TransTime(sdf.format(dt1), "yyyy-MM-dd")
                    + " 24:00:00";

            calendar.add(Calendar.DAY_OF_YEAR, 1);// 日期加2天
            java.util.Date dt2 = calendar.getTime();
            String dian72 = XtomTimeUtil.TransTime(sdf.format(dt2), "yyyy-MM-dd")
                    + " 24:00:00";

//			Date now = null;
            Date date = null;
            Date d24 = null;
            Date d00 = null;
            Date d48 = null;
            Date d72 = null;
            try {
//				now = sdf.parse(current); // 将当前时间转化为日期
                date = sdf.parse(time); // 将传入的时间参数转化为日期
                d00 = sdf.parse(dian00);
                d24 = sdf.parse(dian24);
                d48 = sdf.parse(dian48);
                d72 = sdf.parse(dian72);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (date.getTime() <= d24.getTime()
                    && date.getTime() >= d00.getTime())
                return "今天" + XtomTimeUtil.TransTime(time, "HH:mm");

            if (date.getTime() >= d24.getTime() && date.getTime() <= d48.getTime())
                return "明天" + XtomTimeUtil.TransTime(time, "HH:mm");

            if (date.getTime() >= d48.getTime() && date.getTime() <= d72.getTime())
                return "后天" + XtomTimeUtil.TransTime(time, "HH:mm");

            int sendYear = Integer
                    .valueOf(XtomTimeUtil.TransTime(time, "yyyy"));
            int nowYear = Integer.valueOf(XtomTimeUtil.TransTime(current,
                    "yyyy"));
            if (sendYear < nowYear)
                return XtomTimeUtil.TransTime(time, "yyyy-MM-dd HH:mm");
            else
                return XtomTimeUtil.TransTime(time, "MM-dd HH:mm");

        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 计算百分率 if(x>y) (x-y)/total else (y-x)/total value 是除数 2 total 是被除数 3
     */
    public static String getValue(double x, double y, double total) {
        String result = "";// 接受百分比的值
        String flag = "+"; // 数据的正负值
        DecimalFormat df1 = new DecimalFormat("0.00%"); // ##.00%
        // 百分比格式，后面不足2位的用0补齐
        if (x >= y) {
            flag = "+";
            double tempresult = (x - y) / total;
            result = df1.format(tempresult);
        } else {
            flag = "-";
            double tempresult = (y - x) / total;
            result = df1.format(tempresult);
        }
        return flag + result;
    }

    /**
     * 计算好评率
     */
    public static String getRate(int x, int total) {
        String result = "";// 接受百分比的值
        String flag = "+"; // 数据的正负值
        DecimalFormat df1 = new DecimalFormat("0.0%"); // ##.00%
        // 百分比格式，后面不足2位的用0补齐
        double tempresult = x / total;
        result = df1.format(tempresult);
        return flag + result;
    }

    /**
     * 获利
     */
    public static String income(String old, String now, String count) {
        String result = "";
        String flag = "+";
        Double x = Double.parseDouble(old);
        Double y = Double.parseDouble(now);
        int cou = Integer.parseInt(count);
        if (x >= y) {
            double t = x - y;
            double c = t * cou;
            flag = "-";
            result = String.valueOf(c);
        } else {
            double t = y - x;
            double c = t * cou;
            flag = "+";
            result = String.valueOf(c);
        }
        return flag + result;
    }

    // 聊天中的表情
    public static void SetMessageTextView(Context mContext, TextView mtextview,
                                          String mcontent) {
        if (mcontent == null || "".equals(mcontent)) {
            mtextview.setText("");
            return;
        }

        String unicode = EmojiParser.getInstance(mContext).parseEmoji(mcontent);
        SpannableString spannableString = ParseEmojiMsgUtil
                .getExpressionString(mContext, unicode);
        mtextview.setText(spannableString);
    }

    /**
     * 计算缓存大小的表现形式
     */
    public static String getSize(long size) {

        /** size 如果 小于1024 * 1024,以KB单位返回,反则以MB单位返回 */

        DecimalFormat df = new DecimalFormat("###.##");
        float f;
        if (size < 1024 * 1024) {
            f = (float) ((float) size / (float) 1024);
            return (df.format(new Float(f).doubleValue()) + "KB");
        } else {
            f = (float) ((float) size / (float) (1024 * 1024));
            return (df.format(new Float(f).doubleValue()) + "MB");
        }
    }


    public static String transString(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd",
                Locale.getDefault());
        String str = sdf.format(d);
        return str;
    }

    public static String transString1(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        String str = sdf.format(d);
        return str;
    }

    public static String get2double(double data) {
        DecimalFormat df2 = new DecimalFormat("###.00");
        String value = df2.format(data);
        if (data == 0)
            value = "0.00";
        return value;
    }

    /**
     * 传入要显示评分的五个ImageView和分数 根据四舍五入来计算分数
     */
    public static void transScore(ImageView image_0, ImageView image_1,
                                  ImageView image_2, ImageView image_3, ImageView image_4,
                                  String totalcount, String score) {
        int point = 0;
        if ("".equals(totalcount) || null == totalcount || "null".equals(totalcount)
                || "0".equals(totalcount))
            point = 0;
        else if ("".equals(score) || null == score || "null".equals(score)
                || "0".equals(score))
            point = 0;
        else {
            int t = Integer.parseInt(totalcount);
            int s = Integer.parseInt(score);

            double d = s / t;
            DecimalFormat df2 = new DecimalFormat("###.00");
            String value = df2.format(d);
            double dd = Double.parseDouble(value);
            if (dd >= 0 && dd < 0.5)
                point = 0;
            else if (dd < 1.5)
                point = 1;
            else if (dd < 2.5)
                point = 2;
            else if (dd < 3.5)
                point = 3;
            else if (dd < 4.5)
                point = 4;
            else {
                point = 5;
            }
        }

        if (point == 0) {
            image_0.setImageResource(R.mipmap.img_star_n);
            image_1.setImageResource(R.mipmap.img_star_n);
            image_2.setImageResource(R.mipmap.img_star_n);
            image_3.setImageResource(R.mipmap.img_star_n);
            image_4.setImageResource(R.mipmap.img_star_n);
        } else if (point == 1) {
            image_0.setImageResource(R.mipmap.img_star_s);
            image_1.setImageResource(R.mipmap.img_star_n);
            image_2.setImageResource(R.mipmap.img_star_n);
            image_3.setImageResource(R.mipmap.img_star_n);
            image_4.setImageResource(R.mipmap.img_star_n);
        } else if (point == 2) {
            image_0.setImageResource(R.mipmap.img_star_s);
            image_1.setImageResource(R.mipmap.img_star_s);
            image_2.setImageResource(R.mipmap.img_star_n);
            image_3.setImageResource(R.mipmap.img_star_n);
            image_4.setImageResource(R.mipmap.img_star_n);
        } else if (point == 3) {
            image_0.setImageResource(R.mipmap.img_star_s);
            image_1.setImageResource(R.mipmap.img_star_s);
            image_2.setImageResource(R.mipmap.img_star_s);
            image_3.setImageResource(R.mipmap.img_star_n);
            image_4.setImageResource(R.mipmap.img_star_n);
        } else if (point == 4) {
            image_0.setImageResource(R.mipmap.img_star_s);
            image_1.setImageResource(R.mipmap.img_star_s);
            image_2.setImageResource(R.mipmap.img_star_s);
            image_3.setImageResource(R.mipmap.img_star_s);
            image_4.setImageResource(R.mipmap.img_star_n);
        } else if (point == 5) {
            image_0.setImageResource(R.mipmap.img_star_s);
            image_1.setImageResource(R.mipmap.img_star_s);
            image_2.setImageResource(R.mipmap.img_star_s);
            image_3.setImageResource(R.mipmap.img_star_s);
            image_4.setImageResource(R.mipmap.img_star_s);
        }
    }

    public static void transScoreByPoint(ImageView image_0, ImageView image_1,
                                         ImageView image_2, ImageView image_3, ImageView image_4,
                                         String count) {
        int point = Integer.parseInt(count);

        if (point == 0) {
            image_0.setImageResource(R.mipmap.img_star_n);
            image_1.setImageResource(R.mipmap.img_star_n);
            image_2.setImageResource(R.mipmap.img_star_n);
            image_3.setImageResource(R.mipmap.img_star_n);
            image_4.setImageResource(R.mipmap.img_star_n);
        } else if (point == 1) {
            image_0.setImageResource(R.mipmap.img_star_s);
            image_1.setImageResource(R.mipmap.img_star_n);
            image_2.setImageResource(R.mipmap.img_star_n);
            image_3.setImageResource(R.mipmap.img_star_n);
            image_4.setImageResource(R.mipmap.img_star_n);
        } else if (point == 2) {
            image_0.setImageResource(R.mipmap.img_star_s);
            image_1.setImageResource(R.mipmap.img_star_s);
            image_2.setImageResource(R.mipmap.img_star_n);
            image_3.setImageResource(R.mipmap.img_star_n);
            image_4.setImageResource(R.mipmap.img_star_n);
        } else if (point == 3) {
            image_0.setImageResource(R.mipmap.img_star_s);
            image_1.setImageResource(R.mipmap.img_star_s);
            image_2.setImageResource(R.mipmap.img_star_s);
            image_3.setImageResource(R.mipmap.img_star_n);
            image_4.setImageResource(R.mipmap.img_star_n);
        } else if (point == 4) {
            image_0.setImageResource(R.mipmap.img_star_s);
            image_1.setImageResource(R.mipmap.img_star_s);
            image_2.setImageResource(R.mipmap.img_star_s);
            image_3.setImageResource(R.mipmap.img_star_s);
            image_4.setImageResource(R.mipmap.img_star_n);
        } else if (point == 5) {
            image_0.setImageResource(R.mipmap.img_star_s);
            image_1.setImageResource(R.mipmap.img_star_s);
            image_2.setImageResource(R.mipmap.img_star_s);
            image_3.setImageResource(R.mipmap.img_star_s);
            image_4.setImageResource(R.mipmap.img_star_s);
        }
    }

    public static void transScoreByPoint1(ImageView image_0, ImageView image_1,
                                          ImageView image_2, ImageView image_3, ImageView image_4,
                                          String count) {
        int point = Integer.parseInt(count);

        if (point == 0) {
            image_0.setImageResource(R.mipmap.img_pingjia_n);
            image_1.setImageResource(R.mipmap.img_pingjia_n);
            image_2.setImageResource(R.mipmap.img_pingjia_n);
            image_3.setImageResource(R.mipmap.img_pingjia_n);
            image_4.setImageResource(R.mipmap.img_pingjia_n);
        } else if (point == 1) {
            image_0.setImageResource(R.mipmap.img_pingjia_s);
            image_1.setImageResource(R.mipmap.img_pingjia_n);
            image_2.setImageResource(R.mipmap.img_pingjia_n);
            image_3.setImageResource(R.mipmap.img_pingjia_n);
            image_4.setImageResource(R.mipmap.img_pingjia_n);
        } else if (point == 2) {
            image_0.setImageResource(R.mipmap.img_pingjia_s);
            image_1.setImageResource(R.mipmap.img_pingjia_s);
            image_2.setImageResource(R.mipmap.img_pingjia_n);
            image_3.setImageResource(R.mipmap.img_pingjia_n);
            image_4.setImageResource(R.mipmap.img_pingjia_n);
        } else if (point == 3) {
            image_0.setImageResource(R.mipmap.img_pingjia_s);
            image_1.setImageResource(R.mipmap.img_pingjia_s);
            image_2.setImageResource(R.mipmap.img_pingjia_s);
            image_3.setImageResource(R.mipmap.img_pingjia_n);
            image_4.setImageResource(R.mipmap.img_pingjia_n);
        } else if (point == 4) {
            image_0.setImageResource(R.mipmap.img_pingjia_s);
            image_1.setImageResource(R.mipmap.img_pingjia_s);
            image_2.setImageResource(R.mipmap.img_pingjia_s);
            image_3.setImageResource(R.mipmap.img_pingjia_s);
            image_4.setImageResource(R.mipmap.img_pingjia_n);
        } else if (point == 5) {
            image_0.setImageResource(R.mipmap.img_pingjia_s);
            image_1.setImageResource(R.mipmap.img_pingjia_s);
            image_2.setImageResource(R.mipmap.img_pingjia_s);
            image_3.setImageResource(R.mipmap.img_pingjia_s);
            image_4.setImageResource(R.mipmap.img_pingjia_s);
        }
    }

    // 功能：判断点是否在多边形内
    // 方法：求解通过该点的水平线与多边形各边的交点
    // 结论：单边交点为奇数，成立!
    //参数：
    // POINT p   指定的某个点
    // LPPOINT ptPolygon 多边形的各个顶点坐标（首末点可以不一致）
    public static boolean PtInPolygon(LatLng point, List<LatLng> APoints) {
        int nCross = 0;
        for (int i = 0; i < APoints.size(); i++) {
            LatLng p1 = APoints.get(i);
            LatLng p2 = APoints.get((i + 1) % APoints.size());
            // 求解 y=p.y 与 p1p2 的交点
            if (p1.longitude == p2.longitude)      // p1p2 与 y=p0.y平行
                continue;
            if (point.longitude < Math.min(p1.longitude, p2.longitude))   // 交点在p1p2延长线上
                continue;
            if (point.longitude >= Math.max(p1.longitude, p2.longitude))   // 交点在p1p2延长线上
                continue;
            // 求交点的 X 坐标 --------------------------------------------------------------
            double x = (double) (point.longitude - p1.longitude) * (double) (p2.latitude - p1.latitude) / (double) (p2.longitude - p1.longitude) + p1.latitude;
            if (x > point.latitude)
                nCross++; // 只统计单边交点
        }
        // 单边交点为偶数，点在多边形之外 ---
        return (nCross % 2 == 1);
    }

    public static void swap(String str1, String str2) {
        String tem = str1;
        str1 = str2;
        str2 = tem;
    }

    public static void hideInput(Context context, View v) {
        ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow
                (v.getWindowToken(), 0);
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean net = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || net) {
            return true;
        }
        return false;

    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {


        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public static final void hideInputWhenTouchOtherViewBase(Activity activity, MotionEvent ev, List<View> excludeViews) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (excludeViews != null && !excludeViews.isEmpty()) {
                for (int i = 0; i < excludeViews.size(); i++) {
                    if (isTouchView(excludeViews.get(i), ev)) {
                        return;
                    }
                }
            }
            View v = activity.getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        activity.getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

        }
    }

    public static final boolean isTouchView(View view, MotionEvent event) {
        if (view == null || event == null) {
            return false;
        }
        int[] leftTop = {0, 0};
        view.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + view.getHeight();
        int right = left + view.getWidth();
        if (event.getRawX() > left && event.getRawX() < right
                && event.getRawY() > top && event.getRawY() < bottom) {
            return true;
        }
        return false;
    }

    public static final boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            return !isTouchView(v, event);
        }
        return false;
    }

    public static final String alignmentString(int time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return "" + time;
        }
    }

    //乘
    public static String multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).toString();
    }

    //除
    public static String divide(String v1, String v2, int scale) {
        //如果精确范围小于0，抛出异常信息
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    //保留小数
    public static String round(double v, int scale) {
        if (scale < 0) {
            return "0";
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static long time(String start, String end) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = null;
        try {
            now = df.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = null;
        try {
            date = df.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long l = now.getTime() - date.getTime();
        return l;
//		long min=l/(60*1000);
//		long s=(l/1000);
    }

    public static String initImagePath(Context context) {
        String imagePath;
        try {

            String cachePath_internal = XtomFileUtil.getCacheDir(context)
                    + "images/";// 获取缓存路径
            File dirFile = new File(cachePath_internal);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            imagePath = cachePath_internal + "share.png";
            File file = new File(imagePath);
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic;

                pic = BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher);

                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            imagePath = null;
        }
        return imagePath;
    }

    public static void fitPopupWindowOverStatusBar(PopupWindow pop, boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(pop, needFullScreen);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".versionProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri,
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 生成视图的预览
     *
     * @param activity
     * @param v
     * @return 视图生成失败返回null
     * 视图生成成功返回视图的绝对路径
     */
    public static String saveImage(Activity activity, View v) {
        Bitmap bitmap;
        String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + "/";
        //String path = XtomFileUtil.getFileDir(activity) + "preview.png";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        path = path + "preview.png";
        File file = new File(path);

        try {
            if (file.exists())
                file.delete();
            file.createNewFile();
            bitmap = Bitmap.createBitmap(bitmap, location[0], location[1], v.getWidth(), v.getHeight());
            FileOutputStream fout = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            fout.close();
            // }
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            XtomToastUtil.showShortToast(activity, "已保存图片到手机");
            Log.e("png", "生成预览图片成功：" + path);
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("png", "生成预览图片失败：" + e);
        } catch (IllegalArgumentException e) {
            Log.e("png", "width is <= 0, or height is <= 0");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 清理缓存
            view.destroyDrawingCache();
        }
        return null;

    }

}

package com.hemaapp.wcpc_user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.keepappalive.AppSystemUtils;
import com.hemaapp.wcpc_user.keepappalive.ScreenManager;


/**
 * 类描述：1像素的Activity
 * 创建时间：2017/10/30 17:01
 */
public class SinglePixelActivity extends Activity {
    private static final String packageName = "com.hemaapp.wcpc_user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pixel);
        Log.e("live", "onCreate:  启动1px" );
        Window window = getWindow();
        window.setGravity(Gravity.TOP | Gravity.LEFT);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.x = 0;
        attributes.y = 0;
        attributes.width = 1;
        attributes.height = 1;
        window.setAttributes(attributes);
        ScreenManager.getScreenManager(this).setSingleActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("live", "1像素activity销毁" );
        if(!AppSystemUtils.isRunning(getApplicationContext(),packageName)){
            Intent intent = new Intent(getApplicationContext(),MainNewMapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("live", "onDestroy: 应用被杀死了重启他" );
            startActivity(intent);
        }
    }
}

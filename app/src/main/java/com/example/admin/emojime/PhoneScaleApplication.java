package com.example.admin.emojime;

import android.content.Context;
import android.graphics.Point;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by admin on 1/18/2017.
 */

public class PhoneScaleApplication extends MultiDexApplication
{
    public static int phone_width;
    private static PhoneScaleApplication enableMultiDex;
    public static Context context;

    public PhoneScaleApplication(){
        enableMultiDex=this;
    }

    public static PhoneScaleApplication getEnableMultiDexApp() {
        return enableMultiDex;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();

        MultiDex.install(this);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        phone_width = size.x;
        int height = size.y;
    }
}

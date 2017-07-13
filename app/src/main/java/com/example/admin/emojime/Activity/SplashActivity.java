package com.example.admin.emojime.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import com.example.admin.emojime.Common.Application;
import com.example.admin.emojime.Utils.SerializeObject;
import com.example.admin.emojime.Utils.SharePreferencesUtil;
import com.example.admin.emojime.R;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity
{
    private static long SPLASH_DISPLAY_LENGTH = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        initializeCommonValues();

        //Shown or not shown tutorial page
        SharePreferencesUtil.init(SplashActivity.this);
        final boolean skip_flag = SharePreferencesUtil.get("Skip");

        if(skip_flag)
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    Intent mainIntent = new Intent(SplashActivity.this, TabViewActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }else
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    //Call File called ".dat" from external Storage
    private ArrayList<String> loadResources(String string)
    {
        ArrayList<String> userList = new ArrayList<>();

        String ser = SerializeObject.ReadSettings(this, string);
        if (ser != null && !ser.equalsIgnoreCase(""))
        {
            Object obj = SerializeObject.stringToObject(ser);
            if (obj instanceof ArrayList)
            {
                userList = (ArrayList<String>)obj;
            }
        }
        return userList;
    }

    public void initializeCommonValues()
    {
        Application instance = Application.getSharedInstance();
        instance.bitmapNamesForExpressions = new ArrayList<>();
        instance.bitmapNamesForExpressions = loadResources("myobject.dat");

        instance.bitmapNamesForOther = new ArrayList<>();
        instance.bitmapNamesForOther = loadResources("myobject1.dat");

        instance.ruForExpressions = new ArrayList<>();
        instance.ruForExpressions = loadResources("myobjectrecently.dat");

        instance.ruForOthers = new ArrayList<>();
        instance.ruForOthers = loadResources("myobjectrecentlyother.dat");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return false;
    }
}

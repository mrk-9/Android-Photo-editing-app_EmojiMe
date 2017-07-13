package com.example.admin.emojime.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import com.example.admin.emojime.Utils.SharePreferencesUtil;
import com.example.admin.emojime.R;
import com.hkm.slider.Indicators.PagerIndicator;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener
{
    SliderLayout slider;
    PagerIndicator pagerIndicator;
    Button move_btn;

    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    public static int REQUEST_CALL_PHONE_CODE = 1235;

    //android OS 6.0 permerssion(SYSTEMALERTWINDOW permission)
    public void someMethod() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this))
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_screen);

        //Call permission automatically
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {

                }else
                {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CALL_PHONE_CODE);
                }
            }
        }

        initView();

        //slider operation
        initWidget();
    }

    //Initialization
    public void initView()
    {
        slider = (SliderLayout) findViewById(R.id.slider);
        pagerIndicator = (PagerIndicator) findViewById(R.id.page_indicator);
        move_btn = (Button) findViewById(R.id.move_btn);
        move_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.move_btn:
                new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Confirm")
                    .setMessage("To skip this tutorial when opening app, click ok. If not, click cancel. Tutorial can always be found in settings.")
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                        //Check tutorial Page when opening app
                        SharePreferencesUtil.setBoolean("Skip", false);
                        Log.d("Skip_main",String.valueOf(SharePreferencesUtil.get("Skip")));

                        Intent mainIntent = new Intent(MainActivity.this, TabViewActivity.class);
                        startActivity(mainIntent);
                        finish();
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                    {
                        @Override

                        public void onClick(DialogInterface dialog, int which)
                        {
                        //Check tutorial Page when opening app
                        SharePreferencesUtil.setBoolean("Skip", true);
                        Log.d("Skip_main",String.valueOf(SharePreferencesUtil.get("Skip")));

                        Intent mainIntent = new Intent(MainActivity.this, TabViewActivity.class);
                        startActivity(mainIntent);
                        finish();
                        }
                    })
                .create()
                .show();
                break;
        }
    }

    public void initWidget()
    {
        ArrayList<SliderView> images = new ArrayList<>();
        images.add(new SliderView(this, R.drawable.demo1));
        images.add(new SliderView(this, R.drawable.demo2));
        images.add(new SliderView(this, R.drawable.demo3));
        images.add(new SliderView(this, R.drawable.demo4));
        images.add(new SliderView(this, R.drawable.demo5));
        images.add(new SliderView(this, R.drawable.demo6));
        slider.loadSliderList(images);
        slider.setCustomIndicator(pagerIndicator);
    }

    public class SliderView extends BaseSliderView
    {
        private Context context;
        private Integer image;

        public SliderView(Context context)
        {
            super(context);
            this.context = context;
        }

        public SliderView(Context context, Integer image)
        {
            super(context);
            this.context = context;
            this.image = image;
        }

        @Override
        public View getView()
        {
            View view = LayoutInflater.from(context).inflate(R.layout.slider_view, null, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            Picasso.with(context).load(image).into(imageView);
            return view;
        }
    }
}

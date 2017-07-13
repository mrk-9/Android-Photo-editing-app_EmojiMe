package com.example.admin.emojime.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import com.example.admin.emojime.Adapter.TabViewPagerAdapter;
import com.example.admin.emojime.Common.Application;
import com.example.admin.emojime.Utils.PagerSlidingTabStrip;
import com.example.admin.emojime.R;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class TabViewActivity extends FragmentActivity
{
    private ViewPager pager;
    private PagerSlidingTabStrip tabs;
    private TabViewPagerAdapter adapter;
    Application instance;
    int width = 0;
    int height = 0;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tabview_screen);

        initView();

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pager.setCurrentItem(instance.common_pageNum);
    }

    public void initView()
    {
        pager = (ViewPager)this.findViewById(R.id.pager);
        adapter = new TabViewPagerAdapter(getSupportFragmentManager(), TabViewActivity.this);
        pager.setAdapter(adapter);
        tabs = (PagerSlidingTabStrip) this.findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        instance = Application.getSharedInstance();
        width = getScreenWidth();
        height = getScreenHeight();
    }

    //Call below method after Crop the image in EmojiMeFragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {
                Log.d("url", result.getUri().toString());
                instance.common_uri = result.getUri();

                //Image Compressing
                bmp = loadBitmap(instance.common_uri.toString());

                if(width == 320 && height == 480)
                    scaledImage(320,480);
                else if(width == 480 && height == 800)
                    scaledImage(480, 800);
                else if(width == 480 && height == 854)
                    scaledImage(480, 854);
                else if(width == 540 && height == 960)
                    scaledImage(540,960);
                else if(width == 1080 && height == 1920)
                    scaledImage(1080,1920);
                else if(width == 720 && height == 1280)
                    scaledImage(720,1280);
                else if(width == 768 && height == 1200)
                    scaledImage(720,1200);
                else if(width == 600 && height == 1024)
                    scaledImage(600,1024);
                else if(width == 900 && height == 1400)
                    scaledImage(900,1400);
                else if(width == 720 && height == 1280)
                    scaledImage(720,1280);
                else if(width == 800 && height == 1280)
                    scaledImage(800,1280);
                else if(width == 720 && height == 1280)
                    scaledImage(720,1280);
                else if(width == 1200 && height == 1824)
                    scaledImage(1200,1824);
                else if(width == 1200 && height == 1920)
                    scaledImage(1200,1920);
                else if(width == 1600 && height == 2560)
                    scaledImage(1600,2560);
                else
                    scaledImage(width, height);
            }
        }
    }

    //Convert Uri to Bitmap
    public Bitmap loadBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

    @Override
    public void onBackPressed()
    {}

    public void scaledImage(int recommandWidth, int recommandHeight)
    {
        if(bmp.getWidth() > bmp.getHeight())
        {
            String cropedPath = instance.common_uri.getPath();
            Bitmap temp = BitmapFactory.decodeFile(cropedPath);
            instance.common_bitmap = Bitmap.createScaledBitmap(temp, (int) (recommandWidth * 0.8), (int) (bmp.getHeight()* (recommandWidth * 0.8)/bmp.getWidth()), false);
            Intent mainIntent = new Intent(TabViewActivity.this, EditPictureActivity.class);
            startActivity(mainIntent);
        }else if(bmp.getWidth() < bmp.getHeight())
        {
            String cropedPath = instance.common_uri.getPath();
            Bitmap temp = BitmapFactory.decodeFile(cropedPath);
            instance.common_bitmap = Bitmap.createScaledBitmap(temp, (int)(bmp.getWidth() * (recommandHeight * 0.8)/bmp.getHeight()), (int)(recommandHeight * 0.8), false);
            Intent mainIntent = new Intent(TabViewActivity.this, EditPictureActivity.class);
            startActivity(mainIntent);
        }else
        {
            //864,1536  ,  1080,1920
            String cropedPath = instance.common_uri.getPath();
            Bitmap temp = BitmapFactory.decodeFile(cropedPath);
            instance.common_bitmap = Bitmap.createScaledBitmap(temp, (int) (recommandWidth * 0.8), (int) (recommandHeight * 0.8), false);
            Intent mainIntent = new Intent(TabViewActivity.this, EditPictureActivity.class);
            startActivity(mainIntent);
        }
    }

    private int getScreenHeight()
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        return height;
    }
    private int getScreenWidth()
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        return width;
    }
}

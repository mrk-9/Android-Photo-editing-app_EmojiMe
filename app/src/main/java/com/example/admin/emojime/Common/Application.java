package com.example.admin.emojime.Common;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by admin on 1/20/2017.
 */

public class Application
{
    public static Application instance = null;
    public static Uri common_uri;
    public static Bitmap common_bitmap;
    public static Bitmap common_bitmap2;
    public static Bitmap common_bitmap3;
    public static Bitmap common_resizedBitmap;
    public static Bitmap common_resizedBitmap2;
    public static Bitmap common_bitmap4;
    public static boolean common_flag;
    public static boolean common_flag_final;
    public static boolean common_flaginThree;
    public static boolean common_flagzero;
    public static boolean common_flattwotothree = false;
    public static ArrayList<String> bitmapNamesForExpressions = new ArrayList<String>();
    public static ArrayList<String> bitmapNamesForOther = new ArrayList<String>();
    public static ArrayList<String> ruForExpressions = new ArrayList<String>();
    public static ArrayList<String> ruForOthers = new ArrayList<String>();
    public static int common_pageNum;

    public static Application getSharedInstance()
    {
        if(instance == null)
        {
            instance = new Application();
        }
        return instance;
    }
    public Application()
    {

    }
}

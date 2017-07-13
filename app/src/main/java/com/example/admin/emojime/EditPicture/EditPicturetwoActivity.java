package com.example.admin.emojime.EditPicture;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.admin.emojime.Activity.TabViewActivity;
import com.example.admin.emojime.Common.Application;
import com.example.admin.emojime.R;
import com.example.admin.emojime.Utils.DrawingView;
import com.example.admin.emojime.Utils.SerializeObject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.util.ArrayList;


/**
 * Created by admin on 1/20/2017.
 */

public class EditPicturetwoActivity extends Activity implements View.OnClickListener
{
    private DrawingViewtwo drawingViewtwo;
    RelativeLayout eraser_rel;
    ImageView erasor1, erasor2, erasor3, erasor4;
    Button editpicture_cancelBtn;
    ImageView cut_tool, eraser_tool, replace_tool,confirm_tool;
    Application instance;
    BitmapDrawable ob;
    int width,height;
    int i = 0;
    Bitmap resultBitmap = null;
    Bitmap bm;
    PopupMenu popup;
    boolean isPortrait = false;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.editpicture2_screen);

        //Initialization
        initView();

        // Zoom drawingViewtwo to match cropped image
        scaledDrawingView();

        //Display croped image that have got from previous activity
        if(instance.common_flagzero)
        {
            imageCrop();
            instance.common_flagzero = false;
        }

        if(instance.common_flaginThree)
        {
            imgeCropforThreeActivity();
            instance.common_flaginThree = false;
        }
    }
    public void initView()
    {
        editpicture_cancelBtn = (Button) findViewById(R.id.editpicture2_cancelbtn);
        cut_tool = (ImageView) findViewById(R.id.cut2_tool);
        eraser_tool = (ImageView) findViewById(R.id.erasor2_tool);
        replace_tool = (ImageView) findViewById(R.id.replace2_tool);
        confirm_tool = (ImageView) findViewById(R.id.confirm2_tool);
        eraser_rel = (RelativeLayout) findViewById(R.id.eraser2_rel);
        drawingViewtwo = (DrawingViewtwo) findViewById(R.id.drawingtwo);
        erasor1 = (ImageView) findViewById(R.id.erasortwo1);
        erasor2 = (ImageView) findViewById(R.id.erasortwo2);
        erasor3 = (ImageView) findViewById(R.id.erasortwo3);
        erasor4 = (ImageView) findViewById(R.id.erasortwo4);
        instance = Application.getSharedInstance();
        eraser_rel.setVisibility(View.GONE);

        editpicture_cancelBtn.setOnClickListener(this);
        cut_tool.setOnClickListener(this);
        eraser_tool.setOnClickListener(this);
        erasor1.setOnClickListener(this);
        erasor2.setOnClickListener(this);
        erasor3.setOnClickListener(this);
        erasor4.setOnClickListener(this);
        replace_tool.setOnClickListener(this);
        cut_tool.setOnClickListener(this);
        confirm_tool.setOnClickListener(this);

        instance.common_flattwotothree = false;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.confirm2_tool:
                //Get Bitmap from Screen
                FrameLayout view = (FrameLayout) findViewById(R.id.lycontainer2);
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                bm = view.getDrawingCache();

                popup = new PopupMenu(this, v);
                progress = ProgressDialog.show(EditPicturetwoActivity.this, "Rendering...", "Please wait",true);
                new AsyncCaller().execute();
                break;

            case R.id.editpicture2_cancelbtn:
                finish();
                break;

            case R.id.cut2_tool:
                drawingViewtwo.changeCutFlag();
                break;

            case R.id.erasor2_tool:
                if(i % 2 == 0)
                {
                    eraser_rel.setVisibility(View.VISIBLE);
                }else
                {
                    eraser_rel.setVisibility(View.GONE);
                }
                i++;

                break;

            case R.id.erasortwo1:
                erasor1.setImageResource(R.drawable.ahigh1);
                erasor2.setImageResource(R.drawable.high2);
                erasor3.setImageResource(R.drawable.high3);
                erasor4.setImageResource(R.drawable.high4);
                eraser_rel.setVisibility(View.GONE);

                //Set Eraser tool to erase the background of Image
                drawingViewtwo.getPaint().setStrokeWidth(20);
                drawingViewtwo.activateEraser();
                i++;
                break;

            case R.id.erasortwo2:
                erasor1.setImageResource(R.drawable.high1);
                erasor2.setImageResource(R.drawable.ahigh2);
                erasor3.setImageResource(R.drawable.high3);
                erasor4.setImageResource(R.drawable.high4);
                eraser_rel.setVisibility(View.GONE);

                //Set Eraser tool to erase the background of Image
                drawingViewtwo.getPaint().setStrokeWidth(50);
                drawingViewtwo.activateEraser();
                i++;
                break;

            case R.id.erasortwo3:
                erasor1.setImageResource(R.drawable.high1);
                erasor2.setImageResource(R.drawable.high2);
                erasor3.setImageResource(R.drawable.ahigh3);
                erasor4.setImageResource(R.drawable.high4);
                eraser_rel.setVisibility(View.GONE);

                //Set Eraser tool to erase the background of Image
                drawingViewtwo.getPaint().setStrokeWidth(80);
                drawingViewtwo.activateEraser();
                i++;
                break;

            case R.id.erasortwo4:
                erasor1.setImageResource(R.drawable.high1);
                erasor2.setImageResource(R.drawable.high2);
                erasor3.setImageResource(R.drawable.high3);
                erasor4.setImageResource(R.drawable.ahigh4);
                eraser_rel.setVisibility(View.GONE);

                //Set Eraser tool to erase the background of Image
                drawingViewtwo.getPaint().setStrokeWidth(110);
                drawingViewtwo.activateEraser();
                i++;
                break;
            case R.id.replace2_tool:
                drawingViewtwo.onClickUndo();
                break;
        }
    }

    //Display the ImageRatio after cropping
    public void setImageRatio(BitmapDrawable bd)
    {
        int b_width = bd.getIntrinsicWidth();
        int b_height = bd.getIntrinsicHeight();

        FrameLayout frame = (FrameLayout) findViewById(R.id.lycontainer2);

        int width = getScreenWidth();
        int height = getScreenHeight() - dpToPx(60) - dpToPx(70) - dpToPx(45);

        if((float) width/b_width < (float) height/b_height)
        {
            this.width = width;
            this.height = b_height * width/b_width;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, b_height * width/b_width);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            frame.setLayoutParams(lp);
        } else if((float) width/b_width > (float) height/b_height)
        {
            isPortrait  = true;
            this.width = (int)((float)b_width * (float)height/b_height);
            this.height = height;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)((float)b_width * (float)height/b_height), height);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            frame.setLayoutParams(lp);
        } else
        {
            this.width = b_width;
            this.height = b_height;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(b_width, b_height);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            frame.setLayoutParams(lp);
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

    public int dpToPx(int dp)
    {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void imageCrop()
    {
        //Resize Bitmap image to match with drawingView
        Bitmap bitmap2 = (Bitmap.createScaledBitmap(instance.common_bitmap, width, height, true));

        //Save one to common variable for Displaying Resized Bitmap in DrawingViewtwo when press undo button
        instance.common_resizedBitmap = bitmap2;

        Bitmap resultingImage = Bitmap.createBitmap(width, height, bitmap2.getConfig());
        Canvas canvas = new Canvas(resultingImage);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Path path = new Path();

        for (int i = 0; i < DrawingView.points.size(); i++)
        {
            if (isPortrait)
                path.lineTo(DrawingView.points.get(i).x, DrawingView.points.get(i).y);
            else
                path.lineTo(DrawingView.points.get(i).x, DrawingView.points.get(i).y);
        }

        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, 0, 0, paint);

        //Set ResultImage to drawingViewtwo
        BitmapDrawable resultob = new BitmapDrawable(getResources(), resultingImage);
        if(Build.VERSION.SDK_INT >= 16)
        {
            drawingViewtwo.setBackground(resultob);
            bitmap2 = resultingImage;

        }else {
            drawingViewtwo.setBackgroundDrawable(resultob);
        }

        //For EditPicturethreeActivity
        instance.common_bitmap2 = bitmap2;
        instance.common_flag = true;
    }

    //When moved ThreeActivity to twoActivity
    public void imgeCropforThreeActivity()
    {
        Bitmap bitmap2 = null;
        if(instance.common_flag_final)
        {
            bitmap2 = (Bitmap.createScaledBitmap(instance.common_bitmap3, width, height, true));
            Log.d("TESTME","R");
        }else
        {
            bitmap2 = (Bitmap.createScaledBitmap(instance.common_resizedBitmap, width, height, true));
        }

        //Save one to common variable for Displaying Resized Bitmap in DrawingViewthree when press undo button
        instance.common_resizedBitmap2 = bitmap2;

        Bitmap resultingImage = Bitmap.createBitmap(width, height, bitmap2.getConfig());
        Canvas canvas = new Canvas(resultingImage);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Path path = new Path();

        for (int i = 0; i < DrawingViewthree.points.size(); i++)
        {
            if (isPortrait)
                path.lineTo(DrawingViewthree.points.get(i).x, DrawingViewthree.points.get(i).y);
            else
                path.lineTo(DrawingViewthree.points.get(i).x, DrawingViewthree.points.get(i).y);
        }

        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, 0, 0, paint);

        //Set ResultImage to drawingViewthree
        BitmapDrawable resultob = new BitmapDrawable(getResources(), resultingImage);
        if(Build.VERSION.SDK_INT >= 16)
        {
            drawingViewtwo.setBackground(resultob);
            bitmap2 = resultingImage;

        }else {
            drawingViewtwo.setBackgroundDrawable(resultob);
        }
        instance.common_bitmap4 = bitmap2;
        instance.common_flattwotothree = true;
    }

    public void scaledDrawingView()
    {
        ob = new BitmapDrawable(getResources(),instance.common_bitmap);
        setImageRatio(ob);
        if(Build.VERSION.SDK_INT >= 16)
        {
            drawingViewtwo.setBackground(ob);
        }else
        {
            drawingViewtwo.setBackgroundDrawable(ob);
        }
    }

    //Remove white Background from Bitmap
    public Bitmap getBitmapWithTransparentBG(Bitmap srcBitmap, int fromBgColor)
    {
        Bitmap result = srcBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int nWidth = result.getWidth();
        int nHeight = result.getHeight();

        for (int y = 0; y < nHeight; ++y)
            for (int x = 0; x < nWidth; ++x)
            {
                int nPixelColor = result.getPixel(x, y);
                if(nPixelColor == fromBgColor)
                    result.setPixel(x,y,Color.TRANSPARENT);
            }
        return result;
    }

    //Trim Bitmap
    public Bitmap trimBitmap(Bitmap bmp)
    {
        int imgHeight = bmp.getHeight();
        int imgWidth  = bmp.getWidth();

        //TRIM WIDTH - LEFT
        int startWidth = 0;
        for(int x = 0; x < imgWidth; x++) {
            if (startWidth == 0) {
                for (int y = 0; y < imgHeight; y++)
                {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        startWidth = x;
                        break;
                    }
                }
            } else break;
        }
        //TRIM WIDTH - RIGHT
        int endWidth  = 0;
        for(int x = imgWidth - 1; x >= 0; x--) {
            if (endWidth == 0) {
                for (int y = 0; y < imgHeight; y++)
                {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        endWidth = x;
                        break;
                    }
                }
            }else break;
        }
        //TRIM HEIGHT - TOP
        int startHeight = 0;
        for(int y = 0; y < imgHeight; y++) {
            if (startHeight == 0) {
                for (int x = 0; x < imgWidth; x++)
                {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT)
                    {
                        startHeight = y;
                        break;
                    }
                }
            } else break;
        }
        //TRIM HEIGHT - BOTTOM
        int endHeight = 0;
        for(int y = imgHeight - 1; y >= 0; y--) {
            if (endHeight == 0 ) {
                for (int x = 0; x < imgWidth; x++)
                {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT)
                    {
                        endHeight = y;
                        break;
                    }
                }
            } else break;
        }

        return Bitmap.createBitmap(
                bmp,
                startWidth,
                startHeight,
                endWidth - startWidth,
                endHeight - startHeight
        );
    }

    //Save for File
    private String saveBitmapForExpressions(Bitmap bitmap)
    {
        String filename = "";

        //Get Local Time
        int hours = new Time(System.currentTimeMillis()).getHours();
        int mins = new Time(System.currentTimeMillis()).getMinutes();
        int sec = new Time(System.currentTimeMillis()).getSeconds();
        int amount = hours * 3600 + mins * 60 + sec;

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        FileOutputStream out = null;
        try {
            //Make directory first
            File outputFolder = new File(extStorageDirectory + "/EmojiMe/");
            if (!outputFolder.exists()) {
                outputFolder.mkdir();
            }

            //FilePath
            filename = extStorageDirectory + "/EmojiMe/" + "expressionstwo" + String.valueOf(amount) + ".png";
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filename;
    }

    //Save for File
    private String saveBitmapForOther(Bitmap bitmap)
    {
        String filename = "";

        int hours = new Time(System.currentTimeMillis()).getHours();
        int mins = new Time(System.currentTimeMillis()).getMinutes();
        int sec = new Time(System.currentTimeMillis()).getSeconds();
        int amount = hours * 3600 + mins * 60 + sec;

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        FileOutputStream out = null;
        try {
            //Make directory first
            File outputFolder = new File(extStorageDirectory + "/EmojiMe/");
            if (!outputFolder.exists())
            {
                outputFolder.mkdir();
            }

            //FilePath
            filename = extStorageDirectory + "/EmojiMe/" + "othertwo" + String.valueOf(amount) + ".png";
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filename;
    }

    //Load File called "myobject" to external storage by using SerializeObjects
    private void saveResources(ArrayList<String> stringList)
    {
        String ser = SerializeObject.objectToString(stringList);
        if (ser != null && !ser.equalsIgnoreCase(""))
        {
            SerializeObject.WriteSettings(this, ser, "myobject.dat");
        }else
        {
            SerializeObject.WriteSettings(this, "", "myobject.dat");
        }
    }

    //Load File called "myobject1" to external storage by using SerializeObjects
    private void saveResourcesForOther(ArrayList<String> stringList)
    {
        String ser = SerializeObject.objectToString(stringList);
        if (ser != null && !ser.equalsIgnoreCase(""))
        {
            SerializeObject.WriteSettings(this, ser, "myobject1.dat");
        } else
        {
            SerializeObject.WriteSettings(this, "", "myobject1.dat");
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

    //Disable back button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return false;
    }

    //AsyncTask for PrgoressDialog
    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            //Remove White Background
            Bitmap bm2 = EditPicturetwoActivity.this.getBitmapWithTransparentBG(bm, Color.WHITE);

            //Trim Bitmap
            resultBitmap = EditPicturetwoActivity.this.trimBitmap(bm2);
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            progress.dismiss();
            //PopupAction
            popup.getMenuInflater().inflate(R.menu.confirmpopup, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
            {
                public boolean onMenuItemClick(MenuItem item)
                {
                    if(item.getTitle().equals("Expressions"))
                    {
                        //Save Bitmap to File
                        instance.bitmapNamesForExpressions.add(saveBitmapForExpressions(resultBitmap));

                        //Load File called ".dat" to external storage
                        saveResources(instance.bitmapNamesForExpressions);

                        //Move another Activity with pageNumber
                        instance.common_pageNum = 0;
                        Intent intent = new Intent(EditPicturetwoActivity.this, TabViewActivity.class);
                        startActivity(intent);

                    }else if(item.getTitle().equals("Other"))
                    {
                        //Save Bitmap to File and enter file paths into instance.bitmapNamesForOther
                        instance.bitmapNamesForOther.add(saveBitmapForOther(resultBitmap));

                        //Load File called ".dat" to external storage
                        saveResourcesForOther(instance.bitmapNamesForOther);

                        instance.common_pageNum = 1;
                        Intent intent = new Intent(EditPicturetwoActivity.this, TabViewActivity.class);
                        startActivity(intent);
                    }

                    return true;
                }
            });
            popup.show();
        }
    }

    @Override
    public void onResume()  {
        isPortrait = false;
        super.onResume();
    }
}


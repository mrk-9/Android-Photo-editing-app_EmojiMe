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

public class EditPicturethreeActivity extends Activity implements View.OnClickListener
{
    private DrawingViewthree drawingViewthree;
    RelativeLayout eraser_rel;
    ImageView erasor1, erasor2, erasor3, erasor4;
    Button editpicture_cancelBtn;
    ImageView cut_tool, eraser_tool, replace_tool,confirm_tool;
    Application instance;
    BitmapDrawable ob;
    int width,height;
    boolean isPortrait = false;
    int i = 0;
    Bitmap resultBitmap = null;
    Bitmap bm;
    PopupMenu popup;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.editpicture3_screen);

        //Initialization
        initView();

        // Zoom drawingViewtwo to match cropped image
        scaledDrawingView();

        //Display croped image that have got from previous activity
        imageCrop();
    }
    public void initView()
    {
        editpicture_cancelBtn = (Button) findViewById(R.id.editpicture3_cancelbtn);
        cut_tool = (ImageView) findViewById(R.id.cut3_tool);
        eraser_tool = (ImageView) findViewById(R.id.erasor3_tool);
        replace_tool = (ImageView) findViewById(R.id.replace3_tool);
        confirm_tool = (ImageView) findViewById(R.id.confirm3_tool);
        eraser_rel = (RelativeLayout) findViewById(R.id.eraser3_rel);
        drawingViewthree = (DrawingViewthree) findViewById(R.id.drawingthree);
        erasor1 = (ImageView) findViewById(R.id.erasorthree1);
        erasor2 = (ImageView) findViewById(R.id.erasorthree2);
        erasor3 = (ImageView) findViewById(R.id.erasorthree3);
        erasor4 = (ImageView) findViewById(R.id.erasorthree4);
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
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.confirm3_tool:
                //Get Bitmap from Screen
                FrameLayout view = (FrameLayout) findViewById(R.id.lycontainer3);
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                bm = view.getDrawingCache();

                popup = new PopupMenu(this, v);
                progress = ProgressDialog.show(EditPicturethreeActivity.this, "Rendering...", "Please wait",true);
                new AsyncCaller().execute();
                break;

            case R.id.editpicture3_cancelbtn:
                finish();
                break;

            case R.id.cut3_tool:
                drawingViewthree.changeCutFlag();
                break;

            case R.id.erasor3_tool:
                if(i % 2 == 0)
                {
                    eraser_rel.setVisibility(View.VISIBLE);
                }else
                {
                    eraser_rel.setVisibility(View.GONE);
                }
                i++;

                break;

            case R.id.erasorthree1:
                erasor1.setImageResource(R.drawable.ahigh1);
                erasor2.setImageResource(R.drawable.high2);
                erasor3.setImageResource(R.drawable.high3);
                erasor4.setImageResource(R.drawable.high4);
                eraser_rel.setVisibility(View.GONE);

                drawingViewthree.getPaint().setStrokeWidth(20);
                drawingViewthree.activateEraser();
                i++;
                break;

            case R.id.erasorthree2:
                erasor1.setImageResource(R.drawable.high1);
                erasor2.setImageResource(R.drawable.ahigh2);
                erasor3.setImageResource(R.drawable.high3);
                erasor4.setImageResource(R.drawable.high4);
                eraser_rel.setVisibility(View.GONE);

                drawingViewthree.getPaint().setStrokeWidth(50);
                drawingViewthree.activateEraser();
                i++;
                break;

            case R.id.erasorthree3:
                erasor1.setImageResource(R.drawable.high1);
                erasor2.setImageResource(R.drawable.high2);
                erasor3.setImageResource(R.drawable.ahigh3);
                erasor4.setImageResource(R.drawable.high4);
                eraser_rel.setVisibility(View.GONE);

                drawingViewthree.getPaint().setStrokeWidth(80);
                drawingViewthree.activateEraser();
                i++;
                break;

            case R.id.erasorthree4:
                erasor1.setImageResource(R.drawable.high1);
                erasor2.setImageResource(R.drawable.high2);
                erasor3.setImageResource(R.drawable.high3);
                erasor4.setImageResource(R.drawable.ahigh4);
                eraser_rel.setVisibility(View.GONE);

                drawingViewthree.getPaint().setStrokeWidth(110);
                drawingViewthree.activateEraser();
                i++;
                break;
            case R.id.replace3_tool:
                drawingViewthree.onClickUndo();
                break;
        }
    }

    //Display the ImageRatio after cropping
    public void setImageRatio(BitmapDrawable bd)
    {
        int b_width = bd.getIntrinsicWidth();
        int b_height = bd.getIntrinsicHeight();

        FrameLayout frame = (FrameLayout) findViewById(R.id.lycontainer3);

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
        Bitmap bitmap2 = null;

        if(instance.common_flag)
        {
            if(instance.common_flattwotothree)
            {
                bitmap2 = (Bitmap.createScaledBitmap(instance.common_bitmap4, width, height, true));
                instance.common_flattwotothree = false;
            }else
            {
                bitmap2 = (Bitmap.createScaledBitmap(instance.common_bitmap2, width, height, true));
            }
        }else if(instance.common_flag == false)
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

        for (int i = 0; i < DrawingViewtwo.points.size(); i++)
        {
            if (isPortrait)
                path.lineTo(DrawingViewtwo.points.get(i).x, DrawingViewtwo.points.get(i).y);
            else
                path.lineTo(DrawingViewtwo.points.get(i).x, DrawingViewtwo.points.get(i).y);
        }

        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, 0, 0, paint);

        //Set ResultImage to drawingViewthree
        BitmapDrawable resultob = new BitmapDrawable(getResources(), resultingImage);
        if(Build.VERSION.SDK_INT >= 16)
        {
            drawingViewthree.setBackground(resultob);
            bitmap2 = resultingImage;

        }else {
            drawingViewthree.setBackgroundDrawable(resultob);
        }
        instance.common_bitmap3 = bitmap2;
        instance.common_flag_final = true;
    }

    public void scaledDrawingView()
    {
        ob = new BitmapDrawable(getResources(),instance.common_bitmap2);
        setImageRatio(ob);
        if(Build.VERSION.SDK_INT >= 16)
        {
            drawingViewthree.setBackground(ob);
        }else
        {
            drawingViewthree.setBackgroundDrawable(ob);
        }
    }

    //Remove white Background from Bitmap
    public Bitmap getBitmapWithTransparentBG(Bitmap srcBitmap, int fromBgColor)
    {
        Bitmap result = srcBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int nWidth = result.getWidth();
        int nHeight = result.getHeight();

        for (int y = 0; y < nHeight; ++y)
            for (int x = 0; x < nWidth; ++x) {
                int nPixelColor = result.getPixel(x, y);
                if (nPixelColor == fromBgColor)
                    result.setPixel(x, y, Color.TRANSPARENT);
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
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT)
                    {
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
                     if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
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
            filename = extStorageDirectory + "/EmojiMe/" + "expressionsthree" + String.valueOf(amount) + ".png";
            out = new FileOutputStream
                    (filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
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

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        FileOutputStream out = null;
        try {
            //Make directory first
            File outputFolder = new File(extStorageDirectory + "/EmojiMe/");
            if (!outputFolder.exists()) {
                outputFolder.mkdir();
            }

            //FilePath
            filename = extStorageDirectory + "/EmojiMe/" + "otherthree" + String.valueOf(instance.bitmapNamesForOther.size()) + ".png";
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
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
        } else
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
            Bitmap bm2 = EditPicturethreeActivity.this.getBitmapWithTransparentBG(bm, Color.WHITE);

            //Trim Bitmap
            resultBitmap = EditPicturethreeActivity.this.trimBitmap(bm2);
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
                        Intent intent = new Intent(EditPicturethreeActivity.this, TabViewActivity.class);
                        startActivity(intent);

                    }else if(item.getTitle().equals("Other"))
                    {
                        //Save Bitmap to File and enter file paths into instance.bitmapNamesForOther
                        instance.bitmapNamesForOther.add(saveBitmapForOther(resultBitmap));

                        //Load File called ".dat" to external storage
                        saveResourcesForOther(instance.bitmapNamesForOther);

                        instance.common_pageNum = 1;
                        Intent intent = new Intent(EditPicturethreeActivity.this, TabViewActivity.class);
                        startActivity(intent);
                    }
                    return true;
                }
            });
            popup.show();
        }

    }

    @Override
    public void onResume()
    {
        isPortrait = false;
        super.onResume();
    }
}

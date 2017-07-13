package com.example.admin.emojime.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.admin.emojime.Common.Application;
import com.example.admin.emojime.Fragments.TabViewSubFragments.ObjectsFragment;
import java.io.File;
import java.util.ArrayList;

public class ObjectsLibraryAdapter extends BaseAdapter
{
    private Context mContext;
    Application instance;
    ObjectsFragment fragment;
    ArrayList<String> imageFileNames = new ArrayList<>();

    public ObjectsLibraryAdapter(Context c, ObjectsFragment fragment)
    {
        mContext = c;
        this.fragment = fragment;
        instance = Application.getSharedInstance();

        if(instance.bitmapNamesForOther.size() != 0)
        {
            String tempArray = null;
            for (int i = instance.bitmapNamesForOther.size() - 1; i >= 0; i--)
            {
                tempArray = instance.bitmapNamesForOther.get(i);
                imageFileNames.add(tempArray);
            }
        }
    }

    @Override
    public int getCount()
    {
        return imageFileNames.size();
    }

    @Override
    public Object getItem(int i)
    {
        return imageFileNames.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ImageView imageView;
        if (view == null) {
            int gridWidth = fragment.getScreenWidth();
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(gridWidth/5 - 30, gridWidth/5 - 30));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(5, 5, 5, 5);
        }
        else
        {
            imageView = (ImageView) view;
        }
//        imageView.setImageBitmap(loadImage(imageFileNames.get(i)));
        Bitmap bmp = getResizedBitmap(loadImage(imageFileNames.get(i)), 200);
        imageView.setImageBitmap(bmp);
        return imageView;
    }

    //Get bitmap from filePath
    private Bitmap loadImage(String filePath)
    {
        File imgFile = new File(filePath);

        if (imgFile.exists())
        {
            Bitmap mBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return mBitmap;
        }
        return null;
    }

    //Resize the bitmap with save quality
    public Bitmap getResizedBitmapWithQuality(Bitmap bm, int newWidth, int newHeight)
    {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bm.getWidth();
        float scaleY = newHeight / (float) bm.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bm, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return getResizedBitmapWithQuality(image, width, height);
    }
}

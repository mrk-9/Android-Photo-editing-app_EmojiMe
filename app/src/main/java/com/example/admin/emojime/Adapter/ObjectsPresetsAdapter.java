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
import com.example.admin.emojime.Fragments.TabViewSubFragments.ObjectsFragment;
import com.example.admin.emojime.R;

public class ObjectsPresetsAdapter extends BaseAdapter
{
    private Context mContext;
    ObjectsFragment fragment;
    public Integer[] mThumbIds = {
            R.drawable.obj1, R.drawable.obj2, R.drawable.obj3, R.drawable.obj4, R.drawable.obj5, R.drawable.obj6,
            R.drawable.obj7, R.drawable.obj8, R.drawable.obj9, R.drawable.obj10, R.drawable.obj11, R.drawable.obj12,
            R.drawable.obj13, R.drawable.obj14, R.drawable.obj15, R.drawable.obj16, R.drawable.obj17, R.drawable.obj18,
            R.drawable.obj19, R.drawable.obj20, R.drawable.obj21, R.drawable.obj22, R.drawable.obj23, R.drawable.obj24,
            R.drawable.obj25, R.drawable.obj26, R.drawable.obj27, R.drawable.obj28, R.drawable.obj29, R.drawable.obj30,
            R.drawable.obj31, R.drawable.obj32, R.drawable.obj33, R.drawable.obj34, R.drawable.obj35, R.drawable.obj36,
            R.drawable.obj37, R.drawable.obj38, R.drawable.obj39, R.drawable.obj40, R.drawable.obj41, R.drawable.obj42,
            R.drawable.obj43, R.drawable.obj44, R.drawable.obj45, R.drawable.obj46, R.drawable.obj47, R.drawable.obj48,
            R.drawable.obj49, R.drawable.obj50, R.drawable.obj51, R.drawable.obj52, R.drawable.obj53, R.drawable.obj54,
            R.drawable.obj55
    };

    public  ObjectsPresetsAdapter(Context c, ObjectsFragment fragment)
    {
        mContext = c;
        this.fragment = fragment;
    }

    @Override
    public int getCount()
    {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int i)
    {
        return mThumbIds[i];
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
        if (view == null)
        {
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
//        imageView.setImageResource(mThumbIds[i]);
        //Convert Rosource image to Bitmap
        Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(), mThumbIds[i]);
        Bitmap bmp = getResizedBitmap(largeIcon, 200);
        imageView.setImageBitmap(bmp);
        return imageView;
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

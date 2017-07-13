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
import com.example.admin.emojime.Fragments.TabViewSubFragments.ExpressionsFragment;
import com.example.admin.emojime.R;

public class ExpressionPresetsAdapter extends BaseAdapter
{
    private Context mContext;
    ExpressionsFragment fragment;
    public Integer[] mThumbIds = {
            R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6,
            R.drawable.img7, R.drawable.img8, R.drawable.img9, R.drawable.img10, R.drawable.img11, R.drawable.img12,
            R.drawable.img13, R.drawable.img14, R.drawable.img15, R.drawable.img16, R.drawable.img17, R.drawable.img18,
            R.drawable.img19, R.drawable.img20, R.drawable.img21, R.drawable.img22, R.drawable.img23, R.drawable.img24,
            R.drawable.img25, R.drawable.img26, R.drawable.img27, R.drawable.img28, R.drawable.img29, R.drawable.img30,
            R.drawable.img31, R.drawable.img32, R.drawable.img33
    };

    // Constructor
    public ExpressionPresetsAdapter(Context c, ExpressionsFragment fragment)
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

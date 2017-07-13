package com.example.admin.emojime.Fragments.TabViewSubFragments;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.admin.emojime.Adapter.ExpressionLibraryAdapter;
import com.example.admin.emojime.Adapter.ExpressionPresetsAdapter;
import com.example.admin.emojime.Adapter.ExpressionsRuAdapter;
import com.example.admin.emojime.Common.Application;
import com.example.admin.emojime.R;
import com.example.admin.emojime.Utils.SerializeObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Time;
import java.util.ArrayList;

import static android.R.attr.path;


/**
 * Created by admin on 1/18/2017.
 */

public class ExpressionsFragment extends Fragment
{
    public GridView presets_grid, library_grid, ru_grid;
    public View temp_fragmentView;
    ExpressionPresetsAdapter presetsAdapter;
    ExpressionLibraryAdapter libraryAdapter;
    ExpressionsRuAdapter ruAdapter;
    Application instance;
    Bitmap removeBmp = null;

    static ExpressionsFragment fragmentInstance = null;
    public static ExpressionsFragment newInstance()
    {
        if (fragmentInstance == null)   {
            fragmentInstance = new ExpressionsFragment();
        }
        return fragmentInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View fragmentView = inflater.inflate(R.layout.expressions_fragment, container, false);
        temp_fragmentView = fragmentView;

        initView();

        presets_grid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        if(item.getTitle().equals("Message"))
                        {
                            Bitmap bmp = BitmapFactory.decodeResource(getResources(), (Integer)presetsAdapter.getItem(position));
                            Bitmap rebmp = getResizedBitmap(bmp, 200);

                            Bitmap padBitmap = padBitmap(rebmp);

                            String pathofBmp = getMediaFilePath(getContext().getContentResolver(),padBitmap,"title", null);
                            Uri bmpUri = Uri.parse(pathofBmp);

                            //For Recently Used
                            for (int i = 0; i < instance.ruForExpressions.size(); i++)
                            {
                                if(imagesAreEqual(loadImageFromPath(instance.ruForExpressions.get(i)), bmp))
                                {
                                    instance.ruForExpressions.remove(i);
                                }

                                if(i == 9)
                                {
                                    instance.ruForExpressions.remove(0);
                                }
                            }

                            instance.ruForExpressions.add(saveBitmapForRu(bmp));
                            ruAdapter = new ExpressionsRuAdapter(getContext(), ExpressionsFragment.this);
                            ru_grid.setAdapter(ruAdapter);
                            saveResources(instance.ruForExpressions);

                            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/png");
                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            startActivity(Intent.createChooser(shareIntent, "Share image using"));

                        }else if(item.getTitle().equals("Mail"))
                        {
                            Bitmap bmp = BitmapFactory.decodeResource(getResources(), (Integer)presetsAdapter.getItem(position));
                            Bitmap rebmp = getResizedBitmap(bmp, 200);

                            Bitmap padBitmap = padBitmap(rebmp);

                            String pathofBmp = getMediaFilePath(getContext().getContentResolver(),padBitmap,"title", null);
                            Uri bmpUri = Uri.parse(pathofBmp);

                            //For Recently Used
                            for (int i = 0; i < instance.ruForExpressions.size(); i++)
                            {
                                if(imagesAreEqual(loadImageFromPath(instance.ruForExpressions.get(i)), bmp))
                                {
                                    instance.ruForExpressions.remove(i);
                                }

                                if(i == 9)
                                {
                                    instance.ruForExpressions.remove(0);
                                }
                            }

                            instance.ruForExpressions.add(saveBitmapForRu(bmp));
                            ruAdapter = new ExpressionsRuAdapter(getContext(), ExpressionsFragment.this);
                            ru_grid.setAdapter(ruAdapter);
                            saveResources(instance.ruForExpressions);

                            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/png");
                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            startActivity(Intent.createChooser(shareIntent, "Share image using"));
                        }else if(item.getTitle().equals("Copy"))
                        {

                        }

                        return true;
                    }
                });

                popup.show();
            }
        });

        library_grid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                final PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.getMenuInflater().inflate(R.menu.libpopup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        if(item.getTitle().equals("Message"))
                        {
                            //Get Bitmap from filePath
                            Bitmap bmp = loadImageFromPath((String) libraryAdapter.getItem(position));
                            Bitmap rebmp = getResizedBitmap(bmp, 200);

                            Bitmap padBitmap = padBitmap(rebmp);

                            String pathofBmp = getMediaFilePath(getContext().getContentResolver(),padBitmap,"title", null);
                            Uri bmpUri = Uri.parse(pathofBmp);

                            //For Recently Used
                            for (int i = 0; i < instance.ruForExpressions.size(); i++)
                            {
                                if(imagesAreEqual(loadImageFromPath(instance.ruForExpressions.get(i)), bmp))
                                {
                                    instance.ruForExpressions.remove(i);
                                }

                                if(i == 9)
                                {
                                    instance.ruForExpressions.remove(0);
                                }
                            }

                            instance.ruForExpressions.add(saveBitmapForRu(bmp));
                            ruAdapter = new ExpressionsRuAdapter(getContext(), ExpressionsFragment.this);
                            ru_grid.setAdapter(ruAdapter);
                            saveResources(instance.ruForExpressions);

                            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/png");
                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            startActivity(Intent.createChooser(shareIntent, "Share image using"));

                        }else if(item.getTitle().equals("Mail"))
                        {
                            //Get Bitmap from filePath
                            Bitmap bmp = loadImageFromPath((String) libraryAdapter.getItem(position));
                            Bitmap rebmp = getResizedBitmap(bmp, 200);

                            Bitmap padBitmap = padBitmap(rebmp);

                            String pathofBmp = getMediaFilePath(getContext().getContentResolver(),padBitmap,"title", null);
                            Uri bmpUri = Uri.parse(pathofBmp);

                            //For Recently Used
                            for (int i = 0; i < instance.ruForExpressions.size(); i++)
                            {
                                if(imagesAreEqual(loadImageFromPath(instance.ruForExpressions.get(i)), bmp))
                                {
                                    instance.ruForExpressions.remove(i);
                                }

                                if(i == 9)
                                {
                                    instance.ruForExpressions.remove(0);
                                }
                            }

                            instance.ruForExpressions.add(saveBitmapForRu(bmp));
                            ruAdapter = new ExpressionsRuAdapter(getContext(), ExpressionsFragment.this);
                            ru_grid.setAdapter(ruAdapter);
                            saveResources(instance.ruForExpressions);

                            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/png");
                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            startActivity(Intent.createChooser(shareIntent, "Share image using"));
                        }else if(item.getTitle().equals("Delete"))
                        {

                            new AlertDialog.Builder(getContext())
                                .setTitle("Confirm")
                                .setMessage("Would you like to remove this emoji?")
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {

                                    }
                                })
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                                {
                                    @Override public void onClick(DialogInterface dialog, int which)
                                    {
                                        ArrayList<String> tempArrayList = new ArrayList<String>();
                                        ArrayList<String> targetArrayList = new ArrayList<String>();
                                        tempArrayList = loadResources("myobject.dat");

                                        //Initialization again
                                        instance.bitmapNamesForExpressions = new ArrayList<String>();

                                        if(tempArrayList.size() != 0)
                                        {
                                            String tempArray = null;
                                            for (int i = tempArrayList.size() - 1; i >= 0; i--)
                                            {
                                                tempArray = tempArrayList.get(i);
                                                targetArrayList.add(tempArray);
                                            }
                                        }
                                        removeBmp = loadImageFromPath(targetArrayList.get(position));
                                        targetArrayList.remove(position);

                                        if(targetArrayList.size() != 0)
                                        {
                                            String tempArray = null;
                                            for (int i = targetArrayList.size() - 1; i >= 0; i--)
                                            {
                                                tempArray = targetArrayList.get(i);
                                                instance.bitmapNamesForExpressions.add(tempArray);
                                            }
                                        }

                                        libraryAdapter = new ExpressionLibraryAdapter(getContext(), ExpressionsFragment.this);
                                        library_grid.setAdapter(libraryAdapter);
                                        saveResourcesForExpressions(instance.bitmapNamesForExpressions);

                                        //Remove image on Recently Grid too
                                        ArrayList<String> temp = new ArrayList<String>();
                                        ArrayList<String> target = new ArrayList<String>();
                                        temp = loadResources("myobjectrecently.dat");

                                        //Initialization again
                                        instance.ruForExpressions = new ArrayList<String>();

                                        if(temp.size() != 0)
                                        {
                                            String tempArray = null;
                                            for (int j = temp.size() - 1; j >= 0; j--)
                                            {
                                                tempArray = temp.get(j);
                                                target.add(tempArray);
                                            }
                                        }

                                        for(int i = 0; i < target.size(); i++)
                                        {
                                            if(imagesAreEqual(removeBmp, loadImageFromPath(target.get(i))))
                                            {
                                                target.remove(i);
                                            }
                                        }

                                        if(target.size() != 0)
                                        {
                                            String tempArray = null;
                                            for (int j = target.size() - 1; j >= 0; j--)
                                            {
                                                tempArray = target.get(j);
                                                instance.ruForExpressions.add(tempArray);
                                            }
                                        }

                                        ruAdapter = new ExpressionsRuAdapter(getContext(), ExpressionsFragment.this);
                                        ru_grid.setAdapter(ruAdapter);
                                        saveResources(instance.ruForExpressions);


                                    }
                                })
                                .create()
                                .show();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });

        ru_grid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                final PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.getMenuInflater().inflate(R.menu.recentpopup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        if(item.getTitle().equals("Message"))
                        {
                            //Get Bitmap from filePath
                            Bitmap bmp = loadImageFromPath((String) ruAdapter.getItem(position));
                            Bitmap rebmp = getResizedBitmap(bmp, 200);

                            Bitmap padBitmap = padBitmap(rebmp);

                            String pathofBmp = getMediaFilePath(getContext().getContentResolver(),padBitmap,"title", null);
                            Uri bmpUri = Uri.parse(pathofBmp);

                            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/png");
                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            startActivity(Intent.createChooser(shareIntent, "Share image using"));

                        }else if(item.getTitle().equals("Mail"))
                        {
                            //Get Bitmap from filePath
                            Bitmap bmp = loadImageFromPath((String) ruAdapter.getItem(position));
                            Bitmap rebmp = getResizedBitmap(bmp, 200);

                            Bitmap padBitmap = padBitmap(rebmp);

                            String pathofBmp = getMediaFilePath(getContext().getContentResolver(),padBitmap,"title", null);
                            Uri bmpUri = Uri.parse(pathofBmp);

                            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/png");
                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            startActivity(Intent.createChooser(shareIntent, "Share image using"));
                        }else if(item.getTitle().equals("Remove from Recent"))
                        {

                            new AlertDialog.Builder(getContext())
                                    .setTitle("Confirm")
                                    .setMessage("Would you like to remove this emoji?")
                                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {

                                        }
                                    })
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                                    {
                                        @Override public void onClick(DialogInterface dialog, int which)
                                        {
                                            ArrayList<String> temp = new ArrayList<String>();
                                            ArrayList<String> target = new ArrayList<String>();
                                            temp = loadResources("myobjectrecently.dat");

                                            //Initialization again
                                            instance.ruForExpressions = new ArrayList<String>();

                                            if(temp.size() != 0)
                                            {
                                                String tempArray = null;
                                                for (int i = temp.size() - 1; i >= 0; i--)
                                                {
                                                    tempArray = temp.get(i);
                                                    target.add(tempArray);
                                                }
                                            }

                                            target.remove(position);

                                            if(target.size() != 0)
                                            {
                                                String tempArray = null;
                                                for (int i = target.size() - 1; i >= 0; i--)
                                                {
                                                    tempArray = target.get(i);
                                                    instance.ruForExpressions.add(tempArray);
                                                }
                                            }

                                            ruAdapter = new ExpressionsRuAdapter(getContext(), ExpressionsFragment.this);
                                            ru_grid.setAdapter(ruAdapter);
                                            saveResources(instance.ruForExpressions);
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });
        return fragmentView;
    }

    private String getMediaFilePath(ContentResolver cr, Bitmap source,
                                    String title, String description)   {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.PNG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = source;
                // This is for backward compatibility.
                Bitmap microThumb = StoreThumbnail(cr, miniThumb, id, 50F, 50F,
                        MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    private Bitmap StoreThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width, float height,
            int kind) {
        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true);

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND,     kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int)id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT,   thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH,    thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);

            thumb.compress(Bitmap.CompressFormat.PNG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        }
        catch (FileNotFoundException ex) {
            return null;
        }
        catch (IOException ex) {
            return null;
        }
    }

    public void initView()
    {
        instance = Application.getSharedInstance();
        presetsAdapter = new ExpressionPresetsAdapter(getContext(), ExpressionsFragment.this);
        libraryAdapter = new ExpressionLibraryAdapter(getContext(), ExpressionsFragment.this);
        ruAdapter = new ExpressionsRuAdapter(getContext(), ExpressionsFragment.this);
        presets_grid = (GridView)temp_fragmentView.findViewById(R.id.presets_gridView);
        library_grid = (GridView)temp_fragmentView.findViewById(R.id.library_gridView);
        ru_grid = (GridView)temp_fragmentView.findViewById(R.id.recent_gridView);
        presets_grid.setAdapter(presetsAdapter);
        library_grid.setAdapter(libraryAdapter);
        ru_grid.setAdapter(ruAdapter);
    }

    //Load File called "myobject1" to external storage by using SerializeObjects
    private void saveResourcesForExpressions(ArrayList<String> stringList)
    {
        String ser = SerializeObject.objectToString(stringList);
        if (ser != null && !ser.equalsIgnoreCase(""))
        {
            SerializeObject.WriteSettings(getActivity(), ser, "myobject.dat");
        } else
        {
            SerializeObject.WriteSettings(getActivity(), "", "myobject.dat");
        }
    }

    //Get bitmap from filePath
    private Bitmap loadImageFromPath(String filePath)
    {
        File imgFile = new File(filePath);

        if (imgFile.exists())
        {
            Bitmap mBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return mBitmap;
        }

        return null;
    }

    //Call File called ".dat" from external Storage
    private ArrayList<String> loadResources(String string)
    {
        ArrayList<String> userList = new ArrayList<>();

        String ser = SerializeObject.ReadSettings(getContext(), string);
        if (ser != null && !ser.equalsIgnoreCase(""))
        {
            Object obj = SerializeObject.stringToObject(ser);
            // Then cast it to your object and
            if (obj instanceof ArrayList)
            {
                // Do something
                userList = (ArrayList<String>)obj;
            }
        }
        return userList;
    }

    public int getScreenWidth()
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        return width;
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
//        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    //Load File called "myobject" to external storage by using SerializeObjects
    private void saveResources(ArrayList<String> stringList)
    {
        String ser = SerializeObject.objectToString(stringList);
        if (ser != null && !ser.equalsIgnoreCase(""))
        {
            SerializeObject.WriteSettings(getContext(), ser, "myobjectrecently.dat");
        } else
        {
            SerializeObject.WriteSettings(getContext(), "", "myobjectrecently.dat");
        }
    }

    //Save for File
    private String saveBitmapForRu(Bitmap bitmap)
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
            filename = extStorageDirectory + "/EmojiMe/" + "Ru" + String.valueOf(amount) + ".png";
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
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

    boolean imagesAreEqual(Bitmap i1, Bitmap i2)
    {
        if (i1.getHeight() != i2.getHeight()) return false;
        if (i1.getWidth() != i2.getWidth()) return false;

        for (int y = 0; y < i1.getHeight(); ++y)
            for (int x = 0; x < i1.getWidth(); ++x)
                if (i1.getPixel(x, y) != i2.getPixel(x, y))
                    return false;

        return true;
    }

    public static Bitmap padBitmap(Bitmap bitmap)
    {


        Bitmap paddedBitmap = Bitmap.createBitmap(
                bitmap.getWidth() + 20,
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(paddedBitmap);

        canvas.drawBitmap(
                bitmap,
                20,
                0,
                new Paint(Paint.FILTER_BITMAP_FLAG));

        return paddedBitmap;
    }
}

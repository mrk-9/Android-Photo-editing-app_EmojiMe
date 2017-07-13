package com.example.admin.emojime.Fragments.TabViewSubFragments;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.admin.emojime.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static android.app.Activity.RESULT_OK;


public class EmojiMeFragment extends Fragment
{
    private static final int CAMERA_REQUEST = 1888;
    RelativeLayout camera_btn, gallery_btn;
    TextView cameraBtn_txt, galleryBtn_txt;
    private static final int SELECT_PICTURE = 1;
    Bitmap bmp, resultBmp = null;
    Uri bmpUri = null;
    Uri selectedImageUri = null;
    String selectedImagePath = null;
    InputStream imageStream;
    ExifInterface ei = null;
    View temp_fragmentView;
    int width, height;

    static EmojiMeFragment fragmentInstance = null;
    public static EmojiMeFragment newInstance()
    {
        if (fragmentInstance == null)   {
            fragmentInstance = new EmojiMeFragment();
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
        View fragmentView = inflater.inflate(R.layout.emojime_fragment, container, false);
        temp_fragmentView = fragmentView;

        initView();
        return fragmentView;
    }

    public void initView()
    {
        camera_btn = (RelativeLayout) temp_fragmentView.findViewById(R.id.cameraroll_btn);
        gallery_btn = (RelativeLayout) temp_fragmentView.findViewById(R.id.takepicture_btn);
        cameraBtn_txt = (TextView) temp_fragmentView.findViewById(R.id.cameraroll_txt);
        galleryBtn_txt = (TextView) temp_fragmentView.findViewById(R.id.takepicture_txt);
        width = getScreenWidth();
        height = getScreenHeight();

        camera_btn.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        camera_btn.setBackgroundColor(getResources().getColor(R.color.selected_color));
                        cameraBtn_txt.setTextColor(getResources().getColor(R.color.textselected_color));
                        break;
                    case MotionEvent.ACTION_UP:
                        camera_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.layout_corner));
                        cameraBtn_txt.setTextColor(getResources().getColor(R.color.textunselected_color));
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                        break;
                }
                return true;
            }
        });

        gallery_btn.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        gallery_btn.setBackgroundColor(getResources().getColor(R.color.selected_color));
                        galleryBtn_txt.setTextColor(getResources().getColor(R.color.textselected_color));
                        break;
                    case MotionEvent.ACTION_UP:
                        gallery_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.layout_corner));
                        galleryBtn_txt.setTextColor(getResources().getColor(R.color.textunselected_color));
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //function is for fixing Image Rotaation before croping
        if (resultCode == RESULT_OK)
        {
            selectedImageUri = data.getData();

            if(width == 320 && height == 480)
                lgSize();
            else if(width == 480 && height == 800)
                lgSize();
            else if(width == 480 && height == 854)
                lgSize();
            else if(width == 540 && height == 960)
                lgSize();
            else if(width == 1080 && height == 1920)
                samsungSize();
            else if(width == 1080 && height == 1794)
                lgSize();
            else if(width == 1080 && height == 1776)
                lgSize();
            else if(width == 720 && height == 1280)
                lgSize();
            else if(width == 720 && height == 1184)
                lgSize();
            else if(width == 768 && height == 1200)
                lgSize();
            else if(width == 768 && height == 1184)
                lgSize();
            else if(width == 600 && height == 1024)
                lgSize();
            else if(width == 900 && height == 1400)
                lgSize();
            else if(width == 720 && height == 1280)
                lgSize();
            else if(width == 800 && height == 1280)
                lgSize();
            else if(width == 720 && height == 1280)
                samsungSize();
            else if(width == 1200 && height == 1824)
                samsungSize();
            else if(width == 1200 && height == 1920)
                samsungSize();
            else if(width == 1600 && height == 2560)
                lgSize();
            else if(width == 1440 && height == 2392)
                lgSize();
            else
                bmpUri = selectedImageUri;

            if (requestCode == CAMERA_REQUEST || requestCode == SELECT_PICTURE)
            {
//                startCropImageActivity(selectedImageUri);
                startCropImageActivity(bmpUri);
            }
        }
    }


    private void startCropImageActivity(Uri imageUri)
    {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(getActivity());
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public void solveRotationImageforLG()
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                ei = new ExifInterface(imageStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        switch(orientation)
        {
            case ExifInterface.ORIENTATION_ROTATE_90:
                showSuccessAlert("Rotation 90");
                resultBmp = rotateImage(bmp, 90);
                String pathofBmp = getMediaFilePath(getContext().getContentResolver(),resultBmp,"title", null);
                bmpUri = Uri.parse(pathofBmp);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                showSuccessAlert("Rotation 180");
                resultBmp = rotateImage(bmp, 180);
                String pathofBmp1 = getMediaFilePath(getContext().getContentResolver(),resultBmp,"title", null);
                bmpUri = Uri.parse(pathofBmp1);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                showSuccessAlert("Rotation 270");
                resultBmp = rotateImage(bmp, 270);
                String pathofBmp2 = getMediaFilePath(getContext().getContentResolver(),resultBmp,"title", null);
                bmpUri = Uri.parse(pathofBmp2);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
                bmpUri = selectedImageUri;
            default:
                bmpUri = selectedImageUri;
                break;
        }
    }

    public void solvedImageRotationforSamsung()
    {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(selectedImageUri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                showSuccessAlert("Rotation 90");
                resultBmp = rotateImage(bmp, 90);
                String pathofBmp = getMediaFilePath(getContext().getContentResolver(),resultBmp,"title", null);
                bmpUri = Uri.parse(pathofBmp);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                showSuccessAlert("Rotation 180");
                resultBmp = rotateImage(bmp, 180);
                String pathofBmp1 = getMediaFilePath(getContext().getContentResolver(),resultBmp,"title", null);
                bmpUri = Uri.parse(pathofBmp1);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                showSuccessAlert("Rotation 270");
                resultBmp = rotateImage(bmp, 270);
                String pathofBmp2 = getMediaFilePath(getContext().getContentResolver(),resultBmp,"title", null);
                bmpUri = Uri.parse(pathofBmp2);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
                bmpUri = selectedImageUri;
            default:
                bmpUri = selectedImageUri;
                break;
        }
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
    private void showSuccessAlert(String message)  {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Test");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private int getScreenHeight()
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        return height;
    }
    private int getScreenWidth()
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        return width;
    }

    public void samsungSize()
    {
        selectedImagePath = selectedImageUri.getPath();
        bmp = loadBitmap(selectedImageUri.toString());

        solvedImageRotationforSamsung();
    }

    public void lgSize()
    {
        try {
            imageStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
            bmp = BitmapFactory.decodeStream(imageStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Solve Image Rotation
        solveRotationImageforLG();
    }

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

}

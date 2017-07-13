package com.example.admin.emojime.EditPicture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.emojime.Common.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin_PC on 1/31/2017.
 */

public class DrawingViewthree extends ImageView implements View.OnTouchListener
{
    //Cut
    private Paint paint;
    public static List<Point> points;
    int DIST = 2;
    boolean flgPathDraw = true;
    Point mfirstpoint = null;
    Point mlastpoint = null;
    boolean bfirstpoint = true;
    Context mContext;
    Application instance;
    public boolean setCutFlag = true;
    boolean flag = false;
    boolean bTouchUp = false;

    //Eraser with Undo
    private Canvas m_Canvas;
    private Path m_Path;
    private Paint m_Paint, temp_paint;
    private ArrayList<Pair<Path, Paint>> paths = new ArrayList<Pair<Path, Paint>>();
    private ArrayList<Pair<Path, Paint>> undonePaths = new ArrayList<Pair<Path, Paint>>();
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    private boolean isEraserActive = false;

    public DrawingViewthree(Context c)
    {
        super(c);
        mContext = c;
        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[] {10,20},0));
        paint.setStrokeWidth(18);
        points = new ArrayList<Point>();
        bfirstpoint = false;

        this.setOnTouchListener(this);
    }

    public DrawingViewthree(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        setFocusable(true);
        setFocusableInTouchMode(true);

        //Eraser with Undo
        onCanvasInitialization();

        //Cut
        onCutInitialization();

        this.setOnTouchListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        if(setCutFlag == true)
        {
            //Display the Eraser Paths before Cut
            for (Pair<Path, Paint> p : paths)
            {
                canvas.drawPath(p.first, p.second);
            }

            Path path = new Path();
            boolean first = true;

            for (int i = 0; i < points.size(); i += 1) {
                Point point = points.get(i);
                if (first)
                {
                    first = false;
                    path.moveTo(point.x, point.y);
                } else if (i < points.size() - 1)
                {
                    Point next = points.get(i + 1);
                    path.quadTo(point.x, point.y, next.x, next.y);
                } else if (bTouchUp == true)
                {
                    mlastpoint = points.get(i);
                    path.moveTo(points.get(0).x, points.get(0).y);
                    path.lineTo(mlastpoint.x, mlastpoint.y);
                }
            }
            canvas.drawPath(path, paint);
        }

        if(isEraserActive == true)
        {
            for (Pair<Path, Paint> p : paths)
            {
                canvas.drawPath(p.first, p.second);

            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(setCutFlag == true)
        {
            Point point = new Point();
            point.x = (int) event.getX();
            point.y = (int) event.getY();

            if (flgPathDraw)
            {
                if (bfirstpoint)
                {
                    if (comparepoint(mfirstpoint, point))
                    {
                        flgPathDraw = false;

                        //Move EditPictureActivity to EditPicturetwoActivity for displaying cropped image
                        Intent intent = new Intent(mContext, EditPicturetwoActivity.class);
                        mContext.startActivity(intent);
                        instance.common_flaginThree = true;

                        //Kill the Activity
                        Activity activity = (Activity)mContext;
                        activity.finish();
                    } else {
                        points.add(point);
                    }
                }

                if (!(bfirstpoint))
                {
                    mfirstpoint = point;
                    bfirstpoint = true;
                }
            }

            invalidate();
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                bTouchUp = true;
                mlastpoint = point;
                if (flgPathDraw)
                {
                    if (points.size() > 12) {
                        if (!comparepoint(mfirstpoint, mlastpoint))
                        {
                            flgPathDraw = false;

                            points.add(points.get(0));
                            //Move EditPictureActivity to EditPicturetwoActivity for displaying cropped image
                            Intent intent = new Intent(mContext, EditPicturetwoActivity.class);
                            mContext.startActivity(intent);
                            instance.common_flaginThree = true;

                            //Kill the Activity
                            Activity activity = (Activity)mContext;
                            activity.finish();
                        }
                    }
                }
            }
        }
        if(isEraserActive == true)
        {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
        }
        return true;
    }

    //For Eraser tool
    private void touch_start(float x, float y)
    {
        if (isEraserActive)
        {
            Paint newPaint = new Paint(m_Paint);
            paths.add(new Pair<Path, Paint>(m_Path, newPaint));
        } else
        {
            m_Paint.setStrokeWidth(18);
            Paint newPaint = new Paint(m_Paint);
            paths.add(new Pair<Path, Paint>(m_Path, newPaint));
        }
        m_Path.reset();
        m_Path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    //For Eraser Tool
    private void touch_move(float x, float y)
    {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)
        {
            m_Path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    //For Eraser Tool
    private void touch_up()
    {
        m_Path.lineTo(mX, mY);

        // commit the path to our offscreen
        m_Canvas.drawPath(m_Path, m_Paint);
        temp_paint = m_Paint;

        // kill this so we don't double draw
        m_Path = new Path();
        Paint newPaint = new Paint(m_Paint);
    }

    //For Cut
    public Paint getPaint()
    {
        return m_Paint;
    }

    //For undo Button
    public void onClickUndo()
    {
        //if size of Eraser Paths > 0
        if (paths.size() > 0)
        {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        }else if(paths.size() == 0)
        {
            BitmapDrawable ob = new BitmapDrawable(getResources(), instance.common_resizedBitmap);
            instance.common_flag_final = false;
            if (Build.VERSION.SDK_INT >= 16)
            {
                setBackground(ob);
            } else {
                setBackgroundDrawable(ob);
            }
        }
    }

    //For Cut
    private boolean comparepoint(Point first, Point current)
    {
        int left_range_x = (int) (current.x - 3);
        int left_range_y = (int) (current.y - 3);
        int right_range_x = (int) (current.x + 3);
        int right_range_y = (int) (current.y + 3);
        if ((left_range_x < first.x && first.x < right_range_x)
                && (left_range_y < first.y && first.y < right_range_y))
        {
            if (points.size() < 10)
            {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    //For Cut
    public class Point
    {
        public float dy;
        public float dx;
        public float x, y;

        @Override
        public String toString()
        {
            return x + "," + y;
        }
    }

    //For Eraser Tool
    public void activateEraser()
    {
        isEraserActive = true;
        setCutFlag = false;
    }

    //For Cut Tool
    public void changeCutFlag()
    {
        setCutFlag = true;
        isEraserActive = false;
    }

    //Cut
    public void onCutInitialization()
    {
        instance = Application.getSharedInstance();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        paint.setColor(Color.WHITE);
        points = new ArrayList<Point>();
        bfirstpoint = false;
    }

    //Eraser with Undo
    public void onCanvasInitialization()
    {
        m_Paint = new Paint();
        temp_paint = new Paint();
        m_Paint.setDither(true);
        m_Paint.setColor(Color.WHITE);
        m_Paint.setStyle(Paint.Style.STROKE);
        m_Paint.setStrokeJoin(Paint.Join.ROUND);
        m_Paint.setStrokeCap(Paint.Cap.ROUND);
        m_Paint.setStrokeWidth(18);
        m_Canvas = new Canvas();
        m_Path = new Path();
    }
}

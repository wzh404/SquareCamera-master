package com.desmond.squarecamera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by wangzunhui on 2016/5/23.
 */
public class ScanLineView extends SurfaceView implements SurfaceHolder.Callback {
    protected SurfaceHolder sh;
    private int width;
    private int height;

    private final static double ASPECT_RATIO = 4.0 / 3.0;

    public ScanLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        sh = getHolder();
        sh.addCallback(this);
        sh.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);

//        final boolean isPortrait =
//                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
//
//        if (isPortrait) {
//            if (width > height * ASPECT_RATIO) {
//                width = (int) (height * ASPECT_RATIO + 0.5);
//            } else {
//                height = (int) (width / ASPECT_RATIO + 0.5);
//            }
//        } else {
//            if (height > width * ASPECT_RATIO) {
//                height = (int) (width * ASPECT_RATIO + 0.5);
//            } else {
//                width = (int) (height / ASPECT_RATIO + 0.5);
//            }
//        }

        setMeasuredDimension(width, height);
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int w, int h) {
        // TODO Auto-generated method stub
        width = w;
        height = h;
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    public void drawLine() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Canvas canvas = sh.lockCanvas();
            if (canvas != null) {
                Paint p = new Paint();
                p.setAntiAlias(true);
                p.setColor(Color.GREEN);
                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth((float) 5.0);
                p.setAlpha(128);

                // draw center green line
                canvas.drawLine(width / 4, height / 2, width * 3 / 4, height / 2, p);

                int f = 32;
                int g = 10;
                int h = 400;  // center rect height

                p.setStyle(Paint.Style.FILL);
                p.setStrokeWidth(10);
                p.setAlpha(255);

                // left-top
                canvas.drawRect(width / 4, height / 2 - h / 2, width / 4 + f, height / 2 - h / 2 + g, p);
                canvas.drawRect(width / 4, height / 2 - h / 2, width / 4 + g , height / 2 - h / 2 + f, p);

                // left-bottom
                canvas.drawRect(width / 4, height / 2 + h / 2, width / 4 + f, height / 2 + h / 2 - g, p);
                canvas.drawRect(width / 4, height / 2 + h / 2, width / 4 + g, height / 2 + h / 2 - f, p);

                // right-top
                canvas.drawRect(width * 3 / 4, height / 2 - h / 2, width * 3 / 4 - f, height / 2 - h / 2 + g, p);
                canvas.drawRect(width * 3 / 4, height / 2 - h / 2, width * 3 / 4 - g, height / 2 - h / 2 + f, p);

                // right-bottom
                canvas.drawRect(width * 3 / 4, height / 2 + h / 2, width  * 3 / 4 - f, height / 2 + h / 2 - g, p);
                canvas.drawRect(width * 3 / 4, height / 2 + h / 2, width  * 3 / 4 - g, height / 2 + h / 2 - f, p);

                // draw center rect
                Paint mAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mAreaPaint.setColor(Color.GRAY);
                mAreaPaint.setStyle(Paint.Style.FILL);
                mAreaPaint.setAlpha(180);
                canvas.drawRect(0, 0, width, height / 2 - h / 2, mAreaPaint);
                canvas.drawRect(0, height / 2 + h / 2, width, height, mAreaPaint);
                canvas.drawRect(0, height / 2 - h / 2, width / 4, height / 2 + h / 2, mAreaPaint);
                canvas.drawRect(width * 3 / 4, height / 2 - h / 2, width, height / 2 + h / 2, mAreaPaint);

                sh.unlockCanvasAndPost(canvas);
                break;
            }
        }
    }
}

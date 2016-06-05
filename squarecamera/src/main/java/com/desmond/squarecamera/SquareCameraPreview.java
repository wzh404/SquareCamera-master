package com.desmond.squarecamera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SquareCameraPreview extends SurfaceView{

    public static final String TAG = SquareCameraPreview.class.getSimpleName();
    private static final int INVALID_POINTER_ID = -1;

    private static final int ZOOM_OUT = 0;
    private static final int ZOOM_IN = 1;
    private static final int ZOOM_DELTA = 1;

    private static final int FOCUS_SQR_SIZE = 100;
    private static final int FOCUS_MAX_BOUND = 1000;
    private static final int FOCUS_MIN_BOUND = -FOCUS_MAX_BOUND;

    private static final double ASPECT_RATIO = 3.0 / 4.0;
    private volatile Camera mCamera;

    private float mLastTouchX;
    private float mLastTouchY;

    // For scaling
    private int mMaxZoom;
    private boolean mIsZoomSupported;
    private int mActivePointerId = INVALID_POINTER_ID;
    private int mScaleFactor = 1;
//    private ScaleGestureDetector mScaleDetector;

    // For focus
    private boolean mIsFocus;
    private boolean mIsFocusReady;
    private Camera.Area mFocusArea;
    private ArrayList<Camera.Area> mFocusAreas;
    private Camera.AutoFocusCallback autoFocusCallback;
    private int cnt = 0;

    public SquareCameraPreview(Context context) {
        super(context);
        init(context);
    }

    public SquareCameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SquareCameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
//        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mFocusArea = new Camera.Area(new Rect(), 1000);
        mFocusAreas = new ArrayList<>();
        mFocusAreas.add(mFocusArea);
    }

    /**
     * Measure the view and its content to determine the measured width and the
     * measured height
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

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

        Log.e("Measure", width + "-" + height);
        setMeasuredDimension(width, height);
    }

//    public int getViewWidth() {
//        return getWidth();
//    }
//
//    public int getViewHeight() {
//        return getHeight();
//    }

    public void setCamera(Camera camera) {
        mCamera = camera;

        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            mIsZoomSupported = params.isZoomSupported();
            if (mIsZoomSupported) {
                mMaxZoom = params.getMaxZoom();
Log.e("Drug", "mMaxZoom is " + mMaxZoom);
                params.setZoom(50);
                camera.setParameters(params);
                params.setPreviewFormat(ImageFormat.NV21);
            }
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        mScaleDetector.onTouchEvent(event);
//
//        final int action = event.getAction();
//        switch (action & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN: {
//                mIsFocus = true;
//
//                mLastTouchX = event.getX();
//                mLastTouchY = event.getY();
//
//                mActivePointerId = event.getPointerId(0);
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                if (mIsFocus && mIsFocusReady) {
//                    handleFocus(mCamera.getParameters());
//                }
//                mActivePointerId = INVALID_POINTER_ID;
//                break;
//            }
//            case MotionEvent.ACTION_POINTER_DOWN: {
//                mCamera.cancelAutoFocus();
//                mIsFocus = false;
//                break;
//            }
//            case MotionEvent.ACTION_CANCEL: {
//                mActivePointerId = INVALID_POINTER_ID;
//                break;
//            }
//        }
//
//        return true;
//    }

//    private void handleZoom(Camera.Parameters params) {
//        int zoom = params.getZoom();
//        if (mScaleFactor == ZOOM_IN) {
//            if (zoom < mMaxZoom) zoom += ZOOM_DELTA;
//        } else if (mScaleFactor == ZOOM_OUT) {
//            if (zoom > 0) zoom -= ZOOM_DELTA;
//        }
//        params.setZoom(zoom);
//        mCamera.setParameters(params);
//    }


//    private void handleFocus(Camera.Parameters params) {
//        float x = mLastTouchX;
//        float y = mLastTouchY;
//
//        if (!setFocusBound(x, y)) return;
//        startAutoFocusInCenter(params);

//        List<String> supportedFocusModes = params.getSupportedFocusModes();
//        if (supportedFocusModes != null
//                && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
//            Log.d(TAG, mFocusAreas.size() + "");
//            params.setFocusAreas(mFocusAreas);
//            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//            mCamera.setParameters(params);
//            mCamera.autoFocus(new Camera.AutoFocusCallback() {
//                @Override
//                public void onAutoFocus(boolean success, Camera camera) {
//                    // Callback when the auto focus completes
//                    Log.e("Callback", "----------------auto focus completes-------");
//                }
//            });
//        }
//    }

    public void startAutoFocusInCenter(){
        Log.e("Preview", "----************--start auto focus--------" + Thread.currentThread().getId());
        Camera.Parameters params = mCamera.getParameters();
        mCamera.cancelAutoFocus();
        if (autoFocusCallback == null){
            autoFocusCallback = new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, final Camera camera) {
                    // Callback when the auto focus completes
                    if (success){
                        Log.e("Callback", "----------------auto focus completes true-------"+ Thread.currentThread().getId());
                        camera.setPreviewCallback(previewCallback);
                    }
                    else{
                        if (camera != null){
                            camera.cancelAutoFocus();
                            camera.autoFocus(this);
                        }
                    }

                }
            };
        }
        mCamera.autoFocus(autoFocusCallback);
    }

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback(){
        @Override
        public void onPreviewFrame(final byte[] data, final Camera camera) {
//            Log.e("Preview", "------data coming--------" + Thread.currentThread().getId());
            camera.setPreviewCallback(null);
            final CameraActivity cameraActivity = (CameraActivity)getContext();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    boolean ok = decodeToBitMap(data, camera);
                    Log.e("Times", getContext() + " - times is " + (System.currentTimeMillis() - start) + "ms");

                    if (!ok){
//                        if (mCamera != null) {
//                            camera.cancelAutoFocus();
//                        }
//                        if (mCamera != null) {
//                            camera.autoFocus(autoFocusCallback);
//                        }
                        cameraActivity.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                cameraActivity.retryAutoFoucs();
                            }
                        });
                    }
                }
            }).start();
        }
    };

    public boolean decodeToBitMap(byte[] data, Camera camera) {
        Camera.Size size = camera.getParameters().getPreviewSize();
        try {
            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
            if (image != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);
                Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                stream.close();

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap nbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),  bmp.getHeight(), matrix, true);
                bmp = null;
                return ocrImage(nbmp);
            }
        } catch (Exception ex) {
            Log.e("Sys", "Error:" + ex.getMessage());
        }
        return false;
    }

    private Bitmap ImageCrop(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int wh = 200;
        int retX = w/4;//0 ;
        int retY = h / 2 - wh/2;

        return Bitmap.createBitmap(bitmap, retX, retY, /*w - retX*/ w / 2,  wh, null, false);
    }

    private boolean ocrImage(Bitmap source) {
        long start = System.currentTimeMillis();
        Bitmap bmp = ImageCrop(source);
        Log.e("times", "ImageCrop is " + (System.currentTimeMillis() - start) + "ms");
        source = null;

        Bitmap b;
        if (cnt > 6) {
            b = binarizationAve(bmp);
        }
        else{
            b = binarization(bmp);
        }
        cnt++;
        Log.e("times", "binarization is " + (System.currentTimeMillis() - start) + "ms");

        final Bitmap gray = b;
        final CameraActivity cameraActivity = (CameraActivity)getContext();
        cameraActivity.mHandler.post(new Runnable() {
            @Override
            public void run() {
                cameraActivity.setPreviewImage(gray);
            }
        });

//        TessBaseAPI baseApi = new TessBaseAPI();
//        File appDir = new File(Environment.getExternalStorageDirectory().getPath(), "Boohee");
//        baseApi.init(appDir.toString(), "eng");
        Log.e("times", "TessBaseAPI is " + (System.currentTimeMillis() - start) + "ms");

//        cameraActivity.baseApi.setImage(b);
//        final String ori = cameraActivity.baseApi.getUTF8Text();
        String ori = cameraActivity.baseApi.parser(b);
        if (ori == null){
            return false;
        }

        Log.e("times", "getUTF8Text is " + (System.currentTimeMillis() - start) + "ms");
        final String outputText = parser(ori);
        Log.e("Tesseract", "[" + ori + "]-------[" + outputText + "]*****************");
//        baseApi.end();

        if (outputText != null){
            cameraActivity.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    cameraActivity.returnDrugCode(outputText);
                }
            });
        }
        return outputText != null;
    }

    private  String parser(String s) {
        StringBuilder sb = new StringBuilder();
        int next = 0;
        int prev = 0;
        for (char c : s.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
            } else if (c >= '0' && c <= '9') {
            }
            else if (c >= 'a' && c <= 'z'){}
            else {
                next++;
                continue;
            }

            if ((next - prev) == 1) {
                sb.append(c);
            } else {
                if (sb.length() >= 9){
                    break;
                }
                else {
                    sb.setLength(0);
                    sb.append(c);
                }
            }

            prev = next;
            next++;
        }

        Log.e("Drug", "string is [" + sb.toString() + "]");
        if (sb.length() > 9) {
            sb.delete(0, sb.length() - 9);
        }
        Log.e("Drug", "delete is [" + sb.toString() + "]");
        if (check(sb.toString())){
            if (sb.charAt(0) == '2' || sb.charAt(0) == 'z' || sb.charAt(0) == 'E'){
                sb.setCharAt(0, 'Z');
            }

            for (int i = 1; i < sb.length(); i++){
                char c = sb.charAt(i);

                if (c == 'i' || c == 'I' ){
                    sb.setCharAt(i, '1');
                }

                if (c == 'o' || c == 'O'){
                    sb.setCharAt(i, '0');
                }

                if (c == 's' || c == 'S'){
                    sb.setCharAt(i, '5');
                }
            }
            return sb.toString();
        }
        else
            return null;
    }

    private boolean check(String s){
        if (s == null || s.length() != 9){
            return false;
        }

        char c = s.charAt(0);
        if (c >= 'A' && c <= 'Z') {}
        else if (c >= 'a' && c <= 'z'){}
        else if (c == '2'){}
        else{
            return false;
        }

        for (int i =1; i < s.length(); i++){
            c = s.charAt(i);
            if (c >= '0' && c <= '9') {}
            else if (c == 'i' || c == 'I' ||
                     c == 'o' || c == 'O' ||
                     c == 's' || c == 'S'){}
            else{
                return false;
            }
        }

        return true;
    }

    public static int getAverageColor(int[][] gray, int x, int y, int w, int h) {
        int rs = gray[x][y]
                + (x == 0 ? 255 : gray[x - 1][y])
                + (x == 0 || y == 0 ? 255 : gray[x - 1][y - 1])
                + (x == 0 || y == h - 1 ? 255 : gray[x - 1][y + 1])
                + (y == 0 ? 255 : gray[x][y - 1])
                + (y == h - 1 ? 255 : gray[x][y + 1])
                + (x == w - 1 ? 255 : gray[x + 1][y])
                + (x == w - 1 || y == 0 ? 255 : gray[x + 1][y - 1])
                + (x == w - 1 || y == h - 1 ? 255 : gray[x + 1][y + 1]);
        return rs / 9;
    }

    public Bitmap binarizationAve(Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();
        int pixelGray;

        int gray[][] = new int[width][height];
        int[] pix = new int[width * height];

        img.getPixels(pix, 0, width, 0, 0, width, height);
        for (int i = 0; i < width; i++) { // 不算边界行和列，为避免越界
            for (int j = 0; j < height; j++) {
                int x = j * width + i;
                int r = (pix[x] >> 16) & 0xff;
                int g = (pix[x] >> 8) & 0xff;
                int b = pix[x] & 0xff;
//                pixelGray = (int) (0.3 * r + 0.59 * g + 0.11 * b);// 计算每个坐标点的灰度
                gray[i][j] = (r + g + b) / 3; //(pixelGray << 16) + (pixelGray << 8) + (pixelGray);
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int in = j * width + i;

                int g = getAverageColor(gray, i, j, width, height);
                if (g > 60) {
                    pix[in] = Color.rgb(255, 255, 255);
                } else {
                    pix[in] = Color.rgb(0, 0, 0);
                }
            }
        }

        Bitmap temp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        temp.setPixels(pix, 0, width, 0, 0, width, height);
        return temp;
    }

    /**
     * Ostu二值化算法
     *
     * @param img
     * @return
     */
    public Bitmap binarization(Bitmap img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int area = width * height;
        int gray[][] = new int[width][height];
        int average = 0;// 灰度平均值
        int graysum = 0;
        int graymean = 0;
        int grayfrontmean = 0;
        int graybackmean = 0;
        int pixelGray;
        int front = 0;
        int back = 0;
        int[] pix = new int[width * height];
        img.getPixels(pix, 0, width, 0, 0, width, height);
        for (int i = 1; i < width; i++) { // 不算边界行和列，为避免越界
            for (int j = 1; j < height; j++) {
                int x = j * width + i;
                int r = (pix[x] >> 16) & 0xff;
                int g = (pix[x] >> 8) & 0xff;
                int b = pix[x] & 0xff;
                pixelGray = (int) (0.3 * r + 0.59 * g + 0.11 * b);// 计算每个坐标点的灰度
                gray[i][j] = (pixelGray << 16) + (pixelGray << 8) + (pixelGray);
                graysum += pixelGray;
            }
        }
        graymean = (int) (graysum / area);// 整个图的灰度平均值
        average = graymean;
//        Log.i(TAG,"Average:"+average);
        for (int i = 0; i < width; i++) // 计算整个图的二值化阈值
        {
            for (int j = 0; j < height; j++) {
                if (((gray[i][j]) & (0x0000ff)) < graymean) {
                    graybackmean += ((gray[i][j]) & (0x0000ff));
                    back++;
                } else {
                    grayfrontmean += ((gray[i][j]) & (0x0000ff));
                    front++;
                }
            }
        }
        int frontvalue = (int) (grayfrontmean / front);// 前景中心
        int backvalue = (int) (graybackmean / back);// 背景中心
        float G[] = new float[frontvalue - backvalue + 1];// 方差数组
        int s = 0;

        for (int i1 = backvalue; i1 < frontvalue + 1; i1++)// 以前景中心和背景中心为区间采用大津法算法（OTSU算法）
        {
            back = 0;
            front = 0;
            grayfrontmean = 0;
            graybackmean = 0;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (((gray[i][j]) & (0x0000ff)) < (i1 + 1)) {
                        graybackmean += ((gray[i][j]) & (0x0000ff));
                        back++;
                    } else {
                        grayfrontmean += ((gray[i][j]) & (0x0000ff));
                        front++;
                    }
                }
            }
            grayfrontmean = (int) (grayfrontmean / front);
            graybackmean = (int) (graybackmean / back);
            G[s] = (((float) back / area) * (graybackmean - average)
                    * (graybackmean - average) + ((float) front / area)
                    * (grayfrontmean - average) * (grayfrontmean - average));
            s++;
        }
        float max = G[0];
        int index = 0;
        for (int i = 1; i < frontvalue - backvalue + 1; i++) {
            if (max < G[i]) {
                max = G[i];
                index = i;
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int in = j * width + i;
                if (((gray[i][j]) & (0x0000ff)) < (index + backvalue)) {
                    pix[in] = Color.rgb(0, 0, 0);
                } else {
                    pix[in] = Color.rgb(255, 255, 255);
                }
            }
        }

        Bitmap temp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        temp.setPixels(pix, 0, width, 0, 0, width, height);
        return temp;
    }

    public void setIsFocusReady(final boolean isFocusReady) {
        mIsFocusReady = isFocusReady;
    }

//    private boolean setFocusBound(float x, float y) {
//        int left = (int) (x - FOCUS_SQR_SIZE / 2);
//        int right = (int) (x + FOCUS_SQR_SIZE / 2);
//        int top = (int) (y - FOCUS_SQR_SIZE / 2);
//        int bottom = (int) (y + FOCUS_SQR_SIZE / 2);
//
//        if (FOCUS_MIN_BOUND > left || left > FOCUS_MAX_BOUND) return false;
//        if (FOCUS_MIN_BOUND > right || right > FOCUS_MAX_BOUND) return false;
//        if (FOCUS_MIN_BOUND > top || top > FOCUS_MAX_BOUND) return false;
//        if (FOCUS_MIN_BOUND > bottom || bottom > FOCUS_MAX_BOUND) return false;
//
//        mFocusArea.rect.set(left, top, right, bottom);
//
//        return true;
//    }

//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            mScaleFactor = (int) detector.getScaleFactor();
//            handleZoom(mCamera.getParameters());
//            return true;
//        }
//    }
}

package com.desmond.demo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.desmond.demo.action.ErrorAction1;
import com.desmond.demo.action.Result;
import com.desmond.demo.action.ResultAction1;
import com.desmond.demo.rxbus.RxBus;
import com.desmond.squarecamera.CameraActivity;
import com.desmond.squarecamera.ImageUtility;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private Point mSize;
    private WebService webService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        mSize = new Point();
        display.getSize(mSize);

        DrugApplication app = (DrugApplication)this.getApplicationContext();

        Retrofit retrofit = app.getRetrofit();
        webService = retrofit.create(WebService.class);

        rxbusRegister("drug", drugAction1);
    }

    private Action1 drugAction1 = new Action1<Result>() {

        @Override
        public void call(Result result) {
            if (result.isResult("drug", "OK")){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                JsonObject jsonObject = result.getObj().getAsJsonObject("drug");
                builder.setTitle("批准文号: " + jsonObject.get("code").getAsString());
                builder.setMessage("药品名称: " + jsonObject.get("name").getAsString());
                builder.create().show();
            }else {
                Toast.makeText(MainActivity.this,  result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
//            Uri photoUri = data.getData();
//            // Get the bitmap in according to the width of the device
//            Bitmap bitmap = ImageUtility.decodeSampledBitmapFromPath(photoUri.getPath(), mSize.x, mSize.x);
//            ((ImageView) findViewById(R.id.image)).setImageBitmap(bitmap);
//
////            saveImage(ImageCrop(bitmap));
            String code = data.getStringExtra("code");
            drug(code);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void requestForCameraPermission(View view) {
        final String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                showPermissionRationaleDialog("Test", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.requestForPermission(permission);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
    }

    private void launch() {
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    launch();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void drug(String code) {
        Observable<JsonObject> observable = webService.drug(code);
        call(observable, "drug");
    }

    private void call(Observable<JsonObject> observable, String tag) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultAction1(tag), new ErrorAction1(tag));
    }

    public void rxbusRegister(String tag, Action1 action1) {
        Observable<Result> observable = RxBus.get().register(tag, Result.class);
        observable.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);

//        map.put(tag, observable);
    }

    @Override
    public void onDestroy(){
        RxBus.get().unregister("drug");
        super.onDestroy();
    }


//    public static Bitmap ImageCrop(Bitmap bitmap) {
//        int w = bitmap.getWidth(); // 得到图片的宽，高
//        int h = bitmap.getHeight();
//
//        int wh = 160;// 裁切后所取的正方形区域边长
//
//        int retX = w / 4 ;//基于原图，取正方形左上角x坐标
//        int retY = h / 2 - 80;
//
//        Log.e("--------", retX + " - " + retY);
//        //下面这句是关键
//        return Bitmap.createBitmap(bitmap, retX, retY, w - retX, wh, null, false);
//    }
//
//    public static void  saveImage(Bitmap bmp) {
//        File appDir = new File(Environment.getExternalStorageDirectory().getPath(), "Boohee");
//        if (! appDir.exists()) {
//            appDir.mkdir();
//        }
//        Log.e("--------", bmp.getWidth() + " - " + bmp.getHeight());
//        String fileName = System.currentTimeMillis() + ".jpg";
//        File file = new File(appDir, fileName);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

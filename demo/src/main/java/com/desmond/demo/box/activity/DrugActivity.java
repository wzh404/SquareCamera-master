package com.desmond.demo.box.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.desmond.demo.R;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.presenter.DrugPresenter;
import com.desmond.demo.box.view.DrugView;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.common.util.DateUtil;
import com.desmond.squarecamera.CameraActivity;
import com.google.gson.JsonObject;

import io.realm.Realm;
import rx.functions.Action1;


public class DrugActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private DrugPresenter presenter;
    private DrugView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.presenter = new DrugPresenter(drugAction1);
        this.view = new DrugView(this, null, this.presenter);
        setContentView(this.view.getView());

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        toolbar.setOnMenuItemClickListener(
//                new Toolbar.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
////                        switch (item.getItemId()) {
////                            case R.id.add_drug:
////                                requestForCameraPermission(null);
////                                break;
////                        }
//                        return false;
//                    }
//                });
    }

    private Action1 drugAction1 = new Action1<Result>() {
        @Override
        public void call(Result result) {
            if (result.isResult("drug", "OK")) {

                JsonObject jsonObject = result.getObj().getAsJsonObject("drug");
                final Drug drug = new Drug();
                drug.setId(jsonObject.get("id").getAsInt());
                drug.setName(jsonObject.get("name").getAsString());
                drug.setCompany(jsonObject.get("manufacturer").getAsString());
                drug.setCode(jsonObject.get("code").getAsString());
                drug.setForm(jsonObject.get("form").getAsString());
                drug.setOtc(jsonObject.get("otc").getAsString());
                drug.setTime(DateUtil.getCurrentTime());

                Log.e("Drug", "---start save to database---");
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(
                        new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Log.e("Drug", "---save execute---");
                                realm.copyToRealm(drug);
                            }
                        },
                        new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                Log.e("Drug", "---save success---");
                                view.addItem(drug);
                            }
                        },
                        new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                Log.e("Drug", "---save error---");
                                error.printStackTrace();
                            }
                        });

            } else {
                Toast.makeText(DrugActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            String code = data.getStringExtra("code");
            this.presenter.drug(code);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void requestForCameraPermission(View view) {
        final String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(DrugActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DrugActivity.this, permission)) {
                showPermissionRationaleDialog("Test", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(DrugActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DrugActivity.this.requestForPermission(permission);
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
        ActivityCompat.requestPermissions(DrugActivity.this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
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

    @Override
    public void onDestroy() {
        this.presenter.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_account, menu);
        return true;
    }
}

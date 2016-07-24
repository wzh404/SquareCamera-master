package com.desmond.demo.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.desmond.demo.R;
import com.desmond.demo.box.activity.DrugAddActivity;
import com.desmond.demo.box.activity.DrugSettingActivity;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.presenter.DrugPresenter;
import com.desmond.demo.box.view.DrugItemView;
import com.desmond.demo.box.view.DrugView;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.common.util.Constants;
import com.desmond.demo.common.util.IconCenterEditText;
import com.desmond.demo.common.util.MaterialDialogUtil;
import com.desmond.demo.common.util.NetworkUtil;
import com.desmond.demo.plan.activity.NewPlanActivity;
import com.desmond.squarecamera.CameraActivity;
import com.google.gson.JsonObject;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * 药箱
 *
 * Created by WIN10 on 2016/5/31.
 */
public class DrugFragment extends Fragment {
    private static final int REQUEST_CAMERA = 0;
    private DrugPresenter presenter;
    private DrugView view;
    private MaterialDialog.Builder builder;
    private MaterialDialog dialog;
    private RealmAsyncTask realmAsyncTask;
    private RealmResults<Drug> result;
    private Context context;
    private boolean query = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        Log.e("Drug", "----onCreateView----");
        this.context = getContext();
        this.presenter = new DrugPresenter(drugAction1);
        this.view = new DrugView(context, null, new DrugOnClickListener());
        view.setOnMenuItemClickListener(menuItemClickListener);
//        IconCenterEditText icet_search = view.get(R.id.icet_search);
//        icet_search.setOnSearchClickListener(new IconCenterEditText.OnSearchClickListener() {
//            @Override
//            public void onSearchClick(View view) {
//                if (view instanceof IconCenterEditText){
//                    String code = ((IconCenterEditText) view).getText().toString();
//                    if (code.length() <= 0){
//                        Toast.makeText(view.getContext(), R.string.please_input_drug_code, Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        dialog = builder.show();
//                        presenter.drug(code.toUpperCase());
//                    }
//                }
//            }
//        });
//
//        ImageView vt = view.get(R.id.sao);
//        vt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (NetworkUtil.isMobileConnected(context) || NetworkUtil.isNetworkConnected(context)) {
//                    start(v);
//                }
//                else{
//                    Toast.makeText(context, "无网络连接!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        result = this.presenter.queryDrugAsync();
        result.addChangeListener(new RealmChangeListener<RealmResults<Drug>>() {
            @Override
            public void onChange(RealmResults<Drug> element) {
//                Log.e("Drug", "----onChange----" + element.size());
                if (query){
                    view.addItem(element);
                    query = false;
                    result.removeChangeListener(this);
                }
            }
        });

        return view.getView();
    }

    Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_add_drug:
//                    if (NetworkUtil.isMobileConnected(context) || NetworkUtil.isNetworkConnected(context)) {
//
//                    }
//                    else{
//                        Toast.makeText(context, "无网络连接!", Toast.LENGTH_SHORT).show();
//                    }
                    launchDrugAddActivity();
                    break;
            }
            return true;
        }
    };

    private void createProgressDialog(boolean horizontal) {
        builder = new MaterialDialog.Builder(getContext())
                .title("药品查询")
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .titleColorRes(R.color.black)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(horizontal)
                .canceledOnTouchOutside(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.e("Drug", "----onCreate----");
        setHasOptionsMenu(true);
        createProgressDialog(false);
    }

    @Override
    public void onStop(){
//        Log.e("Drug", "----onStop----");
        if (realmAsyncTask != null && !realmAsyncTask.isCancelled()) {
            realmAsyncTask.cancel();
        }

        result.removeChangeListeners();
        super.onStop();
    }

    @Override
    public void onDestroy(){
//        Log.e("Drug", "----OnDestroy----");
        this.presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != -1) return;

        if (requestCode == REQUEST_CAMERA) {
            dialog = builder.show();
            String code = data.getStringExtra("code");
            this.presenter.drug(code);
        }
    }

    public void start(){
        final String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(getContext(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            requestForPermission(permission);
        } else {
            launch();
        }
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, 1);
    }

    private void launch() {
        Intent startCustomCameraIntent = new Intent(getContext(), CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, 0);
    }

    private void launchDrugAddActivity() {
        Intent intent = new Intent(getContext(), DrugAddActivity.class);
        startActivityForResult(intent, 0);
    }

    private Action1 drugAction1 = new Action1<Result>() {
        @Override
        public void call(final Result result) {
            if (result.isResult("drug", "OK")) {
                JsonObject jsonObject = result.getObj().getAsJsonObject("data");
                final Drug drug = new Drug();
                drug.setId(jsonObject.get("id").getAsInt());
                drug.setName(jsonObject.get("name").getAsString());
                drug.setCompany(jsonObject.get("manufacturer").getAsString());
                drug.setCode(jsonObject.get("code").getAsString());
                drug.setForm(jsonObject.get("form").getAsString());
                drug.setOtc(jsonObject.get("otc").isJsonNull() ? "OTHER" : jsonObject.get("otc").getAsString());
                drug.setCategory(jsonObject.get("category").getAsString());
                drug.setIcon(jsonObject.get("icon").getAsString());
                drug.setDosage(jsonObject.get("dosage").getAsString());
                drug.setTime(new Date());
                drug.setSync(false);
                drug.setState(Constants.DRUG_STATE_NORMAL);
                drug.setMeal(Constants.DRUG_DEFAULT_MEAL);
                drug.setStock(1);

                final Realm realm = Realm.getDefaultInstance();
                long count = realm.where(Drug.class).equalTo("id", drug.getId()).count();
                if (count > 0){
                    if (dialog != null) dialog.dismiss();
                    Toast.makeText(context, R.string.drug_exists, Toast.LENGTH_SHORT).show();
                    return;
                }

                // 异步插入
                realmAsyncTask = realm.executeTransactionAsync(
                        new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealm(drug);
                            }
                        },
                        new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                view.addItem(drug);
                            }
                        },
                        new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                error.printStackTrace();
                            }
                        });
            } else {
                Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
            }

            if (dialog != null) dialog.dismiss();
        }
    };

    private class DrugOnClickListener implements DrugItemView.ClickListener{
        @Override
        public void onClick(View v, Drug drug) {
            startDrugSetting(drug);
        }

        @Override
        public void onLongClick(View v, int which, Drug drug) {
             switch (which){
                case 0:
                    startNewPlan(drug);
                    break;

                case 1:
                    if (presenter.allowDelete(drug)) {
                        view.deleteItem(drug);
                        presenter.deleteDrug(drug);
                    }
                    else{
                        MaterialDialogUtil.showMessage(context, drug.getName(), "用药计划正在执行,不能删除!");
                    }
                    break;
             }
        }
    }

    private void startDrugSetting(Drug drug){
        Intent intent = new Intent(getContext(), DrugSettingActivity.class);
        intent.putExtra("drug", drug);
        startActivity(intent);
    }

    private void startNewPlan(Drug drug){
        Intent intent = new Intent(getContext(), NewPlanActivity.class);
        intent.putExtra("drug", drug);
        startActivity(intent);
    }
}

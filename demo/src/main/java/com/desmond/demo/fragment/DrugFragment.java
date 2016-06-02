package com.desmond.demo.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desmond.demo.R;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.presenter.DrugPresenter;
import com.desmond.demo.box.view.DrugView;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.common.util.AndroidUtil;
import com.desmond.demo.common.util.DateUtil;
import com.desmond.demo.common.util.IconCenterEditText;
import com.desmond.squarecamera.CameraActivity;
import com.google.gson.JsonObject;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.presenter = new DrugPresenter(drugAction1);
        this.view = new DrugView(getContext(), null, this.presenter);

//        View view = inflater.inflate(R.layout.fragment_drug_box, container, false);
        IconCenterEditText icet_search = view.get(R.id.icet_search);

        icet_search.setOnSearchClickListener(new IconCenterEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                Toast.makeText(getContext(), "i'm going to seach", Toast.LENGTH_SHORT).show();
            }
        });

        TextView vt = (TextView) view.get(R.id.sao);
        vt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(v);
            }
        });

//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.search_view_toolbar);
////        int h = ((AppCompatActivity)getActivity()).getSupportActionBar().getHeight();
////        Log.e("Drug", "h is " + h);
//        toolbar.inflateMenu(R.menu.menu_my_account);
//
////        toolbar.setBackgroundColor(Color.parseColor("#FF0033"));
////        LinearLayout searchBar =  (LinearLayout)toolbar.findViewById(R.id.search_bar);
////        LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams)searchBar.getLayoutParams();
////        params.topMargin = AndroidUtil.dip2px(getContext(), 100);
////        searchBar.setLayoutParams(params);
//
//        SearchView searchView = (SearchView) toolbar.findViewById(R.id.action_search);
//        searchView.setIconified(false);
////        searchView.setIconifiedByDefault(false);
//        toolbar.setTitle("家庭药箱");
//        searchView.setQueryHint("国药准字号");
//        searchView.setMaxWidth(AndroidUtil.dip2px(getContext(), 240));
//        searchView.setSubmitButtonEnabled(true);
//
//        View searchplate = searchView.findViewById(R.id.search_plate);
//        searchplate.setBackgroundResource(R.drawable.underline);

        return view.getView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != -1) return;

        if (requestCode == REQUEST_CAMERA) {
            String code = data.getStringExtra("code");
            this.presenter.drug(code);
        }
    }

    public void start(View view){
        final String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(view.getContext(), permission)
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

    private Action1 drugAction1 = new Action1<Result>() {
        @Override
        public void call(Result result) {
            if (result.isResult("drug", "OK")) {
                JsonObject jsonObject = result.getObj().getAsJsonObject("drug");
                Drug drug = new Drug();

                drug.setId(jsonObject.get("id").getAsInt());
                drug.setName(jsonObject.get("name").getAsString());
                drug.setCompany(jsonObject.get("manufacturer").getAsString());
                drug.setCode(jsonObject.get("code").getAsString());
                drug.setForm(jsonObject.get("form").getAsString());
                drug.setOtc(jsonObject.get("otc").isJsonNull() ? "OTHER" : jsonObject.get("otc").getAsString());
                drug.setCategory(jsonObject.get("category").getAsString());
                drug.setTime(DateUtil.getCurrentTime());

                view.addItem(drug);
            } else {
                Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };
}

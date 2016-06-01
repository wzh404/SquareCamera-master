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
import com.desmond.demo.box.presenter.DrugPresenter;
import com.desmond.demo.common.util.AndroidUtil;
import com.desmond.demo.common.util.IconCenterEditText;
import com.desmond.squarecamera.CameraActivity;

/**
 * Created by WIN10 on 2016/5/31.
 */
public class DrugFragment extends Fragment {
    private DrugPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_drug, container, false);
        IconCenterEditText icet_search = (IconCenterEditText) view.findViewById(R.id.icet_search);

        icet_search.setOnSearchClickListener(new IconCenterEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                Toast.makeText(getContext(), "i'm going to seach", Toast.LENGTH_SHORT).show();
            }
        });

        TextView vt = (TextView) view.findViewById(R.id.sao);
        vt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(v);
            }
        });

//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.search_view_toolbar);
////        int h = ((AppCompatActivity)getActivity()).getSupportActionBar().getHeight();
////        Log.e("Drug", "h is " + h);
//        toolbar.inflateMenu(R.menu.menu_main);
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

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.e("Drug", resultCode + " - " + requestCode);
    }

    public void start(View view){
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
}

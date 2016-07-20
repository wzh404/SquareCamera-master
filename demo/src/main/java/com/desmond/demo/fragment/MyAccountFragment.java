package com.desmond.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.desmond.demo.R;
import com.desmond.demo.base.view.SpacesItemDecoration;
import com.desmond.demo.common.action.Result;
import com.desmond.demo.plan.model.DrugPlan;
import com.desmond.demo.reminder.presenter.ReminderPresenter;
import com.desmond.demo.reminder.adapter.ReminderRecyclerAdapter;
import com.desmond.demo.reminder.model.Reminder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * 用药
 *
 * Created by wangzunhui on 2016/6/2.
 */
public class MyAccountFragment extends Fragment {
    private ReminderPresenter presenter;
    private RealmResults<DrugPlan> result;
    private RecyclerView recyclerView;
    private List<Reminder> items = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Reminder empty = new Reminder();
        items.add(empty);

        View view = inflater.inflate(R.layout.fragment_my_account, null);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitle("我的提醒");
        toolbar.inflateMenu(R.menu.menu_my_account);

        recyclerView = (RecyclerView)view.findViewById(R.id.my_account_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
        adapter = new ReminderRecyclerAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);

        this.presenter = new ReminderPresenter(reminderAction1);
        createReminderItems();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    private Action1 reminderAction1 = new Action1<Result>() {
        @Override
        public void call(Result result) {
            if (result.isResult("reminder", "OK")) {
                createReminderItems();
            } else {
                Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void createReminderItems(){
        result = presenter.queryPlanAsync();
        result.addChangeListener(new RealmChangeListener<RealmResults<DrugPlan>>() {
            @Override
            public void onChange(RealmResults<DrugPlan> element) {
                TreeMap<String, List<String>> map = new TreeMap<>();
                for (DrugPlan plan : result) {
                    plan.createCurrentReminder(map);
                }

                items.clear();
                for (Map.Entry<String, List<String>>  entry: map.entrySet()){
                    String key = entry.getKey();
                    List<String> val = entry.getValue();

                    String content = "";
                    for (String s : val){
                        if (! "".equalsIgnoreCase(content)){
                            content += "\n";
                        }
                        content +=  s;
                    }
                    Reminder r = new Reminder();
                    r.setTime(key);
                    r.setReminders(content);
//                    Log.e("Drug", r.getTime() + " ---- " + r.getReminders());

                    items.add(r);
                }
                adapter.notifyDataSetChanged();
                result.removeChangeListener(this);
            }
        });
    }
}

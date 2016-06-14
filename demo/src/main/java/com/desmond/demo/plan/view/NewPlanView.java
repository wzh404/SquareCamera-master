package com.desmond.demo.plan.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.desmond.demo.R;
import com.desmond.demo.base.adapter.DefaultItemRecyclerAdapter;
import com.desmond.demo.base.view.AbstractRecyclerView;
import com.desmond.demo.base.view.IView;
import com.desmond.demo.box.model.Drug;
import com.desmond.demo.common.util.AndroidUtil;
import com.desmond.demo.common.util.MaterialDialogUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class NewPlanView extends AbstractRecyclerView {
    private JsonArray items;
    private Context context;
    private Drug drug;

    public NewPlanView(final Context context, Drug drug) {
        this.context = context;
        this.drug = drug;
        super.init(context, null, R.layout.activity_new_drug_plan);

        Toolbar toolbar = get(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_new_plan_title);
    }

    @Override
    public RecyclerView.Adapter createAdapter(Context context) {
        String json = AndroidUtil.getFromAssets(context, "drug_plan.json");
        items = (new JsonParser()).parse(json).getAsJsonArray();

        setItemDesc("drug", drug.getName());
        setItemDesc("user", "本人");

        setItemDesc("interval", "每日");
        setItemDesc("dose", "每日3次,每次1片");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        setItemDesc("date", sdf.format(new Date()));
        setItemDesc("days", "一周");

        return new DefaultItemRecyclerAdapter(context, items, listener);
    }

    private void addItemProperty(String code, String key, String val) {
        for (JsonElement element : items) {
            if (element.getAsJsonObject().get("code") == null ||
                    element.getAsJsonObject().get("code").isJsonNull()) {
                continue;
            }

            String elementCode = element.getAsJsonObject().get("code").getAsString();
            if (code.equalsIgnoreCase(elementCode)) {
                element.getAsJsonObject().addProperty(key, val);
                break;
            }
        }
    }

    private void setItemDesc(String code, String desc) {
        addItemProperty(code, "desc", desc);
    }

    private OnSelectListener listener = new OnSelectListener() {
        @Override
        public void onSelected(IView view, String code, final String... arg) {
            if ("interval".equalsIgnoreCase(code)) {
                String val = arg[0];
                if ("每日".equalsIgnoreCase(val)) {
                    showDay(view);
                }
                else if ("每周".equalsIgnoreCase(val)) {
                    showWeek(view);
                }
                else if ("间隔天".equalsIgnoreCase(val)) {
                    showIntervalDays(view);
                }
                else if ("间隔小时".equalsIgnoreCase(val)) {
                    showIntervalHours(view);
                }
            }
        }
    };

    private void showDay(final IView view) {
        MaterialDialog.ListCallback callback = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View v, int which, CharSequence text) {
                TextView tv = view.get(R.id.item_my_desc);
                tv.setText(text);
            }
        };

        String[] items = {"每日1次", "每日2次", "每日3次", "每日4次", "每日5次", "每日6次", "每日7次"};
        MaterialDialogUtil.showList(view.getView(), items, callback);
    }

    private void showIntervalHours(final IView view) {
        MaterialDialog.ListCallback callback = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View v, int which, CharSequence text) {
                TextView tv = view.get(R.id.item_my_desc);
                tv.setText(text);
            }
        };

        String[] items = {"间隔1小时", "间隔2小时", "间隔3小时", "间隔4小时", "间隔5小时", "间隔6小时",
                "间隔7小时", "间隔8小时", "间隔9小时", "间隔10小时", "间隔11小时", "间隔12小时",
        };
        MaterialDialogUtil.showList(view.getView(), items, callback);
    }

    private void showIntervalDays(final IView view) {
        MaterialDialog.ListCallback callback = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View v, int which, CharSequence text) {
                TextView tv = view.get(R.id.item_my_desc);
                tv.setText(text);
            }
        };

        String[] items = {"间隔1天", "间隔2天", "间隔3天", "间隔4天", "间隔5天"};
        MaterialDialogUtil.showList(view.getView(), items, callback);
    }

    private void showWeek(final IView view){
        MaterialDialog.SingleButtonCallback callback = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                String[] weeks ={"一", "二", "三", "四", "五", "六", "日"};
                String text = "每周";
                for (int k : dialog.getSelectedIndices())
                    text += weeks[k];

                TextView tv = view.get(R.id.item_my_desc);
                tv.setText(text);
            }
        };
        String[] items = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};

        MaterialDialogUtil.showMultiChoiceLimited(view.getView(), items, callback);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return get(R.id.new_plan_recycler_view);
    }

    public Toolbar getToolbar() {
        return get(R.id.toolbar);
    }

    public void setToolbarOnBackPressed() {
        Toolbar toolbar = get(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) context).onBackPressed();
            }
        });
    }
}

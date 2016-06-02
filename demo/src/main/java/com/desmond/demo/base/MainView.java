package com.desmond.demo.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.desmond.demo.MainActivity;
import com.desmond.demo.R;
import com.desmond.demo.common.AbstractView;
import com.desmond.demo.fragment.DrugFragment;
import com.desmond.demo.fragment.MyAccountFragment;
import com.desmond.demo.fragment.PlanFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WIN10 on 2016/5/31.
 */
public class MainView extends AbstractView {
    private FragmentManager fragmentManager;
    private FragmentAdapter fragmentAdapter;

    public MainView(Context context){
        super.init(context, null, R.layout.activity_main);

        this.fragmentManager = ((MainActivity)context).getSupportFragmentManager();

        List<TabBean> tabs = getTabs();
        final ViewPager viewPager = get(R.id.pager);
        initViewPager(viewPager, tabs);

        final FragmentTabHost fragmentTabHost =get(android.R.id.tabhost);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                TabWidget widget = fragmentTabHost.getTabWidget();
                int oldFocusability = widget.getDescendantFocusability();
                widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                fragmentTabHost.setCurrentTab(position);
                widget.setDescendantFocusability(oldFocusability);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                viewPager.setCurrentItem(fragmentTabHost.getCurrentTab());
            }
        });

        initTabs(context, fragmentTabHost, tabs);
    }

    private List<TabBean> getTabs(){
        List<TabBean> tabs = new ArrayList<TabBean>();
        tabs.add(new TabBean("首页", R.drawable.selector_tab_home, new DrugFragment()));
        tabs.add(new TabBean("用药", R.drawable.selector_tab_share, new PlanFragment()));
        tabs.add(new TabBean("我的账户", R.drawable.selector_tab_me, new MyAccountFragment()));

        return tabs;
    }

    private void initTabs(Context context, FragmentTabHost tabHost,  List<TabBean> tbs) {
        tabHost.setup(context, fragmentManager, R.id.pager);

        for (TabBean tb : tbs) {
            TabshotContentView v = new TabshotContentView();
            v.init(context, null);
            v.setText(tb.name);
            v.setImageSelector(tb.selector);

            tabHost.addTab(tabHost.newTabSpec("").setIndicator(v.getView()), Fragment.class, null);
            tabHost.setTag(tb.name);
            tabHost.setBackgroundResource(R.drawable.selector_tab_background);
        }
        //设置tabs之间的分隔线不显示
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    private void initViewPager(ViewPager vp, List<TabBean> tabs) {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        for (TabBean tab : tabs) {
            fragmentList.add(tab.fragment);
        }

        fragmentAdapter = new FragmentAdapter(fragmentManager, fragmentList);
        vp.setAdapter(fragmentAdapter);
        vp.setCurrentItem(0);
        vp.setOffscreenPageLimit(fragmentList.size());
    }

    private class TabBean {
        public TabBean(String name, int selector, Fragment fragment) {
            this.name = name;
            this.selector = selector;
            this.fragment = fragment;
        }

        public String name;
        public int selector;
        public Fragment fragment;
    }
}

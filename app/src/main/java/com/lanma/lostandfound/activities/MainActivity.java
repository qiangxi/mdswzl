package com.lanma.lostandfound.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lanma.lostandfound.R;
import com.lanma.lostandfound.adapter.DrawerLayoutAdapter;
import com.lanma.lostandfound.beans.DrawerLayoutBean;
import com.lanma.lostandfound.beans.StudentInfo;
import com.lanma.lostandfound.constants.AppConstants;
import com.lanma.lostandfound.fragment.FoundFragment;
import com.lanma.lostandfound.fragment.LostFragment;
import com.lanma.lostandfound.utils.AppManager;
import com.lanma.lostandfound.utils.YouMiAdUtils;
import com.lanma.lostandfound.view.RoundImageView;
import com.lanma.lostandfound.view.ScaleTransitionPagerTitleView;
import com.umeng.analytics.MobclickAgent;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity {
    @Bind(R.id.mainIndicator)
    MagicIndicator mainIndicator; //viewpager指示器
    @Bind(R.id.mainViewPager)
    ViewPager mainViewPager;//viewpager
    @Bind(R.id.userHeader)
    RoundImageView mUserHeader;//用户头像
    @Bind(R.id.userSex)
    TextView mUserSex;//用户性别
    @Bind(R.id.drawer)
    DrawerLayout mDrawer;//侧滑菜单
    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.drawerListView)
    ListView drawerListView;

    private DrawerLayoutAdapter adapter;
    private List<DrawerLayoutBean> list = new ArrayList<>();
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] TITLES = {"失物", "招领"};
    private CommonNavigator mainNavigator;//指示器
    private long timeSpace;
    private int[] resId = new int[]{R.drawable.icon_lost, R.drawable.icon_found, R.drawable.icon_message_center,
            R.drawable.icon_user_note, R.drawable.icon_suggestion};
    private String[] desc = new String[]{"我的失物", "我的招领", "消息中心", "用户须知", "反馈建议",};
    private Class<?>[] classes = new Class[]{MyLostAndFoundActivity.class, MyLostAndFoundActivity.class,
            MessageCenterActivity.class, UserNotesActivity.class, SuggestActivity.class};
    private String[] intentAction = new String[]{AppConstants.MyLostInfoAction, AppConstants.MyFoundInfoAction, "", "", "", "",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
        getSwipeBackLayout().setEnableGesture(false);
        initViewPagerAndIndicator();
        initData();
    }

    private void initData() {
        for (int i = 0; i < resId.length; i++) {
            list.add(new DrawerLayoutBean(resId[i], desc[i], classes[i], intentAction[i]));
        }
        adapter = new DrawerLayoutAdapter(this, list);
        drawerListView.setAdapter(adapter);
    }

    private void initToolBar() {
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolBar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // 解决当侧滑菜单显示出来时,下层view依然可以点击的问题
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();//必不可少,该方法用来同步滑动状态,没有这句会造成Toggle按钮没有动画效果
        mToolBar.setTitle("失物招领");
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (null != BmobUser.getCurrentUser(StudentInfo.class)) {
                    if (!TextUtils.isEmpty(BmobUser.getCurrentUser(StudentInfo.class).getObjectId())) {
                        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                            mDrawer.closeDrawer(GravityCompat.START);
                        } else {
                            mDrawer.openDrawer(GravityCompat.START);
                        }
                    } else {
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        mToolBar.inflateMenu(R.menu.menu_main);
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIsLogin();
    }

    @Override
    protected void onPause() {
        super.onPause();
        YouMiAdUtils.onAdPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        YouMiAdUtils.onAdStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        YouMiAdUtils.onAdDestroy(this);
    }

    /**
     * 检查是否已经登陆
     */
    private void checkIsLogin() {
        if (null != BmobUser.getCurrentUser(StudentInfo.class)) {
            if (!TextUtils.isEmpty(BmobUser.getCurrentUser(StudentInfo.class).getObjectId())) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                //设置头像
                if (TextUtils.isEmpty(BmobUser.getCurrentUser(StudentInfo.class).getStudentHeadImage())) {
                    mUserHeader.setImageResource(R.drawable.icon_passenger_man);
                } else {
                    Glide.with(this).load(BmobUser.getCurrentUser(StudentInfo.class).getStudentHeadImage()).diskCacheStrategy(DiskCacheStrategy.ALL).
                            centerCrop().placeholder(R.drawable.icon_passenger_man).error(R.drawable.icon_passenger_man).
                            into(mUserHeader);
                }
                //设置性别
                if ("女".equals(BmobUser.getCurrentUser(StudentInfo.class).getStudentSex())) {
                    mUserSex.setText("漂亮的小姑娘");
                } else {
                    mUserSex.setText("帅气的小伙子");
                }
            } else {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        } else {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void initViewPagerAndIndicator() {
        mainIndicator.setBackgroundColor(getResources().getColor(R.color.actionBarColor));
        mainViewPager.setAdapter(new LostFoundPagerAdapter(getSupportFragmentManager()));
        mainNavigator = new CommonNavigator(this);
        mainNavigator.setAdjustMode(true);  // 自适应模式
        mainNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return TITLES.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(TITLES[index]);
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setNormalColor(Color.WHITE);
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
                indicator.setColors(Color.WHITE);
                return indicator;
            }
        });
        mainIndicator.setNavigator(mainNavigator);
        ViewPagerHelper.bind(mainIndicator, mainViewPager);
    }

    @OnClick({R.id.userHeaderLayout})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //查看个人信息
            case R.id.userHeaderLayout:
                intent = new Intent(MainActivity.this, StudentInfoActivity.class);
                startActivity(intent);
                break;
        }
    }

    @OnItemClick(R.id.drawerListView)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, list.get(position).getClazz());
        if (!TextUtils.isEmpty(list.get(position).getIntentAction())) {
            intent.setAction(list.get(position).getIntentAction());
        }
        startActivity(intent);
    }

    public class LostFoundPagerAdapter extends FragmentPagerAdapter {

        public LostFoundPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return LostFragment.newInstance(position);
            } else {
                return FoundFragment.newInstance(position);
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawer.isDrawerVisible(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
            if (System.currentTimeMillis() - timeSpace > 2000) {
                timeSpace = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            } else {
                MobclickAgent.onKillProcess(this);
                AppManager.newInstance().AppExit(this);
            }
        }
        return true;
    }
}

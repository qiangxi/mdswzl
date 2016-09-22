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
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lanma.lostandfound.R;
import com.lanma.lostandfound.beans.StudentInfo;
import com.lanma.lostandfound.constants.AppConstants;
import com.lanma.lostandfound.fragment.FoundFragment;
import com.lanma.lostandfound.fragment.LostFragment;
import com.lanma.lostandfound.utils.AppManager;
import com.lanma.lostandfound.utils.ImageViewTintUtil;
import com.lanma.lostandfound.utils.YouMiAdUtils;
import com.lanma.lostandfound.view.RoundImageView;
import com.umeng.analytics.MobclickAgent;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
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

    private String[] TITLES = {"失物", "招领"};
    private CommonNavigator mainNavigator;//指示器
    private long timeSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ImageViewTintUtil.setImageViewTint((ImageView) findViewById(R.id.mainToggle));
        initViewPagerAndIndicator();
        initEvent();
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
     * 初始化事件
     */
    private void initEvent() {
        // DrawerLayout监听事件.
        mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // 解决当侧滑菜单显示出来时,下层view依然可以点击的问题
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
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
        mainViewPager.addOnPageChangeListener(this);
        mainViewPager.setAdapter(new LostFoundPagerAdapter(getSupportFragmentManager()));
        mainNavigator = new CommonNavigator(this);
        mainNavigator.setScrollPivotX(0.35f);
        mainNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return TITLES.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(TITLES[index]);
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.actionBarColor));
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
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(getResources().getColor(R.color.bg_indicator_slide));
                indicator.setRoundRadius(10);
                return indicator;
            }
        });
        mainIndicator.setNavigator(mainNavigator);
    }

    @OnClick({R.id.mainToggle, R.id.userHeaderLayout, R.id.mainLost, R.id.mainFound,
            R.id.mainMessageCenter, R.id.mainUserNote, R.id.mainSuggestion})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mainToggle:
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
                break;
            //查看个人信息
            case R.id.userHeaderLayout:
                intent = new Intent(MainActivity.this, StudentInfoActivity.class);
                startActivity(intent);
                break;
            //发布的失物信息
            case R.id.mainLost:
                intent = new Intent(MainActivity.this, MyLostAndFoundActivity.class);
                intent.setAction(AppConstants.MyLostInfoAction);
                startActivity(intent);
                break;
            //发布的招领信息
            case R.id.mainFound:
                intent = new Intent(MainActivity.this, MyLostAndFoundActivity.class);
                intent.setAction(AppConstants.MyFoundInfoAction);
                startActivity(intent);
                break;
            //消息中心
            case R.id.mainMessageCenter:
                intent = new Intent(MainActivity.this, MessageCenterActivity.class);
                startActivity(intent);
                break;
            //用户须知
            case R.id.mainUserNote:
                intent = new Intent(MainActivity.this, UserNotesActivity.class);
                startActivity(intent);
                break;
            //反馈建议
            case R.id.mainSuggestion:
                intent = new Intent(MainActivity.this, SuggestActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mainIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        mainNavigator.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mainIndicator.onPageSelected(position);
        mainNavigator.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mainIndicator.onPageScrollStateChanged(state);
        mainNavigator.onPageScrollStateChanged(state);
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

package com.lanma.lostandfound.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.fragment.LoginFragment;
import com.lanma.lostandfound.fragment.RegisterFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.TriangularPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity  {

    @Bind(R.id.loginRegisterIndicator)
    MagicIndicator mLoginRegisterIndicator; //viewpager指示器
    @Bind(R.id.loginRegisterViewPager)
    ViewPager mLoginRegisterViewPager;//viewpager

    private String[] TITLES = {"登陆", "注册"};
    private CommonNavigator loginRegisterNavigator;//指示器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        initViewPagerAndIndicator();
    }

    public void initViewPagerAndIndicator() {

        mLoginRegisterViewPager.setAdapter(new LoginRegisterPagerAdapter(getSupportFragmentManager()));
        loginRegisterNavigator = new CommonNavigator(this);
        loginRegisterNavigator.setScrollPivotX(0.35f);
        loginRegisterNavigator.setAdjustMode(true);  // 自适应模式
        loginRegisterNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return TITLES.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(TITLES[index]);
                simplePagerTitleView.setNormalColor(Color.parseColor("#003972"));
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoginRegisterViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                TriangularPagerIndicator indicator = new TriangularPagerIndicator(context);
                indicator.setLineColor(Color.WHITE);
                return indicator;
            }
        });
        mLoginRegisterIndicator.setNavigator(loginRegisterNavigator);
        ViewPagerHelper.bind(mLoginRegisterIndicator, mLoginRegisterViewPager);
    }

    public class LoginRegisterPagerAdapter extends FragmentPagerAdapter {

        public LoginRegisterPagerAdapter(FragmentManager fm) {
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
                return LoginFragment.newInstance(position);
            } else {
                return RegisterFragment.newInstance(position);
            }
        }
    }
}

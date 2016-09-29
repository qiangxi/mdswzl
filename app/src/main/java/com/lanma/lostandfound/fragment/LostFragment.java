package com.lanma.lostandfound.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lanma.lostandfound.R;
import com.lanma.lostandfound.activities.AddLostInfoActivity;
import com.lanma.lostandfound.activities.InfoDetailActivity;
import com.lanma.lostandfound.activities.LoginActivity;
import com.lanma.lostandfound.adapter.LostFoundInfoAdapter;
import com.lanma.lostandfound.beans.LostFoundInfo;
import com.lanma.lostandfound.beans.StudentInfo;
import com.lanma.lostandfound.constants.AppConstants;
import com.lanma.lostandfound.net.ServerConnection;
import com.lanma.lostandfound.presenter.LostInfoListPresenter;
import com.lanma.lostandfound.utils.EmptyViewUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/**
 * 作者 任强强 on 2016/9/1 18:25.
 */
public class LostFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LostInfoListPresenter,
        BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.addLostInfo)
    FloatingActionButton mAddLostInfoButton;
    @Bind(R.id.lostRecyclerView)
    RecyclerView mLostRecyclerView;

    private List<LostFoundInfo> mList = new ArrayList<>();
    private boolean isInited = false;//布局是否已经初始化完毕
    private boolean isDataLoaded = false;//如果已加载过数据,再次回到该界面不用再次加载数据
    private LostFoundInfoAdapter mAdapter;

    public static LostFragment newInstance(int position) {
        LostFragment fragment = new LostFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        initData();
    }

    private void initData() {
        if (isVisible && isInited && !isDataLoaded) {
            //必须加上这句代码,mSwipeRefreshLayout才会在一开始的时候就显示圆圈loading
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, 100);
            mSwipeRefreshLayout.setRefreshing(true);
            ServerConnection.getLostFoundInfoList(AppConstants.LostInfoType, 0, this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == mAdapter) {
            mAdapter = new LostFoundInfoAdapter(mList);
            mAdapter.openLoadAnimation();
            mAdapter.isFirstOnly(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_layout, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isInited = true;
        initSwipeRefreshLayout();
        initEvent();
        initData();
    }

    private void initSwipeRefreshLayout() {
        mLostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.BLUE, Color.RED);
    }

    private void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLostRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
                intent.putExtra("LostOrFoundInfo", mList.get(position));
                startActivity(intent);
            }
        });
        mAdapter.openLoadMore(mList.size());
        mAdapter.setOnLoadMoreListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.addLostInfo)
    public void onClick() {
        if (null != BmobUser.getCurrentUser(StudentInfo.class)) {
            if (!TextUtils.isEmpty(BmobUser.getCurrentUser(StudentInfo.class).getObjectId())) {
                MobclickAgent.onEvent(getActivity(), "addLostInfo");
                Intent intent = new Intent(getActivity(), AddLostInfoActivity.class);
                startActivityForResult(intent, AppConstants.LostInfoRequestCode);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        ServerConnection.getLostFoundInfoList(AppConstants.LostInfoType, 0, this);
    }

    @Override
    public void onRefreshLostInfoListSuccess(List<LostFoundInfo> list) {
        if (null != list && list.size() > 0) {
            isDataLoaded = true;
        } else {
            isDataLoaded = false;
            mAdapter.setEmptyView(EmptyViewUtil.getEmptyView(getActivity()));
        }
        mSwipeRefreshLayout.setRefreshing(false);
        isDataLoaded = true;
        mList = list;
        mAdapter = new LostFoundInfoAdapter(mList);
        mLostRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefreshLostInfoListFailure(String failureMessage) {
        mAdapter.setEmptyView(EmptyViewUtil.getEmptyView(getActivity()));
        mSwipeRefreshLayout.setRefreshing(false);
        showToast(failureMessage);
    }

    @Override
    public void onLoadMoreLostInfoListSuccess(List<LostFoundInfo> list) {
        mAdapter.loadComplete();
        if (null == list || list.size() == 0) {
            showToast("没有更多数据了");
            return;
        }
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreLostInfoListFailure(String failureMessage) {
        mAdapter.loadComplete();
        mAdapter.showLoadMoreFailedView();
        showToast(failureMessage);
    }

    @Override
    public void requestStart() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data) {
            if (AppConstants.LostInfoRequestCode == requestCode) {
                //必须加上这句代码,mSwipeRefreshLayout才会在一开始的时候就显示圆圈loading
                mSwipeRefreshLayout.setProgressViewOffset(false, 0, 100);
                mSwipeRefreshLayout.setRefreshing(true);
                ServerConnection.getLostFoundInfoList(AppConstants.LostInfoType, 0, this);
            }
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getActivity().getClass().getSimpleName()); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getActivity().getClass().getSimpleName());
    }

    @Override
    public void onLoadMoreRequested() {
        ServerConnection.getLostFoundInfoList(AppConstants.FoundInfoType, mList.size(), this);
    }
}

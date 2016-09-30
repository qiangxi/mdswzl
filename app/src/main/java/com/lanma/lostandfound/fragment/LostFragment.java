package com.lanma.lostandfound.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

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
import com.lanma.lostandfound.utils.AnimationAdapterUtil;
import com.lanma.lostandfound.utils.EmptyViewUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import cn.bmob.v3.BmobUser;

/**
 * 作者 任强强 on 2016/9/1 18:25.
 */
public class LostFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, LostInfoListPresenter {

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.addLostInfo)
    FloatingActionButton mAddLostInfoButton;
    @Bind(R.id.lostListView)
    ListView mLostListView;

    private List<LostFoundInfo> mList = new ArrayList<>();
    private boolean isInited = false;//布局是否已经初始化完毕
    private boolean isDataLoaded = false;//如果已加载过数据,再次回到该界面不用再次加载数据
    private LostFoundInfoAdapter mAdapter;
    private int mListViewHeight;//listView的高度

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
            mAdapter = new LostFoundInfoAdapter(getActivity(), mList);
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
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.BLUE, Color.RED);
    }

    private void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLostListView.setOnScrollListener(this);
        mLostListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mListViewHeight = mLostListView.getHeight();
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    mLostListView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mLostListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
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

    @OnItemClick(R.id.lostListView)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
        intent.putExtra("LostOrFoundInfo", mList.get(position));
        startActivity(intent);
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
            mLostListView.setEmptyView(EmptyViewUtil.getEmptyView(getActivity(), mLostListView));
        }
        mSwipeRefreshLayout.setRefreshing(false);
        isDataLoaded = true;
        mList = list;
        mAdapter = new LostFoundInfoAdapter(getActivity(), mList);
        mLostListView.setAdapter(AnimationAdapterUtil.getSwingBottomInAnimationAdapter(mAdapter, mLostListView));
    }

    @Override
    public void onRefreshLostInfoListFailure(String failureMessage) {
        mLostListView.setEmptyView(EmptyViewUtil.getEmptyView(getActivity(), mLostListView));
        mSwipeRefreshLayout.setRefreshing(false);
        showToast(failureMessage);
    }

    @Override
    public void onLoadMoreLostInfoListSuccess(List<LostFoundInfo> list) {
        if (null == list || list.size() == 0) {
            showToast("没有更多数据了");
            return;
        }
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreLostInfoListFailure(String failureMessage) {
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 只有在闲置状态情况下检查
        // 如果满足允许上拉加载、非加载状态中、最后一个显示的 item 与数据源的大小一样，则表示滑动入底部
        if (scrollState == SCROLL_STATE_IDLE && !mSwipeRefreshLayout.isRefreshing()) {
            if (view.getLastVisiblePosition() == (mLostListView.getCount() - 1)) {
                View childView = mLostListView.getChildAt(mLostListView.getChildCount() - 1);
                if (null != childView && childView.getBottom() == mListViewHeight) {
                    ServerConnection.getLostFoundInfoList(AppConstants.LostInfoType, mList.size(), this);
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //下面这段代码用来解决listView与swipeRefreshLayout滑动冲突的问题
        boolean enable = false;
        if (mLostListView != null && mLostListView.getChildCount() > 0) {
            // check if the first item of the list is visible
            boolean firstItemVisible = mLostListView.getFirstVisiblePosition() == 0;
            // check if the top of the first item is visible
            boolean topOfFirstItemVisible = mLostListView.getChildAt(0).getTop() == 0;
            // enabling or disabling the refresh layout
            enable = firstItemVisible && topOfFirstItemVisible;
        }
        mSwipeRefreshLayout.setEnabled(enable);
    }
}

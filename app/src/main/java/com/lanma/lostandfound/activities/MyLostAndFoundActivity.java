package com.lanma.lostandfound.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.adapter.MyLostAndFoundAdapter;
import com.lanma.lostandfound.beans.LostFoundInfo;
import com.lanma.lostandfound.beans.StudentInfo;
import com.lanma.lostandfound.constants.AppConstants;
import com.lanma.lostandfound.dialog.LoadingDialog;
import com.lanma.lostandfound.net.ServerConnection;
import com.lanma.lostandfound.presenter.DeleteCurrentInfoPresenter;
import com.lanma.lostandfound.presenter.MyLostAndFoundPresenter;
import com.lanma.lostandfound.utils.AnimationAdapterUtil;
import com.lanma.lostandfound.utils.EmptyViewUtil;
import com.lanma.lostandfound.utils.ScreenUtils;
import com.lanma.lostandfound.utils.YouMiAdUtils;
import com.lanma.lostandfound.view.swipemenulistview.SwipeMenu;
import com.lanma.lostandfound.view.swipemenulistview.SwipeMenuCreator;
import com.lanma.lostandfound.view.swipemenulistview.SwipeMenuItem;
import com.lanma.lostandfound.view.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cn.bmob.v3.BmobUser;

public class MyLostAndFoundActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener,
        AbsListView.OnScrollListener, MyLostAndFoundPresenter, DeleteCurrentInfoPresenter,
        SwipeMenuListView.OnMenuItemClickListener {

    @Bind(R.id.myLostAndFoundListView)
    SwipeMenuListView mMyLostAndFoundListView;
    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    private List<LostFoundInfo> mList = new ArrayList<>();
    private MyLostAndFoundAdapter mAdapter;
    private String InfoType;
    private int mListViewHeight;
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lost_and_found);
        ButterKnife.bind(this);
        initToolBar();
        initSwipeMenuListView();
        initData();
        YouMiAdUtils.showSuspendBannerAdInBottom(this);
    }

    private void initToolBar() {
        mToolBar.setTitle("我的失物信息");
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initSwipeMenuListView() {
        mDialog = new LoadingDialog(this);
        mMyLostAndFoundListView.setOnScrollListener(this);
        mMyLostAndFoundListView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mMyLostAndFoundListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.RED));
                // set item width
                openItem.setWidth(ScreenUtils.dpToPx(MyLostAndFoundActivity.this, 90));
                // set item title
                openItem.setTitle("删除");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
        mMyLostAndFoundListView.setMenuCreator(creator);
        mMyLostAndFoundListView.setOnMenuItemClickListener(this);
    }

    private void initData() {
        if (AppConstants.MyLostInfoAction.equals(getIntent().getAction())) {
            mToolBar.setTitle("我的失物信息");
            InfoType = AppConstants.LostInfoType;
        } else {
            mToolBar.setTitle("我的招领信息");
            InfoType = AppConstants.FoundInfoType;
        }
        mDialog.show();
        ServerConnection.getMyLostAndFoundInfo(BmobUser.getCurrentUser(StudentInfo.class), 0, InfoType, this);
    }

    @OnItemClick(R.id.myLostAndFoundListView)
    public void onItemClickListener(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MyLostAndFoundDetailActivity.class);
        intent.putExtra("LostOrFoundInfo", mList.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, AppConstants.myLostAndFoundInfoDetailRequestCode);
    }

    @Override
    public void onRefreshMyLostAndFoundListSuccess(List<LostFoundInfo> list) {
        mDialog.dismiss();
        mList = list;
        if (null == list || list.size() == 0) {
            mMyLostAndFoundListView.setEmptyView(EmptyViewUtil.getEmptyView(this, mMyLostAndFoundListView));
        }
        mAdapter = new MyLostAndFoundAdapter(this, mList);
        mMyLostAndFoundListView.setAdapter(AnimationAdapterUtil.getAnimationInAdapter(mAdapter, mMyLostAndFoundListView));
    }

    @Override
    public void onRefreshMyLostAndFoundListFailure(String failureMessage) {
        mDialog.dismiss();
        showToast(failureMessage);
        mMyLostAndFoundListView.setEmptyView(EmptyViewUtil.getEmptyView(this, mMyLostAndFoundListView));
    }

    @Override
    public void onLoadMoreMyLostAndFoundListSuccess(List<LostFoundInfo> list) {
        if (null == list || list.size() == 0) {
            showToast("没有更多数据了");
            return;
        }
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreMyLostAndFoundListFailure(String failureMessage) {
        showToast(failureMessage);
    }

    @Override
    public void requestStart() {
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 只有在闲置状态情况下检查
        // 如果满足允许上拉加载、非加载状态中、最后一个显示的 item 与数据源的大小一样，则表示滑动入底部
        if (scrollState == SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == (mMyLostAndFoundListView.getCount() - 1)) {
                View childView = mMyLostAndFoundListView.getChildAt(mMyLostAndFoundListView.getChildCount() - 1);
                if (null != childView && childView.getBottom() == mListViewHeight) {
                    ServerConnection.getMyLostAndFoundInfo(BmobUser.getCurrentUser(StudentInfo.class), mList.size(), InfoType, this);
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onGlobalLayout() {
        mListViewHeight = mMyLostAndFoundListView.getHeight();
        mMyLostAndFoundListView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onDeleting() {
        mDialog.show();
    }

    @Override
    public void onDeleteSuccess(int position) {
        mDialog.dismiss();
        mList.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteFailure(String failureMessage) {
        mDialog.dismiss();
        showToast(failureMessage);
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
            // 删除
            case 0:
                ServerConnection.deleteCurrentInfo(mList.get(position).getObjectId(), position, this);
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            if (AppConstants.myLostAndFoundInfoDetailRequestCode == requestCode) {
                int position = data.getIntExtra("position", -1);
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}

package com.lanma.lostandfound.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lanma.lostandfound.R;


/**
 * 基类fragment
 */
public abstract class BaseFragment extends Fragment {
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    public BaseFragment() {

    }

    //adapter中的每个fragment切换的时候都会被调用，
    //如果是切换到当前页，那么isVisibleToUser==true，否则为false
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 错误码转String
     *
     * @param errorCode 错误码
     */
    public String errorCodeToString(String errorCode) {
        int id = getResources().getIdentifier(errorCode, "string", getActivity().getPackageName());
        if (id != 0) {
            return getString(id);
        }
        return "";
    }
}

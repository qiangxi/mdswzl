package com.lanma.lostandfound.presenter;

import com.lanma.lostandfound.beans.MessageInfo;

import java.util.List;

/**
 * 作者 任强强 on 2016/9/10 15:25.
 */
public interface MessageInfoPresenter extends BasePresenter {
    void getMessageInfoSuccess(List<MessageInfo> list);

    void getMessageInfoFailure(String failureMessage);
}

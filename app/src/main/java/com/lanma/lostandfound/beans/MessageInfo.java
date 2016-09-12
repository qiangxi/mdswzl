package com.lanma.lostandfound.beans;

import cn.bmob.v3.BmobObject;

/**
 * 作者 任强强 on 2016/9/10 15:14.
 */
public class MessageInfo extends BmobObject {
    private String messageTitle;
    private String messageContent;

    public MessageInfo() {
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}

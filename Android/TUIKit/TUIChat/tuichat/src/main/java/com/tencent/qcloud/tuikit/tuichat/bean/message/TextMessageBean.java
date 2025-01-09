package com.tencent.qcloud.tuikit.tuichat.bean.message;

import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tuikit.timcommon.bean.TUIMessageBean;
import com.tencent.qcloud.tuikit.timcommon.bean.TUIReplyQuoteBean;
import com.tencent.qcloud.tuikit.tuichat.bean.message.reply.TextReplyQuoteBean;

public class TextMessageBean extends TUIMessageBean {
    private String text;

    @Override
    public String onGetDisplayString() {
        return text;
    }

    @Override
    public void onProcessMessage(V2TIMMessage v2TIMMessage) {
        if (v2TIMMessage.getTextElem() != null) {
            text = v2TIMMessage.getTextElem().getText();
        }else{
            if(v2TIMMessage.getSender().equals("huanxin331")){
                text="系统提醒";
            }
            if(v2TIMMessage.getSender().equals("huanxin330")){
                text="订单提醒";
            }
            if(v2TIMMessage.getSender().equals("huanxin332")){
                text="互动提醒";
            }
        }
        setExtra(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Class<? extends TUIReplyQuoteBean> getReplyQuoteBeanClass() {
        return TextReplyQuoteBean.class;
    }
}

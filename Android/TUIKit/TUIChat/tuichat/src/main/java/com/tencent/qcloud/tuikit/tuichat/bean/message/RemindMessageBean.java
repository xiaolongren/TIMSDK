package com.tencent.qcloud.tuikit.tuichat.bean.message;

import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tuikit.timcommon.bean.TUIMessageBean;

import org.json.JSONException;
import org.json.JSONObject;

public class RemindMessageBean extends TUIMessageBean {
    public String content;
    public String actionContent;
    public String actionColor;
    public String color;
    public String bgColor;
    public String action;
    @Override
    public String onGetDisplayString() {
        return "提醒消息";
    }

    @Override
    public void onProcessMessage(V2TIMMessage v2TIMMessage) {

      String json= new String(v2TIMMessage.getCustomElem().getData()) ;
        try {
            JSONObject jsonObject=new JSONObject(json);
            content=jsonObject.optString("content");
            actionContent=jsonObject.optString("actionContent");
            actionColor=jsonObject.optString("actionColor");
            color=jsonObject.optString("color");
            bgColor=jsonObject.optString("bgColor");
            action=jsonObject.optString("action");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}

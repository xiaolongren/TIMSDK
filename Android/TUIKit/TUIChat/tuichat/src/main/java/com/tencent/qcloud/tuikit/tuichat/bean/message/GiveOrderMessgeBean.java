package com.tencent.qcloud.tuikit.tuichat.bean.message;

import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tuikit.timcommon.bean.TUIMessageBean;

import org.json.JSONException;
import org.json.JSONObject;

public class GiveOrderMessgeBean  extends TUIMessageBean {

    public String type;

    /**
     * 单为：秒，最大值900
     */
    public int duration;

    public  long fromUid;

    public  long   toUid;

    public  long   createTime;

    /**
     * 0 待领取 1已领取
     */
    public   int stauts;

    /**
     * 单位秒 ，多长时间内领取有效
     */
    public  int withinTimeValidity;
    public   String action;




    @Override
    public String onGetDisplayString() {
        return "赠送订单";
    }

    @Override
    public void onProcessMessage(V2TIMMessage v2TIMMessage) {
        String json = new String(v2TIMMessage.getCustomElem().getData());
        try {
            JSONObject jsonObject=new JSONObject(json);
            this.type=jsonObject.optString("type");
            this.duration=jsonObject.optInt("duration");
            this.fromUid=jsonObject.optLong("fromUid");
            this.toUid=jsonObject.optLong("toUid");
            this.createTime=jsonObject.optLong("createTime");
            this.stauts=jsonObject.optInt("stauts");
            this.withinTimeValidity=jsonObject.optInt("withinTimeValidity");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}

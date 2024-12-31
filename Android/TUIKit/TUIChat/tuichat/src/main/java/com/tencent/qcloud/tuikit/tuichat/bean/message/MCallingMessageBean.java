package com.tencent.qcloud.tuikit.tuichat.bean.message;

import android.util.Log;

import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tuikit.timcommon.bean.TUIMessageBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * av消息
 */
public class MCallingMessageBean extends TUIMessageBean {


    public int actionType;
    public int callType;
    public String msg;


    @Override
    public String onGetDisplayString() {
        return "提醒消息";
    }

    @Override
    public void onProcessMessage(V2TIMMessage v2TIMMessage) {

        String json = new String(v2TIMMessage.getCustomElem().getData());
        Log.i("MCallingMessageBean", json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.optInt("actionType") == 1.0 || jsonObject.optInt("actionType") == 1) {
                msg = "邀请通话";

                if (jsonObject.optInt("timeout", 0) > 0) {
                  //  msg = "超时未接听";

                }
            }
            if (jsonObject.optInt("actionType") == 2.0 || jsonObject.optInt("actionType") == 2) {
                msg = "取消通话";
            }
            if (jsonObject.optInt("actionType") == 3.0 || jsonObject.optInt("actionType") == 3) {
                msg = "通话结束";
            }
            if (jsonObject.optInt("actionType") == 4.0 || jsonObject.optInt("actionType") == 4) {
                if (isSelf()) {
                    msg = "已拒绝";
                } else {
                    msg = "对方正忙";
                }

            }

            if (jsonObject.optInt("actionType") == 5.0 || jsonObject.optInt("actionType") == 5) {
               if(isSelf()){
                   msg = "超时未接听";

               }else{
                   msg = "对方未响应";

               }
            }


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}

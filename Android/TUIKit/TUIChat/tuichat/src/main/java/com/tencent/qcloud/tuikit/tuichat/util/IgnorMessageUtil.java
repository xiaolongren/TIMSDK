package com.tencent.qcloud.tuikit.tuichat.util;

import com.tencent.imsdk.v2.V2TIMMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class IgnorMessageUtil {

public static     boolean canAddToUi(V2TIMMessage message){
        if(message.getCustomElem()!=null&&message.getCustomElem().getData()!=null&&message.getCustomElem().getData().length>0){
            String json=new String(message.getCustomElem().getData());
            try {
                JSONObject jsonObject=new JSONObject(json);
                if(jsonObject.optInt("businessID")==1){
                    if(jsonObject.optInt("actionType",0)==2||jsonObject.optInt("actionType",0)==5){
                        return true;
                    }
                    return false;
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}

package com.tencent.qcloud.tuikit.tuichat.classicui.page;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.reflect.TypeToken;
//import com.sw.base.core.PublicParmaters;
//import com.sw.base.net.response.Response;
import com.sw.base.core.ListenerVo;
import com.sw.base.core.PublicParmaters;
import com.sw.base.net.response.Response;
import com.tencent.qcloud.tuikit.tuichat.bean.custom.ChatStatusInfo;
import com.tencent.qcloud.tuikit.tuichat.bean.custom.ImOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

public class ImViewModel extends ViewModel {
    public String imId;
    public void  setImId(String imId){
        this.imId=imId;
    }
    public String parseUid(String imId){
        return  imId.replace("huanxin","");
    }

    MutableLiveData<ChatStatusInfo> chatStatusInfoLiveData=new MutableLiveData<>();
    MutableLiveData<ListenerVo> listenerVoLiveData=new MutableLiveData<>();
    MutableLiveData<ImOrder> imOrderMutableLiveData=new MutableLiveData<>(null);
    void getChatStatusInfo(){
        String  loadChatStatusInfoPath = "https://app.xiyouqingsu.com/usergroup/im/commen/checkChatInfo";
         Map<String, Object> paramters = PublicParmaters.getPublicParametes();
         paramters.put("remoteUid",parseUid(imId));
          RxHttp.get(loadChatStatusInfoPath)
                .addAll(paramters)
                .toObservable(new TypeToken<Response<ChatStatusInfo>>(){}.getType()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {

                    Response<ChatStatusInfo> response= ( Response<ChatStatusInfo> )res ;
                    if(response.getErrorCode()==0&&response.getData()!=null){
                        chatStatusInfoLiveData.postValue(response.getData());
                    }else{
                        chatStatusInfoLiveData.postValue(null);

                    }
                }, throwable -> {
                    //Abnormal callback
                    chatStatusInfoLiveData.postValue(null);

                });
    }
    void getListenerInfo(){
        String  loadListenerInfoPath = "https://app.xiyouqingsu.com/usergroup/listener/getlistenerinfo";
         Map<String, Object> paramters = PublicParmaters.getPublicParametes();
         paramters.put("targetUid",parseUid(imId));
          RxHttp.get(loadListenerInfoPath)
                .addAll(paramters)
                .toObservable(new TypeToken<Response<ListenerVo>>(){}.getType()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {

                    Response<ListenerVo> response= ( Response<ListenerVo> )res ;
                    if(response.getErrorCode()==0&&response.getData()!=null){
                        listenerVoLiveData.postValue(response.getData());
                    }else{
                        chatStatusInfoLiveData.postValue(null);

                    }
                }, throwable -> {
                    //Abnormal callback
                    chatStatusInfoLiveData.postValue(null);

                });
    }
    void getUseOrder(){

    }


    /**
     * 保留一位小数并截断，不进行四舍五入。
     *
     * @param value 要格式化的数值
     * @return 保留一位小数后的结果
     */
    public static float truncateToOneDecimalPlace(double value) {
        // 使用 BigDecimal 进行精确计算
        BigDecimal bd = new BigDecimal(Double.toString(value));
        // 设置保留一位小数，并使用 RoundingMode.DOWN 进行截断
        bd = bd.setScale(1, RoundingMode.DOWN);
        // 返回 float 类型的结果
        return bd.floatValue();
    }

}

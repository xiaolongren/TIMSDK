package com.tencent.qcloud.tuikit.tuichat.classicui.page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.gson.reflect.TypeToken;
import com.sw.base.arouterprovider.CallingBottomSheetDialogProvider;
import com.sw.base.core.ArouterPath;
import com.sw.base.core.ListenerVo;
import com.sw.base.entity.UserManager;
import com.sw.base.event.CloseBottomSheetDialogEvent;
import com.sw.base.event.OrderCountDStartEvent;
import com.sw.base.event.OrderEvent;
import com.sw.base.event.TxtChatEvent;
import com.sw.base.net.PublicParameter;
import com.sw.base.net.callback.Error;
import com.sw.base.net.callback.ReqCallback;
import com.sw.base.net.response.Response;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.bean.C2CChatInfo;
import com.tencent.qcloud.tuikit.tuichat.bean.ChatInfo;
import com.tencent.qcloud.tuikit.tuichat.bean.InputMoreItem;
import com.tencent.qcloud.tuikit.tuichat.bean.custom.ChatStatusInfo;
import com.tencent.qcloud.tuikit.tuichat.config.classicui.TUIChatConfigClassic;
import com.tencent.qcloud.tuikit.tuichat.presenter.C2CChatPresenter;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

public class TUIC2CChatActivity extends TUIBaseChatActivity {
    private static final String TAG = TUIC2CChatActivity.class.getSimpleName();

    private TUIC2CChatFragment chatFragment;
    ImViewModel imViewModel;
    CallingBottomSheetDialogProvider provider;
    String noteNick;//对方为咨询师时使用
    boolean fastcall;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imViewModel = new ViewModelProvider(this).get(ImViewModel.class);
        imViewModel.chatStatusInfoLiveData.observe(this, new Observer<ChatStatusInfo>() {
            @Override
            public void onChanged(ChatStatusInfo chatStatusInfo) {
                if (chatStatusInfo == null) {
                    finish();
                } else {
                    setData(chatStatusInfo);
                 }
            }
        });
        imViewModel.listenerVoLiveData.observe(this, new Observer<ListenerVo>() {
            @Override
            public void onChanged(ListenerVo listenerVo) {
                if (listenerVo == null) {
                    //finish();
                } else {
                    setListenerData(listenerVo);
                 }
            }
        });
        EventBus.getDefault().register(this);


        fastcall= getIntent().getBooleanExtra("fastcall",false);
    }


    @Override
    public void initChat(ChatInfo chatInfo) {
        TUIChatLog.i(TAG, "inti chat " + chatInfo);

        if (!(chatInfo instanceof C2CChatInfo)) {
            TUIChatLog.e(TAG, "init C2C chat failed , chatInfo = " + chatInfo);
            ToastUtil.toastShortMessage("init c2c chat failed.");
            return;
        }
        getCustomNoteNick("c2c_"+chatInfo.getId());
        chatFragment = new TUIC2CChatFragment();
        chatFragment.setChatInfo((C2CChatInfo) chatInfo);

        getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, chatFragment).commitAllowingStateLoss();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chatFragment.chatView.getTitleBar().setVisibility(View.GONE);
                imViewModel.setImId(chatFragment.getChatInfo().getId());
                imViewModel.getChatStatusInfo();
                imViewModel.getListenerInfo();

            }
        }, 32);

    }

    @Override
    protected void onDestroy() {
        C2CChatPresenter chatPresenter = null;
        if (chatFragment != null) {
            chatPresenter = chatFragment.getPresenter();
        }
        if (chatPresenter != null) {
            chatPresenter.removeC2CChatEventListener();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    void checkChatinfo() {
        imViewModel.getChatStatusInfo();
    }

    void setData(ChatStatusInfo chatStatusInfo) {

        TextView tvNick=findViewById(R.id.tv_title);
        TextView tvStatus=findViewById(R.id.tv_status);
        tvNick.setText(chatStatusInfo.remoteNick);
        if(!chatStatusInfo.isRemoteListener){
            tvNick.setText(noteNick);
        }
        findViewById(R.id.tv_jubao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(ArouterPath.route_jubao).withLong("targetUid",
                        chatStatusInfo.remoteUid).withInt("type",1).navigation();

             }
        });
        findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              actionselect(chatStatusInfo);
             }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        tvStatus.setText(chatStatusInfo.remoteUserOnlineStatusTitle);
        tvStatus.setBackground(getStatusBg(chatStatusInfo.remoteUserOnlineStatus));
        chatFragment.chatView.getInputLayout().setLeftFreeMsgcount(chatStatusInfo.leftFeeMsgcount);
        chatFragment.chatView.getInputLayout().setInSameOrder(chatStatusInfo.isInSameOrder);
//        TextView tvNumer=findViewById(R.id.tv_numer);
//        Glide.with(imageView).load(chatStatusInfo.rem)
//        tvNumer.setText(chatStatusInfo.);
        if(chatStatusInfo.isInSameOrder&&chatStatusInfo.order!=null&&chatStatusInfo.order.name.contains("文字")){

            EventBus.getDefault().post(new OrderCountDStartEvent(chatStatusInfo.order.id));
            //TODO 通知，开始倒计时
        }
        if(!chatStatusInfo.isRemoteListener){
            chatFragment.getView().findViewById(R.id.lv_order).setVisibility(View.GONE);
            if(chatStatusInfo.remoteUid==436){
                chatFragment.getView().findViewById(R.id.lv_tocall).setVisibility(View.GONE);

            }
            chatFragment.getView().findViewById(R.id.lv_listenerinfo).setVisibility(View.GONE);

        }
        chatFragment.getView().findViewById(R.id.lv_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url= chatFragment.getChatInfo().getFaceUrl();


                ARouter.getInstance().build(ArouterPath.route_listener_placeorder).withString("nick",chatStatusInfo.remoteNick).withLong("targetUid",chatStatusInfo.remoteUid).withString("avatar",url)
                        .withString("jiyu","").navigation();
            }
        });
        chatFragment.getView().findViewById(R.id.lv_tocall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  provider = (CallingBottomSheetDialogProvider) ARouter.getInstance()
                        .build("/listener/bottomsheetdialogprovider")
                        .navigation(TUIC2CChatActivity.this);

                if (provider != null) {
                   String url= chatFragment.getChatInfo().getFaceUrl();
                    provider.showBottomSheetDialog(TUIC2CChatActivity.this,imViewModel.chatStatusInfoLiveData.getValue().remoteUid,imViewModel.chatStatusInfoLiveData.getValue().remoteNick,url,false);
                }
              //  EventBus.getDefault().post(new ShowCallBottomDialogEvent(imViewModel.chatStatusInfoLiveData.getValue().remoteUid));
            }
        });
        chatFragment.getView().findViewById(R.id.lv_freeorder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //领取10分钟免费通话
                showGetFreeOrderNotif();
            }
        });
        if(chatStatusInfo.isListener){
            initChatInputMoreDataSource();
        }
        if(fastcall){
            fastcall=false;
            String url= chatFragment.getChatInfo().getFaceUrl();
            if(provider==null){
                provider = (CallingBottomSheetDialogProvider) ARouter.getInstance()
                        .build("/listener/bottomsheetdialogprovider")
                        .navigation(TUIC2CChatActivity.this);

            }
            provider.showBottomSheetDialog(TUIC2CChatActivity.this,imViewModel.chatStatusInfoLiveData.getValue().remoteUid,imViewModel.chatStatusInfoLiveData.getValue().remoteNick,url,true);
        }

    }

    void setListenerData(ListenerVo listenerVo) {
        ImageView imageView = findViewById(R.id.iv_head);
        TextView tvNumer = findViewById(R.id.tv_numer);
        TextView tvScore = findViewById(R.id.tv_score);
        TextView tvHours = findViewById(R.id.tv_hours);
        TextView tvShenfen = findViewById(R.id.tv_shenfen);
        TextView tvCommentNum = findViewById(R.id.tv_comment_num);
        Glide.with(imageView).load(listenerVo.headUrl).transform(new CircleCrop()).into(imageView);
        tvNumer.setText(listenerVo.openChatUserCount + "");
        tvScore.setText(imViewModel.truncateToOneDecimalPlace(listenerVo.commentScore) + "");
        tvHours.setText("" + imViewModel.truncateToOneDecimalPlace((listenerVo.serviceSeconds / 3600.0) + listenerVo.thirdHours));
        tvShenfen.setText(listenerVo.certificateName);
        tvCommentNum.setText("好评数(" + listenerVo.commentNums + ")");
        tvCommentNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(ArouterPath.route_listener_listenerComment).withLong("targetUid", listenerVo.uid).withInt("count",listenerVo.commentNums).navigation();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(ArouterPath.route_userpage).withLong("targetUid", listenerVo.uid).navigation();

            }
        });
        if(UserManager.getInstance().user.hasGetFreeOrder==0&&listenerVo.newUserFree==1){
            chatFragment.getView().findViewById(R.id.lv_freeorder).setVisibility(View.VISIBLE);

        }
    }



    @Override
    public void onStop() {
        super.onStop();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OrderEvent orderEvent) {
        if (orderEvent.event == OrderEvent.event_start) {
            chatFragment.getChatView().getInputLayout().setInSameOrder(true);
        }
        if (orderEvent.event == OrderEvent.event_stop) {
            chatFragment.getChatView().getInputLayout().setInSameOrder(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTxtChatEvent(TxtChatEvent txtChatEvent) {
        if(TextUtils.isEmpty(txtChatEvent.inputHint)){
            checkChatinfo();
        }

        chatFragment.getChatView().getInputLayout().getInputText().setHint(txtChatEvent.inputHint);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    void onMessageEvent(CloseBottomSheetDialogEvent event) {
         if(provider!=null){

         }
    }

    public Drawable getStatusBg(int status){
        if(status==6){
            return getResources().getDrawable(R.drawable.bg_imstatus_serviceing);
        }
       else if(status==3){
            return getResources().getDrawable(R.drawable.bg_imstatus_resting);
        }
        else  {
            return getResources().getDrawable(R.drawable.bg_imstatus_online);
        }
    }

    public void initChatInputMoreDataSource(){
        // When to call: After initializing the message list interface and before entering it.
        TUIChatConfigClassic.setChatInputMoreDataSource(new TUIChatConfigClassic.ChatInputMoreDataSource() {
            @Override
            public List<InputMoreItem> inputBarShouldAddNewItemToMoreMenuOfInfo(ChatInfo chatInfo) {
                InputMoreItem inputMoreItem = new InputMoreItem() {
                    @Override
                    public void onAction(String chatInfoId, int chatType) {
                        FastMsgBottomSheetDialog.showdialog(getSupportFragmentManager(),imViewModel.imId,chatFragment.getPresenter());
                    }
                };
                inputMoreItem.setName("快捷用语");
               // inputMoreItem.setPriority(10000);
                inputMoreItem.setIconResId(R.drawable.custom_kuaijie);


                InputMoreItem inputMoreItem2 = new InputMoreItem() {
                    @Override
                    public void onAction(String chatInfoId, int chatType) {
                        new  GiveFreeOrderDialog(imViewModel).show(getSupportFragmentManager(),"orderfrr");

                    }
                };
                inputMoreItem2.setName("赠送免费订单");
               // inputMoreItem2.setPriority(8000);
                inputMoreItem2.setIconResId(R.drawable.custom_morder);




                InputMoreItem inputMoreItem3 = new InputMoreItem() {
                    @Override
                    public void onAction(String chatInfoId, int chatType) {

                       new  GiveFreeMsgDialog(imViewModel).show(getSupportFragmentManager(),"gress");
                     }
                };
                inputMoreItem3.setName("赠送免费条数");
                // inputMoreItem2.setPriority(8000);
                inputMoreItem3.setIconResId(R.drawable.custom_freemsg);

                return Arrays.asList(inputMoreItem, inputMoreItem2,inputMoreItem3);
            }
        });
    }
    public   void getCustomNoteNick(String conversationid){
        V2TIMManager.getConversationManager().getConversation(conversationid, new V2TIMValueCallback<V2TIMConversation>() {
            @Override
            public void onSuccess(V2TIMConversation conversationInfo) {
                noteNick=   conversationInfo.getShowName();
                String customdata=conversationInfo.getCustomData();
                if(TextUtils.isEmpty(customdata)){
                     return;
                }
                try {
                    JSONObject jsonObject=new JSONObject(customdata);
                  String  cnoteNick =jsonObject.optString("noteNick");
                    if(!TextUtils.isEmpty(cnoteNick)){
                        noteNick=cnoteNick;

                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }

            @Override
            public void onError(int code, String desc) {

            }
        });
    }

    public void actionselect(ChatStatusInfo chatStatusInfo){
        new AlertDialog.Builder(this).setItems(new CharSequence[]{"举报","拉黑","关注"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(i==0){
                    ARouter.getInstance().build(ArouterPath.route_jubao).withLong("targetUid",
                            chatStatusInfo.remoteUid).withInt("type",1).navigation();

                }
               else if(i==1){

                    setBlack(imViewModel.imId);

                }
               else{
                    follow(imViewModel.imId);
                }

            }
        }).setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }


    public  void follow(String imId){

        //   param.set('fromUid',HttpRequest.getInstance().uid.toString());
        //    param.set('followId',targetUid);
        //    param.set('type',"1");
        //  static String setBlackUserPath =   "usergroup/operation/blackuser/setBlackUser";
        String followPath =   "https://app.xiyouqingsu.com/content/interaction/follow/follow";

        Map<String, Object> paramters = new HashMap<>();;
        paramters.put("fromUid", PublicParameter.getValue(PublicParameter.KEY_UID));
        paramters.put("followId",imViewModel.parseUid(imId));
        paramters.put("type","1");
        RxHttp.get(followPath)
                .addAll(paramters)
                .toObservable(new TypeToken<Response<Integer>>(){}.getType()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {

                    Response<Integer> response= ( Response<Integer> )res ;
                    if(response.getErrorCode()==0&&response.getData()!=null){
                        ToastUtil.show("已关注",true,Gravity.CENTER);
                         //     chatStatusInfoLiveData.postValue(response.getData());
                    }else{
                        // chatStatusInfoLiveData.postValue(null);
                        ToastUtil.show("关注失败",true,Gravity.CENTER);

                    }
                }, throwable -> {
                    ToastUtil.show("关注失败",true,Gravity.CENTER);

                    //Abnormal callback
                    // chatStatusInfoLiveData.postValue(null);

                });
    }

    public  void setBlack(String imId){
        //  static String setBlackUserPath =   "usergroup/operation/blackuser/setBlackUser";
        String setBlackUserPath =   "https://app.xiyouqingsu.com/usergroup/operation/blackuser/setBlackUser";

        Map<String, Object> paramters = new HashMap<>();;
        paramters.put("blackUid",imViewModel.parseUid(imId));
        RxHttp.get(setBlackUserPath)
                .addAll(paramters)
                .toObservable(new TypeToken<Response<Integer>>(){}.getType()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {

                    Response<Integer> response= ( Response<Integer> )res ;
                    if(response.getErrorCode()==0&&response.getData()!=null){
                        ToastUtil.show("已拉黑",true,Gravity.CENTER);
                        finish();
                        //     chatStatusInfoLiveData.postValue(response.getData());
                    }else{
                        // chatStatusInfoLiveData.postValue(null);
                        ToastUtil.show("拉黑失败",true,Gravity.CENTER);

                    }
                }, throwable -> {
                    ToastUtil.show("拉黑失败",true,Gravity.CENTER);

                    //Abnormal callback
                    // chatStatusInfoLiveData.postValue(null);

                });
    }
   public void  showGetFreeOrderNotif(){
        new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("1、新用户首单免费只有一次机会，重新注册账号不会再次获得免费机会。\n 2、建议与咨询师取得联系后再领取免费订单，以免浪费机会。").setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setNegativeButton("领取免费订单", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if(UserManager.getInstance().user.isListener==1){
                    ToastUtil.show("咨询师不能领取",true,Gravity.CENTER);

                    return;
                }
                imViewModel.getPlatformFreeOrder(Long.parseLong(imViewModel.parseUid(imViewModel.imId)), new ReqCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        UserManager.getInstance().user.hasGetFreeOrder=1;
                        ToastUtil.show("已领取",true,Gravity.CENTER);
                        if(!TUIC2CChatActivity.this.isFinishing()){
                            chatFragment.getView().findViewById(R.id.lv_freeorder).setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        ToastUtil.show(error.getMsg(),true,Gravity.CENTER);

                    }
                });
            }
        }).create().show();
    }

}

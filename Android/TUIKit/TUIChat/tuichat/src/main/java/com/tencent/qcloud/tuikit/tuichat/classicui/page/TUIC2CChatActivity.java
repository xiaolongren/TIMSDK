package com.tencent.qcloud.tuikit.tuichat.classicui.page;

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
import com.sw.base.arouterprovider.CallingBottomSheetDialogProvider;
import com.sw.base.core.ArouterPath;
import com.sw.base.core.ListenerVo;
import com.sw.base.event.CloseBottomSheetDialogEvent;
import com.sw.base.event.OrderCountDStartEvent;
import com.sw.base.event.OrderEvent;
import com.sw.base.event.ShowCallBottomDialogEvent;
import com.sw.base.event.TxtChatEvent;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.timcommon.util.TextUtil;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatConstants;
import com.tencent.qcloud.tuikit.tuichat.bean.C2CChatInfo;
import com.tencent.qcloud.tuikit.tuichat.bean.ChatInfo;
import com.tencent.qcloud.tuikit.tuichat.bean.custom.ChatStatusInfo;
import com.tencent.qcloud.tuikit.tuichat.presenter.C2CChatPresenter;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatLog;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TUIC2CChatActivity extends TUIBaseChatActivity {
    private static final String TAG = TUIC2CChatActivity.class.getSimpleName();

    private TUIC2CChatFragment chatFragment;
    ImViewModel imViewModel;
    CallingBottomSheetDialogProvider provider;
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
    }


    @Override
    public void initChat(ChatInfo chatInfo) {
        TUIChatLog.i(TAG, "inti chat " + chatInfo);

        if (!(chatInfo instanceof C2CChatInfo)) {
            TUIChatLog.e(TAG, "init C2C chat failed , chatInfo = " + chatInfo);
            ToastUtil.toastShortMessage("init c2c chat failed.");
            return;
        }

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
        findViewById(R.id.tv_jubao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(ArouterPath.route_jubao).withLong("targetUid",
                        chatStatusInfo.remoteUid).withInt("type",1).navigation();

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
                    provider.showBottomSheetDialog(TUIC2CChatActivity.this,imViewModel.chatStatusInfoLiveData.getValue().remoteUid,imViewModel.chatStatusInfoLiveData.getValue().remoteNick,url);
                }
              //  EventBus.getDefault().post(new ShowCallBottomDialogEvent(imViewModel.chatStatusInfoLiveData.getValue().remoteUid));
            }
        });

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
        tvCommentNum.setText("评价(" + listenerVo.commentNums + ")");
        tvCommentNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(ArouterPath.route_listener_listenerComment).withLong("targetUid", listenerVo.uid).navigation();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(ArouterPath.route_userpage).withLong("targetUid", listenerVo.uid).navigation();

            }
        });
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
            return getResources().getDrawable(R.drawable.bg_status_serviceing);
        }
       else if(status==3){
            return getResources().getDrawable(R.drawable.bg_status_resting);
        }
        else  {
            return getResources().getDrawable(R.drawable.bg_status_online);
        }
    }

}

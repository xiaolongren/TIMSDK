package com.tencent.qcloud.tuikit.tuichat.classicui.widget.message.viewholder;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sw.base.routehandler.RouteHandler;
import com.tencent.qcloud.tuikit.timcommon.bean.TUIMessageBean;
import com.tencent.qcloud.tuikit.timcommon.classicui.widget.message.MessageBaseHolder;
import com.tencent.qcloud.tuikit.timcommon.classicui.widget.message.MessageContentHolder;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.bean.message.MCallingMessageBean;
import com.tencent.qcloud.tuikit.tuichat.bean.message.RemindMessageBean;

public class MCallingMessageHolder extends MessageContentHolder {
    MCallingMessageBean mCallingMessageBean;

    ImageView leftimg;
    ImageView rightimg;
    TextView textView;

    public MCallingMessageHolder(View itemView) {
        super(itemView);
        textView=itemView.findViewById(R.id.msg_body_tv);
        leftimg=itemView.findViewById(R.id.left_icon);
        rightimg=itemView.findViewById(R.id.right_icon);

    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_calling;
    }



    @Override
    public void layoutVariableViews(TUIMessageBean msg, int position) {
        mCallingMessageBean= (MCallingMessageBean) msg;
        textView.setText(mCallingMessageBean.msg+"");
        if(msg.isSelf()){
            leftimg.setVisibility(View.GONE);
            rightimg.setColorFilter(Color.BLACK);
        }else{
            rightimg.setVisibility(View.GONE);
            leftimg.setColorFilter(Color.BLACK);
        }
    }

//    @Override
//    public int getVariableLayout() {
//
//        return R.layout.message_adapter_content_remind;
//    }


}

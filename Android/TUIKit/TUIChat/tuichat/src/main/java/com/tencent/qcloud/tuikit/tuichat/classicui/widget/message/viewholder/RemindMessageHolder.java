package com.tencent.qcloud.tuikit.tuichat.classicui.widget.message.viewholder;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sw.base.routehandler.ActionHandlerManager;
import com.sw.base.routehandler.RouteHandler;
import com.tencent.qcloud.tuikit.timcommon.bean.TUIMessageBean;
import com.tencent.qcloud.tuikit.timcommon.classicui.widget.message.MessageBaseHolder;
import com.tencent.qcloud.tuikit.timcommon.classicui.widget.message.MessageContentHolder;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.bean.message.RemindMessageBean;

public class RemindMessageHolder extends MessageBaseHolder {
    TextView textView;
    public RemindMessageHolder(View itemView) {
        super(itemView);
//        msgContentFrame.setMinimumWidth(2080);
//        FrameLayout.LayoutParams layoutParams= (FrameLayout.LayoutParams) msgContentFrame.getLayoutParams();
//        layoutParams.width=2080;
//        msgContentFrame.setLayoutParams(layoutParams);
//        rightGroupLayout.findViewById(R.id.left_user_icon_view).setVisibility(View.INVISIBLE);
        textView=  itemView.findViewById(R.id.tv_remindMsg);


    }

    @Override
    public void layoutViews(TUIMessageBean msg, int position) {
        super.layoutViews(msg, position);
        RemindMessageBean remindMessageBean= (RemindMessageBean) currentMessageBean;
        String originalText =remindMessageBean.content;

        // 创建 SpannableString

        SpannableString spannableString = new SpannableString(originalText+(remindMessageBean.actionContent==null?"":remindMessageBean.actionContent));

        if(!TextUtils.isEmpty(remindMessageBean.actionContent)){
             // 定义可点击部分的起始和结束位置
            int start = remindMessageBean.content.length(); // "这里" 的起始位置
            int end = start+remindMessageBean.actionContent.length();
            spannableString.setSpan(
                    new ForegroundColorSpan(Color.BLUE),
                    start,
                    end,
                    0
            );// "文本" 的结束位置
            // 设置点击事件
            ClickableSpan clickableSpan =  new ClickableSpan() {

                @Override
                public void onClick(@NonNull View view) {

                    if(!TextUtils.isEmpty(remindMessageBean.action)){
                        RouteHandler.handle(remindMessageBean.action);
                     }
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };

            spannableString.setSpan(clickableSpan, start, end, 0);
        }


        // 设置蓝色
         textView.setText( spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getVariableLayout() {

        return R.layout.message_adapter_content_remind;
    }


}

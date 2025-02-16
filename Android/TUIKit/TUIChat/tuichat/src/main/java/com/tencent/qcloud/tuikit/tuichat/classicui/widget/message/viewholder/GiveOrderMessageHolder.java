package com.tencent.qcloud.tuikit.tuichat.classicui.widget.message.viewholder;

import android.graphics.Color;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.sw.base.routehandler.RouteHandler;
import com.tencent.qcloud.tuikit.timcommon.bean.TUIMessageBean;
import com.tencent.qcloud.tuikit.timcommon.classicui.widget.message.MessageBaseHolder;
import com.tencent.qcloud.tuikit.timcommon.classicui.widget.message.MessageContentHolder;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.bean.message.GiveOrderMessgeBean;
import com.tencent.qcloud.tuikit.tuichat.bean.message.MCallingMessageBean;

public class GiveOrderMessageHolder extends MessageContentHolder {
    GiveOrderMessgeBean giveOrderMessgeBean;

    TextView tvTitle;
    View container;


    public GiveOrderMessageHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.msg_tv_title);
        container = itemView.findViewById(R.id.lv_giveorderlayout);



    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_giveorder;
    }




//    @Override
//    public int getVariableLayout() {
//
//        return R.layout.message_adapter_content_remind;
//    }


    @Override
    public void layoutViews(TUIMessageBean msg, int position) {
        super.layoutViews(msg, position);


    }

    @Override
    public void layoutVariableViews(TUIMessageBean msg, int position) {
        giveOrderMessgeBean = (GiveOrderMessgeBean) msg;
       LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) tvTitle.getLayoutParams();

        tvTitle.setText("赠送您"+(giveOrderMessgeBean.duration/60)+"分钟"+(giveOrderMessgeBean.type.equals("voice")?"语音":"文字")+"时长");


        msgArea.setBackground(tvTitle.getResources().getDrawable(R.drawable.bg_msg_item_giveorder));

        if (msg.isSelf()) {
//            leftimg.setVisibility(View.GONE);
//            rightimg.setColorFilter(Color.BLACK);
        } else {
//            rightimg.setVisibility(View.GONE);
//            leftimg.setColorFilter(Color.BLACK);
        }
        container.setClickable(true);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(giveOrderMessgeBean.action!=null&&giveOrderMessgeBean.action.length()>0){
                    RouteHandler.handle(giveOrderMessgeBean.action);
                }
            }
        });
    }
}

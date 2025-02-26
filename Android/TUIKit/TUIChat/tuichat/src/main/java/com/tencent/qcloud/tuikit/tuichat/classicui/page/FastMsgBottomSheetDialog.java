package com.tencent.qcloud.tuikit.tuichat.classicui.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.UiMessageUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sw.base.uitil.SharedPreferencesUtils;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tuikit.timcommon.bean.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TextMessageBean;
import com.tencent.qcloud.tuikit.tuichat.presenter.C2CChatPresenter;
import com.tencent.qcloud.tuikit.tuichat.util.ChatMessageBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 快捷用语
 */
public class FastMsgBottomSheetDialog extends BottomSheetDialogFragment {
    View contentView;
    String targetImId;
    C2CChatPresenter presenter;

    public FastMsgBottomSheetDialog(String targetImId, C2CChatPresenter presenter) {
        this.targetImId = targetImId;

        this.presenter=presenter;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate( R.layout.dialog_fastmsg, container, false);
        contentView.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getContext(),FastMsgActivity.class);
                startActivity(intent);
                dismiss();
            }
        });
        return  contentView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }





    @Override
    public void onStart() {
        super.onStart();
        //         获取 BottomSheet 的根视图并设置圆角
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);

                behavior.setDraggable(true);

                // 设置圆角背景
                // bottomSheet.setBackgroundColor(Color.WHITE);
                bottomSheet.setBackground(getContext().getDrawable(R.drawable.tencent_bottom_sheet_background));

                // 设置最大高度为屏幕高度的一半
//                behavior.setFitToContents(false);  // 确保 BottomSheet 不会自动调整到全屏
              //  behavior.setMaxWidth(500);  // 设置最大高度为屏幕高度的一半
                behavior.setHalfExpandedRatio(0.5f);
            }
           String[] arrs= loadData();
            if(arrs!=null){
                initViews(arrs);
            }
        }
    }
    public static void showdialog(FragmentManager fragmentManager,String targetImId,C2CChatPresenter presenter) {
        FastMsgBottomSheetDialog fragment = new FastMsgBottomSheetDialog(targetImId,presenter);
         fragment.show(fragmentManager,"targetuid");
    }
    public interface CloseBottomSheetDialog{
        void  mdissmiss();
    }

    public String[]  loadData(){
       String msgs= SharedPreferencesUtils.getInstance().getString("fastmsgs","");
       if(msgs.length()>0){
           String[] arrs=msgs.split(",");

           return  arrs;
       }
       return null;
    }
    void  initViews(String[] msgs){
        LinearLayout msgslayout=contentView.findViewById(R.id.lv_container);
        if(msgs!=null){
            for (String msg : msgs) {
                View view=LayoutInflater.from(getContext()).inflate(R.layout.item_fastmsg,null,false);
               TextView textView= view.findViewById(R.id.tv_content);
               textView.setText(msg);
               msgslayout.addView(view);
               view.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                        TUIMessageBean messageBean=ChatMessageBuilder.buildTextMessage(msg);
                       presenter.sendMessage(messageBean,targetImId,false,false);
                       dismiss();

                   }
               });
            }
        }
    }



}

package com.tencent.qcloud.tuikit.tuichat.classicui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tencent.qcloud.tuikit.tuichat.R;

public class GiveFreeOrderDialog extends   DialogFragment implements View.OnClickListener {
    View contentView;
    int duration=300;
    ImViewModel imViewModel;
    public GiveFreeOrderDialog(ImViewModel imViewModel) {
        this.imViewModel=imViewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundDialog); // 应用样式

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_giveorder, container, false);
        // 设置布局背景（防止某些机型残留默认背景）
        view.setBackgroundResource(R.drawable.white_round_rect_border);
        view.findViewById(R.id.tv_five).setOnClickListener(this);
        view.findViewById(R.id.tv_10).setOnClickListener(this);

        contentView=view;
        view.findViewById(R.id.tv_five).setSelected(true);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imViewModel.givefreeOrder(duration);
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        onSelect(view);
        if(view.getId()==R.id.tv_five){
            duration=300;
        }
        if(view.getId()==R.id.tv_10){
            duration=600;
        }

    }
    void  onSelect(View selectedview){
        contentView.findViewById(R.id.tv_five).setSelected(false);
        contentView.findViewById(R.id.tv_10).setSelected(false);

        selectedview.setSelected(true);
    }
}

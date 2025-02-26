package com.tencent.qcloud.tuikit.tuichat.classicui.page;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sw.base.ui.BaseActivity;
import com.sw.base.uitil.SharedPreferencesUtils;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.tuichat.R;

public class AddFastMsgActivity extends BaseActivity {
    EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfastmsg);
        editText=findViewById(R.id.edt_content);
        setActionbar("添加快捷用语");
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().length()==0){
                    ToastUtil.show("请输入内容",true, Gravity.CENTER);

                }else{
                    add();
                }

            }
        });
    }
    public  void add(){
        String msgs= SharedPreferencesUtils.getInstance().getString("fastmsgs","");

        if(msgs.length()>0){
            msgs=editText.getText().toString()+","+msgs;
        }else{
            msgs=editText.getText().toString();

        }
        SharedPreferencesUtils.getInstance().put("fastmsgs",msgs);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },300);
    }
    public void  setActionbar(String title){
        Toolbar toolbar= findViewById(com.sw.base.R.id.toolbar);
        if(toolbar!=null){
            setSupportActionBar(toolbar);
            if(getSupportActionBar()!=null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(title);
            }

        }
    }
}

package com.tencent.qcloud.tuikit.tuichat.classicui.page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sw.base.core.ArouterPath;
import com.sw.base.ui.BaseActivity;
import com.sw.base.uitil.SharedPreferencesUtils;
import com.tencent.qcloud.tuikit.tuichat.R;

public class FastMsgActivity extends BaseActivity {
    LinearLayout contentView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fastmsg);
        contentView=findViewById(R.id.lv_container);
        setActionbar("快捷用语");



    }

    public String[]  loadData(){
        String msgs= SharedPreferencesUtils.getInstance().getString("fastmsgs","");
        if(msgs.length()>0){
            String[] arrs=msgs.split(",");

            return  arrs;
        }
        return null;
    }
    void  initViews(){
        String[]  msgs=  loadData();
        contentView.removeAllViews();
         if(msgs!=null){

            for (String msg : msgs) {
                View view= LayoutInflater.from(this).inflate(R.layout.item_fastmsg_list,null,false);

                TextView textView= view.findViewById(R.id.tv_content);
                textView.setText(msg);
                contentView.addView(view);
                view.findViewById(R.id.btn_edt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        delete(msg);
                    }
                });
            }
           //  contentView.notifyAll();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       initViews();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fastmsg_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {

            Intent intent=new Intent();
            intent.setClass(this,AddFastMsgActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void delete(String content){
        String[]  msgs=  loadData();
        StringBuffer sb=new StringBuffer();
        for (String msg : msgs) {
            if(msg.equals(content)){
               continue;
            }
            sb.append(msg).append(",");
        }
        if (sb.toString().length()>1)
        sb.deleteCharAt(sb.toString().length()-1);
        SharedPreferencesUtils.getInstance().put("fastmsgs",sb.toString());
        initViews();
    }

}

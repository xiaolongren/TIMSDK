package com.tencent.qcloud.tuikit.tuichat.bean.custom;

public class ImOrder {
  public  long  id;

    // 订单项
    public  String name;


    public long  sellerId;
    public long buyerId;


    //是否已评论
    public long   updateTime;

    //剩余秒数
    public int leftTime;
    public int  totalTime;

    public long createTime;
    public  long payTime;
}

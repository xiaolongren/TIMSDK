package com.tencent.qcloud.tuikit.tuichat.bean.custom;

import java.util.ArrayList;
import java.util.List;

public class ChatStatusInfo {
   public long remoteUid;
    //我方uid

    public long   uid;
    public  boolean  isRemoteListener;
    //我方是否是倾听者
    public  boolean isListener;
    //我方的在线状态
    public int  onlineStatus;
    //我方的在线状态标题
    public String onlineStatusTitle;
    public int  remoteUserOnlineStatus;
    public String remoteUserOnlineStatusTitle;
    //是否在同一个订单
    public  boolean isInSameOrder;
    public long  endTime;
    public  boolean voiceCallOrderEnable;
    public  boolean videoCallOrderEnable;
    public  boolean  isRemoteCustomerService;
    public  boolean  isCustomerService;
   public String remoteNick;
    public int  canShowFree;
    //双方正在使用的同一个订单
    public  ImOrder order;
    public int  leftFeeMsgcount;
    public List<Long> ignorMsgCountUsers;


}

package com.tencent.qcloud.tuikit.tuiconversation.classicui.widget;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sw.base.core.ArouterPath;
import com.tencent.qcloud.tuicore.TUIThemeManager;
import com.tencent.qcloud.tuikit.timcommon.bean.TUIMessageBean;
import com.tencent.qcloud.tuikit.timcommon.component.UnreadCountTextView;
import com.tencent.qcloud.tuikit.timcommon.component.face.FaceManager;
import com.tencent.qcloud.tuikit.timcommon.util.DateTimeUtil;
import com.tencent.qcloud.tuikit.tuiconversation.R;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.bean.DraftInfo;
import com.tencent.qcloud.tuikit.tuiconversation.commonutil.TUIConversationLog;
import com.tencent.qcloud.tuikit.tuiconversation.commonutil.TUIConversationUtils;
import com.tencent.qcloud.tuikit.tuiconversation.config.classicui.TUIConversationConfigClassic;
import com.tencent.qcloud.tuikit.tuiconversation.presenter.ConversationPresenter;

import java.util.Date;
import java.util.HashMap;

public class ConversationCommonHolder extends ConversationBaseHolder {
    public ConversationIconView conversationIconView;
    protected LinearLayout leftItemLayout;
    protected TextView titleText;
    protected TextView messageText;
    protected TextView timelineText;
    protected UnreadCountTextView unreadText;
    protected TextView atAllTv;
    protected TextView atMeTv;
    protected TextView draftTv;
    protected TextView riskTv;
    protected TextView foldGroupNameTv;
    protected TextView foldGroupNameDivider;
    protected ImageView disturbView;
    protected ImageView markBannerView;
    protected CheckBox multiSelectCheckBox;
    protected RelativeLayout messageStatusLayout;
    public ImageView messageSending;
    public ImageView messageFailed;
    private boolean isForwardMode = false;
    protected View userStatusView;
    private boolean showFoldedStyle = true;
    private ConversationInfo currentConversation;

    public ConversationCommonHolder(View itemView) {
        super(itemView);
        leftItemLayout = rootView.findViewById(R.id.item_left);
        conversationIconView = rootView.findViewById(R.id.conversation_icon);
        titleText = rootView.findViewById(R.id.conversation_title);
        messageText = rootView.findViewById(R.id.conversation_last_msg);
        timelineText = rootView.findViewById(R.id.conversation_time);
        unreadText = rootView.findViewById(R.id.conversation_unread);
        foldGroupNameTv = rootView.findViewById(R.id.fold_group_name);
        foldGroupNameDivider = rootView.findViewById(R.id.fold_group_name_divider);
        atAllTv = rootView.findViewById(R.id.conversation_at_all);
        atMeTv = rootView.findViewById(R.id.conversation_at_me);
        draftTv = rootView.findViewById(R.id.conversation_draft);
        riskTv = rootView.findViewById(R.id.conversation_risk);
        disturbView = rootView.findViewById(R.id.not_disturb);
        markBannerView = rootView.findViewById(R.id.mark_banner);
        multiSelectCheckBox = rootView.findViewById(R.id.select_checkbox);
        messageStatusLayout = rootView.findViewById(R.id.message_status_layout);
        messageFailed = itemView.findViewById(R.id.message_status_failed);
        messageSending = itemView.findViewById(R.id.message_status_sending);
        userStatusView = itemView.findViewById(R.id.user_status);
    }

    public void setForwardMode(boolean forwardMode) {
        isForwardMode = forwardMode;
    }

    public void setShowFoldedStyle(boolean showFoldedStyle) {
        this.showFoldedStyle = showFoldedStyle;
    }

    public void layoutViews(ConversationInfo conversation, int position) {
        currentConversation = conversation;

        if (showFoldedStyle && conversation.isMarkFold()) {
            titleText.setText(R.string.folded_group_chat);
            timelineText.setVisibility(View.GONE);
            foldGroupNameTv.setVisibility(View.VISIBLE);
            foldGroupNameDivider.setVisibility(View.VISIBLE);
            foldGroupNameTv.setText(conversation.getTitle());
        } else {
            titleText.setText(conversation.getTitle());
            foldGroupNameTv.setVisibility(View.GONE);
            foldGroupNameDivider.setVisibility(View.GONE);
        }
        messageText.setText("");
        timelineText.setText("");
        setLastMessageAndStatus(conversation);

        if (!mAdapter.hasItemUnreadDot()) {
            unreadText.setVisibility(View.GONE);
        }

        conversationIconView.setShowFoldedStyle(showFoldedStyle);
        conversationIconView.setConversation(conversation);


        if (conversation.isShowDisturbIcon() && !isForwardMode) {
            if (showFoldedStyle && conversation.isMarkFold()) {
                disturbView.setVisibility(View.GONE);
            } else {
                disturbView.setVisibility(View.VISIBLE);
            }
        } else {
            disturbView.setVisibility(View.GONE);
        }

        if (conversation.isMarkStar() && !isForwardMode) {
            if (showFoldedStyle && conversation.isMarkFold()) {
                markBannerView.setVisibility(View.GONE);
            } else {
                markBannerView.setVisibility(View.VISIBLE);
                timelineText.setVisibility(View.GONE);
            }
        } else {
            markBannerView.setVisibility(View.GONE);
            timelineText.setVisibility(View.VISIBLE);
        }

        if (isForwardMode) {
            messageText.setVisibility(View.GONE);
            timelineText.setVisibility(View.GONE);
            unreadText.setVisibility(View.GONE);
            messageStatusLayout.setVisibility(View.GONE);
            messageFailed.setVisibility(View.GONE);
            messageSending.setVisibility(View.GONE);
            atAllTv.setVisibility(View.GONE);
            atMeTv.setVisibility(View.GONE);
            draftTv.setVisibility(View.GONE);
            riskTv.setVisibility(View.GONE);
        }

        if (!conversation.isGroup() && TUIConversationConfigClassic.isShowUserOnlineStatusIcon()) {
            userStatusView.setVisibility(View.VISIBLE);
            if (conversation.getStatusType() == ConversationInfo.USER_STATUS_ONLINE) {
                userStatusView.setBackgroundResource(
                    TUIThemeManager.getAttrResId(rootView.getContext(), com.tencent.qcloud.tuikit.timcommon.R.attr.user_status_online));
            } else {
                userStatusView.setBackgroundResource(
                    TUIThemeManager.getAttrResId(rootView.getContext(), com.tencent.qcloud.tuikit.timcommon.R.attr.user_status_offline));
            }
        } else {
            userStatusView.setVisibility(View.GONE);
        }

        applyCustomConfig();
    }

    private void applyCustomConfig() {
        if (currentConversation.isTop() && !isForwardMode) {
            Drawable pinnedCellBackground = TUIConversationConfigClassic.getPinnedCellBackground();
            if (pinnedCellBackground != null) {
                leftItemLayout.setBackground(pinnedCellBackground);
            } else {
                leftItemLayout.setBackgroundColor(rootView.getResources().getColor(R.color.conversation_item_top_color));
            }
        } else {
            Drawable cellBackground = TUIConversationConfigClassic.getCellBackground();
            if (cellBackground != null) {
                leftItemLayout.setBackground(cellBackground);
            } else {
                leftItemLayout.setBackgroundColor(Color.WHITE);
            }
        }
        if (!TUIConversationConfigClassic.isShowCellUnreadCount()) {
            unreadText.setVisibility(View.GONE);
            disturbView.setVisibility(View.GONE);
        }
        if (!TUIConversationConfigClassic.isShowUserOnlineStatusIcon()) {
            userStatusView.setVisibility(View.GONE);
        }
        if (TUIConversationConfigClassic.getCellTitleLabelFontSize() != TUIConversationConfigClassic.UNDEFINED) {
            titleText.setTextSize(TUIConversationConfigClassic.getCellTitleLabelFontSize());
        }
        if (TUIConversationConfigClassic.getCellSubtitleLabelFontSize() != TUIConversationConfigClassic.UNDEFINED) {
            messageText.setTextSize(TUIConversationConfigClassic.getCellSubtitleLabelFontSize());
        }
        if (TUIConversationConfigClassic.getCellTimeLabelFontSize() != TUIConversationConfigClassic.UNDEFINED) {
            timelineText.setTextSize(TUIConversationConfigClassic.getCellTimeLabelFontSize());
        }
        if (TUIConversationConfigClassic.getAvatarCornerRadius() != TUIConversationConfigClassic.UNDEFINED) {
            conversationIconView.setRadius(TUIConversationConfigClassic.getAvatarCornerRadius());
        }
    }

    private void setLastMessageAndStatus(ConversationInfo conversation) {
        DraftInfo draftInfo = conversation.getDraft();
        String draftText = "";
        if (draftInfo != null) {
            Gson gson = new Gson();
            HashMap draftJsonMap;
            draftText = draftInfo.getDraftText();
            try {
                draftJsonMap = gson.fromJson(draftInfo.getDraftText(), HashMap.class);
                if (draftJsonMap != null) {
                    draftText = (String) draftJsonMap.get("content");
                }
            } catch (JsonSyntaxException e) {
                TUIConversationLog.e("ConversationCommonHolder", " getDraftJsonMap error ");
            }
        }

        atAllTv.setVisibility(View.GONE);
        atMeTv.setVisibility(View.GONE);
        draftTv.setVisibility(View.GONE);
        riskTv.setVisibility(View.GONE);
        if (draftInfo != null) {
            messageText.setText(FaceManager.emojiJudge(draftText));
            timelineText.setText(DateTimeUtil.getTimeFormatText(new Date(draftInfo.getDraftTime() * 1000)));
        } else {
            if (TUIConversationUtils.hasRiskContent(conversation.getLastMessage())) {
                riskTv.setVisibility(View.VISIBLE);
            } else {
                TUIMessageBean lasTUIMessageBean = conversation.getLastTUIMessageBean();
                if (lasTUIMessageBean != null) {
                    String displayString = ConversationPresenter.getMessageDisplayString(lasTUIMessageBean);
                    messageText.setText(Html.fromHtml(displayString));
                    messageText.setTextColor(rootView.getResources().getColor(R.color.list_bottom_text_bg));
                }
                if (conversation.getLastMessage() != null) {
                    timelineText.setText(DateTimeUtil.getTimeFormatText(new Date(conversation.getLastMessageTime() * 1000)));
                }
            }
        }

        if (conversation.isShowDisturbIcon()) {
            if ((showFoldedStyle && conversation.isMarkFold())) {
                if (conversation.isMarkLocalUnread()) {
                    unreadText.setVisibility(View.VISIBLE);
                    unreadText.setText("");
                } else {
                    unreadText.setVisibility(View.GONE);
                }
            } else {
                if (conversation.getUnRead() == 0) {
                    if (conversation.isMarkUnread()) {
                        unreadText.setVisibility(View.VISIBLE);
                        unreadText.setText("");
                    } else {
                        unreadText.setVisibility(View.GONE);
                    }
                } else {
                    unreadText.setVisibility(View.VISIBLE);
                    unreadText.setText("");

                    if (messageText.getText() != null) {
                        String text = messageText.getText().toString();
                        messageText.setText("[" + conversation.getUnRead() + " " + rootView.getContext().getString(R.string.message_num) + "] " + text);
                    }
                }
            }
        } else if (conversation.getUnRead() > 0) {
            unreadText.setVisibility(View.VISIBLE);
            if (conversation.getUnRead() > 99) {
                unreadText.setText("99+");
            } else {
                unreadText.setText("" + conversation.getUnRead());
            }
        } else {
            if (conversation.isMarkUnread()) {
                unreadText.setVisibility(View.VISIBLE);
                unreadText.setText("1");
            } else {
                unreadText.setVisibility(View.GONE);
            }
        }

        if (conversation.getAtType() == ConversationInfo.AT_TYPE_AT_ME) {
            atMeTv.setVisibility(View.VISIBLE);
        } else if (conversation.getAtType() == ConversationInfo.AT_TYPE_AT_ALL) {
            atAllTv.setVisibility(View.VISIBLE);
        } else if (conversation.getAtType() == ConversationInfo.AT_TYPE_AT_ALL_AND_ME) {
            atAllTv.setVisibility(View.VISIBLE);
            atMeTv.setVisibility(View.VISIBLE);
        }

        if (draftInfo != null) {
            draftTv.setVisibility(View.VISIBLE);
            messageStatusLayout.setVisibility(View.GONE);
            messageFailed.setVisibility(View.GONE);
            messageSending.setVisibility(View.GONE);
        } else {
            if (TUIConversationUtils.hasRiskContent(conversation.getLastMessage())) {
                messageFailed.setVisibility(View.VISIBLE);
                messageStatusLayout.setVisibility(View.VISIBLE);
                messageSending.setVisibility(View.GONE);
            } else {
                int status = conversation.getLastMessageStatus();
                if (status == ConversationInfo.LAST_MSG_STATUS_SEND_FAIL) {
                    messageStatusLayout.setVisibility(View.VISIBLE);
                    messageFailed.setVisibility(View.VISIBLE);
                    messageSending.setVisibility(View.GONE);
                } else if (status == ConversationInfo.LAST_MSG_STATUS_SENDING) {
                    messageStatusLayout.setVisibility(View.VISIBLE);
                    messageFailed.setVisibility(View.GONE);
                    messageSending.setVisibility(View.VISIBLE);
                } else {
                    messageStatusLayout.setVisibility(View.GONE);
                    messageFailed.setVisibility(View.GONE);
                    messageSending.setVisibility(View.GONE);
                }
            }
        }
    }
}

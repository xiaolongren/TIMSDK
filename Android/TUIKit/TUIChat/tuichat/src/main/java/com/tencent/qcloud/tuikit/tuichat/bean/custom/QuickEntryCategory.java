package com.tencent.qcloud.tuikit.tuichat.bean.custom;

import java.util.List;

/**
 * 快捷用语分组
 */
public class QuickEntryCategory {
    public String name;
    public List<String> questions;

    public QuickEntryCategory(String name, List<String> questions) {
        this.name = name;
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}

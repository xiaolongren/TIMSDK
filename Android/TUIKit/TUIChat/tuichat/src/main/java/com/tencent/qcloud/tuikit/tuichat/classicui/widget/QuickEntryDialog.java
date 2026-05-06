package com.tencent.qcloud.tuikit.tuichat.classicui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.bean.custom.QuickEntryCategory;

import java.util.ArrayList;
import java.util.List;

public class QuickEntryDialog extends Dialog {

    private QuickEntryCategory category;
    private OnQuestionClickListener listener;

    public QuickEntryDialog(@NonNull Context context, QuickEntryCategory category, OnQuestionClickListener listener) {
        super(context, R.style.QuickEntryDialogTheme);
        this.category = category;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_quick_entry_dialog);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }

        android.widget.TextView tvTitle = findViewById(R.id.tv_dialog_title);
        tvTitle.setText(category.getName());

        RecyclerView rvQuestions = findViewById(R.id.rv_questions);
        rvQuestions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvQuestions.setAdapter(new QuestionAdapter(category.getQuestions()));
    }

    class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.VH> {
        private List<String> questions;

        QuestionAdapter(List<String> questions) {
            this.questions = questions != null ? questions : new ArrayList<>();
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_quick_entry_question, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.bind(questions.get(position));
        }

        @Override
        public int getItemCount() {
            return questions.size();
        }

        class VH extends RecyclerView.ViewHolder {
            android.widget.TextView tvQuestion;

            VH(@NonNull android.view.View itemView) {
                super(itemView);
                tvQuestion = itemView.findViewById(R.id.tv_question);
            }

            void bind(String question) {
                tvQuestion.setText(question);
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onQuestionClick(question);
                    }
                    dismiss();
                });
            }
        }
    }

    public interface OnQuestionClickListener {
        void onQuestionClick(String question);
    }
}

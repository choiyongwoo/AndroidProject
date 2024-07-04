package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HintActivity extends AppCompatActivity {
    private TextView hintText, answerText, progressText;
    private Button showAnswerButton, closeButton;

    private String getAnswerByHintCode(String hintCode) {
        int answerResourceId = getResources().getIdentifier("answer_" + hintCode, "string", getPackageName());
        if (answerResourceId != 0) {
            return getString(answerResourceId);
        } else {
            return "유효하지 않은 힌트 코드입니다.";
        }
    }

    private int getProgressByHintCode(String hintCode) {
        int progressResourceId = getResources().getIdentifier("progress_" + hintCode, "string", getPackageName());
        if (progressResourceId != 0) {
            String progressString = getString(progressResourceId);
            try {
                return Integer.parseInt(progressString);
            } catch (NumberFormatException e) {
                Log.e("HintActivity", "Invalid progress value for hint code: " + hintCode, e);
                return 0;
            }
        } else {
            return 0;  // 유효하지 않은 힌트 코드에 대한 진행도
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);

        hintText = findViewById(R.id.hintText);
        answerText = findViewById(R.id.answerText);
        showAnswerButton = findViewById(R.id.showAnswerButton);
        closeButton = findViewById(R.id.closeButton);
        progressText = findViewById(R.id.progressText);

        String hintCode = getIntent().getStringExtra("hintCode");

        Log.d("HintActivity", "Received hint code: " + hintCode);

        if (hintCode != null && !hintCode.isEmpty()) {
            int hintResourceId = getResources().getIdentifier("hint_" + hintCode, "string", getPackageName());
            if (hintResourceId != 0) {
                String hintString = getString(hintResourceId);
                hintText.setText(hintString);
            } else {
                hintText.setText("유효한 힌트 코드가 아닙니다.");
            }

            // 진행도 설정
            int progress = getProgressByHintCode(hintCode);
            progressText.setText("진행도: " + progress + "%");
        } else {
            hintText.setText("힌트 코드가 제공되지 않았습니다.");
        }

        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerText.getVisibility() == View.GONE) {
                    answerText.setVisibility(View.VISIBLE);
                    showAnswerButton.setText("정답 숨기기");
                    // 정답 텍스트를 동적으로 설정 예시
                    String hintCode = getIntent().getStringExtra("hintCode");
                    String answer = getAnswerByHintCode(hintCode); // 정답 조회 메소드 호출
                    answerText.setText(answer); // TextView에 정답 설정
                } else {
                    answerText.setVisibility(View.GONE);
                    showAnswerButton.setText("정답 보기");
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // 현재 액티비티를 종료
            }
        });
    }
}

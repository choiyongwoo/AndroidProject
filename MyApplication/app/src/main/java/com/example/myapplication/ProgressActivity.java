package com.example.myapplication;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class ProgressActivity extends AppCompatActivity {
    private TextView progressTextView;
    private Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        progressTextView = findViewById(R.id.progressTextView);
        closeButton = findViewById(R.id.closeButton);

        String hintCode = getIntent().getStringExtra("hintCode");

        if (hintCode != null) {
            switch (hintCode) {
                case "1234e":
                    progressTextView.setText("5%");
                    break;
                case "1235e":
                    progressTextView.setText("10%");
                    break;
                default:
                    progressTextView.setText("유효한 힌트 코드가 아닙니다.");
                    break;
            }
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 액티비티 종료
            }
        });
    }
}


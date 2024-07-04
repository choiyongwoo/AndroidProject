package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.Set;
import android.os.Build;
import androidx.activity.OnBackPressedCallback;


public class MainActivity extends AppCompatActivity {
    private Set<String> usedHintCodes = new HashSet<>();
    private TextView timerTextView, hintCountTextView;
    private Button hintButton, startTimerButton;
    private EditText hintCodeEditText;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private int hintCount = 0;
    private boolean timerRunning = false;
    private List<String> validHintCodes = Arrays.asList("EX000","BS051", "BS052", "BS053", "BS054","BS055","BS056","BS021","BS022","BS023","BS024","BS025","BS091","BS092","BS093","BS094","BS071","BS072","BS073","BS074", "DC051","DC052","DC053","DC054","DC055","DC056","DC021","DC022","DC023","DC024","DC091","DC092","DC093","DC094","DC095","LU051","LU052","LU053","LU021","LU022","LU023","LU024","LU025","LU026","LU091","LU092","LU093","LU071","LU072","LU074","VL051","VL052","VL053","VL021","VL022","VL023","VL024","VL025","VL091","VL092","VL093","VL094","VL071","VL072","VL073","VL074","VL075","MW051","MW052","MW053","MW054","MW021","MW022","MW023","MW024","MW091","MW092","MW071","MW072","MW073","MW074","MW075","MW011","MW012","MW013","MW055","MW056","MW057","CL051","CL052","CL053","CL054","CL055","CL056","CL057","CL058","CL021","CL022","CL023","CL024","CL091","CL092","CL093","CL094","CL095","CL096","CL071","CL072","CL073","CL074","CL075","CL076","CL077"); // 유효한 힌트 코드 리스트

    private long backPressedTime;
    private Toast backToast;
    private Handler handler = new Handler();
    private Runnable resetBackPressedTime = new Runnable() {
        @Override
        public void run() {
            backPressedTime = 0;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33 이상
            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    handleBackPress();
                }
            });
        }



        timerTextView = findViewById(R.id.timerTextView);
        hintCountTextView = findViewById(R.id.hintCountTextView);
        hintButton = findViewById(R.id.hintButton);
        startTimerButton = findViewById(R.id.startTimerButton);
        hintCodeEditText = findViewById(R.id.hintCodeEditText);


        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerRunning) {
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    timerRunning = true;
                    startTimerButton.setText("타이머 실행 중");
                }
            }
        });

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hintCode = hintCodeEditText.getText().toString();
                hintCode = hintCode.toUpperCase(); // 입력된 힌트 코드를 대문자로 변환
                if (isValidHintCode(hintCode)) {
                    // 'e'를 포함하고 있는 경우 진행도 액티비티로 이동하고 힌트 사용 횟수를 증가시키지 않음
//                    if (hintCode.toLowerCase().contains("e")) {
//                        Intent progressIntent = new Intent(MainActivity.this, ProgressActivity.class);
//                        progressIntent.putExtra("hintCode", hintCode);
//                        startActivity(progressIntent);
//                    } else { // 'e'가 없는 경우 힌트 액티비티로 이동하고 힌트 사용 횟수를 증가시킴

                    if (!usedHintCodes.contains(hintCode)) {  // 동일한 코드를 중복 사용하지 않았는지 확인
                        hintCount++;
                        hintCountTextView.setText("힌트 사용 횟수: " + hintCount);
                        usedHintCodes.add(hintCode);  // 사용된 힌트 코드 저장

                    }
                    Intent intent = new Intent(MainActivity.this, HintActivity.class);
                    intent.putExtra("hintCode", hintCode);
                    startActivity(intent);
//                    }
                } else {
                    Toast.makeText(MainActivity.this, "유효하지 않은 힌트 코드입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private boolean isValidHintCode(String code) {
        return validHintCodes.contains(code);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            int seconds = (int) (timeInMilliseconds / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
            customHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) { // API 33 미만
            handleBackPress();
        } else {
            super.onBackPressed();
        }
    }
    private void handleBackPress() {
        if (backPressedTime + 1000 > System.currentTimeMillis()) {
            if (backToast != null) backToast.cancel();
            finishAffinity();
        } else {
            backToast = Toast.makeText(getBaseContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);
            backToast.show();
            backPressedTime = System.currentTimeMillis();
            handler.postDelayed(resetBackPressedTime, 2000);
        }
    }
}

package com.example.pr_3;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CombinedActivity extends AppCompatActivity {
    private int score = 0;
    private int totalQuestions = 10;
    private int questionsAttempted = 0;
    private int questionIndex = 0;
    private boolean[] answered = new boolean[totalQuestions];
    private boolean quizStarted = false;

    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private Button submitButton;
    private CountDownTimer timer;

    private String[] questions = {
            "Question 1: What is the capital of France?",
            "Question 2: Which planet is known as the 'Red Planet'?",
            "Question 3: What is the largest mammal on Earth?"
    };

    private String[] correctAnswers = {
            "Paris",
            "Mars",
            "Whale"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combined);

        questionTextView = findViewById(R.id.questionTextView);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        submitButton = findViewById(R.id.submitButton);

        // Initialize the timer (e.g., 30 seconds for each question)
        timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Update the timer display
                ((TextView) findViewById(R.id.timerTextView)).setText("Time left: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                submitQuestion(null);
            }
        };
    }

    public void startQuiz(View view) {
        quizStarted = true;
        nextQuestion();
        timer.start();
    }

    public void nextQuestion() {
        if (questionIndex < totalQuestions) {
            questionTextView.setText(questions[questionIndex]);
            for (int i = 0; i < optionsRadioGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) optionsRadioGroup.getChildAt(i);
                radioButton.setText("Option " + (i + 1));
            }
            submitButton.setEnabled(true);
            timer.start();
        } else {
            endQuiz();
        }
    }

    public void submitQuestion(View view) {
        if (quizStarted && !answered[questionIndex]) {
            answered[questionIndex] = true;
            questionIndex++;
            questionsAttempted++;

            // Check the selected answer
            int selectedOptionId = optionsRadioGroup.getCheckedRadioButtonId();
            if (selectedOptionId != -1) {
                RadioButton selectedOption = findViewById(selectedOptionId);
                String selectedAnswer = selectedOption.getText().toString();
                if (selectedAnswer.equals(correctAnswers[questionIndex - 1])) {
                    score++;
                }
                selectedOption.setEnabled(false); // Prevent editing

                // Check if all questions have been attempted
                if (questionsAttempted == totalQuestions) {
                    endQuiz();
                } else {
                    nextQuestion();
                }
            }
        }
    }

    public void endQuiz() {
        // Stop the timer
        timer.cancel();

        // Calculate the percentage
        double percentage = (double) score / totalQuestions * 100;

        Intent intent = new Intent(this, CombinedActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("totalQuestions", totalQuestions);
        intent.putExtra("percentage", percentage);
        startActivity(intent);
        finish();
    }
}

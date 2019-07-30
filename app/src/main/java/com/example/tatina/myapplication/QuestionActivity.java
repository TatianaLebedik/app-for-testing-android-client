package com.example.tatina.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioButton;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class QuestionActivity extends Activity {

    private static String hostname;
    private static int port;

    private String name;
    private String group;
    private int score = 0;

    private TextView question_textView;
    private RadioGroup answer_radioGroup;
    private RadioButton
                     first_answer_radioButton,
                     second_answer_radioButton,
                     third_answer_radioButton,
                     fourth_answer_radioButton;
    private Button next_question_button;

    private static BufferedReader reader;
    private static PrintWriter writer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        //получение данных с формы входа

        hostname = getIntent().getStringExtra("hostname");
        port = Integer.parseInt(getIntent().getStringExtra("port"));
        name = getIntent().getStringExtra("name");
        group = getIntent().getStringExtra("group");


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        question_textView = findViewById(R.id.question_textView);
        first_answer_radioButton= findViewById(R.id.first_answer_radioButton);
        second_answer_radioButton= findViewById(R.id.second_answer_radioButton);
        third_answer_radioButton= findViewById(R.id.third_answer_radioButton);
        fourth_answer_radioButton= findViewById(R.id.fourth_answer_radioButton);
        next_question_button= findViewById(R.id.next_question_button);
        answer_radioGroup = findViewById(R.id.radioGroup);


        // обработка переключения состояния переключателя
        answer_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(RadioGroup arg0, int id) {
                if(first_answer_radioButton.isChecked() ||
                        second_answer_radioButton.isChecked()||
                            third_answer_radioButton.isChecked()||
                                fourth_answer_radioButton.isChecked()){
                    next_question_button.setEnabled(true);
                }else{
                    next_question_button.setEnabled(false);
                }

            }
        });


        //инициализация сокета
        try  {
            Socket socket = new Socket(hostname,port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            String time = reader.readLine();
            System.out.println(time);
            writer.println(name);
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
                   Intent intent = new Intent(QuestionActivity.this, ServerNotFoundActivity.class);
                 startActivity(intent);
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
                   Intent intent = new Intent(QuestionActivity.this, ServerNotFoundActivity.class);
                 startActivity(intent);
        }
        //инициализация первого вопроса
        getQuestionFromServer();
    }


    void next_question_button_click(View view) {

            int correct_answer = getCorrectAnswerFromServer();
            if (correct_answer == whatRadioButtonChecked()) {
                score++;
            }
            writer.println("next");
            getQuestionFromServer();
    }

    private int getCorrectAnswerFromServer(){
        String correct_answer = "";
        try {
            correct_answer = reader.readLine();//fromBD by id
        }catch (Exception e){
            System.out.println(e);
        }
        return Integer.parseInt(correct_answer);
    }

    //получение нового вопроса с сервера
    private void getQuestionFromServer() {

        answer_radioGroup.clearCheck();

        String question = "";
        String answer1 = "";
        String answer2 = "";
        String answer3 = "";
        String answer4 = "";

        try {

            question = reader.readLine();

            //если вопросы закончились, отправить на сервер результат
            if (question.equals("stop")) {
                writer.println(name);
                writer.println(group);
                writer.println(Integer.toString(score));

                Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
                intent.putExtra("total_score", Integer.toString(score));
                startActivity(intent);
                return;
            }
            answer1 = reader.readLine();
            answer2 = reader.readLine();
            answer3 = reader.readLine();
            answer4 = reader.readLine();

        }catch (Exception e){
            System.out.println(e);
        }

        question_textView.setText(question);
        first_answer_radioButton.setText(answer1);
        second_answer_radioButton.setText(answer2);
        third_answer_radioButton.setText(answer3);
        fourth_answer_radioButton.setText(answer4);

    }

    //какой ответ выбран
    private int whatRadioButtonChecked() {

        if (first_answer_radioButton.isChecked()) {
            return 1;
        }
        if (second_answer_radioButton.isChecked()) {
            return 2;
        }
        if (third_answer_radioButton.isChecked()) {
            return 3;
        }
        if (fourth_answer_radioButton.isChecked()) {
            return 4;
        } else {
            return 0;
        }

    }

}


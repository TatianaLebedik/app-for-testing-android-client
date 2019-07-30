package com.example.tatina.myapplication;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import android.content.Intent;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static String hostname = "10.0.2.2";
    private static String port = "53200";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    //Инициализация и переход к тесту
    public void button_click(View view){

            EditText name_textEdit = findViewById(R.id.name_editText);
            EditText group_textEdit = findViewById(R.id.group_editText);

            Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
            intent.putExtra("hostname", hostname);
            intent.putExtra("port", port);
            intent.putExtra("name", name_textEdit.getText().toString());
            intent.putExtra("group", group_textEdit.getText().toString());
            startActivity(intent);

    }

}


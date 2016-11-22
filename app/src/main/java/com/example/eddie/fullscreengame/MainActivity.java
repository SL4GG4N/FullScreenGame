package com.example.eddie.fullscreengame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button invisibleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //knappen är osynlig pga. i viewXML:en är alpha = 0
        invisibleButton = (Button) findViewById(R.id.testInvisibleButton);
        invisibleButton.setVisibility(View.VISIBLE);
        invisibleButton.setOnClickListener(new SayHelloListener());
    }

    private class SayHelloListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            toastText("HELLOOOO");
        }
    }

    private void toastText(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}

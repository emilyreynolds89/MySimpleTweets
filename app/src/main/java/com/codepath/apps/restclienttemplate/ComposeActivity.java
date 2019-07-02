package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ComposeActivity extends AppCompatActivity {

    EditText etTweet;
    Button btnTweet;
    TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // initialize and retrieve value of text in EditText
        etTweet = findViewById(R.id.etTweet);
        String tweetBody = etTweet.getText().toString();

        btnTweet = findViewById(R.id.btnTweet);
        tvCount = findViewById(R.id.tvCount);

    }

    public void submitTweet(View view) {
        // prepare data intent
        Intent data = new Intent();

        // pass relevant data back as a result
        data.putExtra("tweet", etTweet.getText().toString());

        // return the data
        // set result code and bundle data for response
        setResult(RESULT_OK, data);

        // closes the activity, passing data to parent
        finish();

    }

}

package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    EditText etTweet;
    Button btnTweet;
    TextView tvCount;

    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // initialize client
        client = new AsyncHttpClient();

        // initialize and retrieve value of text in EditText
        etTweet = findViewById(R.id.etTweet);
        String tweetBody = etTweet.getText().toString();

        btnTweet = findViewById(R.id.btnTweet);
        tvCount = findViewById(R.id.tvCount);
        tvCount.setText("0/280");

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = etTweet.length();
                String convert = String.valueOf(length);
                tvCount.setText(convert+"/280");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public void submitTweet(View view) {
        TwitterClient client = new TwitterClient(this);

        client.sendTweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // prepare data intent
                Intent data = new Intent();

                // initialize TwitterClient & tweet
                Tweet tweet;

                try {
                    tweet = Tweet.fromJSON(response);

                    // pass relevant data back as a result
                    data.putExtra("tweet", tweet);

                    // return the data
                    // set result code and bundle data for response
                    setResult(RESULT_OK, data);

                    // closes the activity, passing data to parent
                    finish();
                    Log.i("ComposeActivity", "Composed tweet");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("ComposeActivity", "Error in composing tweet");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("ComposeActivity", "Error in composing tweet");
                Toast.makeText(ComposeActivity.this, errorResponse.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("ComposeActivity", "Error in composing tweet");
            }
        });


    }

}

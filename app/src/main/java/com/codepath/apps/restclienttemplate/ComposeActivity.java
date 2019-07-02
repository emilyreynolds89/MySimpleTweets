package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

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

    }

    public void submitTweet(View view) {
        // prepare data intent
        Intent data = new Intent();

        // initialize TwitterClient & tweet
        TwitterClient client = new TwitterClient(this);
        final Tweet[] tweet = {new Tweet()};

        client.sendTweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    tweet[0] = Tweet.fromJSON(response);
                    Log.i("ComposeActivity", "Composed tweet");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("ComposeActivity", "Error in composing tweet");
            }
        });

        // pass relevant data back as a result
        data.putExtra("tweet", tweet[0]);

        // return the data
        // set result code and bundle data for response
        setResult(RESULT_OK, data);

        // closes the activity, passing data to parent
        finish();

    }

}

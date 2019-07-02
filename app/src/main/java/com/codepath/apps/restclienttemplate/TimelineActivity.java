package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    // declaring reference to Twitter client
    private TwitterClient client;

    // declaring references for adapter
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;

    Context context;

    private final int REQUEST_CODE = 20; // for intent for onClick

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        context = this;

        // find the RecyclerView
        rvTweets = findViewById(R.id.rvTweet);

        // init the array list - data source
        tweets = new ArrayList<>();

        // construct the adapter from this data source
        tweetAdapter = new TweetAdapter(tweets);

        // RecyclerView setup - layout manager, use adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        rvTweets.setAdapter(tweetAdapter);

        populateTimeline();
    }

    private void populateTimeline() {
        // make network request to get data from Twitter API
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Log.d("TwitterClient", response.toString());

                // iterate through the JSON array, for each entry, deserialize the JSON object
                for (int i = 0; i < response.length(); i++) {
                    try {
                        // convert each object to a Tweet model
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));

                        // add that Tweet model to our data source
                        tweets.add(tweet);

                        // notify the adapter that we've added an item
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu, adding items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    // handles click action for menu item to compose tweet
    public void onComposeAction(MenuItem mi) {
        // create intent for the new activity
        Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
        intent.putExtra("mode", 2);
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // extract tweet value from result extras
            String tweet = data.getExtras().getString("tweet");

            // Toast the tweet to display temporarily on screen
            Toast.makeText(this, tweet, Toast.LENGTH_LONG).show();


            client.sendTweet(tweet, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Tweet tweet = Tweet.fromJSON(response);

                    } catch (JSONException e) {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }
            });
        }
    }

}

package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailTweetActivity extends AppCompatActivity implements View.OnClickListener {

    Tweet tweet;

    ImageView ivProfileImage;
    TextView tvUserName;
    TextView tvBody;
    TextView tvRelativeTimestamp;
    TextView screenName;
    ImageButton ibReply;
    ImageButton ibFavorite;
    ImageButton ibRetweet;
    TextView tvFavoriteCount;
    TextView tvRetweetCount;

    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);

        getSupportActionBar().setTitle("Twitter");

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvUserName = findViewById(R.id.tvUserName);
        tvBody = findViewById(R.id.tvBody);
        tvRelativeTimestamp = findViewById(R.id.tvRelativeTimestamp);
        screenName = findViewById(R.id.tvScreenName);
        ibReply = findViewById(R.id.ibReply);
        ibRetweet = findViewById(R.id.ibRetweet);
        tvRetweetCount = findViewById(R.id.tvRetweetCount);
        ibFavorite = findViewById(R.id.ibFavorite);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);

        tweet = (Tweet) getIntent().getSerializableExtra("tweet");

        tvUserName.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvRelativeTimestamp.setText(tweet.getRelativeTimeAgo(tweet.createdAt));
        screenName.setText(tweet.user.screenName);
        tvRetweetCount.setText(Integer.toString(tweet.retweetCount));
        tvFavoriteCount.setText(Integer.toString(tweet.favoriteCount));

        Glide.with(getBaseContext()).load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(getBaseContext(), 5, 0))
                .error(R.drawable.ic_launcher)
                .into(ivProfileImage);

        if (tweet.favoriteClicked == true) {
            ibFavorite.setBackgroundResource(R.drawable.ic_vector_heart);
        } else {
            ibFavorite.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
        }

        if (tweet.retweetClicked == true) {
            ibRetweet.setBackgroundResource(R.drawable.ic_vector_heart);
        } else {
            ibRetweet.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
        }

        ibReply.setOnClickListener(this);
        ibRetweet.setOnClickListener(this);
        ibFavorite.setOnClickListener(this);

        client = new AsyncHttpClient();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibReply:
                replyTweet(v);
                break;
            case R.id.ibFavorite:
                if (tweet.favoriteClicked == false) {
                    tweet.favoriteCount += 1;
                    tvFavoriteCount.setText(Integer.toString(tweet.favoriteCount));
                    ibFavorite.setBackgroundResource(R.drawable.ic_vector_heart);
                } else {
                    tweet.favoriteCount -= 1;
                    tvFavoriteCount.setText(Integer.toString(tweet.favoriteCount));
                    ibFavorite.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
                }
                tweet.favoriteClicked = toggleButton(tweet.favoriteClicked);
                break;

            case R.id.ibRetweet:
                if (tweet.retweetClicked == false) {
                    tweet.retweetCount += 1;
                    tvRetweetCount.setText(Integer.toString(tweet.retweetCount));
                    ibRetweet.setBackgroundResource(R.drawable.ic_vector_retweet);
                } else {
                    tweet.retweetCount -= 1;
                    tvRetweetCount.setText(Integer.toString(tweet.retweetCount));
                    ibRetweet.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
                }
                tweet.retweetClicked = toggleButton(tweet.retweetClicked);

                TwitterClient client = new TwitterClient(v.getContext());
                client.retweet(Long.toString(tweet.uid), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i("TweetAdapter", "Retweet complete");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("TweetAdapter", "Error in retweeting");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("TweetAdapter", "Error in retweeting");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.e("TweetAdapter", "Error in retweeting");

                    }
                });

                break;
        }
    }

    public void replyTweet(View view) {
        String replyName = "@";
        if (screenName != null) {
            replyName += (String) screenName.getText();
        }
        Intent intent = new Intent(view.getContext(), ComposeActivity.class);
        intent.putExtra("ScreenName", replyName);
        view.getContext().startActivity(intent);
    }

    public boolean toggleButton(boolean buttonOn) {
        return !buttonOn;
    }
}

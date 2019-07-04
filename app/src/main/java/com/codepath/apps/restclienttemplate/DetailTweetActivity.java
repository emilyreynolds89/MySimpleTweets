package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailTweetActivity extends AppCompatActivity implements View.OnClickListener {

    Tweet tweet;

    ImageView ivProfileImage;
    TextView tvUserName;
    TextView tvBody;
    TextView tvRelativeTimestamp;
    TextView screenName;
    ImageButton ibReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvUserName = findViewById(R.id.tvUserName);
        tvBody = findViewById(R.id.tvBody);
        tvRelativeTimestamp = findViewById(R.id.tvRelativeTimestamp);
        screenName = findViewById(R.id.tvScreenName);
        ibReply = findViewById(R.id.ibReply);

        tweet = (Tweet) getIntent().getSerializableExtra("tweet");

        tvUserName.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvRelativeTimestamp.setText(tweet.getRelativeTimeAgo(tweet.createdAt));
        screenName.setText(tweet.user.screenName);

        Glide.with(getBaseContext()).load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(getBaseContext(), 5, 0))
                .error(R.drawable.ic_launcher)
                .into(ivProfileImage);

        ibReply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        replyTweet(v);
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


}

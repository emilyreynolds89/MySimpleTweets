package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.User;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {

    User user;

    ImageView ivProfileImage;
    TextView tvUserName;
    TextView screenName;
    TextView tvFollowers;
    TextView tvFollowing;
    TextView tvFollowersCount;
    TextView tvFollowingCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Twitter");

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvUserName = findViewById(R.id.tvUserName);
        screenName = findViewById(R.id.tvScreenName);
        tvFollowers = findViewById(R.id.tvFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);
        tvFollowersCount = findViewById(R.id.tvFollowersCount);
        tvFollowingCount = findViewById(R.id.tvFollowingCount);

        user = (User) getIntent().getSerializableExtra("user");

        tvUserName.setText(user.name);
        screenName.setText("@" + user.screenName);
        tvFollowers.setText("Followers");
        tvFollowing.setText("Following");
        tvFollowersCount.setText(Integer.toString(user.followersCount));
        tvFollowingCount.setText(Integer.toString(user.friendsCount));

        Glide.with(getBaseContext()).load(user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(getBaseContext(), 5, 0))
                .error(R.drawable.ic_launcher)
                .into(ivProfileImage);

    }
}

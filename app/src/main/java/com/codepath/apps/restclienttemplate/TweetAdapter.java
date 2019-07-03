package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mtweets;
    static Context context;

    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets) {
        mtweets = tweets;
    }

    // for each row, inflate the layout and cache reference into ViewHolder
    // only invoked when needing to create a new row
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate the tweet row
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data according to the position
        Tweet tweet = mtweets.get(position);

        // populate the views according to this data
        holder.tvUserName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvRelativeTimestamp.setText(tweet.getRelativeTimeAgo(tweet.createdAt));
        holder.screenName.setText("@"+tweet.user.screenName);

        Glide.with(context).load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 5, 0))
                .error(R.drawable.ic_launcher)
                .into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return mtweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvRelativeTimestamp;
        public TextView screenName;
        public ImageButton ibReply;


        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvRelativeTimestamp = itemView.findViewById(R.id.tvRelativeTimestamp);
            screenName = itemView.findViewById(R.id.tvScreenName);

            ibReply = itemView.findViewById(R.id.ibReply);
            ibReply.setOnClickListener(this);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ibReply:
                    replyTweet(v.getRootView());
                case R.id.tweet_item:
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        Tweet tweet = mtweets.get(position);

                        Intent intent = new Intent(context, DetailTweetActivity.class);
                        intent.putExtra("tweet", tweet);

                        context.startActivity(intent);
                    }
            }
        }

    }

    // helper methods for refreshing timeline feature

    public void clear() {
        mtweets.clear();
        notifyDataSetChanged();

    }

    public void addAll(List<Tweet> tweets) {
        mtweets.addAll(tweets);
        notifyDataSetChanged();
    }

    public static void replyTweet(View view) {
        TextView tvScreenName = view.findViewById(R.id.tvScreenName);
        String screenName = (String) tvScreenName.getText();
        Intent intent = new Intent(view.getContext(), ComposeActivity.class);
        intent.putExtra("ScreenName", screenName);
        context.startActivity(intent);
    }

}

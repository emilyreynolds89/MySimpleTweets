package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mtweets;
    static Context context;

    AsyncHttpClient client;

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

        client = new AsyncHttpClient();

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
        holder.tvFavoriteCount.setText(Integer.toString(tweet.favoriteCount));
        holder.tvRetweetCount.setText(Integer.toString(tweet.retweetCount));

        Glide.with(context).load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 5, 0))
                .error(R.drawable.ic_launcher)
                .into(holder.ivProfileImage);

        if (tweet.imageUrl != null) {
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(tweet.imageUrl)
                    .error(R.drawable.ic_launcher)
                    .into(holder.imageView);
        }
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
        public ImageView imageView;
        public ImageButton ibReply;
        public ImageButton ibFavorite;
        public ImageButton ibRetweet;
        public TextView tvFavoriteCount;
        public TextView tvRetweetCount;


        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvRelativeTimestamp = itemView.findViewById(R.id.tvRelativeTimestamp);
            imageView = itemView.findViewById(R.id.imageView);

            screenName = itemView.findViewById(R.id.btnScreenName);
            screenName.setOnClickListener(this);

            ibReply = itemView.findViewById(R.id.ibReply);
            ibReply.setOnClickListener(this);

            ibFavorite = itemView.findViewById(R.id.ibFavorite);
            tvFavoriteCount = itemView.findViewById(R.id.tvFavoriteCount);
            ibFavorite.setOnClickListener(this);

            ibRetweet = itemView.findViewById(R.id.ibRetweet);
            tvRetweetCount = itemView.findViewById(R.id.tvRetweetCount);
            ibRetweet.setOnClickListener(this);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            switch (v.getId()) {
                case R.id.ibReply:
                    replyTweet(v.getRootView());
                    break;

                case R.id.ibFavorite:
                    if (position != RecyclerView.NO_POSITION) {
                        Tweet tweet = mtweets.get(position);
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
                    }
                    break;

                case R.id.ibRetweet:
                    if (position != RecyclerView.NO_POSITION) {
                        Tweet tweet = mtweets.get(position);
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
                                Tweet tweet;

                                try {
                                    tweet = Tweet.fromJSON(response);

                                    //fetchTimelineAsync(true);

                                    Log.i("TweetAdapter", "Retweet complete");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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

                    }
                    break;
                case R.id.btnScreenName:
                    if (position != RecyclerView.NO_POSITION) {
                        Tweet tweet = mtweets.get(position);

                        Intent profileIntent = new Intent(context, ProfileActivity.class);
                        profileIntent.putExtra("user", tweet.user);

                        context.startActivity(profileIntent);
                    }
                    break;
                case R.id.tweet_item:
                    if (position != RecyclerView.NO_POSITION) {
                        Tweet tweet = mtweets.get(position);

                        Intent detailIntent = new Intent(context, DetailTweetActivity.class);
                        detailIntent.putExtra("tweet", tweet);

                        context.startActivity(detailIntent);
                    }
                    break;
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
        Button btnScreenName = view.findViewById(R.id.btnScreenName);
        String screenName = (String) btnScreenName.getText();
        Intent intent = new Intent(view.getContext(), ComposeActivity.class);
        intent.putExtra("ScreenName", screenName);
        context.startActivity(intent);
    }

    public boolean toggleButton(boolean buttonOn) {
        return !buttonOn;
    }

}

package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Tweet implements Serializable {

    // defining attributes
    public String body;
    public long uid; // database ID for the tweet
    public String createdAt;
    public User user;
    public Integer retweetCount;
    public Integer favoriteCount;
    public String imageUrl;
    public boolean retweetClicked;
    public boolean favoriteClicked;

    // deserialize the JSON - take in a JSON object and return a Tweet object
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.retweetCount = jsonObject.getInt("retweet_count");
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.retweetClicked = jsonObject.getBoolean("retweeted");
        tweet.favoriteClicked = jsonObject.getBoolean("favorited");

        JSONObject entity = jsonObject.getJSONObject("entities");
        JSONArray media = entity.getJSONArray("media");
        if (media.length() != 0) {
            JSONObject firstMedia = media.getJSONObject(0);
            tweet.imageUrl = firstMedia.getString("media_url_https");
        }

        return tweet;

    }

    // parses raw JSON date, returning relative timestamp
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        relativeDate = relativeDate.replaceAll(" minute ago", "m");
        relativeDate = relativeDate.replaceAll(" minutes ago", "m");

        relativeDate = relativeDate.replaceAll(" hour ago", "h");
        relativeDate = relativeDate.replaceAll(" hours ago", "h");

        return relativeDate;
    }

}

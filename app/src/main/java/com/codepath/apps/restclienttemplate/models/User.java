package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {

    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public int followersCount;
    public int friendsCount;

    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user = new User();

        // extract and fill the values
        user.name = jsonObject.getString("name");
        user.uid = jsonObject.getLong("id");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        user.followersCount = jsonObject.getInt("followers_count");
        user.friendsCount = jsonObject.getInt("friends_count");

        return user;

    }

}

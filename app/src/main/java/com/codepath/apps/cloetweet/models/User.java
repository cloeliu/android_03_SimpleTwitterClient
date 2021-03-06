package com.codepath.apps.cloetweet.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cloeliu on 2015/8/8.
 */
public class User {
    private long uid;
    private String name;
    private String screenName;
    private String profileImageUrl;
    private String tagline;
    private int followers;
    private int following;

    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTagline() { return tagline; }

    public int getFollowersCount() { return followers; }

    public int getFollowingCount() { return following; }

    public static User fromJSON(JSONObject json) {
        User u = new User();
        try {
            u.uid = json.getLong("id");
            u.name = json.getString("name");
            u.screenName = json.getString("screen_name");
            u.tagline = json.getString("description");
            u.followers = json.getInt("followers_count");
            u.following = json.getInt("friends_count");
            u.profileImageUrl = json.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }
}

package com.codepath.apps.cloetweet.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.cloetweet.TwitterApp;
import com.codepath.apps.cloetweet.TwitterClient;
import com.codepath.apps.cloetweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by cloeliu on 2015/8/19.
 */
public class UserTimelineFragment extends TweetsListFragment {

    private TwitterClient client; // singleton client
    private String screenName = "";
    private final int count = 25;
    private long maxId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
        //setTweets();
        populateTimeline(this.maxId, this.count);


    }

    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    /* create request to get timeline from api  */
    @Override
    public void populateTimeline(long maxId, int count) {
        screenName = getArguments().getString("screen_name");
        client.getUserTimeline(maxId, count, screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());
                // get tweets
                addAll(Tweet.fromJSONArray(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}

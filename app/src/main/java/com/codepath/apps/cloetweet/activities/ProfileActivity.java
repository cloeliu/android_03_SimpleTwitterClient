package com.codepath.apps.cloetweet.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.cloetweet.R;
import com.codepath.apps.cloetweet.TwitterApp;
import com.codepath.apps.cloetweet.TwitterClient;
import com.codepath.apps.cloetweet.fragments.UserTimelineFragment;
import com.codepath.apps.cloetweet.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {

    private TwitterClient client;
    private User user;
    private String screenName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApp.getRestClient();
        // get screen_name
        screenName = getIntent().getStringExtra("screen_name");

        // get user info
        if(screenName == null || screenName.equals("")) {
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    // populate header
                    populateUserProfile(user);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("DEBUG", errorResponse.toString());
                }
            });
        } else {
            client.getUserLookup(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    JSONObject resObj = null;
                    try {
                        resObj = response.getJSONObject(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    user = User.fromJSON(resObj);
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    // populate header
                    populateUserProfile(user);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("DEBUG", errorResponse.toString());
                }
            });
        }

        if(savedInstanceState == null) {
            // create user timeline frag.
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            // display user frag. dynamically
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flUserTimeline, fragmentUserTimeline);
            ft.commit();
        }
    }

    private void populateUserProfile(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView imProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        Picasso.with(this).load(user.getProfileImageUrl()).into(imProfileImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

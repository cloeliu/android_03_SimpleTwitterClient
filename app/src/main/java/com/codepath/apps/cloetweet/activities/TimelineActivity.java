package com.codepath.apps.cloetweet.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.cloetweet.R;
import com.codepath.apps.cloetweet.TwitterApp;
import com.codepath.apps.cloetweet.TwitterClient;
import com.codepath.apps.cloetweet.adapters.TweetsArrayAdapter;
import com.codepath.apps.cloetweet.fragments.PostDialog;
import com.codepath.apps.cloetweet.listeners.EndlessScrollListener;
import com.codepath.apps.cloetweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity implements PostDialog.PostDialogListener {

    private TwitterClient client; // singleton client
    private ListView lvTimeline;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;

    private final int count = 25;
    private long maxId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApp.getRestClient();
        setTweets();
        populateTimeline(this.maxId, this.count);

        // setup listener
        lvTimeline.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });
    }

    private void showPostDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PostDialog postDialog = PostDialog.newInstance("Some Title");
        postDialog.show(fm, "fragment_post_dialog");
    }

    private void customLoadMoreDataFromApi(int page) {
        if(tweets.size() >= 1) {
            maxId = tweets.get(tweets.size() - 1).getUid() - 1;
        }
        Log.d("DEBUG", "maxId= " + String.valueOf(maxId));
        populateTimeline(maxId, count);
    }

    /* create request to get timeline from api  */
    private void populateTimeline(long maxId, int count) {
        client.getHomeTimeline(maxId, count, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());
                // get tweets
                tweets = Tweet.fromJSONArray(json);
                aTweets.addAll(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    private void setTweets() {
        lvTimeline = (ListView) findViewById(R.id.lvTimeline);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTimeline.setAdapter(aTweets);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
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

    @Override
    public void onFinishPostDialog(String body) {
        Toast.makeText(this, body, Toast.LENGTH_SHORT);
        // post api
        client.postStatusUpdate(body, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                //super.onSuccess(statusCode, headers, res);
                maxId = 0;
                aTweets.clear();
                populateTimeline(maxId, count);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getBaseContext(), "a", Toast.LENGTH_SHORT);
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    public void onPostClick(MenuItem item) {
        showPostDialog();
    }
}

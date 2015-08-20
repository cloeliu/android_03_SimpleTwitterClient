package com.codepath.apps.cloetweet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.cloetweet.R;
import com.codepath.apps.cloetweet.TwitterApp;
import com.codepath.apps.cloetweet.TwitterClient;
import com.codepath.apps.cloetweet.fragments.HomeTimelineFragment;
import com.codepath.apps.cloetweet.fragments.MentionsTimelineFragment;
import com.codepath.apps.cloetweet.fragments.PostDialog;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;


public class TimelineActivity extends ActionBarActivity implements PostDialog.PostDialogListener {

    private TwitterClient client; // singleton client
    private ViewPager vpPager;
    private TweetsPagerAdapter aPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        vpPager = (ViewPager) findViewById(R.id.viewpager);
        aPager = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(aPager);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

        client = TwitterApp.getRestClient();
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

    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    private void showPostDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PostDialog postDialog = PostDialog.newInstance("Some Title");
        postDialog.show(fm, "fragment_post_dialog");
    }
    /*
    private void customLoadMoreDataFromApi(int page) {
        if(tweets.size() >= 1) {
            maxId = tweets.get(tweets.size() - 1).getUid() - 1;
        }
        Log.d("DEBUG", "maxId= " + String.valueOf(maxId));
        populateTimeline(maxId, count);
    }
    */

    @Override
    public void onFinishPostDialog(String body) {
        //Toast.makeText(this, body, Toast.LENGTH_SHORT);
        // post api
        client.postStatusUpdate(body, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                //super.onSuccess(statusCode, headers, res);
                vpPager.setCurrentItem(0);
                aPager.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getBaseContext(), "Post failed ;(", Toast.LENGTH_SHORT);
                Log.d("DEBUG", errorResponse.toString());
            }
        });

    }

    public void onPostClick(MenuItem item) {
        showPostDialog();
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private  String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            //HomeTimelineFragment f = (HomeTimelineFragment) object;
            //if (f != null) {
            //    f.populateTimeline(0, 25);
            //}
            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}

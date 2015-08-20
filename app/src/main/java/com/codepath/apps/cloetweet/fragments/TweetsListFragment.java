package com.codepath.apps.cloetweet.fragments;

//import android.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.cloetweet.R;
import com.codepath.apps.cloetweet.activities.ProfileActivity;
import com.codepath.apps.cloetweet.adapters.TweetsArrayAdapter;
import com.codepath.apps.cloetweet.listeners.EndlessScrollListener;
import com.codepath.apps.cloetweet.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetsListFragment extends Fragment {

    //private TwitterClient client; // singleton client
    private ListView lvTweets;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;

    private final int count = 25;
    private long maxId = 0;

    // inflation
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        // find the listview:
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        // conn adapter to list view
        lvTweets.setAdapter(aTweets);

        // setup listener
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                Tweet thisTweet = tweets.get(pos);
                i.putExtra("screen_name", thisTweet.getUser().getScreenName());
                startActivity(i);
            }
        });

        return v;
    }

    // lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void populateTimeline(long maxId, int count) {}

    private void customLoadMoreDataFromApi(int page) {
        if(tweets.size() >= 1) {
            maxId = tweets.get(tweets.size() - 1).getUid() - 1;
        }
        Log.d("DEBUG", "maxId= " + String.valueOf(maxId));
        populateTimeline(maxId, count);
    }
}

package com.codepath.apps.cloetweet.adapters;


import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.cloetweet.R;
import com.codepath.apps.cloetweet.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by cloeliu on 2015/8/8.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetsArrayAdapter(Context context, ArrayList<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        ImageView ivUserImg = (ImageView) convertView.findViewById(R.id.ivUserImg);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAt);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);

        tvUserName.setText(tweet.getUser().getScreenName());
        tvCreatedAt.setText(getRelativeTimeAgo(tweet.getCreateAt()));
        tvBody.setText(tweet.getBody());
        ivUserImg.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivUserImg);

        return convertView;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
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

        return relativeDate;
    }
}

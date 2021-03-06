package com.codepath.apps.cloetweet;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = (Class<? extends Api>) TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "B6kPgjGYgR5dl28E5i4NlI8MN";       // Change this
	public static final String REST_CONSUMER_SECRET = "fYdZs9Q8KsWl1GCwcV4ii5HYNtQSThwJu4BjlKeRCvArZVgM52"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cloetweet"; // Change this (here and in manifest)

	private final int DEFAULT_COUNT = 25;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

	public void getHomeTimeline(final long maxId, final int count, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", count);
		params.put("since_id", 1);
		if(maxId > 0) {
			params.put("max_id", maxId);
		}
		client.get(apiUrl, params, handler);
	}

	public void getMentionsTimeline(final long maxId, final int count, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", count);
		params.put("since_id", 1);
		if(maxId > 0) {
			params.put("max_id", maxId);
		}
		client.get(apiUrl, params, handler);
	}

	public void postStatusUpdate(final String body, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		client.post(apiUrl, params, handler);
	}

	public void getUserTimeline(final long maxId, final int count, String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", DEFAULT_COUNT);
		params.put("since_id", 1);
		if(maxId > 0) {
			params.put("max_id", maxId);
		}
		params.put("screen_name", screenName);
		client.get(apiUrl, params, handler);
	}

	public void getUserInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, null, handler);
	}

	public void getUserLookup(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/lookup.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		client.get(apiUrl, params, handler);
	}
}

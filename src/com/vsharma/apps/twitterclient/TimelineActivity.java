package com.vsharma.apps.twitterclient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.vsharma.apps.twitterclient.models.Tweet;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.ListView;

public class TimelineActivity extends Activity implements OnScrollListener{

	private List<Tweet> tweets = new ArrayList<Tweet>();
	private TweetsAdapter tweetsAdapter;
	private long  minId=0;
	private boolean isUpdating = false;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweetsAdapter = new TweetsAdapter(this, tweets);
		lvTweets.setAdapter(tweetsAdapter);
		
		loadHomeTimeline();
		
		lvTweets.setOnScrollListener(this);
	}

	private void loadHomeTimeline() {
		isUpdating = true;
		Log.d("DEBUG", "MINID "+minId);
		TwitterClientApp.getRestClient().getHomeTimeline(minId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				Log.d("DEBUG", jsonTweets.toString());
				tweets = Tweet.fromJson(jsonTweets);
				tweetsAdapter.addAll(tweets);
				
				if(tweets.size()>0) {
					Tweet tweet = tweets.get(tweets.size()-1);
					minId = tweet.getId();
				}
				isUpdating = false;
					
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	public void composeTweet(MenuItem mi) {
		Intent intent = new Intent(getApplicationContext(), TweetActivity.class);
		startActivityForResult(intent, 100);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("DEBUG", "onActivityResult");
		if(requestCode == 100) {
			if(resultCode==RESULT_OK) {
				tweetsAdapter.clear();
				minId = 0;
				loadHomeTimeline();
				Log.d("DEBUG", "onActivityResult-2");
			}
		}
	}
	
	public void loadMore(View v) {
		loadHomeTimeline();
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if(isUpdating || totalItemCount == 0)
			return;
		if((totalItemCount - firstVisibleItem)<8)
			loadHomeTimeline();
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}

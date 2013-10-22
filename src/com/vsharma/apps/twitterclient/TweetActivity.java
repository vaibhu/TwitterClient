package com.vsharma.apps.twitterclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vsharma.apps.twitterclient.models.Tweet;
import com.vsharma.apps.twitterclient.models.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TweetActivity extends Activity {

	EditText etBody;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet);
		etBody = (EditText) findViewById(R.id.etBody);
		TwitterClientApp.getRestClient().getLoggedInUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonObject) {
				Log.d("DEBUG", jsonObject.toString());
				User user = User.fromJson(jsonObject);
				
				Log.d("DEBUG", user.getName());

				ImageView imageView = (ImageView) findViewById(R.id.ivProfile);
			    ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), imageView);
			    
			    TextView nameView = (TextView) findViewById(R.id.tvName);
			    String formattedName = "<b>" + user.getName() + "</b>" + " <small><font color='#777777'>@" +
			        user.getScreenName() + "</font></small>";
			    
			    nameView.setText(Html.fromHtml(formattedName));
			    
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet, menu);
		return true;
	}
	
	public void cancel(View v) {
		finish();
	}
	
	public void tweet(View v) {
		String status = etBody.getText().toString();
		TwitterClientApp.getRestClient().postTweet(status, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonObject) {
				Log.d("DEBUG", jsonObject.toString());
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			    
			}
		});
		
	}

}

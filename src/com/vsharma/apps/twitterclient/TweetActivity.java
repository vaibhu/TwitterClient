package com.vsharma.apps.twitterclient;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vsharma.apps.twitterclient.models.User;

public class TweetActivity extends Activity {

	private EditText etBody;
	private TextView tvCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet);
		etBody = (EditText) findViewById(R.id.etBody);
		
		tvCount = (TextView) findViewById(R.id.tvCount);
		
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
		
		final TextWatcher txwatcher = new TextWatcher() {
			   
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tvCount.setText("Count: "+String.valueOf(141-s.length()));
			}

			public void afterTextChanged(Editable s) {
			
			}
		};
		
		etBody.addTextChangedListener(txwatcher);
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

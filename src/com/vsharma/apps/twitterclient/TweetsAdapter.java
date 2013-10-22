package com.vsharma.apps.twitterclient;

import java.text.SimpleDateFormat;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vsharma.apps.twitterclient.models.Tweet;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetsAdapter extends ArrayAdapter<Tweet> {

	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.tweet_item, null);
        }

    Tweet tweet = getItem(position);   
    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
    String date = tweet.getCreatedAt();
    date = date.replaceAll("\\+0000", "");
    ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
    ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);

    TextView nameView = (TextView) view.findViewById(R.id.tvName);
    String formattedName = "<b>" + tweet.getUser().getName() + "</b>" + " <small><font color='#777777'>@" +
        tweet.getUser().getScreenName() +" ("+date +") </font></small>";
    
    nameView.setText(Html.fromHtml(formattedName));
    
    TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
    bodyView.setText(Html.fromHtml(tweet.getBody()));
    
    return view;
    }
}

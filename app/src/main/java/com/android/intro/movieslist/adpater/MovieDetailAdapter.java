package com.android.intro.movieslist.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.intro.custorm.imageview.NetworkedCacheableImageView;
import com.android.intro.moiveslist.R;
import com.android.intro.moiveslist.YoukuVideoView;
import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.utils.AphidLog;
import com.aphidmobile.utils.UI;
import android.support.v4.app.FragmentManager;

public class MovieDetailAdapter extends BaseAdapter {

	private FlipViewController controller;

	private Context context;

	private LayoutInflater inflater;

	private ArrayList<HashMap<String, String>> movices;
	
	private Integer intent_position;


	public MovieDetailAdapter(Context context, FlipViewController controller, ArrayList<HashMap<String, String>> movices) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.controller = controller;
		this.movices = movices;
	}
	
	public void appendToDataSet(ArrayList<HashMap<String, String>> ms){
		movices.addAll(ms);
	}

	@Override
	public int getCount() {
		return null != movices ? movices.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.detail, null);

			holder = new ViewHolder();
			holder.titleTextView = (TextView) convertView.findViewById(R.id.title);
			holder.title_bottomTextView = (TextView) convertView.findViewById(R.id.title_bottom);
			holder.photoView = (NetworkedCacheableImageView) convertView.findViewById(R.id.photo);
			holder.photoView.setOnClickListener(new OnClickListener() {
			    public void onClick(View v) {
			    	Intent intent = new Intent(v.getContext(),YoukuVideoView.class);
			    	HashMap m= movices.get(position);
					intent.putExtra("vid", m.get("_id").toString());
					context.startActivity(intent);
			    }
			});
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// get data by index position
		HashMap<String, String> data = movices.get(position);

		// set view by data
		holder.titleTextView.setText(String.format("%d. %s", position + 1,
				data.get("title")));
		holder.titleTextView.setBackgroundColor(context.getResources()
				.getColor(R.color.green));
		
		holder.title_bottomTextView.setText(String.format("%s",
				data.get("subtitle")));
		holder.title_bottomTextView.setBackgroundColor(context.getResources()
				.getColor(R.color.red));
		
		final boolean fromCache = holder.photoView.loadImage(data.get("thumb"), false, null);

		return convertView;
	}
	
	
	static class ViewHolder {
		TextView titleTextView;
		TextView title_bottomTextView;
		NetworkedCacheableImageView photoView;
	}

}

package com.android.intro.moiveslist;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.intro.moiveslist.models.HttpCallBackHandler;
import com.android.intro.moiveslist.models.MoiveModel;
import com.android.intro.moiveslist.MoiveDetailAvctivity;
import com.android.intro.movieslist.adpater.MovieAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.TextView;

public class MoiveChannelFragment extends Fragment {

	private static final String KEY_CONTENT = "MoiveChannelFragment:Content";
	private Integer channelId;
	private Context mContext;
	GridView moiveGridView;
	private Integer pg = 1;
	private Integer pz = 30;
	private Integer max_pg = 30;
	private boolean loading = true;
	private int previousTotal = 0;
	private int visibleThreshold = 10;
	protected ProgressBar mProgressBar;
	private View convertView;
	MoiveModel moives;
	String cid;

	public static MoiveChannelFragment newInstance(Context context,
			Integer channel_id) {

		MoiveChannelFragment fragment = new MoiveChannelFragment();
		fragment.channelId = channel_id;
		fragment.mContext = context;

		return fragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			channelId = savedInstanceState.getInt(KEY_CONTENT);
		}
		
		pg = 1;
	}

	@Override
	public void onResume() {
		
		super.onResume();
		if (moiveGridView != null) {
			MovieAdapter adapter=(MovieAdapter) moiveGridView.getAdapter();
			if(adapter != null){
				pg = 1;
				//moiveGridView.setAdapter(adapter);
				//adapter.notifyDataSetChanged();
			}
		}
	}
	

	public void onHttpResponseCallback(ArrayList<String> urls) {
		MovieAdapter adapter = new MovieAdapter(this.mContext, urls);
		moiveGridView.setAdapter(adapter);
	}
	
	
	public void onUpdateHttpResponseCallback(ArrayList<String> urls) {
		MovieAdapter adapter =(MovieAdapter) moiveGridView.getAdapter();
		if(adapter!=null){
			adapter.appendToDataSet(urls);
	        adapter.notifyDataSetChanged();
        }
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(savedInstanceState!=null)
        {
			if(convertView!=null){
				return convertView;
			}
        }
		
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.activity_main, container, false);

		moiveGridView = (GridView) convertView
				.findViewById(R.id.moivesGridView);
		
		mProgressBar = (ProgressBar)convertView.findViewById(R.id.ajax_loading);

		moiveGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(v.getContext(),
						MoiveDetailAvctivity.class);
				intent.putExtra("cid", channelId.toString());
				intent.putExtra("position", position);
				startActivity(intent);
				if(position%2==0){
					((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
				}else{
					((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
				}
			}
		});
		
		moives = new MoiveModel(this.mContext);
		cid = channelId.toString();
		moiveGridView.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {		
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				Integer auto_loading_count = 4;
				if(firstVisibleItem >= (totalItemCount - auto_loading_count) && pg < max_pg){
					//auto loading
					
					if (loading) {
			            if (totalItemCount > previousTotal) {
			            	loading = false;
			                previousTotal = totalItemCount;
			            }
					}
					if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {

						pg++;
						moives.get_channel_list(cid, pg, pz, new HttpCallBackHandler() {
							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(HashMap<String, Object> result) {
								onUpdateHttpResponseCallback((ArrayList<String>) result
										.get("results"));
								
								loading = false;
							}
						});
						loading = true;
					}
				}
			}
			
		});


		mProgressBar.setVisibility(View.VISIBLE); 
		moives.get_channel_list(cid, pg, pz, new HttpCallBackHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(HashMap<String, Object> result) {
				onHttpResponseCallback((ArrayList<String>) result
						.get("results"));
				mProgressBar.setVisibility(View.INVISIBLE);
			}
		});

		return convertView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_CONTENT, channelId);
	}
}

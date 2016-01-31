package com.android.intro.moiveslist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.intro.moiveslist.models.HttpCallBackHandler;
import com.android.intro.moiveslist.models.MoiveModel;
import com.android.intro.movieslist.adpater.MovieDetailAdapter;
import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.utils.AphidLog;
import com.aphidmobile.utils.IO;
import com.aphidmobile.utils.UI;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import android.support.v4.app.FragmentActivity;

public class MoiveDetailAvctivity extends FragmentActivity {

	private FlipViewController flipView;
	VideoView mVideoView;
	Context context;
	private Integer pg = 1;
	private Integer pz = 30;
	private Integer max_pg = 30;
	private int visibleThreshold = 4;
	MoiveModel moives;
	String cid;
	private Integer intent_position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);

		moives = new MoiveModel(this);
		Intent intent = getIntent();
		cid = intent.getStringExtra("cid");
		intent_position = intent.getIntExtra("position", 0);
		
		/* calcultor */
		if(intent_position>=pz){
			Log.d("PAGE:init:intent_position",intent_position.toString());
			pg = (Integer)(intent_position / pz) + 1; //ceil
			intent_position = intent_position % pz;
			Log.d("PAGE:pg",pg.toString());
			Log.d("PAGE:intent_position",intent_position.toString());
		}
		
		flipView = new FlipViewController(this);
		flipView.setAnimationBitmapFormat(Bitmap.Config.RGB_565);
		flipView.setOnViewFlipListener(new FlipViewController.ViewFlipListener() {
			@Override
			public void onViewFlipped(View view, int position) {
				
				if (position == flipView.getAdapter().getCount() - visibleThreshold) {
					//expand the data size on the last page
					pg++;
					moives.get_channel_list(cid, pg, pz, new HttpCallBackHandler() {
						@SuppressWarnings("unchecked")
						@Override
						public void onSuccess(HashMap<String, Object> result) {
							onUpdateHttpResponseCallback((ArrayList<String>) result
									.get("datas"));
						}
					});
				}
			}
			
		});

		context = this;

		moives.get_channel_list(cid, pg, pz, new HttpCallBackHandler() {
			@Override
			public void onSuccess(HashMap<String, Object> result) {
				onHttpResponseCallback((ArrayList<?>) result.get("datas"));
			}
		});

	}
	
	public void onUpdateHttpResponseCallback(ArrayList datas) {
		MovieDetailAdapter adapter =(MovieDetailAdapter) flipView.getAdapter();
		adapter.appendToDataSet(datas);
        adapter.notifyDataSetChanged();
	}

	public void onHttpResponseCallback(ArrayList datas) {
		flipView.setAdapter(new MovieDetailAdapter(this, flipView, datas));
		flipView.setSelection(intent_position);
		setContentView(flipView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		flipView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		flipView.onPause();
	}
	
	@Override
	public void onBackPressed() {
		finish();
		if(intent_position %2 ==0){
	    	overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
		}else{
		    overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
		}
	}

}

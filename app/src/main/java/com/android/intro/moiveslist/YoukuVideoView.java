package com.android.intro.moiveslist;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.intro.moiveslist.models.HttpCallBackHandler;
import com.android.intro.moiveslist.models.MoiveModel;
import com.android.intro.moiveslist.models.PlayModel;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.LinearLayout.LayoutParams;

public class YoukuVideoView extends Activity {

	
	protected ProgressBar mProgressBar;
	protected TextView    mProgressMessage;
	protected VideoView   mVideoView;
	protected  String  vid; 
	private String playurl;
	MediaController lMediaController ;
	
	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
	    
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
	    Intent intent = getIntent();
		vid = intent.getStringExtra("vid");
        
        // create the layout of the view
        setupView();
        
	    // set the flag to keep the screen ON so that the video can play without the screen being turned off
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        PlayModel player = new PlayModel(this);
		
		
		player.get_playurl(vid, new HttpCallBackHandler(){
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(HashMap<String,Object> result){
				try {
					onHttpResponseCallback((JSONObject)result.get("playinfo"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	 	});
		
	  }
	  
	  public void onHttpResponseCallback(JSONObject playinfo) throws JSONException{
		  playurl = playinfo.get("url").toString();
		  Log.e(this.getClass().getSimpleName(), "onHttpResponseCallback  "+playurl);
		  play_video();
	  }


	/**
	 * Create the view in which the video will be rendered.
	 */
	private void setupView() {
		LinearLayout lLinLayout = new LinearLayout(this);
	    lLinLayout.setId(1);
	    lLinLayout.setOrientation(LinearLayout.VERTICAL);
	    lLinLayout.setGravity(Gravity.CENTER);
	    lLinLayout.setBackgroundColor(Color.BLACK);
	    
	    LayoutParams lLinLayoutParms = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
	    lLinLayout.setLayoutParams(lLinLayoutParms);
	    
	    this.setContentView(lLinLayout);

	    
	    RelativeLayout lRelLayout = new RelativeLayout(this);
	    lRelLayout.setId(2);
	    lRelLayout.setGravity(Gravity.CENTER);
	    lRelLayout.setBackgroundColor(Color.BLACK);
	    android.widget.RelativeLayout.LayoutParams lRelLayoutParms = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	    lRelLayout.setLayoutParams(lRelLayoutParms);
	    lLinLayout.addView(lRelLayout);
	    
	    mVideoView = new VideoView(this);
	    mVideoView.setId(3);
	    android.widget.RelativeLayout.LayoutParams lVidViewLayoutParams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	    lVidViewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    mVideoView.setLayoutParams(lVidViewLayoutParams);
	    lRelLayout.addView(mVideoView);
	    
	    mProgressBar = new ProgressBar(this);
	    mProgressBar.setIndeterminate(true);
	    mProgressBar.setVisibility(View.VISIBLE);
	    mProgressBar.setEnabled(true);
	    mProgressBar.setId(4);
	    android.widget.RelativeLayout.LayoutParams lProgressBarLayoutParms = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	    lProgressBarLayoutParms.addRule(RelativeLayout.CENTER_IN_PARENT);
	    mProgressBar.setLayoutParams(lProgressBarLayoutParms);
	    lRelLayout.addView(mProgressBar);
	    
	    mProgressMessage = new TextView(this);
	    mProgressMessage.setId(5);
	    android.widget.RelativeLayout.LayoutParams lProgressMsgLayoutParms = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	    lProgressMsgLayoutParms.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    lProgressMsgLayoutParms.addRule(RelativeLayout.BELOW, 4);
	    mProgressMessage.setLayoutParams(lProgressMsgLayoutParms);
	    mProgressMessage.setTextColor(Color.LTGRAY);
	    mProgressMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
	    mProgressMessage.setText("...");
	    lRelLayout.addView(mProgressMessage);
	}

	
	
	public void updateProgress(String pProgressMsg){
		try {
			mProgressMessage.setText(pProgressMsg);
		} catch(Exception e) {
			Log.e(this.getClass().getSimpleName(), "Error updating video status!", e);
		}
	}
	
	private class ProgressUpdateInfo {
		public String mMsg;
		public ProgressUpdateInfo(String pMsg){
			mMsg = pMsg;
		}
	}

	
	
	public void play_video() {
		
		if(playurl==null){
			Exception e = new Exception("playurl is null");
			showErrorAlert(e);
			return;
		}
		
		try{
			Log.d("PLAYURL" , playurl);
			Uri uri = Uri.parse(playurl);
		    mVideoView.setVideoURI(uri);
		    
		    // TODO:  add listeners for finish of video
		    mVideoView.setOnCompletionListener(new OnCompletionListener(){

				@Override
				public void onCompletion(MediaPlayer pMp) {
					YoukuVideoView.this.finish();
				}
		    });

			lMediaController = new MediaController(YoukuVideoView.this);
			mVideoView.setMediaController(lMediaController);
			
			
			mVideoView.setKeepScreenOn(true);
			
			mVideoView.setOnPreparedListener( new MediaPlayer.OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer pMp) {
					
					YoukuVideoView.this.mProgressBar.setVisibility(View.GONE);
					YoukuVideoView.this.mProgressMessage.setVisibility(View.GONE);
					//mVideoView.requestFocus();
					//mVideoView.start();
					pMp.start();
					lMediaController.show(0);
				}
		    	
		    });
			mVideoView.requestFocus(); 
			//mVideoView.start();  
			
		} catch(Exception e){
			Log.e(this.getClass().getSimpleName(), "Error playing video!", e);
			showErrorAlert(e);
		}
	}

	private void showErrorAlert(Exception e) {
		
		try {
			Builder lBuilder = new AlertDialog.Builder(YoukuVideoView.this);
			lBuilder.setTitle("video error");
			lBuilder.setCancelable(false);
			lBuilder.setMessage(e.getMessage());

			lBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface pDialog, int pWhich) {
					YoukuVideoView.this.finish();
				}
				
			});

			AlertDialog lDialog = lBuilder.create();
			lDialog.show();
		} catch(Exception e1){
			Log.e(this.getClass().getSimpleName(), "Problem showing error dialog.", e1);
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(mVideoView != null){
			mVideoView.stopPlayback();
		}
	    // clear the flag that keeps the screen ON 
		getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.mVideoView = null;
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	
}

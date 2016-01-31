package com.android.intro.moiveslist;


import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONObject;

import com.android.intro.moiveslist.models.*;
import com.android.intro.movieslist.adpater.MovieAdapter;


public class MainActivity extends Activity {

	
	GridView moiveGridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		moiveGridView = (GridView) this.findViewById(R.id.moivesGridView);
		
		MoiveModel moives = new MoiveModel(this);
		String cid = "96";
		Integer pz = 30;
		Integer pg = 1;
		
		moives.get_channel_list(cid , pg , pz , new HttpCallBackHandler(){
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(HashMap<String,Object> result){
				onHttpResponseCallback((ArrayList<String>) result.get("results"));
			}
		});
	}
	
	public void onHttpResponseCallback(ArrayList<String> urls){
		MovieAdapter adapter = new MovieAdapter(this, urls);
        moiveGridView.setAdapter(adapter);
	}
	

}

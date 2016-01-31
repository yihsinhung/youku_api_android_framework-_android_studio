package com.android.intro.moiveslist.models;

import com.loopj.android.http.AsyncHttpClient;

import android.content.Context;

public class BaseModel {

	String Host = "api.3g.youku.com";
	
	String UserAgent = "Android;3.1;Example";
	final String PID = "69b81504767483cf";
	final String GUID = "abcdesfgaddfa";
	Context context;
	
	protected static AsyncHttpClient client = new AsyncHttpClient();
	
	public BaseModel(Context context){
		this.context = context;
	}
}

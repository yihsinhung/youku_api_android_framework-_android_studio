package com.android.intro.moiveslist.models;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MoiveModel extends BaseModel {
	
	
    String Path = "layout/phone3_0/channels";
	
	
	public MoiveModel(Context context){
		super(context);
	}
	
    public void get_channel_list(String cid , Integer pg, Integer pz ,final HttpCallBackHandler callbackhandler) {
    	
    	String MoivesUrl =  String.format("http://%s/%s",  Host, Path);
    	Log.d("URL", MoivesUrl);
    	RequestParams params = new RequestParams();
    	params.put("cid", cid);
    	params.put("pid", PID);
    	params.put("guid", GUID);
    	params.put("image_hd", "1");
    	params.put("image_layout", "v");
    	params.put("pg", pg.toString());
    	params.put("pz", pz.toString());
    	
    	
    	Header[] headers = {
    		     new BasicHeader("Content-type", "application/json")
    		    ,new BasicHeader("Accept", "text/html,text/xml,application/xml")
    		    ,new BasicHeader("User-Agent", UserAgent)
    	};
    	
        client.get(this.context , MoivesUrl , headers, params, new JsonHttpResponseHandler() {
			
            @Override
            public void onSuccess(JSONObject document) {
               JSONArray pugsJsonArray;
               ArrayList<String> urls;
               ArrayList<HashMap<String,String>> datas = new ArrayList<HashMap<String,String>>();
               
			   try {
					pugsJsonArray = document.getJSONArray("results");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				}
			   
			   if(pugsJsonArray.length()>0){
				   
				   urls = new ArrayList<String>();
				   
	               for (int i = 0, z = pugsJsonArray.length(); i < z; i++) {
	                   try {
	                	   JSONObject show =  (JSONObject) pugsJsonArray.get(i);
	                	   urls.add(show.getString("thumb"));
	                	   Log.d("Show URL", show.getString("thumb"));
	                	   
	                	   HashMap<String,String> data = new HashMap<String,String>();
		                   data.put("thumb", show.getString("thumb"));
		                   data.put("subtitle", show.getString("subtitle"));
		                   if(show.has("showid")){
		                	   data.put("_id", show.getString("showid"));
		                   }else if(show.has("videoid")){
			                   data.put("_id", show.getString("videoid"));
		                   }
		                   data.put("title", show.getString("title"));
		                   datas.add(data);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                   
	               }
	               
	               HashMap<String, Object> resultHash = new HashMap<String, Object>();
	               resultHash.put("results", urls);
	               resultHash.put("datas",datas);
	               callbackhandler.onSuccess(resultHash);
			   }
            }
        });
    }
    
}

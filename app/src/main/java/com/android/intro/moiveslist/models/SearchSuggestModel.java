package com.android.intro.moiveslist.models;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SearchSuggestModel extends BaseModel {
	
	String Host = "search.api.3g.youku.com";
	String Path = "keywords/suggest";

	public SearchSuggestModel(Context context) {
		super(context);
	}

	public void get_suggest(String str, final HttpCallBackHandler callbackhandler) {

		String suggestUrl = String.format("http://%s/%s", Host, Path);
		Log.d("URL", suggestUrl);

		RequestParams params = new RequestParams();
		params.put("keywords", str);
		params.put("pid", PID);
		params.put("guid", GUID);
		

		Header[] headers = {
				new BasicHeader("Content-type", "application/json"),
				new BasicHeader("Accept", "text/html,text/xml,application/xml"),
				new BasicHeader("User-Agent", UserAgent) };

		client.get(this.context, suggestUrl, headers, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject document) {
						JSONArray results;
						
						try {
							results = document.getJSONArray("results");
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							return;
						}
						ArrayList<String> r = new ArrayList<String>();
						for (int i = 0, j = results.length(); i < j; i++) {
			                
			                	JSONObject data;
								try {
									data = (JSONObject) results.get(i);
									r.add(data.getString("keyword"));
								} catch (JSONException e) {
									e.printStackTrace();
								}
			             }
						
						Log.d("Search", r.toString());
						HashMap<String,Object> result = new HashMap<String,Object>();
						result.put("results", r);
						
						callbackhandler.onSuccess(result);
					}
				});
	}
}

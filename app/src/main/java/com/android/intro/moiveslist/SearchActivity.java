package com.android.intro.moiveslist;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.intro.custorm.imageview.NetworkedCacheableImageView;
import com.android.intro.moiveslist.models.HttpCallBackHandler;
import com.android.intro.moiveslist.models.SearchSuggestModel;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;


public class SearchActivity extends Activity {

	
	AutoCompleteTextView textView;
	SearchSuggestModel searchModel;
	
	NetworkedCacheableImageView recommendImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.search_layout);
		
		searchModel  = new SearchSuggestModel(this);
		
		textView = (AutoCompleteTextView) findViewById(R.id.search_textbox);
		
		textView.addTextChangedListener(new TextWatcher() {
		    
			@Override
		    public void afterTextChanged(Editable editable) {
				
		    }
		    
		    @Override
		    public void beforeTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
		    	
		    }

		    @Override
		    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
		        String text = charSequence.toString();
		        if (text.length() >= 1) {
		        	
		        	searchModel.get_suggest(text,  new HttpCallBackHandler(){
						@SuppressWarnings("unchecked")
						@Override
						public void onSuccess(HashMap<String,Object> result){
							ArrayList<String> results = (ArrayList<String>) result.get("results");
							
							Log.d("SearchEND", results.toString());
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.suggest_list_item , R.id.suggest_item, results);
							textView.setAdapter(adapter);
							
							if (!textView.isPopupShowing()) {
							        textView.showDropDown();
							}
						}
				 	});
		        }
		    }
		});

	}
	
	@Override
	public void onBackPressed() {
		finish();
	    overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
	}
	

}

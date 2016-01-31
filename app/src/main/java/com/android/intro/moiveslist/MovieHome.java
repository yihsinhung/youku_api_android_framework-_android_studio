package com.android.intro.moiveslist;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.intro.moviceslist.base.BaseActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

public class MovieHome extends BaseActivity {
    
    public static HashMap<Integer,String> channelTabs;
	public static ArrayList<Integer> channels;
	ViewPager pager;
	ImageView gotoSearchBtn;
	private static boolean isExit = false;
	private long clickTime = 0; 
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	 
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.home);
        
        if(savedInstanceState!=null){
        	return;
        }
        
        channelTabs = new HashMap<Integer,String>(); 
        channelTabs.put(96, "電影");
		channelTabs.put(84, "電視劇");
		channelTabs.put(97, "");
		channelTabs.put(85, "����");
		channelTabs.put(91, "��ѯ");
		channelTabs.put(100, "����");
		channelTabs.put(87, "����");
		
		channels = new ArrayList<Integer>();
		channels.add(96);
		channels.add(97);
		channels.add(85);
		channels.add(100);
		channels.add(84);
		channels.add(87);
		channels.add(91);

        FragmentPagerAdapter adapter = new MovieChannelAdapter(getSupportFragmentManager(),this);

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        
        
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) { }

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			} 
		});
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        
        
        gotoSearchBtn = (ImageView) this.findViewById(R.id.gotoSearchBtn);
        gotoSearchBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SearchActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top);
			}
        });

    }

    class MovieChannelAdapter extends FragmentPagerAdapter {
    	Context mContext;
        public MovieChannelAdapter(FragmentManager fm,Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
        	//return TestFragment.newInstance("TestFragment");
            return MoiveChannelFragment.newInstance(mContext,channels.get(position % channels.size()));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Integer cid =  channels.get(position % channels.size());
            if(cid!=null){
            	return channelTabs.get(cid);
            }
            return "weizhi"; 
        }

        @Override
        public int getCount() {
          return channels.size();
        }
    }
    
    
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            exit();  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }  
      
    private void exit() {  
        if ((System.currentTimeMillis() - clickTime) > 2000) {  
            Toast.makeText(getApplicationContext(), "�ٰ�һ�κ��˼��˳�",   Toast.LENGTH_SHORT).show();  
            clickTime = System.currentTimeMillis();  
        } else {  
            this.finish();  
        }  
    }
}

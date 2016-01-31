package com.android.intro.movieslist.adpater;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import uk.co.senab.bitmapcache.CacheableBitmapDrawable;

import com.android.intro.custorm.imageview.NetworkedCacheableImageView;
import com.android.intro.moiveslist.*;

public class MovieAdapter extends BaseAdapter {

	private final ArrayList<String> movicesUrl;
	
	private final Context mContext;
	
	
	public MovieAdapter(Context context, ArrayList<String> url) {
		movicesUrl = url;
		mContext = context;
    }
	
	public void appendToDataSet(ArrayList<String> url){
		movicesUrl.addAll(url);
	}
	
	@Override
	public int getCount() {
		return null != movicesUrl ? movicesUrl.size() : 0;
	}
	
	@Override
	public String getItem(int position) {
	    return movicesUrl.get(position);
	}
	
	@Override
	public long getItemId(int position) {
	    return position;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.gridview_item_layout, parent, false);
        }

        NetworkedCacheableImageView imageView = (NetworkedCacheableImageView) convertView
                .findViewById(R.id.nciv_pug);
        
        
        TextView status = (TextView) convertView.findViewById(R.id.tv_status);

        final boolean fromCache = imageView
                .loadImage(movicesUrl.get(position), false, new UpdateTextViewListener(status));
        

        if (fromCache) {
            status.setText("From Memory Cache");
            status.setBackgroundColor(mContext.getResources().getColor(R.color.translucent_green));
        } else {
            status.setText("Loading...");
            status.setBackgroundColor(mContext.getResources().getColor(R.color.translucent_green));
        }

        return convertView;
    }

    static class UpdateTextViewListener
            implements NetworkedCacheableImageView.OnImageLoadedListener {
        private final WeakReference<TextView> mTextViewRef;

        public UpdateTextViewListener(TextView tv) {
            mTextViewRef = new WeakReference<TextView>(tv);
        }

        @Override
        public void onImageLoaded(CacheableBitmapDrawable result) {
            final TextView tv = mTextViewRef.get();
            if (tv == null) {
                return;
            }

            if (result == null) {
                tv.setText("Failed");
                tv.setBackgroundDrawable(null);
                return;
            }

            switch (result.getSource()) {
                case CacheableBitmapDrawable.SOURCE_UNKNOWN:
                case CacheableBitmapDrawable.SOURCE_NEW:
                    tv.setText("From Disk/Network");
                    tv.setBackgroundColor(tv.getResources().getColor(R.color.translucent_red));
                    break;
                case CacheableBitmapDrawable.SOURCE_INBITMAP:
                    tv.setText("Reused Bitmap");
                    tv.setBackgroundColor(tv.getResources().getColor(R.color.translucent_blue));
                    break;
            }
        }
    }

}

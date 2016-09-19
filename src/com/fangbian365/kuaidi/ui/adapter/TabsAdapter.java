package com.fangbian365.kuaidi.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class TabsAdapter extends FragmentPagerAdapter {
	private final Context mContext;
	private final ArrayList<PageTab> mTabs = new ArrayList<PageTab>();

	public class PageTab {
		private final Class<?> clss;
		public String tag;
		public String title;
		public Bundle bundle;
		public Fragment f;
		
		PageTab(Class<?> clss){
			this.clss = clss;
		}
		
		PageTab(String _tag, Class<?> _class, Bundle _bundle) {
			clss = _class;
			tag = _tag;
			title = _tag;
			f = null;
			bundle = _bundle;
		}

		public Bundle getBundle() {
			return bundle;
		}
		public void setBundle(Bundle bundle) {
			this.bundle = bundle;
		}
		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
		
		public Class<?> getClss() {
			return clss;
		}
		
	}

	public TabsAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
		mContext = activity;
	}
	
	public TabsAdapter(Fragment fragment) {
		super(fragment.getChildFragmentManager());
		mContext = fragment.getActivity();
	}

	public void init() {
		mTabs.clear();
	}
	
	public void addTab(String tag, Class<?> clss) {
		mTabs.add(new PageTab(tag, clss, null));
	}
	
	public void addTab(String tag, Class<?> clss, Bundle bundle) {
		mTabs.add(new PageTab(tag, clss, bundle));
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (mTabs.size() == 0) {
			return null;
		}
		return mTabs.get(position % mTabs.size()).title;
	}

	@Override
	public Fragment getItem(int position) {
		PageTab info = mTabs.get(position);
		if (info.f == null) {
			info.f = Fragment.instantiate(mContext, info.clss.getName(), info.bundle);	
		}
		
		return info.f;
	}
	
	public Fragment getFragment(int position){
		if (position >= mTabs.size() || position < 0) {
			return null;
		}
		PageTab info = mTabs.get(position);
		if(info != null)
			return info.f;
		return null;
	}
	
	public void Resume(){
		for(PageTab info: mTabs){
			if(info != null && info.f!= null && info.f.isAdded()){
				info.f.onResume();
			}
		}
	}
	
	public void Pause(){
		for(PageTab info: mTabs){
			if(info != null && info.f!= null && info.f.isAdded()){
				info.f.onPause();
			}
		}
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

}

package com.fangbian365.kuaidi.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;

public abstract class HeaderFragment extends Fragment implements OnClickListener, OnTouchListener{
	
	
	protected abstract String getTitle();
	protected abstract int getMenuResId();
	public abstract boolean onKeyDown(int keyCode);
	
	private OnRightMenuClickListener mRMenuClickListener = null;
	public interface OnRightMenuClickListener {
		public abstract void OnRightMenuClick();
	}
	protected void setOnRightMenuClickListener(OnRightMenuClickListener listener) {
		mRMenuClickListener = listener;
	}
	
	protected ViewGroup mContentView = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.headerfragment, null);
		
		TextView tvTitle = (TextView) view.findViewById(R.id.header_title);
		tvTitle.setText(getTitle());
		((ImageView) view.findViewById(R.id.iv_header_back)).setOnClickListener(this);
		
		if (getMenuResId() != -1) {
			ImageView ivMenu = (ImageView) view.findViewById(R.id.iv_header_menu);
			ivMenu.setImageResource(getMenuResId());
			ivMenu.setOnClickListener(this);
		}
		
		ImageView ivBg = (ImageView)view.findViewById(R.id.bgskin);
		ivBg.setOnTouchListener(this);
		
		mContentView = (ViewGroup) view.findViewById(R.id.header_content);
		LayoutParams glp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		container.setLayoutParams(glp);
		mContentView.addView(container);
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_header_back:
			if (onKeyDown(KeyEvent.KEYCODE_BACK))
				return;
			SysWorkTools.closeKeyboard(getActivity());
			close();
			break;
		case R.id.iv_header_menu:
			if (mRMenuClickListener != null) {
				mRMenuClickListener.OnRightMenuClick();
			}
			break;
		case R.id.bgskin:
			SysWorkTools.closeKeyboard(getActivity());
			break;
		default:
			break;
		}
	}
	
	public void close(){
		try {
			FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
			fragmentManager.popBackStack();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			FragmentManager f = getChildFragmentManager();
			f.popBackStack();
		}
		
    }
}

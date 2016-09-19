package com.fangbian365.kuaidi.ui.uisupport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.ui.activity.MainActivity;

public class ProgressView {

	private Context mContext;
	private View view = null;
	private static ProgressView single = null;
	
	private ImageView mIvLoading;
	
	private ProgressView(Context context) {
		mContext = context;
	}

	// 静态工厂方法
	public static ProgressView getInstance(Context context) {

		if (single == null) {
			single = new ProgressView(context);
		}
		return single;
	}

	public void showProgress(RelativeLayout relaView) {

		view = LayoutInflater.from(mContext).inflate(R.layout.loading_dialog, null);
		RelativeLayout layoutLoading = (RelativeLayout) view.findViewById(R.id.layout_loading_view);
		layoutLoading.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		mIvLoading = (ImageView) view.findViewById(R.id.iv_loading);
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading_animation); 
		mIvLoading.startAnimation(hyperspaceJumpAnimation);
		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(params);
		try {
			relaView.addView(view);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void hideProgress(final RelativeLayout relaView) {
		if (mContext instanceof MainActivity) {
			((MainActivity)mContext).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					hideProgressView(relaView);
				}
			});
		}else {
			hideProgressView(relaView);
		}
		
	}
	
	private void hideProgressView(final RelativeLayout relaView) {
		try {
			view.setVisibility(View.GONE);
			mIvLoading.clearAnimation();
			relaView.removeView(view);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

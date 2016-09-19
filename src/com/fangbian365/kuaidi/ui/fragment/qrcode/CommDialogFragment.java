package com.fangbian365.kuaidi.ui.fragment.qrcode;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.constans.Constans;
import com.umeng.analytics.MobclickAgent;

public class CommDialogFragment extends DialogFragment implements OnClickListener, OnKeyListener {
	
	private TextView mTvMsg;
	private String mMsg;
	private int type;
	public static final String KEY_DIALOG_TYPE = "key_dialog_type";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null) {
			type = bundle.getInt(KEY_DIALOG_TYPE,0);
			mMsg = bundle.getString(Constans.KEY_ERR_CAMERA);
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = inflater.inflate(R.layout.offline_dialog, null, false);  
		
	    mTvMsg = (TextView) view.findViewById(R.id.tv_tip_msg);
	    mTvMsg.setText(mMsg);
	    
	    view.findViewById(R.id.tv_ok).setOnClickListener(this);
	    
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setOnKeyListener(this);
		return dialog;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		 MobclickAgent.onPageStart("OfflineDialogFragment"); //统计页面
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ok:
			switch (type) {
			case 0:
//				PrefsConfig.logout();
//				PrefsConfig.saveToPref(getActivity());
//				Intent intent = new Intent(getActivity(), EntryActivity.class);
//				startActivity(intent);
//				getActivity().finish();
//				getActivity().overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_from_right);
				break;
			case 1:
			//	EventBus.getDefault().post(new EventItem(EventItem.ID_AUTO_CLOSE_QR));
				dismiss();
				break;
			default:
				break;
			}
			
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		
		return true;
	}
	
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("OfflineDialogFragment"); 
	}

}
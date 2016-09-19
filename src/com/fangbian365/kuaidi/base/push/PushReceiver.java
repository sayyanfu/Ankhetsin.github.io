package com.fangbian365.kuaidi.base.push;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.constans.Constans;

/**
 * Jpush 极光推送
 * @author houjianjiang
 *
 */
public class PushReceiver extends BroadcastReceiver {
	private static final String TAG = "PushReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		FrameLog.d(TAG, "[MyReceiver] 接收Registration Id : " + JPushInterface.getRegistrationID(context));
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            FrameLog.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	FrameLog.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	
        	String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			FrameLog.d(TAG, "msg***" + message + "    ***extras" + extras);
			
			String key = "";
			String tingBh = "";
			String taiBh = "";
			String loginkey = "";
			String opercode = "";
			
			Intent i = null;
			try {
				JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
				key = json.getString("type");
				JSONObject txt = new JSONObject(json.getString("txt"));
				String shopID = txt.getString("shopId");

				FrameLog.d(TAG, "shopID***" + shopID);
				if (!shopID.equals(PrefsConfig.ShopId)) {
					return;
				}
				
				switch (Result.toCode(key)) {
				case TAI_STATUS:
					tingBh = txt.getString("tingBh");
					taiBh = txt.getString("taiBh");
					
					i = new Intent(Constans.ACTION_RECEIVER_TAI_STATUS);
					i.putExtra("tingBh", tingBh);
					i.putExtra("taiBh", taiBh);
					break;
				case TING_STATUS:
					i = new Intent(Constans.ACTION_RECEIVER_TING_STATUS);
					break;
				case PRODUCT_TASTE_EDIT:
					i = new Intent(Constans.ACTION_RECEIVER_PRODUCT_TASTE_EDIT);
					break;
				case PRODUCT_EDIT:
					i = new Intent(Constans.ACTION_RECEIVER_PRODUCT_EDIT);
					break;
				case PRODUCT_TYPE_EDIT:
					i = new Intent(Constans.ACTION_RECEIVER_PRODUCT_TYPE_EDIT);
					break;
				case PRODUCT_RESON_ZENG:
					i = new Intent(Constans.ACTION_RECEIVER_PRODUCT_RESON_ZENG);
					break;
				case PRODUCT_RESON_TUI:
					i = new Intent(Constans.ACTION_RECEIVER_PRODUCT_RESON_TUI);
					break;
				case PRODUCT_STATUS:
					i = new Intent(Constans.ACTION_RECEIVER_PRODUCT_STATUS);
					break;
				case POS_LS_DC:
					
					i = new Intent(Constans.ACTION_RECEIVER_POS_LS_DC);
					break;
				case LOGOUT:
					loginkey = txt.getString("key"); 
					opercode = txt.getString("opercode");
					i = new Intent(Constans.ACTION_RECEIVER_LOGOUT);
					i.putExtra("key", loginkey);
					i.putExtra("opercode", opercode);
					break;
				case PUSHMSG:
					i = new Intent(Constans.ACTION_RECEIVER_PUSH_MSG);
					break;
				default:
					break;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
				
			if(i != null) {
				context.sendBroadcast(i);
			}
				
			
        } else {
        	FrameLog.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}
	
	public enum Result {
		TAI_STATUS, TING_STATUS, PRODUCT_TASTE_EDIT, PRODUCT_EDIT, PRODUCT_TYPE_EDIT, PRODUCT_RESON_ZENG, PRODUCT_RESON_TUI, PRODUCT_STATUS, POS_LS_DC, LOGOUT, PUSHMSG, NOVALUE;

		public static Result toCode(String str) {
			try {
				if(str.equals("taiStatus") || str.equals("taiEdit")) {
					return TAI_STATUS;
				} else if(str.equals("tingEdit")) {
					return TING_STATUS;
				} else if(str.equals("ProductTasteEdit")) {// 口味变化同步数据库
					return PRODUCT_TASTE_EDIT;
				} else if(str.equals("ProductEdit")) {// 菜品变化推送
					return PRODUCT_EDIT;
				} else if(str.equals("ProductTypeEdit")) {// 菜品分类变化推送
					return PRODUCT_TYPE_EDIT;
				} else if(str.equals("ProductZsresonEdit")) {// 赠菜说明变化
					return PRODUCT_RESON_ZENG;
				} else if(str.equals("ProductBackreasonEdit")) {// 退菜说明变化
					return PRODUCT_RESON_TUI;
				} else if(str.equals("ProductStatusEdit")) {// 菜品状态变化
					return PRODUCT_STATUS;
				} else if(str.equals("poslsdc")) {// 1.从iPad点餐 2.微信扫码点餐
					return POS_LS_DC;
				} else if(str.equals("loginQt")){ // 退出
					return LOGOUT;
				} else if(str.equals("deleteposlsdc")){ // 推送消息
					return PUSHMSG;
				}
				
				return NOVALUE;
			} catch (Exception ex) {
				return NOVALUE;
			}
		}
	}

}

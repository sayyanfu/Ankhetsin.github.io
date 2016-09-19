
package com.fangbian365.kuaidi.ui.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class HomeBroadcastReceiver extends BroadcastReceiver {
  
    private static ArrayList<IReceiver> mReceivers;
    private static ArrayList<IDataTransfer> mIDataTransfers;

    static {
        mReceivers = new ArrayList<IReceiver>();
        mIDataTransfers = new ArrayList<IDataTransfer>();
    }

    
    @Override
    public void onReceive(Context context, Intent intent) {
        // Dispatch the received event to fragments.
        for (IReceiver receiver : mReceivers) {
            receiver.onReceive(context, intent);
        }
    }

    /**
     * Register the fragment broadcast receiver. <b> The receiver only works in
     * UI thread. This method should be called in
     * {@link Fragment#onAttach(android.app.Activity)}</b>
     * 
     * @param receiver The receiver to register.
     */
    public static void registerFragmentReceiver(IReceiver receiver) {
        mReceivers.add(receiver);
    }

    /**
     * Unregister the fragment broadcast receiver.<b> The receiver only works in
     * UI thread. This method should be called in {@link Fragment#onDestroy()}
     * </b>
     * 
     * @param receiver The receiver to unregister.
     */
    public static void unregisterFragmentReceiver(IReceiver receiver) {
        mReceivers.remove(receiver);
    }
    
    public static void registerIDataTransfer(IDataTransfer dataTransfer) {
    	mIDataTransfers.add(dataTransfer);
    }
    public static void unregisterIDataTransfer(IDataTransfer dataTransfer) {
    	mIDataTransfers.remove(dataTransfer);
    }
    
    public static void onDataTransfer(int type, Object... data) {
    	for (IDataTransfer transfer : mIDataTransfers) {
    		transfer.onDataTransfer(type, data);
        }
    }
    
}

package com.example.ldsdk_xh.interfaces.impl.lf;

import com.example.ldsdk_xh.callback.ResultCallback;
import com.example.ldsdk_xh.interfaces.IDoor;
import com.example.ldsdk_xh.utils.Constant;
import com.example.ldsdk_xh.view.MainActivity;

import android.app.Activity;
import android.app.Service;
import android.widget.Toast;
import cn.com.reformer.rfBleService.BleService;
import cn.com.reformer.rfBleService.BleService.RfBleKey;

public class LFDoorImpl implements IDoor{

    private BleService mService;
    
    private class MyResultCallback implements cn.com.reformer.rfBleService.ResultCallback {
    	
    	private ResultCallback resultCallback;
    	
		public MyResultCallback(ResultCallback resultCallback) {
			this.resultCallback = resultCallback;
		}

		/**   
		 * <p>Title: onResult</p>   
		 * <p>Description: </p>   
		 * @param mac
		 * @param result   
		 * @see cn.com.reformer.rfBleService.ResultCallback#onResult(byte[], int)   
		 */  
		@Override
		public void onResult(byte[] mac, int result) {
			resultCallback.onResult(mac, result);
		}
    }
    
    public LFDoorImpl(Service service,Activity activity) {
    	this.mService = (BleService) service;
	}

	@Override
    public int openDoor(String mac, int time, String password, int timeout, ResultCallback resultCallback) {
	    RfBleKey rfBleKey = mService.getRfBleKey();
	    return rfBleKey.openDoor(mac, time, password, timeout, new MyResultCallback(resultCallback));
    }

}

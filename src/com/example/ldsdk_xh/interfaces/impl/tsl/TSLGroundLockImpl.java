/**  
 * @Title:  TSLGroundLockImpl.java   
 * @Package com.example.ldsdk_xh.interfaces.impl.tsl   
 * @Description:    TODO(��һ�仰�������ļ���ʲô)   
 * @author: wangjy  
 * @date:   2017��12��29�� ����10:28:38   
 * @version V1.0 
 */  
package com.example.ldsdk_xh.interfaces.impl.tsl;

import com.example.ldsdk_xh.callback.ResultCallback;
import com.example.ldsdk_xh.interfaces.IGroundLock;
import com.example.ldsdk_xh.utils.Constant;
import com.terminus.lock.library.CallBack;
import com.terminus.lock.library.Response;
import com.terminus.lock.library.TslBluetoothManager;
import com.terminus.lock.library.response.GarateStatusResponse;
import com.terminus.lock.library.util.Utils;

import android.app.Activity;
import android.util.Log;

/**   
 * @ClassName:  TSLGroundLockImpl   
 * @Description:TODO(������һ�仰��������������)   
 * @author: wangjy
 * @date:   2017��12��29�� ����10:28:38   
 *      
 */
public class TSLGroundLockImpl implements IGroundLock{
	
	TslBluetoothManager manager; // ��˹������������
	
	private Activity activity;
	
	
	public TSLGroundLockImpl(Activity activity) {
		this.activity = activity;
        //SDK��ʼ��
		manager = TslBluetoothManager.getInstance(activity);
	}
	
	private class myCallBack implements CallBack {
		
		private ResultCallback resultCallback;
		private String mac;
		private String decodeCipher;
		
		public myCallBack(ResultCallback resultCallback,String mac, String decodeCipher) {
			this.resultCallback = resultCallback;
			this.mac = mac;
			this.decodeCipher = decodeCipher;
		}
		
		/**   
		 * <p>Title: onFail</p>   
		 * <p>Description: </p>   
		 * @param arg0   
		 * @see com.terminus.lock.library.CallBack#onFail(int)   
		 */  
		@Override
		public void onFail(int result) {
            switch (result) {
            case Response.ERROR_CONN_TIMEOUT:// ����ʧ��
            	resultCallback.onResult(mac.getBytes(), Constant.TIMEOUT);
                break;
            case Response.ERROR_BLUETOOTH_IS_NOT_ENABLED: // ��������
            	resultCallback.onResult(mac.getBytes(), Constant.ERROR_BLUETOOTH_IS_NOT_ENABLED);
                break;
            case Response.ERROR_OPENED:// �����ɹ�
            	resultCallback.onResult(mac.getBytes(), Constant.OPEN_SUCCESS);
                break;
            case Response.ERROR_OPEN_LOCK_KEY_DISABLE:
            	resultCallback.onResult(mac.getBytes(), Constant.ERROR_OPEN_LOCK_KEY_DISABLE);
                break;
            case Response.ERROR_OPEN_LOCK_KEY_DELETED:
            	resultCallback.onResult(mac.getBytes(), Constant.ERROR_OPEN_LOCK_KEY_DELETED);
                break;
            case Response.ERROR_DEVICE_ADDRESS_ILLEGAL:
            	resultCallback.onResult(mac.getBytes(), Constant.ERROR_DEVICE_ADDRESS_ILLEGAL);
                break;
            case Response.ERROR_PASSWORD:
            	resultCallback.onResult(mac.getBytes(), Constant.PASSWD_ERROR);
                break;
            case Response.ERROR_UNSUPPORT_OPERATOR:
            	resultCallback.onResult(mac.getBytes(), Constant.ERROR_UNSUPPORT_OPERATOR);
                break;
        }
			
		}

		/**   
		 * <p>Title: onSuccess</p>   
		 * <p>Description: </p>   
		 * @param arg0   
		 * @see com.terminus.lock.library.CallBack#onSuccess(com.terminus.lock.library.Response)   
		 */  
		@Override
		public void onSuccess(Response response) {
			resultCallback.onResult(mac.getBytes(), Constant.OPEN_SUCCESS);
		}
		
	}
	
	/**   
	 * <p>Title: openGroundLock</p>   
	 * <p>Description: </p>   
	 * @param mac
	 * @param time
	 * @param password
	 * @param timeout
	 * @param resultCallback
	 * @return   
	 * @see com.example.ldsdk_xh.interfaces.IGroundLock#openGroundLock(java.lang.String, int, java.lang.String, int, com.example.ldsdk_xh.callback.ResultCallback)   
	 */  
	@Override
	public int openGroundLock(final String mac, int time, String password, int timeout, final ResultCallback resultCallback) {
		final String decodeCipher = Utils.decodeCipher(password);
		manager.getGarateStatusWithCipher(decodeCipher, new CallBack(){

			@Override
			public void onFail(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Response response) {
				if (response instanceof GarateStatusResponse) {
					boolean currentOpenStatus = !((GarateStatusResponse)response).isOpened();
					Log.d("��������ѯ״̬����ǰ״̬�Ƿ�����=>" + currentOpenStatus,"openGroundStatus");
					if (currentOpenStatus) {
						resultCallback.onResult(mac.getBytes(), 10);//�������ڹر�
					} else {
						resultCallback.onResult(mac.getBytes(), 11);//�������ڿ���
					}
					manager.openRemoteDoor(decodeCipher, "��������", new myCallBack(resultCallback, mac, decodeCipher));
				}
			}
			
		}); 
		return 0;
	}

}

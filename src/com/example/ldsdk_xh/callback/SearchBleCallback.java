package com.example.ldsdk_xh.callback;

import com.example.ldsdk_xh.model.DeviceInfo;

/**
 * <p>���������Ļص�����  </p> 
 * @author wangjy
 * @version 1.0
 */
public interface SearchBleCallback {
	
	/**
	 * <p>ÿ������һ������������ô˷��� </p> 
	 * @param deviceInfo  �豸��Ϣ
	 */
	void onSearchBle(DeviceInfo deviceInfo);
	
	/**
	 * <p>ֹͣ����ʱ����   </p> 
	 */
	void stopSearchBle();
}

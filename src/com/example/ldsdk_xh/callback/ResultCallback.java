package com.example.ldsdk_xh.callback;

/**
 * <p>�����豸�Ļص����������̶�Ӧ��ʹ�ô˻ص� </p> 
 * @author wangjy
 * @version 1.0
 */
public interface ResultCallback {
	
	/**
	 * <p>�����豸֮����ô˷���</p> 
	 * @param mac mac��ַ
	 * @param result   �������     
	 */
    void onResult(byte[] mac, int result);
}

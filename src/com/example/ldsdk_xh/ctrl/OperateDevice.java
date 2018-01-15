package com.example.ldsdk_xh.ctrl;

import com.example.ldsdk_xh.callback.ResultCallback;
import com.example.ldsdk_xh.factory.DeviceFactory;
import com.example.ldsdk_xh.interfaces.IDoor;
import com.example.ldsdk_xh.interfaces.IElevator;
import com.example.ldsdk_xh.interfaces.IEscalator;
import com.example.ldsdk_xh.interfaces.IGroundLock;
import com.example.ldsdk_xh.utils.Constant;

import android.app.Activity;
import android.app.Service;

/**
 * @ClassName:  OperateDevice   
 * @Description:�����豸
 * @author: wangjy
 * @date:   2017��10��23�� ����11:32:24   
 *
 */
public class OperateDevice{

    private IDoor door;
    private IElevator elevator;
    private IEscalator escalator;
    private IGroundLock groundLock;
	/**
	 * @Title:  OperateDevice   
	 * @Description:    ��ʼ���豸������
	 * @param:  @param manufacturerId ����id
	 * @param:  @param typeId ��Դ����id
	 * @param:  @param service  ����service
	 * @throws
	 */
	public OperateDevice(Integer manufacturerId,Integer typeId,Service service,Activity activity){
		//���ù����࣬����manufacturerId��typeIdͨ�������жϳ��̺͵������Ͳ��õ���Ӧ�Ľӿ�
		switch (typeId) {
		case Constant.DOOR:
			door = DeviceFactory.getDoor(manufacturerId, typeId,service,activity);
			break;
		case Constant.ELEVATOR:
			elevator = DeviceFactory.getEscalator(manufacturerId, typeId,service,activity);
			break;
		case Constant.ESCALATOR:
			escalator = DeviceFactory.getElevator(manufacturerId, typeId,service,activity);
			break;
		case Constant.GROUND_LOCK:
			groundLock = DeviceFactory.getGroundLock(manufacturerId, typeId,service,activity);
			break;

		default:
			break;
		}
	}


	/**
	 * @Title: openDoor   
	 * @Description: ����
	 * @param: @param mac �豸mac��ַ
	 * @param: @param time ��ʱʱ��
	 * @param: @param password �豸����
	 * @param: @param timeout ��ʱʱ��
	 * @param: @param resultCallback �ص�
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	public int  openDoor(String mac,int time,String password,int timeout,ResultCallback resultCallback){
	   return door.openDoor(mac, time, password, timeout, resultCallback);
	}
	
	
	/**
	 * @Title: ctrlElevator   
	 * @Description: ���Ƶ���
	 * @param: @param mac �豸mac��ַ
	 * @param: @param password �豸����
	 * @param: @param phone �ֻ�����
	 * @param: @param floor ¥��
	 * @param: @param timeout ��ʱʱ��
	 * @param: @param resultCallback �ص�
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	public int ctrlElevator(String mac, String password, String phone, int floor, int timeout,ResultCallback resultCallback){
		return elevator.ctrlElevator(mac, password, phone, floor, timeout, resultCallback);
	}
	
	
	/**
	 * @Title: ctrlEscalator   
	 * @Description: ���� 
	 * @param: @param mac �豸mac��ַ
	 * @param: @param password �豸����
	 * @param: @param phone �ֻ�����
	 * @param: @param upOrDown �ϻ����� 0:up,1:down
	 * @param: @param timeout ��ʱʱ��
	 * @param: @param resultCallback �ص�
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	public int ctrlEscalator(String mac,String password,String phone,int upOrDown,int timeout,ResultCallback resultCallback){
		return escalator.ctrlEscalator(mac, password, phone, upOrDown, timeout, resultCallback);
    }
	/**
	 * @Title: ctrlEscalator   
	 * @Description: ����
	 * @param: @param mac �豸mac��ַ
	 * @param: @param password �豸����
	 * @param: @param phone �ֻ�����
	 * @param: @param upOrDown �ϻ����� 0:up,1:down
	 * @param: @param timeout ��ʱʱ��
	 * @param: @param resultCallback �ص�
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	public int ctrlGroundLock(String mac,int time,String password,int timeout,ResultCallback resultCallback){
		return groundLock.openGroundLock(mac, time, password, timeout, resultCallback);
	}
}

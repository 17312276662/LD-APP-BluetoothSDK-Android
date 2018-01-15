package com.example.ldsdk_xh.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;

import com.example.ldsdk_xh.bean.ResourceData;
import com.example.ldsdk_xh.bean.ResourceKey;
import com.example.ldsdk_xh.model.Content;
import com.example.ldsdk_xh.model.DeviceInfo;
import com.example.ldsdk_xh.model.WebReturnData;
import com.example.ldsdk_xh.utils.Constant;
import com.example.ldsdk_xh.utils.OkHttpUtil;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * ����������Դ����
 * @author wangjy
 *
 */
public class DeviceResourceDataDispose {
	
	private static String GET_PUBLIC_RESOURCE_URL = "http://43.254.53.219:8080/LD-PermissionSystem/appApi/queryPubResByBuildingId";
	private static String GET_PRIVATE_RESOURCE_URL = "http://43.254.53.219:8080/LD-PermissionSystem/appApi/queryPrivateResByBIdAndMobile";
	private static String GET_ALL_RESOURCE_URL = "http://auth.greenlandjs.com:8099/LD-PermissionSystem/appApi/queryAvaiableResByMobile";
	public static String TOKEN_KEY = "123qweASDzxc";
	
	/**
	 * @Title: queryAndSaveAllResourceData   
	 * @Description: ��ѯ��Ӧ��Դ��Ϣ�б����ڻ�����  
	 * @param: @param buildingId ������ѯ������Դ
	 * @param: @param mobile
	 * @param: @return      
	 * @return: boolean    true ��ʾ����ɹ�  false ����ʧ�� ��ʾ����  
	 * @throws
	 */
	public static boolean cacheDeviceData(Context context,Integer buildingId,String mobile){
		SharedPreferences sharedPreferences = context.getSharedPreferences("resource-datas", Context.MODE_PRIVATE);
		String buildingIdStr = "";
		if (buildingId != null) {
			buildingIdStr = buildingId.toString();
		}
		/*String pubJson = getPubResourceData(buildingIdStr);
		String priJson = getPriResourceData(mobile, buildingIdStr);*/
		String allJson = getAllResourceData(mobile, buildingIdStr);
		Gson gson = new Gson();
		//��취���  �ܲ���ÿ������������ʱ������ Ӧ�����û�ѡ��¥���ʱ�� 
		WebReturnData webReturnDatas = gson.fromJson(allJson, WebReturnData.class);
		if (webReturnDatas == null || webReturnDatas.getStatus() != 1) {// �����������������粻�� 
			//��ʾ�Ѿ���������Ͳ��û����˻�����������
			if (!sharedPreferences.getAll().isEmpty()) {
				return true;
			}
			if (webReturnDatas != null) {
				Log.e("����״̬", webReturnDatas.getStatus().toString());
			}
			return false;
		}
		List<Content> contents = webReturnDatas.getContent();
		Editor edit = sharedPreferences.edit();
		for (Content content : contents) {
			List<ResourceData> resourceDatas = content.getResourceDatas();
			
			if (resourceDatas != null && resourceDatas.size() > 0) {

				
				/********************����Ϊ�������ݣ���ɾ��************************/
				if (content.getId() == 1) {
					ResourceData resourceData1 = new ResourceData();
					resourceData1.setId(111);
					resourceData1.setName("��˹��-�Ž�1");
					resourceData1.setTypeId(Constant.DOOR);
					resourceData1.setBuildingId(2);
					resourceData1.setBuildingName("�̵�֮��C-2");
					
					List<ResourceKey> resourceKeys1 = new ArrayList<ResourceKey>();
					
					ResourceKey resourceKey1 = new ResourceKey();
					resourceKey1.setMac("3481F40D5FC1");
					resourceKey1.setManufacturerId(1);
					resourceKey1.setPassword("6272D7497DF62C190441CD5F8D3E88801651238D0F7EEE953753730D0F7FAE9D");
					
					resourceKeys1.add(resourceKey1);
					resourceData1.setResourceKeys(resourceKeys1);
					
					resourceDatas.add(resourceData1);
					
				}
				if (content.getId() == 2) {
					ResourceData resourceData1 = new ResourceData();
					resourceData1.setId(111);
					resourceData1.setName("����-�Ž�1");
					resourceData1.setTypeId(Constant.DOOR);
					resourceData1.setBuildingId(2);
					resourceData1.setBuildingName("�̵�֮��C-1");
					
					List<ResourceKey> resourceKeys1 = new ArrayList<ResourceKey>();
					
					ResourceKey resourceKey1 = new ResourceKey();
					resourceKey1.setMac("32357158536D373773");
					resourceKey1.setManufacturerId(0);
					resourceKey1.setPassword("4c464b47396764765737336f51317936");
					ResourceKey resourceKey3 = new ResourceKey();
					resourceKey3.setMac("32357158536D373774");
					resourceKey3.setManufacturerId(0);
					resourceKey3.setPassword("4c464b47396764765737336f51317936");
					
					resourceKeys1.add(resourceKey1);
					resourceKeys1.add(resourceKey3);
					resourceData1.setResourceKeys(resourceKeys1);
					
					resourceDatas.add(resourceData1);
					
				}
				/************************************************************************/
				for (ResourceData resourceData : resourceDatas) {
					List<ResourceKey> resourceKeys = resourceData.getResourceKeys();
					for (ResourceKey resourceKey : resourceKeys) {
						String json = gson.toJson(resourceData);
						edit.putString(resourceKey.getMac(), json);
					}
				}
			}
		}
		boolean commit = edit.commit();
		return commit;
	}
	
	/**
	 * �鿴�豸ɨ�������mac�Ƿ��ڻ�����
	 * @param device
	 * @return ��������õķ�����Դ���ݣ����ǵķ���null
	 */
	public static DeviceInfo isUsefulDevice(final Context context,String mac){
		Gson gson = new Gson();
		SharedPreferences preferences = context.getSharedPreferences("resource-datas", Context.MODE_PRIVATE);
		String result = preferences.getString(mac, Constant.UN_USEFUL);
		if (!Constant.UN_USEFUL.equals(result)) {
			ResourceData resourceData = gson.fromJson(result, ResourceData.class);
			DeviceInfo deviceInfo = new DeviceInfo();
			Integer deviceId = resourceData.getId();
			deviceInfo.setId(deviceId);
			deviceInfo.setMac(mac);
			deviceInfo.setName(resourceData.getName());
			deviceInfo.setTypeId(resourceData.getTypeId());
			deviceInfo.setTypeName(resourceData.getTypeName());
			deviceInfo.setBuildingId(resourceData.getBuildingId());
			deviceInfo.setBuildingName(resourceData.getBuildingName());
			List<ResourceKey> resourceKeys = resourceData.getResourceKeys();
			Integer manufacturerId;
			for (ResourceKey resourceKey : resourceKeys) {
				if (resourceKey.getMac().equals(mac)) {
					manufacturerId  = resourceKey.getManufacturerId();
					deviceInfo.setManufacturerId(manufacturerId);
					deviceInfo.setPassword(resourceKey.getPassword());
					return deviceInfo;
				}
			}
		}
		return null;
	}
	
	/**
	 * �õ������豸��json
	 * @param buildingId
	 * @param token
	 * @return
	 */
	private static String getPubResourceData(String buildingId){
		String token = DigestUtils.md5Hex("buildingId="+buildingId+"&"+TOKEN_KEY);
		GET_PUBLIC_RESOURCE_URL += "?buildingId="+buildingId+"&token="+token;
		String json = "";
		try {
			json = OkHttpUtil.doGet(GET_PUBLIC_RESOURCE_URL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	private static String getPriResourceData(String mobile,String buildingId){
		SortedMap<String, String> sortParams = new TreeMap<String, String>();
		StringBuffer encodeStr =  new StringBuffer();
		sortParams.put("mobile", mobile);
		//����
		sortParams.put("buildingId", buildingId);
		//��ȡҪ���ܵ�string
		Set es = sortParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v)) {
				encodeStr.append(k + "=" + v + "&");
			}
		}
		encodeStr.append(TOKEN_KEY);
		String token = DigestUtils.md5Hex(encodeStr.toString());
		GET_PRIVATE_RESOURCE_URL += "?mobile="+mobile+"&buildingId="+buildingId+"&token="+token;
		String json = "";
		try {
			json = OkHttpUtil.doGet(GET_PRIVATE_RESOURCE_URL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	/**
	 * @Title: getAllResourceData   
	 * @Description: �õ�����˽�к͹��е��豸
	 * @param: @param mobile
	 * @param: @param buildingId
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	private static String getAllResourceData(String mobile,String buildingId) {
		SortedMap<String, String> sortParams = new TreeMap<String, String>();
		StringBuffer encodeStr =  new StringBuffer();
		sortParams.put("mobile", mobile);
		//����
		sortParams.put("buildingId", buildingId);
		//��ȡҪ���ܵ�string
		Set es = sortParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v)) {
				encodeStr.append(k + "=" + v + "&");
			}
		}
		encodeStr.append(TOKEN_KEY);
		String token = DigestUtils.md5Hex(encodeStr.toString());
		GET_ALL_RESOURCE_URL += "?mobile="+mobile+"&buildingId="+buildingId+"&token="+token;
		String json = "";
		try {
			json = OkHttpUtil.doGet(GET_ALL_RESOURCE_URL);
		} catch (IOException e) {
			//��ʾ����ʧ��
			Log.e("getAllResourceData", "��ȡweb�ӿ�����ʧ�ܣ���������");
			return null;
		}
		return json;
	}
	
}

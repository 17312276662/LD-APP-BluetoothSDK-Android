package com.example.ldsdk_xh.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.ldsdk_xh.R;
import com.example.ldsdk_xh.adapter.MyBaseExpandableListAdapter;
import com.example.ldsdk_xh.callback.BindServiceCallback;
import com.example.ldsdk_xh.callback.ResultCallback;
import com.example.ldsdk_xh.callback.SearchBleCallback;
import com.example.ldsdk_xh.ctrl.BluetoothSearch;
import com.example.ldsdk_xh.ctrl.DeviceResourceDataDispose;
import com.example.ldsdk_xh.ctrl.OperateDevice;
import com.example.ldsdk_xh.model.DeviceInfo;
import com.example.ldsdk_xh.utils.Constant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	/*************��ҳ��ؼ�************************/
	//���������豸�İ�ť
	private Button search_ble;
	private TextView operate_status;
	
	/***********MyBaseExpandableListAdapter******/
	private ExpandableListView expandlist;
	private List<String> buildingNameList;
	private Map<Integer, List<String>> blueNameMap;//�����豸��map
	private MyBaseExpandableListAdapter expandableListAdapter;
	
	/***************����***************************/
	//�豸��Ϣ��map����  ��Ϊ�豸��Դ���� ֵΪ�豸��Դ��Ϣ
	Map<String, DeviceInfo> deviceInfos;
	//buildingId��ExpandableList��������Ӧ��ϵ
	Map<Integer, Integer> buildingId_groupId_map;
	//����id
	private Integer manufacturerId;
	//��ͬ���̵�servicemap���� ��Ϊ����id ֵΪ��Ӧservice
	private Map<String,Service> serviceMap;
	//��������������
	private BluetoothSearch bluetoothSearch;
	//����״̬
	private boolean scan = false;
	//group����
	private Integer groupIndex;
	
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();//��ʼ������
		initView();//��ʼ���ؼ�
		initEvent();//��ʼ���¼�
		buildEnv();
	}
	
	/**
	 * @Title: initData   
	 * @Description: ��ʼ������
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	private void initData() {
		buildingNameList = new ArrayList<String>();
		blueNameMap = new HashMap<Integer, List<String>>();
		deviceInfos = new HashMap<String, DeviceInfo>();
		serviceMap = new HashMap<String, Service>();
		buildingId_groupId_map = new HashMap<Integer, Integer>();
		//��ȡѡ��¥�������й���˽�������豸���뻺��(�������������ȡ) ���Ӧ����ѡ���Ǹ�¥��ʱ����� 
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = DeviceResourceDataDispose.cacheDeviceData(MainActivity.this,null,"17366001910");
				if (!result) {//��ʾû����������
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(MainActivity.this, "������֮�󣬹ص����������", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * @Title: initView   
	 * @Description: ��ʼ��ҳ��ؼ�
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	private void initView() {
		expandlist = (ExpandableListView) findViewById(R.id.expandlist);
		expandlist.setGroupIndicator(null);//���ﲻ��ʾϵͳĬ�ϵ�group indicator
		
		search_ble = (Button) findViewById(R.id.search_ble);
		search_ble.setOnClickListener(this);
		operate_status = (TextView) findViewById(R.id.operate_status);
		expandableListAdapter = new MyBaseExpandableListAdapter(MainActivity.this, buildingNameList, blueNameMap);
		expandlist.setAdapter(expandableListAdapter);
	}
	
	/**   
	 * @Title: initEvent   
	 * @Description: ��ʼ���¼�
	 * @param:       
	 * @return: void      
	 * @throws   
	 */
	private void initEvent() {
		expandlist.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				String blueName = (String) expandableListAdapter.getChild(groupPosition, childPosition);
				DeviceInfo deviceInfo = deviceInfos.get(blueName);
				manufacturerId = deviceInfo.getManufacturerId();
				OperateDevice invokeActivity = new OperateDevice(manufacturerId, deviceInfo.getTypeId(),serviceMap.get(manufacturerId.toString()),MainActivity.this);
				String mac = deviceInfo.getMac();
				if (scan) {
					scan = false;
					search_ble.setText(R.string.re_search);
					operate_status.setText(R.string.searched);
				}
				bluetoothSearch.stopScanBleDevice();
				//�����ж������ĸ�ִ��ʲô 
				if (deviceInfo.getTypeId() == Constant.DOOR) {
					int res = invokeActivity.openDoor(mac, Integer.decode("1"), deviceInfo.getPassword(), 2000, resultCallback);
					if (res == 0) {
						operate_status.setText("������...");
					}
				}
				return true;
			}
		});
		
	}
	
	/**
	 * ҳ��ĵ���¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_ble:
			if (!scan) {
				//����֮ǰ����������
				groupIndex = 0;
				deviceInfos.clear();
				buildingId_groupId_map.clear();
				buildingNameList.clear();
				blueNameMap.clear();
				expandableListAdapter.notifyDataSetChanged();
				
				bluetoothSearch = new BluetoothSearch(MainActivity.this,searchBleCallback,bindServiceCallback);
				bluetoothSearch.scanBleDevice(10000);
				search_ble.setText(R.string.stop_search);
				operate_status.setText(R.string.searching);
				scan = true;
			} else {
				bluetoothSearch.stopScanBleDevice();
				search_ble.setText(R.string.re_search);
				operate_status.setText(R.string.searched);
				scan = false;
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * �����г���service�Ļص�����
	 * serviceMap ���ǳ���id ֵ�Ƕ�Ӧ��service
	 */
	private BindServiceCallback bindServiceCallback = new BindServiceCallback() {
		@Override
		public void onBind(Map<String, Service> serviceMap) {
			MainActivity.this.serviceMap = serviceMap;
		}
	};
	/**
	 * �����豸֮��Ļص�����
	 * iΪ�������
	 */
	private ResultCallback resultCallback = new ResultCallback() {
		
		@Override
		public void onResult(byte[] bytes, int i) {
			 final int result = i;
			 runOnUiThread(new Runnable() {
				public void run() {
					switch (result) {
					case 0:
						Toast.makeText(MainActivity.this, "���ųɹ�", Toast.LENGTH_SHORT).show();
						break;
					case 1:
						Toast.makeText(MainActivity.this, "�������", Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(MainActivity.this, "�����ж�", Toast.LENGTH_SHORT).show();
						break;
					case 3:
						Toast.makeText(MainActivity.this, "��ʱ", Toast.LENGTH_SHORT).show();
						break;
					case Constant.ERROR_BLUETOOTH_IS_NOT_ENABLED:
						Toast.makeText(MainActivity.this, "�����豸�쳣", Toast.LENGTH_SHORT).show();
						break;
					case Constant.ERROR_OPEN_LOCK_KEY_DISABLE:
						Toast.makeText(MainActivity.this, "Կ�ױ�����", Toast.LENGTH_SHORT).show();
						break;
					case Constant.ERROR_OPEN_LOCK_KEY_DELETED:
						Toast.makeText(MainActivity.this, "Կ�ױ����", Toast.LENGTH_SHORT).show();
						break;
					case Constant.ERROR_DEVICE_ADDRESS_ILLEGAL:
						Toast.makeText(MainActivity.this, "mac��ַ����", Toast.LENGTH_SHORT).show();
						break;
					case Constant.ERROR_UNSUPPORT_OPERATOR:
						Toast.makeText(MainActivity.this, "�豸��֧��4.0����", Toast.LENGTH_SHORT).show();
						break;
					}
				}
			});
		}
	};
	
	/**
	 * �Զ�������������ص���������
	 * deviceInfo Ϊ������һ�������豸 ����һ���豸��Ϣ
	 */
	private SearchBleCallback searchBleCallback = new SearchBleCallback() {
		/**
		 * ÿ������һ���ͻ����һ��
		 */
		@Override
		public void onSearchBle(DeviceInfo deviceInfo) {
			Integer buildingId = deviceInfo.getBuildingId();
			//buildingId_groupId_map ¥����group��������Ӧmap
			Integer index = buildingId_groupId_map.get(buildingId);
			if (index == null) {
				if (buildingId_groupId_map.size() > 0) {
					groupIndex++;
				}
				buildingId_groupId_map.put(buildingId, groupIndex);
			}
			//���豸��Դ�����豸��Ϣ��Ӧ������childitem�����ʱ����ҵ�
			String name = deviceInfo.getName();
			deviceInfos.put(name, deviceInfo);
			List<String> buildingNames = blueNameMap.get(groupIndex);
			//���buildingIds==null �͸�buildingid��Ӧ��list��ʼ��
			if (buildingNames == null) {
				buildingNames = new ArrayList<String>();
			}
			//����Ź�ͬ�����豸��Դ�Ͳ���
			if (!buildingNames.contains(deviceInfo.getName())) {
				buildingNames.add(deviceInfo.getName());
			}
			//��֤���key�Ѿ���map���ˣ��ͼ���ȥ
			if (!blueNameMap.containsKey(groupIndex)) {
				buildingNameList.add(deviceInfo.getBuildingName());
			}
			blueNameMap.put(groupIndex, buildingNames);
			runOnUiThread(new Runnable() {
				public void run() {
					expandableListAdapter.notifyDataSetChanged();
					expandlist.expandGroup(groupIndex);
				}
			});
		}
		/**
		 * ������������
		 */
		@Override
		public void stopSearchBle() {
			scan = false;
			operate_status.setText(R.string.search_complete);
			search_ble.setText(R.string.re_search);
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**   
	 * @Title: buildEnv   
	 * @Description: ���ڰ�׿6.0�Ƕ�̬����λ����ϢȨ�ޣ�����Ҫ�жϣ���̬ѯ��Ȩ��
	 * @param:       
	 * @return: void      
	 * @throws   
	 */
	@SuppressLint("NewApi")
	private void buildEnv() {
		// Ҫ�����Ȩ�� ���� ����ͬʱ������Ȩ��
	    String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION};
		if (Build.VERSION.SDK_INT >= 23) {
			  //�������6.0����Ҫ��̬Ȩ�ޣ�������Ҫ��̬Ȩ��
	        //���ͬʱ������Ȩ�ޣ�����forѭ������
	        int check = checkSelfPermission(permissions[0]);
	        if (check == PackageManager.PERMISSION_DENIED) {
	        	//�ֶ�ȥ�����û���Ȩ��(��������������Ӷ��Ȩ��) 1 Ϊ������ һ������Ϊfinal��̬����
	            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
			}
		}
	}
	@SuppressLint("NewApi")
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
	    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	    //�ص����ж��û����׵���ǻ��Ƿ�
	    //���ͬʱ������Ȩ�ޣ�����forѭ������
	    if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
	    	
	    } else {
	        // û�л�ȡ ��Ȩ�ޣ��������󣬻��߹ر�app
	        Toast.makeText(this,"��Ҫ���λ��Ȩ�ޣ����ֶ�����ǰӦ�ø���λ��Ȩ��",Toast.LENGTH_LONG).show();
	        finish();
	    }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bluetoothSearch != null) {
			bluetoothSearch.unBindService();
		}
	}
}

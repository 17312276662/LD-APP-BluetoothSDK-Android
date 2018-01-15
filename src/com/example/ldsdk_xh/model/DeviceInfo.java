package com.example.ldsdk_xh.model;

/**
 * <p>�����豸��</p>  
 * @author wangjy
 * @version 1.0
 */
public class DeviceInfo {
	private Integer id;
	private String name;
	private Integer typeId;
	private String typeName;
	private String mac;
	private Integer manufacturerId;
	private String password;
	private Integer buildingId;
	private String buildingName;
	
	/**
	 * <p>�õ��豸����id</p>   
	 * @return Integer      
	 */
	public Integer getManufacturerId() {
		return manufacturerId;
	}
	/**
	 * <p>�����豸����id</p>   
	 */
	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	/**
	 * <p>�õ��豸��Դ����id</p>   
	 * @return Integer      
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * <p>������Դ����id</p>   
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * <p>�õ��豸��Դ���ƣ���������ʾ</p>   
	 * @return String      
	 */
	public String getName() {
		return name;
	}
	/**
	 * <p>�����豸��Դ����</p>   
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * <p>�õ��豸��Դ����id</p>   
	 * @return Integer      
	 */
	public Integer getTypeId() {
		return typeId;
	}
	/**
	 * <p>�����豸��Դ����id</p>   
	 */
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	/**
	 * <p>�õ��豸��Դ��������</p>   
	 * @return String      
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * <p>�����豸��Դ��������</p>   
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * <p>�õ��豸��Դmac��ַ</p>   
	 * @return String      
	 */
	public String getMac() {
		return mac;
	}
	/**
	 * <p>�����豸��Դmac��ַ</p>   
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}
	/**
	 * <p>�õ��豸��Դ��������</p>   
	 * @return String      
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * <p>�����豸��Դ��������</p>   
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * <p>�õ�¥������</p>   
	 * @return String      
	 */
	public String getBuildingName() {
		return buildingName;
	}
	/**
	 * <p>����¥������</p>   
	 */
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	/**
	 * <p>�õ�¥��id</p>   
	 * @return Integer      
	 */
	public Integer getBuildingId() {
		return buildingId;
	}
	/**
	 * <p>����¥��id</p>   
	 */
	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}
	
}

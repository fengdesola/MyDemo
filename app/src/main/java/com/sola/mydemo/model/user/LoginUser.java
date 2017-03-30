package com.sola.mydemo.model.user;

import com.sola.baselib.model.BaseVo;

import org.json.JSONObject;

/**
 * 
 * 
 */

public class LoginUser extends BaseVo {
	
	/**
	 * tenantName : 湘潭慢病
	 * roleName : 患者
	 * lastLoginTime : 2016-12-05 13:21:48
	 * password : 59d72d8cdb0674e34bdfc93e2d8ff09a5d71b0ce4e34e7a14c7cb92ac0654855
	 * roleId : patient
	 * manageUnit : coms
	 * tenantId : coms.xiangtan
	 * userId : d505a7df-4ebd-4682-817a-d038a1f575a5
	 * userName : 一坨屎
	 * displayName : 湘潭慢病-null-患者
	 * lastIPAddress : 122.224.131.226
	 * lastUserAgent : UNKNOWN,null,UNKNOWN
	 */
	
	public boolean isVisitor;//是否游客，自己维护
	public String phone;//手机号，自己维护

	public String tenantName;
	public String roleName;
	public String lastLoginTime;
	public String password;
	public String roleId;
	public String manageUnit;
	public String tenantId;
	public String userId;
	public String userName;
	public String displayName;
	public String lastIPAddress;
	public String lastUserAgent;


	public boolean isVisitor() {
		return isVisitor;
	}
}

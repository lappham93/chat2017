package com.mit.roles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mit.user.enums.AdminType;

public class Privilege {
	private static Map<Integer, Privilege> privilegeMap;
	
	public static Map<Integer, Privilege> getPrivilege() {
		if (privilegeMap == null) {
			privilegeMap = new HashMap<>();
			Privilege adminSystem = new Privilege();
			adminSystem.setRole("System Admin");
			adminSystem.setActions(Action.getSystemAdminActions());
			privilegeMap.put(AdminType.SYSTEM_ADMIN.getValue(), adminSystem);
			
			Privilege regionManager = new Privilege();
			regionManager.setRole("Region Manager");
			regionManager.setActions(Action.getRegionManagerActions());
			privilegeMap.put(AdminType.REGION_MANAGER.getValue(), regionManager);
		}
		
		return privilegeMap;
	}
	
	private String role;
	private List<Action> actions;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

}

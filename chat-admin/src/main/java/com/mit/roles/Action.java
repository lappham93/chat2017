package com.mit.roles;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Action {
	private static List<Action> systemAdminActions;
	private static List<Action> regionManagerActions;
	
	private String uri;
	private List<String> methods;
	private boolean allMethod;

	public Action(String uri, List<String> methods) {
		super();
		this.uri = uri;
		this.methods = methods;
		this.allMethod = false;
	}
	
	public Action(String uri) {
		super();
		this.uri = uri;
		this.methods = null;
		this.allMethod = true;
	}

	public Action() {
		super();
	}
	
	public static List<Action> getSystemAdminActions() {
		if (systemAdminActions == null) {
			systemAdminActions = new LinkedList<>();
			systemAdminActions.add(new Action(""));
		}
		return systemAdminActions;
	}
	
	public static List<Action> getRegionManagerActions() {
		if (regionManagerActions == null) {
			regionManagerActions = new LinkedList<>();
			regionManagerActions.add(new Action("/logout"));
			regionManagerActions.add(new Action("/home", Arrays.asList("GET")));
			regionManagerActions.add(new Action("/promotion/event"));
			regionManagerActions.add(new Action("/service/service"));
			regionManagerActions.add(new Action("/zipcode"));
		}
		return regionManagerActions;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

	public boolean isAllMethod() {
		return allMethod;
	}

	public void setAllMethod(boolean allMethod) {
		this.allMethod = allMethod;
	}

}

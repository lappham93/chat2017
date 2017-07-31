package com.mit.utils;

import com.mit.configer.ConfigUtils;

public class AdminConstant {
	public static enum updateType {
		INFO(1), PHOTO(2), OTHER(3);
		
		private int value;
		
		private updateType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
		
	}
	
	public static final int succCode = 0;
	public static final int errCode = -1;
	//response message
	public static final String loginErrMsg = "Invalid username or password";
	public static final String savePhotoErrMsg = "Can't save photo";
	public static final String uniqueErrMsg = "%s has already existed";
	public static final String serverErrMsg = "Server Error";
	public static final String permissionLimitMsg = "Permission Limit";
	public static final String invalidErrMsg = "Invalid %s";
	
	public static final String loginSuccessMsg = "Login successfully";
	public static final String postSuccessMsg = "Add successfully";
	public static final String getSuccessMsg = "Get successfully";
	public static final String putSuccessMsg = "Edit successfully";
	public static final String deleteSuccessMsg = "Delete successfully";
	
	public static final String checkrExist = "Candidate has been created already";
	public static final String checkrMissInfo = "Please fill candidate infomation";
	public static final String checkrCandidateRequired = "Candidate is not found";
	public static final String checkrCandidateReportProcessing = "Candidate is in report processing";
	
	public static final int pageSize = ConfigUtils.getConfig().getInt("admin.paging.pageSize", 50);
	public static final int lessPageSize = ConfigUtils.getConfig().getInt("admin.paging.lessPageSize", 10);
	
	public static final String[] disqualifiedReasons = {"insufficient personal info", "insufficient business info", "banking info" };
	public static final String[] rejectedReasons = {"insufficient background check data"};
	
	public static final int photoSize = 250;
	
}

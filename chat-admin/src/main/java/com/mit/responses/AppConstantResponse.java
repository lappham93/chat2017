package com.mit.responses;

import com.mit.app.entities.AppConst;
import com.mit.app.entities.ListIntAppConst;
import com.mit.app.entities.ListLongAppConst;
import com.mit.app.entities.ListStringAppConst;
import com.mit.app.entities.MapStringAppConst;
import com.mit.app.entities.SetStringAppConst;
import com.mit.utils.AdminUtils;

public class AppConstantResponse extends ActiveTime {
	private String key;
	private Object value;
	private String desc;
	private boolean isElement;
	private boolean isCollection;
	private boolean isMap;
	
	public AppConstantResponse(AppConst appConst) {
		this.key = appConst.getKey();
		this.value = appConst.getValue();
		this.desc = appConst.getDesc();
		this.updatedDate = AdminUtils.date2String(appConst.getUpdatedDate());
		if (appConst instanceof ListIntAppConst || appConst instanceof ListLongAppConst
				|| appConst instanceof ListStringAppConst || appConst instanceof SetStringAppConst) {
			this.isCollection = true;
		} else if (appConst instanceof MapStringAppConst) {
			this.isMap = true;
		} else  {
			this.isElement = true;
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isElement() {
		return isElement;
	}

	public void setElement(boolean isElement) {
		this.isElement = isElement;
	}

	public boolean isCollection() {
		return isCollection;
	}

	public void setCollection(boolean isCollection) {
		this.isCollection = isCollection;
	}

	public boolean isMap() {
		return isMap;
	}

	public void setMap(boolean isMap) {
		this.isMap = isMap;
	}

}

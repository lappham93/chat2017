package com.mit.controllers;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mit.app.entities.AppConst;
import com.mit.app.repositories.AppConstRepo;
import com.mit.app.services.AppConstManager;
import com.mit.bodies.AppConstBody;
import com.mit.http.ApiResponse;
import com.mit.responses.AppConstantResponse;
import com.mit.utils.AdminConstant;
import com.mit.utils.AdminUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "AppConst controller")
@RestController
@RequestMapping(value = "/app-constant")
public class AppConstController {
	@Autowired
	private AppConstRepo appConstRepo;
	@Autowired
	private AppConstManager appConstManager;

	@ApiOperation(value = "render app constant")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView renderAppConstant() {
		ModelAndView rs = AdminUtils.initPage("app_constant", "App Constant", null, "isAppConstantTab");
		
		List<AppConst> appConsts = appConstRepo.getAll();
		rs.addObject("constants", buildAppConstantList(appConsts));
		return rs;
	}
	
	@ApiOperation(value = "get app constant")
	@RequestMapping(value = "/{key}/get", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponse<Object> getAppConstant(HttpServletRequest request, @PathVariable(value = "key") String key) {
		AppConst appConst = appConstRepo.getByKey(key);
		if (appConst == null) {
			return new ApiResponse<>(AdminConstant.errCode, AdminConstant.serverErrMsg);
		}
		return new ApiResponse<Object>(buildAppConstant(appConst));
	}
	
	@ApiOperation(value = "edit app constant")
	@RequestMapping(value = "/{key}/edit", method = RequestMethod.PUT)
	@ResponseBody
	public ApiResponse<Object> editAppConstant(HttpServletRequest request, @PathVariable(value = "key") String key, @RequestBody AppConstBody body) {
		AppConst appConst = appConstRepo.getByKey(key);
		if (appConst == null) {
			return new ApiResponse<>(AdminConstant.errCode, AdminConstant.serverErrMsg);
		}
		try {
			appConst.setValue(body.getValue());
			appConstRepo.save(appConst, false);
			appConstManager.deleteCache(appConst.getKey());
		} catch (Exception e) {
			return new ApiResponse<>(AdminConstant.errCode, String.format(AdminConstant.invalidErrMsg, "value"));
		}
		return new ApiResponse<Object>(0, AdminConstant.putSuccessMsg);
	}
	
	public List<AppConstantResponse> buildAppConstantList(List<AppConst> appConstList) {
		if (CollectionUtils.isEmpty(appConstList)) {
			return null;
		}
		List<AppConstantResponse> appConstantResponses = new LinkedList<>();
		for (AppConst appConst: appConstList) {
			appConstantResponses.add(buildAppConstant(appConst));
		}
		return appConstantResponses;
	}
	
	public AppConstantResponse buildAppConstant(AppConst appConst) {
		return new AppConstantResponse(appConst);
	}
}

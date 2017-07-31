package com.mit.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mit.asset.services.PhotoService;
import com.mit.common.enums.ObjectType;
import com.mit.http.ApiResponse;
import com.mit.utils.AdminConstant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "upload controller")
@Controller
@RequestMapping(value = "/upload")
public class UploadController {
	@Autowired
	private PhotoService photoService;

	@ApiOperation(value = "upload photo")
	@RequestMapping(value = "/photo", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse<List<Long>> uploadPhoto(HttpServletRequest request,
			@RequestParam(name = "type", required = true) int type,
			@RequestParam(name = "photos", required = true) MultipartFile[] files,
			@RequestParam(name = "photos", required = true) Collection<Part> parts) throws IOException, ServletException {
		ApiResponse<List<Long>> rs = new ApiResponse<>();
		ObjectType objectType = ObjectType.getType(type);
		List<Long> photoIds = new LinkedList<>();
		int err = 0;
		if (objectType != null) {
			err = photoService.saveMultiDataPhoto(parts, objectType, photoIds);
		} else {
			err = -1;
		}
		if (err < 0) {
			rs.setCode(AdminConstant.errCode);
			rs.setMsg(AdminConstant.savePhotoErrMsg);
		} else {
			rs.setData(photoIds);
		}
		
		return rs;
	}
}

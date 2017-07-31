package com.mit.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import com.mit.address.entities.Address;
import com.mit.address.entities.State;
import com.mit.address.entities.ZipCode;
import com.mit.address.repositories.StateRepo;
import com.mit.address.repositories.ZipCodeRepo;
import com.mit.app.Application;
import com.mit.sessions.AdminSession;
import com.mit.user.entities.AdminProfile;
import com.mit.user.entities.Profile;
import com.mit.user.entities.RegionManagerProfile;

@Service
public class AdminUtils {
	public static String date2String(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return sdf.format(date);
	}
	
	public static int clampPage(int page, int totalRecord, int pageSize) {
		int maxPage = totalRecord % pageSize == 0 ? totalRecord/pageSize : totalRecord/pageSize + 1;
		return Math.max(0, Math.min(page, maxPage));
	}
	
	public static ModelAndView initPage(String template, String breadCrumb, AdminPaging paging, String... tabs) {
		boolean isPaging =  paging != null && (!org.apache.commons.lang3.StringUtils.isEmpty(paging.getNext()) || !StringUtils.isEmpty(paging.getPrevious()));
		ModelAndView mav = new ModelAndView(template);
		int from = 0;
		mav.addObject("breadcrumbTxt", breadCrumb);
		mav.addObject("isPaging", isPaging);
		if (isPaging) {
			mav.addObject("pagingPrev", paging.getPrevious());
			mav.addObject("pagingNext", paging.getNext());
			from = paging.getFrom();
		}
		mav.addObject("pagingFrom", from);
		if (tabs != null) {
			for (String tab: tabs) {
				mav.addObject(tab, true);
			}
		}
		return mav;
	}
	
	public static String buildCode(ZipCode zipCode) { 
		if (zipCode == null) {
			return "";
		}
		return zipCode.getStateName() + ", " + zipCode.getCityName() + ", " + zipCode.getCode();
	}
	
	public static String buildAddress(Address address) {
		if (address == null) {
			return "";
		}
		if (!StringUtils.isEmpty(address.getFullAddress())) {
			return address.getFullAddress();
		}
		return String.format("%s, %s, %s", StringUtils.defaultString(address.getAddressLine1(), "-"), 
				StringUtils.defaultString(address.getCity(), "-"), StringUtils.defaultString(address.getState(), "-"));
	}
	
	public static String buildLink(String servletPath, Map<String, String[]> paramMap) {
		String link = servletPath;
		if (!CollectionUtils.isEmpty(paramMap)) {
			link += "?";
			for (String key : paramMap.keySet()) {
				for (String value : paramMap.get(key)) {
					link += key + "=" + value + "&";
				}
			}
			link = link.substring(0, link.length() - 1); 
		}
		return link;
	}
	
	public static String buildLink(HttpServletRequest request, Map<String, String[]> newParamMap) {
		if (request == null) {
			return "";
		}
		Map<String, String[]> paramMap = new HashMap<String, String[]>();
		for (String param : request.getParameterMap().keySet()) {
			paramMap.put(param, request.getParameterMap().get(param));
		}
		if (!CollectionUtils.isEmpty(newParamMap)) {
			for (String param : newParamMap.keySet()) {
				paramMap.put(param, newParamMap.get(param));
			}
		}
		return buildLink(request.getServletPath(), paramMap);
	}
	
	public static String buildProfileName(Profile profile) {
		if (profile == null || StringUtils.isEmpty(profile.getFirstName()) || StringUtils.isEmpty(profile.getLastName())) {
			return "";
		}
		return String.format("%s %s", profile.getFirstName(), profile.getLastName());
	}
	
	public static List<ZipCode> getZipCodeLst(HttpServletRequest request) {
		AdminProfile profile = AdminSession.getProfile(request);
		StateRepo stateRepo = Application.AppCtx.getBean(StateRepo.class);
		ZipCodeRepo zipCodeRepo = Application.AppCtx.getBean(ZipCodeRepo.class);
		if (profile instanceof RegionManagerProfile) {
			RegionManagerProfile regionManagerProfile = (RegionManagerProfile) profile;
			if (!CollectionUtils.isEmpty(regionManagerProfile.getZipCodes())) {
				return zipCodeRepo.getListByCodes(regionManagerProfile.getZipCodes());
			}
		} else {
			List<State> states = stateRepo.getListActive("US");
			List<Long> stateIds = new LinkedList<>();
			states.forEach(t -> stateIds.add(t.getId()));
			return zipCodeRepo.getListByStates(stateIds);
		}
		
		return null;
	}
	
	public static List<String> getCodeLst(HttpServletRequest request) {
		List<ZipCode> zipCodes = getZipCodeLst(request);
		if (CollectionUtils.isEmpty(zipCodes)) {
			return null;
		}
		List<String> codes = new LinkedList<>();
		for (ZipCode zipCode : zipCodes) {
			codes.add(zipCode.getCode());
		}
		return codes;
	}
	
}

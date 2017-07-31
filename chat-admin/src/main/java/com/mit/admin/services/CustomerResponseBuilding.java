package com.mit.admin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mit.responses.CustomerResponse;
import com.mit.user.entities.Profile;
import com.mit.user.entities.UserSocial;
import com.mit.user.repositories.UserSocialRepo;

@Service
public class CustomerResponseBuilding extends ObjectResponseBuilding<Profile, CustomerResponse> {
	@Autowired
	private UserSocialRepo userSocialRepo;
	
	@Override
	public CustomerResponse buildResponse(Profile t) {
		if (t == null) {
			return null;
		}
		UserSocial userSocial = userSocialRepo.getByUserId(t.getId());
		return new CustomerResponse(t, userSocial);
	}
	
}

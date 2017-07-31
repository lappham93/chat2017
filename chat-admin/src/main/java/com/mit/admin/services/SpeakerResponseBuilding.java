package com.mit.admin.services;

import org.springframework.stereotype.Service;

import com.mit.responses.SpeakerResponse;
import com.mit.user.entities.Speaker;

@Service
public class SpeakerResponseBuilding extends ObjectResponseBuilding<Speaker, SpeakerResponse> {
	@Override
	public SpeakerResponse buildResponse(Speaker t) {
		if (t == null) {
			return null;
		}
		return new SpeakerResponse(t);
	}

}

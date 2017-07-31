package com.mit.admin.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mit.bodies.PathResponse;

@Service
public class PathResponseBuilding extends ObjectResponseBuilding<List<String>, PathResponse> {
	
	@Override
	public PathResponse buildResponse(List<String> t) {
		if (t == null || t.size() < 2) {
			return null;
		}
		return new PathResponse(t.get(0), t.get(1));
	}
	
}

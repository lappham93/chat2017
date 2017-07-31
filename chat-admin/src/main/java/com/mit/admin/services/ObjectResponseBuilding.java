package com.mit.admin.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public abstract class ObjectResponseBuilding<T, M> {
	public abstract M buildResponse(T t);

	public List<M> buildResponseList(List<T> ts) {
		if (CollectionUtils.isEmpty(ts)) {
			return null;
		}
		List<M> responses = new LinkedList<>();
		ts.forEach(t -> responses.add(buildResponse(t)));
		return responses;
	}

}

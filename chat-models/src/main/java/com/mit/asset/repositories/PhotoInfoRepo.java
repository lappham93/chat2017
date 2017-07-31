package com.mit.asset.repositories;

import com.mit.asset.entities.PhotoInfo;
import com.mit.common.repositories.TimeDocRepo;
import com.mit.seq.repositories.SeqIdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PhotoInfoRepo extends TimeDocRepo<PhotoInfo>{
	@Autowired
	MongoOperations mongoOps;

	@Autowired
	SeqIdRepo seqIdRepo;

	public PhotoInfo getById(long id) {
		return mongoOps.findById(id, PhotoInfo.class);
	}
	
	public PhotoInfo getByPhotoAndType(long photoId, int type) {
		return mongoOps.findOne(new Query(Criteria.where("photoId").is(photoId).and("type").is(type)), PhotoInfo.class);
	}
	
	public List<PhotoInfo> getListByPhotoAndType(List<Long> photoIds, int type) {
		return mongoOps.find(new Query(Criteria.where("photoId").in(photoIds).and("type").is(type)), PhotoInfo.class);
	}

	public List<PhotoInfo> getByIds(List<Long> ids) {
		List<PhotoInfo> photoInfos = mongoOps.find(new Query(Criteria.where("id").in(ids)), PhotoInfo.class);
		return photoInfos;
	}
	
	public Map<Long, PhotoInfo> getMapByIds(List<Long> photoIds, int type) {
		List<PhotoInfo> photoInfos = getListByPhotoAndType(photoIds, type);
		Map<Long, PhotoInfo> photoInfoMap = new HashMap<>();
		for (PhotoInfo photoInfo: photoInfos) {
			photoInfoMap.put(photoInfo.getPhotoId(), photoInfo);
		}

		return photoInfoMap;
	}

}

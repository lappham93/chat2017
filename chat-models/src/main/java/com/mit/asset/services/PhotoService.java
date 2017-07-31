package com.mit.asset.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mit.asset.entities.PhotoInfo;
import com.mit.asset.repositories.PhotoInfoRepo;
import com.mit.common.enums.ObjectType;
import com.mit.http.exception.SequenceException;
import com.mit.mphoto.thrift.TMPhoto;
import com.mit.mphoto.thrift.TMPhotoResult;
import com.mit.mphoto.thrift.TMapMPhotoResult;
import com.mit.seq.repositories.SeqIdRepo;
import com.mit.utils.ImageInfoUtils;
import com.mit.utils.MIMETypeUtil;

@Service
public class PhotoService {
	private static final String _preKey = "photo";
	@Autowired
	private SeqIdRepo seqIdRepo;
	@Autowired
	private PhotoInfoRepo photoInfoRepo;

	public long genPhotoId(ObjectType objectType) {
		try {
			if (objectType == ObjectType.SUBFEED || objectType == ObjectType.COMMENT) {
				objectType = ObjectType.FEED;
			}
			long id = seqIdRepo.getNextSequenceId(genKey(objectType.getName()));
			return id;
		} catch (Exception e) {
			return -1;
		}
	}

	public int putPhoto(TMPhoto tmp, ObjectType objectType) {
		int err = -1;
		PhotoClient photoClient = PhotoClientFactory.getPhotoClient(objectType);
		if (photoClient != null) {
			err = photoClient.putMPhoto(tmp);
		}

		return err;
	}
	
	public int putMultiPhoto(List<TMPhoto> tmps, ObjectType objectType) {
		int err = -1;
		PhotoClient photoClient = PhotoClientFactory.getPhotoClient(objectType);
		if (photoClient != null) {
			err = photoClient.multiPutMPhoto(tmps);
		}

		return err;
	}

	public TMPhotoResult getPhoto(long pid, ObjectType objectType) {
		TMPhotoResult tmp = null;
		PhotoClient photoClient = PhotoClientFactory.getPhotoClient(objectType);
		if (photoClient != null) {
			tmp = photoClient.getMPhoto(pid);
		}

		return tmp;
	}

	public TMapMPhotoResult getMapPhoto(List<Long> pids, ObjectType objectType) {
		TMapMPhotoResult tmp = null;
		PhotoClient photoClient = PhotoClientFactory.getPhotoClient(objectType);
		if (photoClient != null) {
			tmp = photoClient.multiGetMPhoto(pids);
		}

		return tmp;
	}

	public long saveFilePhoto(File file, ObjectType objectType) throws IOException {
		long id = genPhotoId(objectType);
		if (id > 0) {
			TMPhoto tmp = new TMPhoto();
			tmp.setId(id);
			String filename = file.getName();
			tmp.setFilename(filename);
			tmp.setData(Files.readAllBytes(file.toPath()));
			String ext = FilenameUtils.getExtension(filename);
			String contentType = MIMETypeUtil.getInstance().getMIMETypeImage(ext);
			tmp.setContentType(contentType);
			int err = putPhoto(tmp, objectType);
			if (err < 0) {
				id = err;
			}
		}

		return id;
	}

	public long saveDataPhoto(byte[] data, String fileName, ObjectType objectType) throws IOException {
		long id = genPhotoId(objectType);
		if (id > 0) {
			TMPhoto tmp = new TMPhoto();
			tmp.setId(id);
			tmp.setFilename(fileName);
			tmp.setData(data);
			String ext = FilenameUtils.getExtension(fileName);
			String contentType = MIMETypeUtil.getInstance().getMIMETypeImage(ext);
			tmp.setContentType(contentType);
			int err = putPhoto(tmp, objectType);
			if (err < 0) {
				id = err;
			}
			ImageInfoUtils iu = new ImageInfoUtils(data);
            int width = iu.getWidth();
            int height = iu.getHeight();
            PhotoInfo pi = new PhotoInfo();
            pi.setPhotoId(id);
            pi.setWidth(width);
            pi.setHeight(height);
            pi.setType(objectType.getValue());
            try {
				photoInfoRepo.save(pi);
			} catch (SequenceException e) {
				e.printStackTrace();
			}
		}

		return id;
	}
	
	public int saveMultiDataPhoto(MultipartFile[] files, ObjectType objectType, List<Long> ids) throws IOException {
		List<TMPhoto> tmps = new LinkedList<>();
		int err = 0;
		for (MultipartFile file : files) {
			long id = genPhotoId(objectType);
			if (id > 0) {
				TMPhoto tmp = new TMPhoto();
				tmp.setId(id);
				tmp.setFilename(file.getOriginalFilename());
				tmp.setData(file.getBytes());
				String ext = FilenameUtils.getExtension(file.getOriginalFilename());
				String contentType = MIMETypeUtil.getInstance().getMIMETypeImage(ext);
				tmp.setContentType(contentType);
				tmps.add(tmp);
			} else {
				err = -1;
			}
		}
		if (err >= 0) {
			err = putMultiPhoto(tmps, objectType);
		}

		return err;
	}
	
	public int saveMultiDataPhoto(Collection<Part> parts, ObjectType objectType, List<Long> ids) throws IOException {
		List<TMPhoto> tmps = new LinkedList<>();
		int err = 0;
		for (Part part : parts) {
			long id = genPhotoId(objectType);
			if (id > 0) {
				TMPhoto tmp = new TMPhoto();
				tmp.setId(id);
				tmp.setFilename(part.getSubmittedFileName());
				byte[] bytes = new byte[(int)part.getSize()];
				part.getInputStream().read(bytes, 0, bytes.length);
				tmp.setData(bytes);
				tmp.setContentType(part.getContentType());
				tmps.add(tmp);
				ids.add(id);
				
				ImageInfoUtils iu = new ImageInfoUtils(bytes);
	            int width = iu.getWidth();
	            int height = iu.getHeight();
	            PhotoInfo pi = new PhotoInfo();
	            pi.setPhotoId(id);
	            pi.setWidth(width);
	            pi.setHeight(height);
	            pi.setType(objectType.getValue());
	            try {
					photoInfoRepo.save(pi);
				} catch (SequenceException e) {
					e.printStackTrace();
				}
			} else {
				err = -1;
			}
		}
		if (err >= 0) {
			err = putMultiPhoto(tmps, objectType);
		}

		return err;
	}
	
	public long saveModel3D(FileItem file, ObjectType objectType) {
        long id = genPhotoId(objectType);
        if(id > 0){
            TMPhoto tmp = new TMPhoto();
            tmp.setId(id);
            String filename = file.getName().replaceAll(" ", "_");
            tmp.setFilename(filename);
            tmp.setData(file.get());
            String ext = FilenameUtils.getExtension(filename);
            String contentType = "application/octet-stream"; //MIMETypeUtil.getInstance().getMIMETypeImage(ext);
            if("fbx".equalsIgnoreCase(ext)){
                contentType = "application/octet-stream";
            } else if("obj".equalsIgnoreCase(ext)){
                contentType = "application/octet-stream";
            } else if("json".equalsIgnoreCase(ext)){
                contentType = "application/json";
            } else if("js".equalsIgnoreCase(ext)){
                contentType = "application/javascript";
            } else {
                contentType = MIMETypeUtil.getInstance().getMIMETypeImage(ext);
            }
            tmp.setContentType(contentType);
            int err = putPhoto(tmp, objectType);
            if (err < 0) {
            	id = err;
            }
        }
        
        return id;
	}

	public String genKey(String baseName) {
		return _preKey + baseName;
	}

	public byte[] parseThumbnailData(String thumbUri) {
		try {
			URL url = new URL(thumbUri);
			InputStream is = url.openStream();
			return IOUtils.toByteArray(is);
		} catch (Exception e) {
			return null;
		}
	}
}

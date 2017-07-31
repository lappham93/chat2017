package com.mit.upload.services;

import com.mit.asset.entities.PhotoInfo;
import com.mit.asset.repositories.PhotoInfoRepo;
import com.mit.asset.responses.PhotoResponse;
import com.mit.asset.services.PhotoService;
import com.mit.common.enums.ObjectType;
import com.mit.define.ApiError;
import com.mit.http.ApiResponse;
import com.mit.http.exception.SequenceException;
import com.mit.session.SessionManagerImpl;
import com.mit.session.services.ProfileCacheService;
import com.mit.upload.bodies.FileInfo;
import com.mit.upload.entities.UploadTemp;
import com.mit.upload.repositories.UploadTempRepo;
import com.mit.upload.responses.FilePartResponse;
import com.mit.user.entities.Profile;
import com.mit.user.repositories.ProfileRepo;
import com.mit.utils.ImageInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by Hung on 3/24/2017.
 */

@Service
public class FileUploadService {

    Logger _logger = LoggerFactory.getLogger(FileUploadService.class);

    @Autowired
    UploadTempRepo uploadTempRepo;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private PhotoInfoRepo photoInfoRepo;
    @Autowired
    private ProfileRepo profileRepo;
    @Autowired
    private SessionManagerImpl sessionManagerImpl;
    @Autowired
    private ProfileCacheService profileCacheService;

    public UploadTemp addUploadTemp(long userId, FileInfo fileInfo) throws SequenceException {
        UploadTemp uploadTemp = fileInfo.toUploadTemp();
        uploadTemp.setUserId(userId);
        if (uploadTemp.getName() == null) {
            uploadTemp.setName("");
        }
        uploadTempRepo.save(uploadTemp);

        return uploadTemp;
    }

    public ApiResponse addFilePart(byte[] data) throws Exception {
        int dataLength = data.length;
        if(dataLength > 12) {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            long id = buffer.getLong();
            int part = buffer.getInt();

            UploadTemp info = uploadTempRepo.getById(id);
//        _logger.info("==============In UploadFilePartRoomHandler UploadTemp: " + info.toString() + " ||||| content: " + packets.getHeader());
            if (info != null) {
                if (part < info.getTotal() && !info.getCheck().get(part)) {
                    int partLength = dataLength - buffer.position();
                    _logger.info("=====|||||===== PartComplete[" + info.getTotalComplete() + "] <==> PartFile[" + info.getTotal() + "]");
                    _logger.info("=====|||||===== SizeComplete(" + info.getSizeComplete() + ") + PartLength(" + partLength + ") = TotalLenght(" + (info.getSizeComplete() + partLength) + ") <==> FileSize(" + info.getSize() + ")");
                    if (info.getTotalComplete() + 1 <= info.getTotal()) {
                        if (info.getSizeComplete() + partLength <= info.getSize()) { // when photo part full to update and join file photo.
                            byte[] dataPart = new byte[partLength];
                            buffer.get(dataPart);
                            long tempFile = photoService.saveDataPhoto(dataPart, "", ObjectType.TEMP_FILE);
                            info = uploadTempRepo.updateData(id, part, tempFile, dataPart.length);

                            FilePartResponse filePartResponse = new FilePartResponse();
                            filePartResponse.setId(id);
                            filePartResponse.setPart(part);

                            if (info.getTotalComplete() == info.getTotal()) {
                                if (info.getSizeComplete() == info.getSize()) {
                                    // TODO: Join file
//                            _logger.info("[[[==============]]] UploadTemp [COMPLETE]: " + tempData.toString());
                                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                                    for (long tempId : info.getTempFiles()) {
                                        _logger.info("=====||===== uploadTempId=" + id + " tempId=" + tempId);
                                        os.write(photoService.getPhoto(tempId, ObjectType.TEMP_FILE).getValue().getData());
                                    }

                                    ObjectType objectType = ObjectType.getType(info.getType());
                                    byte[] photoData = os.toByteArray();
                                    long photoId = photoService.saveDataPhoto(photoData, info.getName(), objectType);

                                    if (photoId > 0) {
                                        ImageInfoUtils iu = new ImageInfoUtils(photoData);
                                        int width = iu.getWidth();
                                        int height = iu.getHeight();
                                        PhotoInfo pi = new PhotoInfo();
                                        pi.setPhotoId(photoId);
                                        pi.setWidth(width);
                                        pi.setHeight(height);
                                        pi.setType(objectType.getValue());
                                        photoInfoRepo.save(pi);

                                        if (objectType == ObjectType.USER) {
                                            profileRepo.updateAvatar(info.getUserId(), photoId);
                                            sessionManagerImpl.expireAllUserSession(info.getUserId(), Profile.class);
                                            profileCacheService.deleteCache(info.getUserId());
                                        } else if (objectType == ObjectType.COVER) {
                                            profileRepo.updateCover(info.getUserId(), photoId);
                                            sessionManagerImpl.expireAllUserSession(info.getUserId(), Profile.class);
                                        }

                                        filePartResponse.setPhoto(new PhotoResponse(pi));
                                        return new ApiResponse<>(filePartResponse);
                                    } else {
                                        return new ApiResponse<>(ApiError.SERVER_ERROR);
                                    }
//                            _logger.info("=====|||||===== successPacket successPacket.getHeader: [" + successPacket.getHeader().toString() + "] ==== successPacket.DataContent[" + rs + "]");
                                } else {
                                    return new ApiResponse(ApiError.UPLOAD_DATA_INVALID);
                                }
                            } else {
                                return new ApiResponse<>(filePartResponse);
                            }
                        } else {
                            return new ApiResponse(ApiError.UPLOAD_DATA_INVALID);
                        }
                    } else if (info.getTotalComplete() + 1 > info.getTotal()) {
                        return new ApiResponse(ApiError.UPLOAD_DATA_INVALID);
//                    } else if (info.getTotalComplete() + 1 < info.getTotal()) { // upload part file photo.
//                        if (info.getSizeComplete() + partLength < info.getSize()) {
//                            byte[] dataPart = new byte[partLength];
//                            buffer.get(dataPart);
//                            long tempFile = photoService.saveDataPhoto(dataPart, "", ObjectType.TEMP_FILE);
//                            uploadTempRepo.updateData(id, part, tempFile, dataPart.length);
//
//                            FilePartResponse filePartResponse = new FilePartResponse();
//                            filePartResponse.setId(id);
//                            filePartResponse.setPart(part);
//
//                            return new ApiResponse(filePartResponse);
//                        } else {
//                            return new ApiResponse(ApiError.UPLOAD_DATA_INVALID);
//                        }
                    } else if (info.getSizeComplete() + partLength >= info.getSize()) {
                        return new ApiResponse<>(ApiError.UPLOAD_DATA_INVALID);
                    } else {
                        return new ApiResponse<>(ApiError.UPLOAD_DATA_INVALID);
                    }
                } else {
                    return new ApiResponse<>(ApiError.UPLOAD_PART_NOT_EXIST);
                }
            } else {
                return new ApiResponse<>(ApiError.ITEM_NOT_FOUND);
            }
        } else {
            return new ApiResponse<>(ApiError.UPLOAD_DATA_INVALID);
        }
    }
}

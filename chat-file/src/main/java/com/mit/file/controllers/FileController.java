package com.mit.file.controllers;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mit.app.services.AppConstManager;
import com.mit.asset.repositories.LinkRepo;
import com.mit.asset.services.PhotoService;
import com.mit.common.enums.ObjectType;
import com.mit.file.utils.ImageResize;
import com.mit.mphoto.thrift.TMPhoto;
import com.mit.mphoto.thrift.TMPhotoResult;
import com.mit.user.repositories.ProfileRepo;
import com.mit.utils.LinkBuilder;
import com.mit.utils.MIdNoise;

@Controller
@RequestMapping("/load")
public class FileController {

	Logger logger = LoggerFactory.getLogger(FileController.class);

    private final int minSize = 20;
    
    @Value("${app.title}")
    private String title;
    @Value("${app.desc}")
    private String desc;
    @Value("${app.resources.path}")
    private String resourcesPath;
    @Value("${system.domain-file}")
    private String domainFile;
    
    @Autowired
    private ProfileRepo profileRepo;
    
    @Autowired
	private PhotoService photoService;
    @Autowired
	private AppConstManager appConstManager;
    @Autowired
	private LinkRepo linkRepo;
    
	@RequestMapping(value = "/{objectName}/photo/{id}", method = RequestMethod.GET)
	@ResponseBody
	public void loadPhoto(HttpServletRequest request, HttpServletResponse response,
		  	@PathVariable("objectName") String objectName, @PathVariable("id") String hashId,
			@RequestParam(name = "size", defaultValue = "0", required = false) int size,
			@RequestParam(name = "height", defaultValue = "0", required = false) int height,
			@RequestParam(name="quadKey", defaultValue = "", required = false) String quadKey) {
		try {
            if(!StringUtils.isEmpty(hashId)){
            	long pId = MIdNoise.deNoiseLId(hashId);
            	ObjectType objectType = ObjectType.getTypeByName(objectName);
				TMPhotoResult tptrs = photoService.getPhoto(pId, objectType);
				if(tptrs != null && tptrs.error >= 0 && tptrs.value != null){
					TMPhoto tmp = tptrs.getValue();
					String fileName = tmp.getFilename();
					String mimeType = tmp.getContentType();
					byte[] data = tmp.getData();

					if (data != null) {
						logger.info("FileController.loadPhoto load from rockdb - pId: " + pId + " Size: " + size + " FileSize: " + FileUtils.byteCountToDisplaySize(data.length));
						if (size > 0) {
							size = size > minSize ? size : minSize;
							ByteArrayOutputStream out;
							if (height > 0) {
								height = height > minSize ? height : minSize;
								out = ImageResize.resizeImage(data, size, height);
							} else {
								out = ImageResize.resizeImage(data, size);
							}
							if (out != null) {
								data = out.toByteArray();
								out.close();
							}
						}

						logger.info("FileController.loadPhoto after resizeImage - pId: " + pId + " Size: " + size + " FileSize: " + FileUtils.byteCountToDisplaySize(data.length));
						ByteArrayOutputStream outData = new ByteArrayOutputStream(data.length);
						outData.write(data, 0, data.length);
						outData.flush();
						printBigFile(response, outData, fileName, mimeType);
					} else {
						logger.info("FileController.loadPhoto load from rockdb - pId: " + pId + " Size: " + size + " -------> NULL ");
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
					}
				} else{
					logger.info("FileController.loadPhoto load from rockdb - pId: " + pId + " Size: " + size + " -------> NULL ");
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
        } catch (Exception e) {    		
	        try {
	            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException ex) {
			}
        }
		
	}
	
	protected void printBigFile(HttpServletResponse response, ByteArrayOutputStream bigfile, String fileName, String mimeType) throws IOException {
        ServletOutputStream stream = null;
        BufferedInputStream buf = null;
        try {
            stream = response.getOutputStream();

            //set response headers
            response.setBufferSize(1024 * 1024 * 6); // 6M
            response.setContentType(mimeType + ";charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setHeader("Accept-Ranges", "bytes");
            response.setContentLength(bigfile.size());

            InputStream input = new ByteArrayInputStream(bigfile.toByteArray());
            buf = new BufferedInputStream(input);

            //read from the file; write to the ServletOutputStream
            byte[] bb = new byte[1024 * 1024 * 5]; // 5M
            int readByte;
            while ((readByte = buf.read(bb, 0, bb.length)) != -1) {
                stream.write(bb, 0, readByte);
            }
        } catch (Exception e) {

        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
            if (buf != null) {
                buf.close();
            }
            if(bigfile != null){
                bigfile.close();
            }
        }
    }
	
	@RequestMapping(value = "/{objectName}/share/{id}", method = RequestMethod.GET)
	public ModelAndView loadFileShare(HttpServletRequest request, 
			@PathVariable(value = "objectName") String objectName, @PathVariable(value = "id") String id) {
		ModelAndView view = new ModelAndView();
		view.addObject("title", title);
		view.addObject("desc", desc);
		view.addObject("resources_path", resourcesPath);
		view.addObject("url_web", LinkBuilder.buildControllerLink(ControllerLinkBuilder.methodOn(FileController.class)
				.loadFileShare(null, objectName, id)));
//		view.addObject("deep_link", "homeber://" + objectName + "/" + id);
		view.addObject("open_app", false);
		view.addObject("image_url", domainFile + "/homber/load/static/images/saocoo-01.png");
		ObjectType objectType = ObjectType.getTypeByName(objectName);
		return view;
	}
	
}

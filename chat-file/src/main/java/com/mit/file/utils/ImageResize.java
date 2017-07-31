package com.mit.file.utils;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageResize {
	private static Logger logger = LoggerFactory.getLogger(ImageResize.class);
	public static ByteArrayOutputStream resizeImage(byte[] data, int width, int height) {
		ConvertCmd cmd = new ConvertCmd();

		// create the operation, add images and operators/options
		IMOperation op = new IMOperation();
		op.addImage("-");
		op.resize(width, height);
        op.sharpen(1.2);
        op.quality(95D);
        op.background("white");
        op.alpha("remove");
		op.addImage("jpg:-");
		try {
			Pipe pipeIn = new Pipe(new ByteArrayInputStream(data), null);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Pipe pipeOut = new Pipe(null,out);
			cmd.setOutputConsumer(pipeOut);
			cmd.setInputProvider(pipeIn);
			cmd.run(op);
			return out;
		} catch (Exception e) {
			logger.info("resizeImage error");
		}

		return null;
    }

	public static ByteArrayOutputStream resizeImage(byte[] data, int width) {
		ConvertCmd cmd = new ConvertCmd();

		// create the operation, add images and operators/options
		IMOperation op = new IMOperation();
		op.addImage("-");
		op.resize(width);
        op.sharpen(1.2);
        op.quality(95D);
        op.background("white");
        op.alpha("remove");
		op.addImage("jpg:-");
		try {
			Pipe pipeIn = new Pipe(new ByteArrayInputStream(data), null);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Pipe pipeOut = new Pipe(null,out);
			cmd.setOutputConsumer(pipeOut);
			cmd.setInputProvider(pipeIn);
			cmd.run(op);

			return out;
		} catch (Exception e) {
			logger.error("resizeImage", e);
		}
		return null;
    }

    public static void main(String[] args) {
        int width = 200;
        IMOperation op = new IMOperation();
		op.addImage("-");
		op.resize(width);
        op.sharpen(1.2);
        op.quality(95D);
        op.background("white");
        op.alpha("remove");
		op.addImage("jpg:-");
        
        System.out.println("op: " + op);
        
    }
    
}

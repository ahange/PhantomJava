package org.phantomjscef.websocket;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

import javax.websocket.DeploymentException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.glassfish.tyrus.server.Server;
import org.phantomjscef.data.Data;
import org.phantomjscef.data.Events;
import org.phantomjscef.data.Page;
import org.phantomjscef.data.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

public class PhantomJs {
	private final static Logger logger = LoggerFactory.getLogger(PhantomJs.class);

	public static void startServer(String host, int port){
	    try {
			Server server = new Server(host, port, "/", null, WebsocketClientEndpoint.class);
			server.start();
			logger.info("Starte Websocket Server ...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						System.in.read();
					} catch (IOException e) {}
				}
			}).start();
		} catch (DeploymentException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void startServer(int port){
		startServer("localhost", port);
	}
	
	public static void startServer(){
		startServer("localhost", 8887);
	}
	
	public static CompletableFuture<?> waitForAckFuture(){
		return FutureManager.waitForAckFuture();
	}
	
	private String encodeBase64(String s){
		return Base64.encodeBase64String(s.getBytes(Charset.forName("ISO-8859-1")));
	}

	public CompletableFuture<Result> loadUrl(String url){
		Page page = new Page();
		Data data = new Data("start", page, url);	
		String json = JsonMapper.getJsonString(data);
		WebsocketClientEndpoint.sendMessage(json);
		return FutureManager.addAndWaitForCompletableFuture(Events.onLoadFinished, page);
	}

	public static void exit(){
		Data data = new Data("exit");
		String json = JsonMapper.getJsonString(data);
		WebsocketClientEndpoint.sendMessage(json);
	}

	

	public void writeToDisk(String pic, String filename) {
		byte[] b = Base64.decodeBase64(pic);
		writeByteArrayToFile(new File(filename), b);
	}

	private void writeByteArrayToFile(File f,byte[] b){
		try {
			FileUtils.writeByteArrayToFile(f, b);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public static void startPhantomProcess(String executableDirectory){
		try {
			logger.debug("starte phantomjs process ...");
			File execDir = new File(executableDirectory);
			if (!execDir.exists()){
				throw new RuntimeException("cannot find "+execDir.getAbsolutePath());
			}
			logger.debug("executing in "+executableDirectory);
			String exec = FilenameUtils.concat(executableDirectory,SystemUtils.IS_OS_WINDOWS?"phantomjs.exe":"phantomjs");
			File execFile = new File(exec);
			if (!execFile.exists()){
				throw new RuntimeException("cannot find "+execFile.getAbsolutePath());
			}
			URL url = PhantomJs.class.getResource("/phantomscript/javabindings.js");
			File jsFile = new File(url.toURI());
			if (jsFile.exists()){
				new ProcessExecutor().directory(execDir).command(SystemUtils.IS_OS_WINDOWS?"phantomjs.exe":"./phantomjs",jsFile.getAbsolutePath())
				.redirectOutput(Slf4jStream.ofCaller().asInfo()).destroyOnExit().start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

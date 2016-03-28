/*
 * (c) 2010 by dwpbank Deutsche WertpapierService Bank AG
 */
/**
 * Actual Version
 * ==============
 * @version $Revision$
 * @author $Author$
 * For a detailed history of this file see bottom !
 */
package org.phantomjscef.data;

import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.codec.binary.Base64;
import org.phantomjscef.websocket.FutureManager;
import org.phantomjscef.websocket.JsonMapper;
import org.phantomjscef.websocket.WebsocketClientEndpoint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class Page {
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public ClipRect clipRect;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public ViewportSize viewportSize;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Settings settings;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public PaperSize paperSize;
//	@JsonProperty
//	public double zoomFactor = 1;
//	@JsonProperty
//	@JsonInclude(Include.NON_NULL)
//	public int resourceTimeout=2000;
	@JsonProperty
	public final String uid;
	@JsonProperty
	public String id;
	//public String command;
	@JsonProperty
	public String libraryPath;
	@JsonIgnoreProperties
	@JsonIgnore
	public String event;
	public Page(){
		this.uid = java.util.UUID.randomUUID().toString();
	}

	
	public  CompletableFuture<Page> awaitEvent(Events event){
		return FutureManager.addAndWaitForCompletableFuture(event,this);
	}

	public CompletableFuture<Page> waitForDomElement(String selector){
		Data data = new Data("waitForDomElement", this,encodeBase64(selector));
		String json = JsonMapper.getJsonString(data);
		WebsocketClientEndpoint.sendMessage(json);
		return  FutureManager.addAndWaitForCompletableFuture(Events.waitForDomElement,this);
	}
	
	public CompletableFuture<PageImage> renderImage(ImageType pictureType){
		Data data = new Data("capture",this);
		String json = JsonMapper.getJsonString(data);
		WebsocketClientEndpoint.sendMessage(json);
		return FutureManager.addAndWaitForCompletableFuture(Events.Image,this);
	}
	public CompletableFuture<Page> sendMouseEvent(MouseEvent me, String selector){
		Data data = new Data("sendMouseEvent",this,me.name(),encodeBase64(selector));
		String json = JsonMapper.getJsonString(data);
		WebsocketClientEndpoint.sendMessage(json);
		return FutureManager.addAndWaitForCompletableFuture(Events.MouseEventFinished,this);
	}
	public CompletableFuture<Page> sendKeyEvent(KeyType kt, String text){
		Data data = new Data("sendKeyEvent",this,kt.name(),text);
		String json = JsonMapper.getJsonString(data);
		WebsocketClientEndpoint.sendMessage(json);
		return  FutureManager.addAndWaitForCompletableFuture(Events.KeyEventFinished,this);
	}
	public CompletableFuture<Page> pollPaint(int retries,long timeout) {
		Data data = new Data("pollPaint",this,String.valueOf(timeout),String.valueOf(retries));
		String json = JsonMapper.getJsonString(data);
		WebsocketClientEndpoint.sendMessage(json);
		return FutureManager.addAndWaitForCompletableFuture(Events.pollPaint,this);
	}
	public CompletableFuture<Result> waitTime(int timeout){
		Result r = new Result(this);
		r.event = Events.Timeout;
		return FutureManager.addAndWaitForTimeFuture(timeout, r);
	}
	public CompletableFuture<Page> execute(String executable){
		Data data = new Data("execute",this,encodeBase64(executable));
		String json = JsonMapper.getJsonString(data);
		WebsocketClientEndpoint.sendMessage(json);
		return FutureManager.addAndWaitForCompletableFuture(Events.execute,this);
	}
	public CompletableFuture<Page> test(String testFunction){
		Data data = new Data("test",this,encodeBase64(testFunction));
		String json = JsonMapper.getJsonString(data);
		WebsocketClientEndpoint.sendMessage(json);
		return FutureManager.addAndWaitForCompletableFuture(Events.test,this);
	}
	public CompletableFuture<PageObject> testScript(String jsFile){
		Data data = new Data("testScript",this,jsFile);
		String json = JsonMapper.getJsonString(data);
		WebsocketClientEndpoint.sendMessage(json);
		return FutureManager.addAndWaitForCompletableFuture(Events.testScript,this);
	}
	public CompletableFuture<PageText> text(String selector) {
		Data data = new Data("text", this,encodeBase64(selector));
		String json = JsonMapper.getJsonString(data);
		WebsocketClientEndpoint.sendMessage(json);
		return  FutureManager.addAndWaitForCompletableFuture(Events.text,this);
	}

	public CompletableFuture<PageList> list(String selector) {
		Data data = new Data("list", this,encodeBase64(selector));
		String json = JsonMapper.getJsonString(data);
		WebsocketClientEndpoint.sendMessage(json);
		return FutureManager.addAndWaitForCompletableFuture(Events.list,this);
	}
	
	public void setclipRect(int top, int left, int width, int height){
		if (this.clipRect==null){
			this.clipRect = new ClipRect();
		}
		this.clipRect.top=top;
		this.clipRect.left=left;
		this.clipRect.width=width;
		this.clipRect.height=height;
	}

	public void setViewportSize(int width, int height){
		if (this.viewportSize==null){
			this.viewportSize=new ViewportSize();
		}
		this.viewportSize.width=width;
		this.viewportSize.height=height;
	}

	public void setUserAgent(String value) {
		if (this.settings==null){
			this.settings=new Settings();
		}
		this.settings.userAgent=value;
	}
	
	private String encodeBase64(String s){
		return Base64.encodeBase64String(s.getBytes(Charset.forName("ISO-8859-1")));
	}
	
}

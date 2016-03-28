package org.phantomjscef.websocket;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;
import org.phantomjscef.data.Events;
import org.phantomjscef.data.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/")
public class WebsocketClientEndpoint {
	private static Session session;
	private static final Logger logger = LoggerFactory.getLogger(WebsocketClientEndpoint.class);
	@OnOpen
    public void onOpen(Session _session) throws IOException {
		session = _session;
		logger.info("open");
    }

    @OnMessage
    public void echo(String message) {
    	logger.info(message);
		Result result = JsonMapper.getResult(message);
		if (result.event==Events.ACK){
			Iterator<CompletableFuture<Events>> iter = FutureManager.getAckFutureIterator();
			while (iter.hasNext()){
				iter.next().complete(Events.ACK);
			}
		} else if (!StringUtils.isEmpty(result.event.toString())) {
			String key = result.event.toString()+"_"+result.page.uid;
			CompletableFuture cf = FutureManager.getCompletableFuture(key);
			if (cf!=null){
				logger.info(key+" --> completed");
				cf.complete(result.getObject());
			}
		}
    }
    
    @OnError
    public void onError(Throwable t) {
    	logger.info("error:"+t.toString());
    }

    @OnClose
    public void onClose(Session session) {
    	logger.info("close");
		System.exit(0);
    }
    
    /**
     * Send a message.
     *
     * @param message
     */
    public static void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }
    
}

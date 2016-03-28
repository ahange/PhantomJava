package org.phantomjscef.websocket;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.phantomjscef.data.Events;
import org.phantomjscef.data.Page;
import org.phantomjscef.data.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FutureManager {
	private static final Map<String, CompletableFuture<?>> map = new ConcurrentHashMap<>();
	private static final Set<CompletableFuture<Events>> set = ConcurrentHashMap.newKeySet();
	private static final Logger logger = LoggerFactory.getLogger(FutureManager.class);
	
	static CompletableFuture<?> getCompletableFuture(String key){
		CompletableFuture<?> cf = map.remove(key);
		return cf;
	}
	
	static <T> CompletableFuture<T> addCompletableFuture(Events ev, String uid){
		CompletableFuture<T> cf = new CompletableFuture<>();
		String key = ev.toString()+"_"+uid;
		map.put(key,cf);
		return cf;
	}
	
	public static <T> CompletableFuture<T> addAndWaitForCompletableFuture(Events ev, Page page) {
		return addCompletableFuture(ev, page.uid);
	}

	static CompletableFuture<Events> waitForAckFuture(){
		CompletableFuture<Events> cf = new CompletableFuture<>();
		set.add(cf);
		return cf;
	}
	
	static Iterator<CompletableFuture<Events>> getAckFutureIterator(){
		return set.iterator();
	}
	

	public static CompletableFuture<Result> addAndWaitForTimeFuture(int time, Result result) {
		logger.info("pageId: "+result.page.id+": waiting for "+time+" milliseconds");
		CompletableFuture<Result> cf = new CompletableFuture<>();
		new Thread(){
			public void run() {
				try {
					Thread.sleep(time);
					logger.info("pageId: "+result.page.id+": waiting ready ...");
					cf.complete(result);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
		try {
			return cf;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String... args) {
		if (args.length>0){
			System.out.println(args[0]);
		} else {
			System.out.println("no args ...");
		}
	}
}

package examples;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.phantomjscef.data.Events;
import org.phantomjscef.data.KeyType;
import org.phantomjscef.data.MouseEvent;
import org.phantomjscef.data.Page;
import org.phantomjscef.websocket.PhantomJs;

public class Arc42 {
	
	public static void main(String[] args) {
		PhantomJs.startServer();
		PhantomJs phantom = new PhantomJs();
		if (System.getProperty("phantomjsDir") != null){
			String phantomjsDir = System.getProperty("phantomjsDir");
			PhantomJs.startPhantomProcess(phantomjsDir);
		}
		
		CompletableFuture<Page> ac = PhantomJs.waitForAckFuture().thenCompose((ev) -> {
			return phantom.loadUrl("http://confluence.arc42.org/display/templateEN/arc42+Template+%28English%29+-+Home");
		}).thenCompose((page) -> {
			page.setViewportSize(1920, 1200);
			return page.sendMouseEvent(MouseEvent.doubleclick, "input[name=queryString][class*=medium-field]");	
		}).thenCompose((page) -> {
			return page.sendKeyEvent(KeyType.keypress, "Requirements");	
		}).thenCompose((page) -> {
			return page.sendMouseEvent(MouseEvent.click, "input[type=submit][class=aui-button]");	
		}).thenCompose((page) -> {
			return page.awaitEvent(Events.onLoadFinished);
		}).thenCompose((page) -> {
			return page.list("a[class*=search-result-link]");
		}).thenApply((plist) -> {
			printArray(plist.getList());
			return plist.getPage();
		});
		
		checkWeiterLink(ac);
	}
	
	private static void checkWeiterLink(CompletableFuture<Page> ac) {
		CompletableFuture<Page> finish = new CompletableFuture<Page>();
		ac.whenCompleteAsync((page,t)-> {
			page.list("a.pagination-next")
			.thenAccept(plist -> {
				if (plist.getList().size()==0){
					PhantomJs.exit();
				} else {
					finish.complete(plist.getPage());
					iterateSearchResults(finish);
				}
			});
		});
	}
	
	private static void iterateSearchResults(CompletableFuture<Page> ac) {
		CompletableFuture<Page> finish = new CompletableFuture<Page>();
		ac.whenCompleteAsync((page,t)-> {
			page.sendMouseEvent(MouseEvent.click, "a.pagination-next")
			.thenCompose((p)-> {
				return p.awaitEvent(Events.onResourceReceived);
			})
			.thenCompose((p) -> {
				return p.list("a[class*=search-result-link]");
			})
			.thenAccept((pList) -> {
				printArray(pList.getList());
				finish.complete(pList.getPage());
				checkWeiterLink(finish);
			});
		});
		
	}

	private static void printArray(List<String> array){
		for (String text : array){
			System.out.println(text);
		}
	}
}

package examples;

import java.util.concurrent.CompletableFuture;

import org.phantomjscef.data.Events;
import org.phantomjscef.data.ImageType;
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
		}).thenCompose((result) -> {
			result.page.setViewportSize(1920, 1200);
			return result.page.sendMouseEvent(MouseEvent.doubleclick, "input[name=queryString][class*=medium-field]");	
		}).thenCompose((result) -> {
			return result.page.sendKeyEvent(KeyType.keypress, "Requirements");	
		}).thenCompose((result) -> {
			return result.page.sendMouseEvent(MouseEvent.click, "input[type=submit][class=aui-button]");	
		}).thenCompose((result) -> {
			return result.page.awaitEvent(Events.onLoadFinished);
		}).thenCompose((result) -> {
			return result.page.pollPaint(2,500);
		}).thenCompose((result) -> {
			return result.page.renderImage(ImageType.PNG);
		}).thenCompose((result) -> {
			phantom.writeToDisk(result.pic, "arc42.png");
			return result.page.list("a[class*=search-result-link]");
		}).thenApply((result) -> {
			printArray(result.array);
			return result.page;
		});
		
		checkWeiterLink(ac);
	}
	
	private static void checkWeiterLink(CompletableFuture<Page> ac) {
		CompletableFuture<Page> finish = new CompletableFuture<Page>();
		ac.whenCompleteAsync((page,t)-> {
			page.list("a.pagination-next")
			.thenAccept(result -> {
				if (result.array.length==0){
					PhantomJs.exit();
				} else {
					finish.complete(result.page);
					iterateSearchResults(finish);
				}
			});
		});
	}
	
	private static void iterateSearchResults(CompletableFuture<Page> ac) {
		CompletableFuture<Page> finish = new CompletableFuture<Page>();
		ac.whenCompleteAsync((page,t)-> {
			page.sendMouseEvent(MouseEvent.click, "a.pagination-next")
			.thenCompose((result)-> {
				return result.page.awaitEvent(Events.onResourceReceived);
			})
			.thenCompose((result) -> {
				return result.page.list("a[class*=search-result-link]");
			})
			.thenAccept((result) -> {
				printArray(result.array);
				finish.complete(result.page);
				checkWeiterLink(finish);
			});
		});
		
	}

	private static void printArray(String[] array){
		for (String text : array){
			System.out.println(text);
		}
	}
}

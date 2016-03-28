package examples;

import java.util.List;

import org.phantomjscef.data.Events;
import org.phantomjscef.data.ImageType;
import org.phantomjscef.data.KeyType;
import org.phantomjscef.data.MouseEvent;
import org.phantomjscef.data.Page;
import org.phantomjscef.websocket.PhantomJs;

public class Arc42Old {
	
	public static void main(String[] args) {
		PhantomJs.startServer();
		PhantomJs phantom = new PhantomJs();
		
		if (System.getProperty("phantomjsDir") != null){
			String phantomjsDir = System.getProperty("phantomjsDir");
			PhantomJs.startPhantomProcess(phantomjsDir);
		}
		
		PhantomJs.waitForAckFuture().thenCompose((ev) -> {
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
			return page.pollPaint(2,500);
		}).thenCompose((page) -> {
			return page.renderImage(ImageType.PNG);
		}).thenCompose((pic) -> {
			phantom.writeToDisk(pic.getImage(), "arc42.png");
			return pic.getPage().list("a[class*=search-result-link]");
		}).thenCompose((list) -> {
			printArray(list.getList());
			return list.getPage().sendMouseEvent(MouseEvent.click, "a.pagination-next");
		}).thenCompose((page) -> {
			return page.awaitEvent(Events.onResourceReceived);
		}).thenCompose((page) -> {
			return page.list("a[class*=search-result-link]");
		}).thenAccept((list) -> {
			printArray(list.getList());
			PhantomJs.exit();
		});
	}
	
	private static void printArray(List<String> array){
		for (String text : array){
			System.out.println(text);
		}
	}
}

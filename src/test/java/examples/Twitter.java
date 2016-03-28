package examples;

import java.io.IOException;

import org.phantomjscef.websocket.PhantomJs;

public class Twitter {
	
	public static void main(String[] args) throws IOException{
		PhantomJs.startServer();
		PhantomJs phantom = new PhantomJs();
		if (System.getProperty("phantomjsDir") != null){
			String phantomjsDir = System.getProperty("phantomjsDir");
			PhantomJs.startPhantomProcess(phantomjsDir);
		}
		
		PhantomJs.waitForAckFuture().thenCompose((ev) -> {
			return phantom.loadUrl("http://mobile.twitter.com/PhantomJS");
		}).thenCompose((page) -> {
			page.setViewportSize(1280, 1024);
			return page.text(".UserProfileHeader-statCount");
		}).thenAccept((ptext) -> {
			System.out.println(">>>>>>> PhantomJS folgt "+ptext.getText()+" Personen");
			PhantomJs.exit();
		});

	}

}

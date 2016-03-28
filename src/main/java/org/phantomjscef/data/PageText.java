package org.phantomjscef.data;

public class PageText {
	private Page page;

	public PageText(Page p, String text){
		this.page = p;
		this.text = text;
	}
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	private String text = null;

	public String getText() {
		return text;
	}
	
}

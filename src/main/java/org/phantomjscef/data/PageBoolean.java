package org.phantomjscef.data;

public class PageBoolean {

	private Page page;

	public PageBoolean(Page p, Boolean b){
		this.page = p;
		this.b = b;
	}
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	private Boolean b;

	public Object getBoolean() {
		return b;
	}

	
}

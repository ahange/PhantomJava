package org.phantomjscef.data;

public class PageObject {

	private Page page;

	public PageObject(Page p, Object o){
		this.page = p;
		this.object = o;
	}
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	private Object object = null;

	public Object getObject() {
		return object;
	}

	
}

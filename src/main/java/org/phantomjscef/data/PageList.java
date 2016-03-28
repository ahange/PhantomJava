package org.phantomjscef.data;

import java.util.List;

public class PageList {

	private Page page;

	public PageList(Page p, List list){
		this.page = p;
		this.list = list;
	}
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	
	List<String> list;

	public List<String> getList() {
		return list;
	}

}

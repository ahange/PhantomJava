package org.phantomjscef.data;

public class PageImage {
	
	private Page page;

	public PageImage(Page p, String image){
		this.page = p;
		this.image = image;
	}
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	private String image = new String();

	public String getImage() {
		return image;
	}
}

/*
 * (c) 2010 by dwpbank Deutsche WertpapierService Bank AG
 */
/**
 * Actual Version
 * ==============
 * @version $Revision$
 * @author $Author$
 * For a detailed history of this file see bottom !
 */
package org.phantomjscef.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.esotericsoftware.reflectasm.FieldAccess;

/**
 *
 */
public class Result {
	public Result(Page page) {
		this.page = page;
	}

	public Result() {
	}
	public Events event;
	public Page page;
	public String text;
	public String pic;
	public Boolean test;
	public String[] array;

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder(getClass().getName());
		toString(this,b);
		return b.toString();
	}
	public void toString(Object o, StringBuilder b){
		b.append("[");
		FieldAccess access = FieldAccess.get(o.getClass());
		for(String fn: access.getFieldNames()) {
			Object no = access.get(o, fn);
			if (no!=null &&  no.toString().contains("@")){
				b.append(fn);
				toString(no, b);
			} else if (no!=null){
				b.append(fn).append("=").append(no.toString()).append(";");
			}
		}
		b.append("]");

	}
	public Object getObject(){
		FieldAccess access = FieldAccess.get(this.getClass());
		Object result = page;
		for(String fn: access.getFieldNames()) {
			Object no = access.get(this, fn);
			if (no != null && !StringUtils.equals(fn, "page")){
				switch (fn) {
					case "text": result=new PageText(page,text);break;
					case "pic": result=new PageImage(page, pic);break;
					case "array": result=new PageList(page, Arrays.asList(array));break;
					case "test": result=new PageBoolean(page, test);break;
				}
			}
		}
		return result;
	}
	
//	public (){
//		for (Field field : this.getClass().getDeclaredFields()) {
//			Object v = field.get(o);
//			if (v.)
//		}
//	}
	public static void main(String[] args) {
		Result r = new Result();
		System.out.println(r);
	}

}

/*
 *
 * File History
 * ==============
 * $Log$
 */

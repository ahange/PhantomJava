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

import com.esotericsoftware.reflectasm.FieldAccess;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;

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
	public boolean test;
	public String[] array;
	public boolean error;
	public String errormsg;

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
	public static void main(String[] args) {
		Result r = new Result();
		System.out.println(r);
		//Integer obj;
		//System.out.println(Number.class.isAssignableFrom(obj.getClass()));
	}

}

/*
 *
 * File History
 * ==============
 * $Log$
 */

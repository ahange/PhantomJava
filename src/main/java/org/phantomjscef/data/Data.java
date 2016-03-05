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
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.esotericsoftware.reflectasm.FieldAccess;

public final class Data {

	public final String command;
	public final String[] args;
	public Page page;
	
    public Data(String command, Page page,  String... args){
    	this.command=command;
    	this.page = page;
    	this.args = args;
    }
    public Data(String command, String... args){
    	this.command=command;
    	this.args = args;
    }
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
			if (no!=null &&  no.toString().contains("@") && !no.getClass().isArray()){
				b.append(fn);
				toString(no, b);
			} else if (no instanceof String[]){
				b.append(fn).append("=").append(Arrays.deepToString((Object[])no)).append(";");
			} else if (no!=null){
				b.append(fn).append("=").append(no.toString()).append(";");
			}
		}
		b.append("]");

	}
}
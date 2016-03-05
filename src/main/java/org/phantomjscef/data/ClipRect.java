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

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 *
 */
public class ClipRect {
	@JsonProperty
	public int top;
	@JsonProperty
	public int left;
	@JsonProperty
	public int width;
	@JsonProperty
	public int height;
}
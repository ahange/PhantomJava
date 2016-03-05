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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class PaperSize {
	@JsonProperty
	public Format format;
	@JsonProperty
	public Orientation orientation;
	@JsonIgnore
	public Margin margin;
}

/*
 *
 * File History
 * ==============
 * $Log$
 */

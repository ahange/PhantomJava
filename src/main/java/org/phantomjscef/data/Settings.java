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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class Settings {
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String userAgent;
	@JsonIgnore
	public String javascriptEnabled;
	@JsonIgnore
	public String loadImages;
	@JsonIgnore
	public String webSecurityEnabled;
	@JsonIgnore
	public String localToRemoteUrlAccessEnabled;
	@JsonIgnore
	public String javascriptOpenWindows;
	@JsonIgnore
	public String javascriptCloseWindows;
	@JsonIgnore
	public String userName;
	@JsonIgnore
	public String password;
	@JsonIgnore
	public String resourceTimeout;	
}

package com.badlogic.gdx.jnigen.gradle;

import java.util.Map;

public class JnigenSigningExtension {
	private Map<String, String> jsignParams;
	private String identity;

	public Map<String, String> getJsignParams() {
		return jsignParams;
	}

	public void setJsignParams(Map<String, String> jsignParams) {
		this.jsignParams = jsignParams;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
}

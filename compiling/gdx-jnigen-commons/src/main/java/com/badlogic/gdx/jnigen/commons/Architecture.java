package com.badlogic.gdx.jnigen.commons;

public enum Architecture {

	x86("x86"),
	ARM("Arm"),
	RISCV("Riscv"),
	LOONGARCH("LoongArch");

	private final String displayName;

	Architecture (String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName () {
		return displayName;
	}

	public String toSuffix() {
		if (this == x86) return "";
		else return this.name().toLowerCase();
	}

	public enum Bitness {
		_32, _64, _128;

		public String toSuffix() {
			if (this == _32) return "";
			else return this.name().substring(1);
		}
	}
}

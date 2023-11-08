package com.badlogic.gdx.utils;

public enum Architecture {
	x86, ARM, RISCV;

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

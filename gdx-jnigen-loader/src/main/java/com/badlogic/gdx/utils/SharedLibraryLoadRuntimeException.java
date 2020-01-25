/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.utils;

/** Typed runtime exception used in jnigen's SharedLibraryLoader
 * 
 * @author Desu */
public class SharedLibraryLoadRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 8263101105331379889L;

	public SharedLibraryLoadRuntimeException (String message) {
		super(message);
	}

	public SharedLibraryLoadRuntimeException (Throwable t) {
		super(t);
	}

	public SharedLibraryLoadRuntimeException (String message, Throwable t) {
		super(message, t);
	}
}

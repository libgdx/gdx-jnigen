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

package com.badlogic.gdx.jnigen.loader;

/** Strategy controlling how {@link SharedLibraryLoader} loads a native library on the current platform. Implementations may
 * load a system library by name, extract it from the natives jar, load it from an absolute path, or do anything custom on any
 * platform. Install one via {@link SharedLibraryLoader#setLoadStrategy(SharedLibraryLoadStrategy)} before the first library is
 * loaded.
 * <p>
 * Override only what you need by extending {@link DefaultSharedLibraryLoadStrategy} and delegating the rest to {@code super}, for
 * example to route Android loading through <a href="https://github.com/KeepSafe/ReLinker">ReLinker</a>. */
public interface SharedLibraryLoadStrategy {

	/** Loads the native library for the current platform.
	 * @param loader the loader to use as a toolkit. Provides the loading primitives
	 *           ({@link SharedLibraryLoader#loadSystemLibrary(String)}, {@link SharedLibraryLoader#extractFile(String, String)},
	 *           {@link SharedLibraryLoader#loadFromAbsolutePath(String)}, {@link SharedLibraryLoader#mapLibraryName(String)}).
	 * @param libraryName the platform independent name passed to {@link SharedLibraryLoader#load(String)}.
	 * @param platformName the platform dependent name, i.e. the result of {@link SharedLibraryLoader#mapLibraryName(String)}. */
	void load (SharedLibraryLoader loader, String libraryName, String platformName);
}

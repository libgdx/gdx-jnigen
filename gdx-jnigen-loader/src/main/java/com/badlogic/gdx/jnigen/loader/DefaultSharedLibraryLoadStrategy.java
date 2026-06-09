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

import com.badlogic.gdx.jnigen.commons.HostDetection;

import java.io.File;
import java.io.IOException;

/** Default {@link SharedLibraryLoadStrategy}: on iOS nothing is loaded, on Android the library is loaded by name via {@link System#loadLibrary(String)}, and on the desktop it
 * is extracted from the natives jar and loaded from an absolute path. Extend this class and override {@link #load} to customise a
 * single platform while delegating the rest to {@code super}. */
public class DefaultSharedLibraryLoadStrategy implements SharedLibraryLoadStrategy {

	@Override
	public void load (SharedLibraryLoader loader, String libraryName, String platformName) {
		switch (HostDetection.os) {
		case IOS:
			return;
		case Android:
			loader.loadSystemLibrary(platformName);
			break;
		default:
			File extracted = loader.extractFile(platformName, null);
			loader.loadFromAbsolutePath(extracted.getAbsolutePath());
		}
	}
}

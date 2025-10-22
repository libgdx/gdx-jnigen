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

import com.badlogic.gdx.jnigen.commons.Architecture;
import com.badlogic.gdx.jnigen.commons.HostDetection;
import com.badlogic.gdx.jnigen.commons.Os;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/** Loads shared libraries from a natives jar file (desktop) or arm folders (Android). For desktop projects, have the natives jar
 * in the classpath, for Android projects put the shared libraries in the libs/armeabi and libs/armeabi-v7a folders.
 * @author mzechner
 * @author Nathan Sweet */
public class SharedLibraryLoader {

	static private final HashSet<String> loadedLibraries = new HashSet<>();
	static private final Random random = new Random();

	static private AndroidLibraryLoader androidLibraryLoader = new AndroidLibraryLoader() {
		@Override
		public void load(String name) {
			System.loadLibrary(name);
		}
	};

	/** Sets a custom library loader for Android. */
	public static void setAndroidLibraryLoader(AndroidLibraryLoader loader) {
		androidLibraryLoader = loader;
	}

	private String nativesJar;

	public SharedLibraryLoader () {
	}

	static String randomUUID () {
		return new UUID(random.nextLong(), random.nextLong()).toString();
	}

	/** Fetches the natives from the given natives jar file. Used for testing a shared lib on the fly.
	 * @param nativesJar */
	public SharedLibraryLoader (String nativesJar) {
		this.nativesJar = nativesJar;
	}

	/** Returns a CRC of the remaining bytes in the stream. */
	public String crc (InputStream input) {
		if (input == null) throw new IllegalArgumentException("input cannot be null.");
		CRC32 crc = new CRC32();
		byte[] buffer = new byte[4096];
		try {
			while (true) {
				int length = input.read(buffer);
				if (length == -1) break;
				crc.update(buffer, 0, length);
			}
		} catch (Exception ex) {
		} finally {
			closeQuietly(input);
		}
		return Long.toString(crc.getValue(), 16);
	}

	/** Maps a platform independent library name to a platform dependent name. */
	public String mapLibraryName (String libraryName) {
		if (HostDetection.os == Os.Android)
			return libraryName;
		return HostDetection.os.getLibPrefix() + libraryName + HostDetection.architecture.toSuffix() + HostDetection.bitness.toSuffix() + "." + HostDetection.os.getLibExtension();
	}

	/** Loads a shared library for the platform the application is running on.
	 * @param libraryName The platform independent library name. If not contain a prefix (eg lib) or suffix (eg .dll). */
	public void load (String libraryName) {
		// in case of iOS, it's unnecessary to dlopen
		if (HostDetection.os == Os.IOS) return;

		synchronized (SharedLibraryLoader.class) {
			if (isLoaded(libraryName)) return;
			String platformName = mapLibraryName(libraryName);
			try {
				if (HostDetection.os == Os.Android)
					androidLibraryLoader.load(platformName);
				else
					loadFile(platformName);
				setLoaded(libraryName);
			} catch (Throwable ex) {
				throw new SharedLibraryLoadRuntimeException("Couldn't load shared library '" + platformName + "' for target: "
					+ (HostDetection.os == Os.Android ? "Android" : (System.getProperty("os.name") + ", " + HostDetection.architecture.name() + ", " + HostDetection.bitness.name().substring(1) + "-bit")),
						ex);
			}
		}
	}

	private InputStream readFile (String path) {
		if (nativesJar == null) {
			InputStream input = SharedLibraryLoader.class.getClassLoader().getResourceAsStream(path);
			if (input == null) throw new SharedLibraryLoadRuntimeException("Unable to read file for extraction: " + path);
			return input;
		}

		// Read from JAR.
		try {
			ZipFile file = new ZipFile(nativesJar);
			ZipEntry entry = file.getEntry(path);
			if (entry == null) throw new SharedLibraryLoadRuntimeException("Couldn't find '" + path + "' in JAR: " + nativesJar);
			return file.getInputStream(entry);
		} catch (IOException ex) {
			throw new SharedLibraryLoadRuntimeException("Error reading '" + path + "' in JAR: " + nativesJar, ex);
		}
	}

	/** Extracts the specified file to the specified directory if it does not already exist or the CRC does not match. If file
	 * extraction fails and the file exists at java.library.path, that file is returned.
	 * @param sourcePath The file to extract from the classpath or JAR.
	 * @param dirName The name of the subdirectory where the file will be extracted. If null, the file's CRC will be used.
	 * @return The extracted file. */
	public File extractFile (String sourcePath, String dirName) throws IOException {
		try {
			String sourceCrc = crc(readFile(sourcePath));
			if (dirName == null) dirName = sourceCrc;

			File extractedFile = getExtractedFile(dirName, new File(sourcePath).getName());
			if (extractedFile == null) {
				extractedFile = getExtractedFile(randomUUID(), new File(sourcePath).getName());
				if (extractedFile == null) throw new SharedLibraryLoadRuntimeException(
					"Unable to find writable path to extract file. Is the user home directory writable?");
			}
			return extractFile(sourcePath, sourceCrc, extractedFile);
		} catch (RuntimeException ex) {
			// Fallback to file at java.library.path location, eg for applets.
			File file = new File(System.getProperty("java.library.path"), sourcePath);
			if (file.exists()) return file;
			throw ex;
		}
	}

	/** Extracts the specified file into the temp directory if it does not already exist or the CRC does not match. If file
	 * extraction fails and the file exists at java.library.path, that file is returned.
	 * @param sourcePath The file to extract from the classpath or JAR.
	 * @param dir The location where the extracted file will be written. */
	public void extractFileTo (String sourcePath, File dir) throws IOException {
		extractFile(sourcePath, crc(readFile(sourcePath)), new File(dir, new File(sourcePath).getName()));
	}

	/** Returns a path to a file that can be written. Tries multiple locations and verifies writing succeeds.
	 * @return null if a writable path could not be found. */
	private File getExtractedFile (String dirName, String fileName) {
		// Temp directory with username in path.
		File idealFile = new File(
			System.getProperty("java.io.tmpdir") + "/libgdx" + System.getProperty("user.name") + "/" + dirName, fileName);
		if (canWrite(idealFile)) return idealFile;

		// System provided temp directory.
		try {
			File file = File.createTempFile(dirName, null);
			if (file.delete()) {
				file = new File(file, fileName);
				if (canWrite(file)) return file;
			}
		} catch (IOException ignored) {
		}

		// User home.
		File file = new File(System.getProperty("user.home") + "/.libgdx/" + dirName, fileName);
		if (canWrite(file)) return file;

		// Relative directory.
		file = new File(".temp/" + dirName, fileName);
		if (canWrite(file)) return file;

		// We are running in the OS X sandbox.
		if (System.getenv("APP_SANDBOX_CONTAINER_ID") != null) return idealFile;

		return null;
	}

	/** Returns true if the parent directories of the file can be created and the file can be written. */
	private boolean canWrite (File file) {
		File parent = file.getParentFile();
		File testFile;
		if (file.exists()) {
			if (!file.canWrite() || !canExecute(file)) return false;
			// Don't overwrite existing file just to check if we can write to directory.
			testFile = new File(parent, randomUUID().toString());
		} else {
			parent.mkdirs();
			if (!parent.isDirectory()) return false;
			testFile = file;
		}
		try {
			new FileOutputStream(testFile).close();
			if (!canExecute(testFile)) return false;
			return true;
		} catch (Throwable ex) {
			return false;
		} finally {
			testFile.delete();
		}
	}

	private boolean canExecute (File file) {
		try {
			Method canExecute = File.class.getMethod("canExecute");
			if ((Boolean)canExecute.invoke(file)) return true;

			Method setExecutable = File.class.getMethod("setExecutable", boolean.class, boolean.class);
			setExecutable.invoke(file, true, false);

			return (Boolean)canExecute.invoke(file);
		} catch (Exception ignored) {
		}
		return false;
	}

	private File extractFile (String sourcePath, String sourceCrc, File extractedFile) throws IOException {
		String extractedCrc = null;
		if (extractedFile.exists()) {
			try {
				extractedCrc = crc(new FileInputStream(extractedFile));
			} catch (FileNotFoundException ignored) {
			}
		}

		// If file doesn't exist or the CRC doesn't match, extract it to the temp dir.
		if (extractedCrc == null || !extractedCrc.equals(sourceCrc)) {
			InputStream input = null;
			FileOutputStream output = null;
			try {
				input = readFile(sourcePath);
				extractedFile.getParentFile().mkdirs();
				output = new FileOutputStream(extractedFile);
				byte[] buffer = new byte[4096];
				while (true) {
					int length = input.read(buffer);
					if (length == -1) break;
					output.write(buffer, 0, length);
				}
			} catch (IOException ex) {
				throw new SharedLibraryLoadRuntimeException("Error extracting file: " + sourcePath + "\nTo: " + extractedFile.getAbsolutePath(),
					ex);
			} finally {
				closeQuietly(input);
				closeQuietly(output);
			}
		}

		return extractedFile;
	}

	/** Extracts the source file and calls System.load. Attemps to extract and load from multiple locations. Throws runtime
	 * exception if all fail. */
	private void loadFile (String sourcePath) {
		String sourceCrc = crc(readFile(sourcePath));

		String fileName = new File(sourcePath).getName();

		// Temp directory with username in path.
		File file = new File(System.getProperty("java.io.tmpdir") + "/libgdx" + System.getProperty("user.name") + "/" + sourceCrc,
			fileName);
		Throwable ex = loadFile(sourcePath, sourceCrc, file);
		if (ex == null) return;

		// System provided temp directory.
		try {
			file = File.createTempFile(sourceCrc, null);
			if (file.delete() && loadFile(sourcePath, sourceCrc, file) == null) return;
		} catch (Throwable ignored) {
		}

		// User home.
		file = new File(System.getProperty("user.home") + "/.libgdx/" + sourceCrc, fileName);
		if (loadFile(sourcePath, sourceCrc, file) == null) return;

		// Relative directory.
		file = new File(".temp/" + sourceCrc, fileName);
		if (loadFile(sourcePath, sourceCrc, file) == null) return;

		// Fallback to java.library.path location, eg for applets.
		file = new File(System.getProperty("java.library.path"), sourcePath);
		if (file.exists()) {
			loadFromAbsolutePath(file.getAbsolutePath());
			return;
		}

		throw new SharedLibraryLoadRuntimeException(ex);
	}

	/** @return null if the file was extracted and loaded. */
	private Throwable loadFile (String sourcePath, String sourceCrc, File extractedFile) {
		try {
			loadFromAbsolutePath(extractFile(sourcePath, sourceCrc, extractedFile).getAbsolutePath());
			return null;
		} catch (Throwable ex) {
			return ex;
		}
	}

	private void loadFromAbsolutePath(String path) {
		System.load(path);
	}

	/** Sets the library as loaded, for when application code wants to handle libary loading itself. */
	static public synchronized void setLoaded (String libraryName) {
		loadedLibraries.add(libraryName);
	}

	static public synchronized boolean isLoaded (String libraryName) {
		return loadedLibraries.contains(libraryName);
	}
	
	public static void closeQuietly (Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (Throwable ignored) {
			}
		}
	}
}

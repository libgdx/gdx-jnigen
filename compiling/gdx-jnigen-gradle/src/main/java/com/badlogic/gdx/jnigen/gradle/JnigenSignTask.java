package com.badlogic.gdx.jnigen.gradle;

import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.FileDescriptor;
import com.badlogic.gdx.jnigen.build.RuntimeEnv;
import com.badlogic.gdx.jnigen.build.ToolFinder;
import com.badlogic.gdx.jnigen.build.ToolchainExecutor;
import groovy.lang.Closure;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JnigenSignTask extends DefaultTask {


	private static final Logger log = LoggerFactory.getLogger(JnigenSignTask.class);

	private final JnigenExtension ext;

	@Inject
	public JnigenSignTask(JnigenExtension ext) {
		this.ext = ext;

		setGroup("jnigen");
		setDescription("Signs native libraries");
	}

	@TaskAction
	public void run() {
		BuildConfig buildConfig = new BuildConfig(ext.sharedLibName, ext.subProjectDir + ext.temporaryDir, ext.subProjectDir + ext.libsDir, ext.subProjectDir + ext.jniDir, null, new FileDescriptor(ext.subProjectDir));
		RuntimeEnv env = new RuntimeEnv();
		List<String> args = new ArrayList<>();
		ext.targets.forEach(target -> {
			switch (target.os) {
				case Windows:
					Map<String, String> params = ext.signing.getJsignParams();
					params.put("file", target.getTargetBinaryFile(buildConfig).getPath());
					Closure<?> jsign = (Closure<?>) ext.project.getExtensions().getByName("jsign");
					jsign.call(params);
					break;
				case Linux:
					File bsign = ToolFinder.getToolFile("bsign", env, true);
					String secretKeyRingFile = (String) getProject().property("signing.secretKeyRingFile");
					String keyId = (String) getProject().property("signing.keyId");
					String password = (String) getProject().property("signing.password");
					args.add("--sign");
					args.add(target.getTargetBinaryFile(buildConfig).getPath());
					args.add("-P");
					args.add(String.format("\"--keyring %s --default-key %s --passphrase %s --batch\"", secretKeyRingFile, keyId, password));
					ToolchainExecutor.execute(bsign, new File(""), args, new ToolchainExecutor.ToolchainCallback() {
						@Override
						public void onInfoMessage(String message) {
							log.info(message);
						}

						@Override
						public void onErrorMessage(String message) {
							log.error(message);
						}

						@Override
						public void onSuccess() {
							log.info("Signing successful");
						}

						@Override
						public void onFail(int statusCode) {
							log.error("Signing failed with status code: {}", statusCode);
						}
					});
					break;
				case MacOsX:
				case IOS:
					File codesign = ToolFinder.getToolFile("codesign", env, true);
					args.add("-s");
					args.add(ext.signing.getIdentity());
					args.add("-f");
					args.add(target.getTargetBinaryFile(buildConfig).getPath());
					ToolchainExecutor.execute(codesign, new File(""), args, new ToolchainExecutor.ToolchainCallback() {
						@Override
						public void onInfoMessage(String message) {
							log.info(message);
						}

						@Override
						public void onErrorMessage(String message) {
							log.error(message);
						}

						@Override
						public void onSuccess() {
							log.info("Signing successful");
						}

						@Override
						public void onFail(int statusCode) {
							log.error("Signing failed with status code: {}", statusCode);
						}
					});
					break;
				case Android:
					log.info("Signing not supported on Android");
					break;
			}
		});
	}
}

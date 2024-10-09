package com.badlogic.gdx.jnigen.build.packaging;

import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class PlatformPackager {

    private static final Logger logger = LoggerFactory.getLogger(PlatformPackager.class);

    protected BuildConfig buildConfig;
    protected List<BuildTarget> targetList;

    public void startPackage (BuildConfig config, List<BuildTarget> targetList) {
        this.buildConfig = config;
        this.targetList = targetList;

        packagePlatformImpl();
    }

    protected abstract void packagePlatformImpl ();

}

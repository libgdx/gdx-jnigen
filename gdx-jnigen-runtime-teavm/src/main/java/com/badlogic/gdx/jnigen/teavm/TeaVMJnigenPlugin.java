package com.badlogic.gdx.jnigen.teavm;

import org.teavm.vm.spi.TeaVMHost;
import org.teavm.vm.spi.TeaVMPlugin;

/**
 * TeaVM plugin that registers the class transformer for substituting
 * CHandler native methods with web-compatible implementations.
 * <p>
 * This plugin is discovered via {@code META-INF/services/org.teavm.vm.spi.TeaVMPlugin}.
 */
public class TeaVMJnigenPlugin implements TeaVMPlugin {

    @Override
    public void install(TeaVMHost host) {
        host.add(new CHandlerTransformer());
    }
}

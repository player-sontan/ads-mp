/*
 * Copyright 2024 <player.sontan@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ads.registry;

import org.ads.AdsContext;
import org.ads.driver.DriverType;
import org.ads.exception.AdsRuntimeException;
import org.ads.exception.AdsStartupException;
import org.ads.processor.AbstractDataShieldProcessor;
import org.ads.processor.DataShieldProcessor;
import org.ads.processor.SimpleDrivenProcessor;
import org.ads.provider.AssetsProvider;

import java.util.HashMap;
import java.util.Map;

public class DefautDriverRegistry implements DriverRegistry {
    private final Map<String, DriverType> driverTypes;
    private final Map<String, DataShieldProcessor> processors;
    private final Map<String, AssetsProvider> assetsProviders;

    private final AdsContext adsContext;
    private final SimpleDrivenProcessor simpleDrivenProcessor;

    public DefautDriverRegistry(AdsContext adsContext) {
        this.driverTypes = new HashMap<>();
        this.processors = new HashMap<>();
        this.assetsProviders = new HashMap<>();

        this.adsContext = adsContext;
        try {
            this.simpleDrivenProcessor = (SimpleDrivenProcessor) new SimpleDrivenProcessor().initializer().adsContext(adsContext).finish();
        } catch (Exception e) {
            throw new AdsStartupException("failed to initialize the simpleDrivenProcessor", e);
        }
    }

    @Override
    public DriverRegistry registeredDriverType(DriverType driverType, AssetsProvider assetsProvider) {
        if (driverTypes.containsKey(driverType.name())) {
            throw new AdsStartupException("driver type " + driverType.name() + " already defined");
        }

        driverTypes.put(driverType.name(), driverType);
        assetsProviders.put(driverType.name(), assetsProvider);

        return this;
    }

    @Override
    public DriverRegistry registeredDriverType(DriverType driverType, DataShieldProcessor processor) {
        if (driverTypes.containsKey(driverType.name())) {
            throw new AdsStartupException("driver type " + driverType.name() + " already defined");
        }

        if (processor instanceof AbstractDataShieldProcessor) {
            try {
                ((AbstractDataShieldProcessor) processor)
                        .initializer()
                        .adsContext(adsContext);
            } catch (Exception e) {
                throw new AdsStartupException("failed to initialize processor for driver type: " + driverType.name(), e);
            }
        }

        driverTypes.put(driverType.name(), driverType);
        processors.put(driverType.name(), processor);
        return this;
    }

    @Override
    public AssetsProvider getPermittedAssetsProvider(String driverType) {
        AssetsProvider assetsProvider = assetsProviders.get(driverType);
        if (assetsProvider == null) {
            throw new AdsRuntimeException("assets provider for driver type " + driverType + " is not defined");
        }
        return assetsProviders.get(driverType);
    }

    @Override
    public boolean hasAvailableDriver(String driverType) {
        if (!driverTypes.containsKey(driverType)) {
            return false;
        }

        DataShieldProcessor processor = getProcessor(driverType);

        if (processor == null) {
            return false;
        }

        return !(processor instanceof SimpleDrivenProcessor) || getPermittedAssetsProvider(driverType) != null;
    }

    @Override
    public DataShieldProcessor getProcessor(String driverType) {
        DataShieldProcessor processor = processors.get(driverType);
        if (processor == null) {
            return simpleDrivenProcessor;
        }
        return processor;
    }
}

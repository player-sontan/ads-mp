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

package org.ads.config;

import org.ads.registry.DataShieldRegistry;

import java.util.Objects;
import java.util.function.BiConsumer;

public class AdsTableConfigCenter implements DataShieldRegistry {

    private final AdsTableGlobalConfiguration adsTableGlobalConfiguration;

    public AdsTableConfigCenter(AdsTableGlobalConfiguration adsTableGlobalConfiguration) {
        this.adsTableGlobalConfiguration = adsTableGlobalConfiguration;
    }

    @Override
    public boolean globalShouldSkipShieldCheck() {
        return !adsTableGlobalConfiguration.getEnabled();
    }

    @Override
    public boolean tableHasDataShield(String tableName) {
        DataShield dataShield = adsTableGlobalConfiguration.getDataShields().get(tableName);
        return Objects.nonNull(dataShield);
    }

    @Override
    public DataShield getTableDataShield(String tableName) {
        return adsTableGlobalConfiguration.getDataShields().get(tableName);
    }

    @Override
    public void iterateDataShields(BiConsumer<String, DataShield> consumer) {
        adsTableGlobalConfiguration.getDataShields().forEach(consumer);
    }
}

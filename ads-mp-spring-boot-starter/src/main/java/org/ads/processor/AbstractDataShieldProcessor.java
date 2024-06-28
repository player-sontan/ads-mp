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

package org.ads.processor;

import org.ads.AdsContext;
import org.ads.registry.DataShieldRegistry;

public abstract class AbstractDataShieldProcessor implements DataShieldProcessor {
    protected DataShieldRegistry dataShieldRegistry;
    protected AdsContext adsContext;

    protected void setTablePermissionRegistry(DataShieldRegistry dataShieldRegistry) {
        this.dataShieldRegistry = dataShieldRegistry;
    }

    protected void setAdsContext(AdsContext adsContext) {
        this.adsContext = adsContext;
    }

    @Override
    public boolean shouldSkipPermissionCheck() {
        return false;
    }

    @Override
    public boolean shouldSkipPermissionCheckForTable(String tableName) {
        return !dataShieldRegistry.tableHasDataShield(tableName);
    }

    @Override
    public void preprocessBeforeModifying(String mappedStatementId, String tableName) {
        // do nothing
    }

    public NormalProcessorInitializer initializer() throws Exception {
        return new NormalProcessorInitializer(this);
    }
}

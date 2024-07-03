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

package org.ads;

import org.ads.config.AdsTableConfigCenter;
import org.ads.config.DataShield;
import org.ads.exception.AdsRuntimeException;
import org.ads.exception.AdsStartupException;
import org.ads.processor.DataShieldProcessor;
import org.ads.provider.AssetsProvider;
import org.ads.registry.DefautDriverRegistry;
import org.ads.registry.DriverRegistry;
import org.ads.registry.DataShieldRegistry;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.update.Update;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class AdsManager implements DataShieldProcessor, ConfigurableAdsManager, AdsContext, InitializingBean {

    private BooleanSupplier customizedShouldByPassShieldCheck;
    private final DataShieldRegistry dataShieldRegistry;
    private DriverRegistry driverRegistry;
    private final AdsManagerConfigurer adsManagerConfigurer;

    public AdsManager(AdsTableConfigCenter adsTableConfigCenter, AdsManagerConfigurer adsManagerConfigurer) {
        this.customizedShouldByPassShieldCheck = () -> true;

        this.dataShieldRegistry = adsTableConfigCenter;

        this.adsManagerConfigurer = adsManagerConfigurer;
    }

    @Override
    public void configureCustomizedShouldByPassShieldCheck(BooleanSupplier customizedShouldByPassShieldCheck) {
        this.customizedShouldByPassShieldCheck = customizedShouldByPassShieldCheck;
    }

    @Override
    public boolean shouldByPassShield() {
        if (dataShieldRegistry.globalShouldSkipShieldCheck()) {
            return true;
        }

        return !customizedShouldByPassShieldCheck.getAsBoolean();
    }

    @Override
    public boolean shouldByPassShieldForTable(String tableName) {
        DataShield dataShield = dataShieldRegistry.getTableDataShield(tableName);
        if (Objects.isNull(dataShield)) {
            return true;
        }
        String driverType = dataShield.getDriverType();
        if (!driverRegistry.hasAvailableDriver(driverType)) {
            throw new AdsRuntimeException("driver type " + driverType + " not available");
        }

        return driverRegistry.getProcessor(driverType).shouldByPassShieldForTable(tableName);
    }

    @Override
    public void processSelect(Table table, Supplier<PlainSelect> parentSelectNode) {
        DataShield dataShield = dataShieldRegistry.getTableDataShield(table.getName());
        String driverType = dataShield.getDriverType();

        driverRegistry.getProcessor(driverType).processSelect(table, parentSelectNode);
    }

    @Override
    public void preprocessBeforeModifying(String mappedStatementId, String tableName) {
        DataShield dataShield = dataShieldRegistry.getTableDataShield(tableName);
        String driverType = dataShield.getDriverType();

        driverRegistry.getProcessor(driverType).preprocessBeforeModifying(mappedStatementId, tableName);
    }

    @Override
    public void processUpdate(Table table, Supplier<Update> parentUpdateNode) {
        DataShield dataShield = dataShieldRegistry.getTableDataShield(table.getName());
        String driverType = dataShield.getDriverType();

        driverRegistry.getProcessor(driverType).processUpdate(table, parentUpdateNode);
    }

    @Override
    public void processDelete(Table table, Supplier<Delete> parentDeleteNode) {
        DataShield dataShield = dataShieldRegistry.getTableDataShield(table.getName());
        String driverType = dataShield.getDriverType();

        driverRegistry.getProcessor(driverType).processDelete(table, parentDeleteNode);
    }

    @Override
    public DataShield getDataShield(String tableName) {
        return dataShieldRegistry.getTableDataShield(tableName);
    }

    @Override
    public AssetsProvider getAssetsProvider(String tableName) {
        DataShield dataShield = dataShieldRegistry.getTableDataShield(tableName);
        String driverType = dataShield.getDriverType();
        return driverRegistry.getPermittedAssetsProvider(driverType);
    }

    @Override
    public DataShieldRegistry getDataShieldRegistry() {
        return this.dataShieldRegistry;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.driverRegistry = new DefautDriverRegistry(this);

        if (this.adsManagerConfigurer != null) {
            this.adsManagerConfigurer.configureDriver(this.driverRegistry);
            this.adsManagerConfigurer.configureCustomizedShouldByPassShieldCheck(this);
        }

        this.dataShieldRegistry.iterateDataShields((tableName, tableShield) -> {
            String driverType = tableShield.getDriverType();
            if (!driverRegistry.hasAvailableDriver(driverType)) {
                throw new AdsStartupException("driver type " + driverType + " not available");
            }
        });
    }
}

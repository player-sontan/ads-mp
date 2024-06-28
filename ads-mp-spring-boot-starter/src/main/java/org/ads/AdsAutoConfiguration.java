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
import org.ads.config.AdsTableGlobalConfiguration;
import org.ads.exception.AdsStartupException;
import org.ads.util.SpringContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AdsTableGlobalConfiguration.class)
public class AdsAutoConfiguration {

    @Bean
    public AdsTableGlobalConfiguration dataPermissionGlobalConfiguration() {
        return new AdsTableGlobalConfiguration();
    }

    @Bean
    public AdsTableConfigCenter dataPermissionConfigCenter() {
        return new AdsTableConfigCenter(dataPermissionGlobalConfiguration());
    }

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }

    @Bean
    @ConditionalOnBean(AdsManagerConfigurer.class)
    public AdsManager adsManager(AdsManagerConfigurer adsManagerConfigurer) {
        return new AdsManager(dataPermissionConfigCenter(), adsManagerConfigurer);
    }

    @Bean
    @ConditionalOnMissingBean(AdsManagerConfigurer.class)
    public AdsManager adsManagerDefault() {
        if (dataPermissionConfigCenter().globalShouldSkipShieldCheck()) {
            throw new AdsStartupException("ads is enabled, but you have not config ads by implementing AdsManagerConfigurer");
        }
        return new AdsManager(dataPermissionConfigCenter(), null);
    }
}

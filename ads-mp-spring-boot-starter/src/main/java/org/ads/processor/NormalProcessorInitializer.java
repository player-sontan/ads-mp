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

public class NormalProcessorInitializer {

    private final AbstractDataShieldProcessor processor;

    public NormalProcessorInitializer(AbstractDataShieldProcessor processor) throws Exception {
        if (processor == null) {
            throw new Exception("attempting to initialize a processor, but processor instance is null");
        }
        this.processor = processor;
    }

    public NormalProcessorInitializer adsContext(AdsContext adsContext) {
        processor.setAdsContext(adsContext);
        processor.setDataShieldRegistry(adsContext.getDataShieldRegistry());
        return this;
    }

    public AbstractDataShieldProcessor finish() {
        return processor;
    }
}

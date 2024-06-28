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

package org.ads.test.ads;

import org.ads.AdsManagerConfigurer;
import org.ads.ConfigurableAdsManager;
import org.ads.registry.DriverRegistryConfigurer;
import org.ads.test.ads.cls.ClassDriver;
import org.ads.test.ads.cls.ClassAssetsProvider;
import org.ads.test.ads.grade.GradeDataShieldProcessor;
import org.ads.test.ads.grade.GradeDriver;
import org.ads.test.ads.teacher.TeacherDataShieldProcessor;
import org.ads.test.ads.teacher.TeacherDriver;
import org.springframework.stereotype.Component;

@Component
public class AdsCustomConfigurer implements AdsManagerConfigurer {

    @Override
    public void configureDriver(DriverRegistryConfigurer driverRegistry) {
        driverRegistry
                .definePermissionDriver(new ClassDriver(), new ClassAssetsProvider())
                .definePermissionDriver(new GradeDriver(), new GradeDataShieldProcessor())
                .definePermissionDriver(new TeacherDriver(), new TeacherDataShieldProcessor());
    }

    @Override
    public void configurePermissionCheckPredicate(ConfigurableAdsManager configurableAdsManager) {
        configurableAdsManager.configPermissionCheckPredicate(() -> true);
    }
}

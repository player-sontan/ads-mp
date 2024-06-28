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

package org.ads.test.ads.cls;

import org.ads.provider.AssetsProvider;
import org.ads.provider.ProvidedValueType;
import org.ads.test.security.FakeSecurityContext;

import java.util.List;

public class ClassAssetsProvider implements AssetsProvider {

    @Override
    public ProvidedValueType valueType() {
        return ProvidedValueType.INT;
    }

    @Override
    public List<Object> provideReadOnlyValueList() {
        FakeSecurityContext currentUser = FakeSecurityContext.getCurrentUser();
        if (currentUser.hasUserLogin()) {
            return currentUser.getUserClasses();
        }

        return null;
    }

    @Override
    public List<Object> provideWriteOnlyValueList() {
        FakeSecurityContext currentUser = FakeSecurityContext.getCurrentUser();
        if (currentUser.hasUserLogin()) {
            return currentUser.getUserClasses();
        }

        return null;
    }

    @Override
    public List<Object> provideReadAndWriteValueList() {
        FakeSecurityContext currentUser = FakeSecurityContext.getCurrentUser();
        if (currentUser.hasUserLogin()) {
            return currentUser.getUserClasses();
        }

        return null;
    }

    @Override
    public boolean hasAsset(Object asset) {
        return false;
    }

    @Override
    public boolean hasReadOnlyAsset(Object asset) {
        return false;
    }

    @Override
    public boolean hasWriteOnlyAsset(Object asset) {
        return false;
    }

    @Override
    public boolean hasReadAndWriteAsset(Object asset) {
        return false;
    }
}

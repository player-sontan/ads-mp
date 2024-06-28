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

package org.ads.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.ads.test.mapper.ClassMaterialMapper;
import org.ads.test.security.FakeSecurityContext;
import org.ads.test.service.ClassMaterial;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.mockStatic;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Application.class)
public class SimpleTest {
    @Autowired
    private ClassMaterialMapper classMaterialMapper;

    @Test
    void should_see_personal_class_materials() {
        FakeSecurityContext amy = FakeSecurityContext.amy();
        MockedStatic<FakeSecurityContext> fakeSecurityContextMockedStatic = mockStatic(FakeSecurityContext.class);
        fakeSecurityContextMockedStatic.when(FakeSecurityContext::getCurrentUser).thenReturn(amy);

        LambdaQueryWrapper<ClassMaterial> wrapper = Wrappers.lambdaQuery(ClassMaterial.class);
        List<ClassMaterial> classMaterials = classMaterialMapper.selectList(wrapper);

        fakeSecurityContextMockedStatic.close();

    }
}

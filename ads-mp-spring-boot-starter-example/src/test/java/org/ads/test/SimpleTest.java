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
import org.ads.test.entity.ClassMaterial;
import org.ads.test.entity.TeacherNotice;
import org.ads.test.mapper.ClassMaterialMapper;
import org.ads.test.mapper.StudentMapper;
import org.ads.test.security.FakeSecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Application.class)
public class SimpleTest {
    @Autowired
    private ClassMaterialMapper classMaterialMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Test
    void should_see_personal_class_materials() throws IOException {
        FakeSecurityContext amy = FakeSecurityContext.amy();
        MockedStatic<FakeSecurityContext> fakeSecurityContextMockedStatic = mockStatic(FakeSecurityContext.class);
        fakeSecurityContextMockedStatic.when(FakeSecurityContext::getCurrentUser).thenReturn(amy);

        PrintStream out = System.out;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        LambdaQueryWrapper<ClassMaterial> wrapper = Wrappers.lambdaQuery(ClassMaterial.class);
        List<ClassMaterial> classMaterials = classMaterialMapper.selectList(wrapper);

        assertTrue(bos.toString().contains("SELECT class_id, material_name FROM class_material WHERE class_material.class_id IS NULL OR class_material.class_id IN (1)"));
        assertEquals(1, classMaterials.size());

        fakeSecurityContextMockedStatic.close();
        System.setOut(out);
        System.out.print(bos);
        bos.close();

    }

    /**
     * this represents a complicated sql query with join expressions
     */
    @Test
    void should_see_personal_teacher_notice() throws IOException {
        FakeSecurityContext amy = FakeSecurityContext.amy();
        MockedStatic<FakeSecurityContext> fakeSecurityContextMockedStatic = mockStatic(FakeSecurityContext.class);
        fakeSecurityContextMockedStatic.when(FakeSecurityContext::getCurrentUser).thenReturn(amy);

        PrintStream out = System.out;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        List<TeacherNotice> teacherNotices = studentMapper.selectNotice();

        assertTrue(bos.toString().contains("SELECT * FROM class_teacher ct LEFT JOIN teacher_notice tn ON ct.teacher_id = tn.teacher_id WHERE ct.class_id IS NULL OR ct.class_id IN (1)"));
        assertEquals(2, teacherNotices.size());

        fakeSecurityContextMockedStatic.close();
        System.setOut(out);
        System.out.print(bos);
        bos.close();
    }
}

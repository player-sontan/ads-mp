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

package org.ads.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.ads.test.entity.Student;
import org.ads.test.entity.TeacherNotice;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper extends BaseMapper<Student> {

    @Select("select * from class_teacher ct left join teacher_notice tn on ct.teacher_id=tn.teacher_id")
    List<TeacherNotice> selectNotice();
}

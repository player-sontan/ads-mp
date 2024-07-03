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

create table if not exists student (
    student_name varchar(255) not null,
    class_id int not null,
    grade_id int not null
);

create table if not exists class_teacher (
    class_id int not null,
    teacher_id   int not null
);

create table if not exists class_material (
    class_id int not null,
    material_name varchar(255) not null
);

create table if not exists grade_book (
    grade_id int not null,
    book_name varchar(255) not null
);

create table if not exists teacher_notice (
    teacher_id int not null,
    notice_content varchar(255) not null,
    book_name varchar(255) not null
);
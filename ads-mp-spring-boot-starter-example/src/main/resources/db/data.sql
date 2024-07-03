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

insert into student (student_name, class_id, grade_id) values ('Amy', 1, 1);
insert into student (student_name, class_id, grade_id) values ('Bob', 2, 1);
insert into student (student_name, class_id, grade_id) values ('Carl', 2, 2);

insert into class_teacher(class_id, teacher_id) values (1, 1);
insert into class_teacher(class_id, teacher_id) values (2, 2);
insert into class_teacher(class_id, teacher_id) values (2, 3);
insert into class_teacher(class_id, teacher_id) values (1, 4);
insert into class_teacher(class_id, teacher_id) values (2, 5);
insert into class_teacher(class_id, teacher_id) values (2, 6);

insert into class_material(class_id, material_name) values (1, 'BlackBoard');
insert into class_material(class_id, material_name) values (2, 'WhiteBoard');
insert into class_material(class_id, material_name) values (3, 'Pen');

insert into grade_book(grade_id, book_name) values (1, 'Math1');
insert into grade_book(grade_id, book_name) values (2, 'Math2');
insert into grade_book(grade_id, book_name) values (3, 'Math3');
insert into grade_book(grade_id, book_name) values (1, 'Physics1');
insert into grade_book(grade_id, book_name) values (2, 'Physics2');
insert into grade_book(grade_id, book_name) values (3, 'Physics3');


insert into teacher_notice(teacher_id, notice_content, book_name) values (1, 'Do math homework page 1 to 2 please', 'Math1');
insert into teacher_notice(teacher_id, notice_content, book_name) values (2, 'Do math homework page 1 to 2 please', 'Math2');
insert into teacher_notice(teacher_id, notice_content, book_name) values (3, 'Do math homework page 1 to 2 please', 'Math3');
insert into teacher_notice(teacher_id, notice_content, book_name) values (4, 'Read books yourself', 'Physics1');
insert into teacher_notice(teacher_id, notice_content, book_name) values (5, 'Read books yourself', 'Physics2');
insert into teacher_notice(teacher_id, notice_content, book_name) values (6, 'Read books yourself', 'Physics3');




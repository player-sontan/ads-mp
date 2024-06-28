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

package org.ads.test.ads.grade;

import org.ads.processor.AbstractDataShieldProcessor;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.update.Update;

import java.util.function.Supplier;

public class GradeDataShieldProcessor extends AbstractDataShieldProcessor {

    @Override
    public void processSelect(Table table, Supplier<PlainSelect> parentSelectNode) {

    }

    @Override
    public void processUpdate(Table table, Supplier<Update> parentUpdateNode) {

    }

    @Override
    public void processDelete(Table table, Supplier<Delete> parentDeleteNode) {

    }
}

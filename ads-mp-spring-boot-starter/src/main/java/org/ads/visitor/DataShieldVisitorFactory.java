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

package org.ads.visitor;

import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;

public class DataShieldVisitorFactory {

    public static DataShieldSelectVisitorAdapter SELECT = new DataShieldSelectVisitorAdapter();

    public static DataShieldSelectTableVisitor SELECT_FROM_PART = new DataShieldSelectTableVisitor();

    public static DataShieldSelectJoinTableVisitor SELECT_JOIN_PART = new DataShieldSelectJoinTableVisitor();

    public static DataShieldUpdateTableVisitor updateTableVisitorWithParent(Update parent) {
        return new DataShieldUpdateTableVisitor(parent);
    }

    public static DataShieldDeleteTableVisitor deleteTableVisitorWithParent(Delete parent) {
        return new DataShieldDeleteTableVisitor(parent);
    }
}

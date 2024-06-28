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

import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class DataShieldSelectVisitorAdapter extends SelectVisitorAdapter {

    @Override
    public void visit(PlainSelect plainSelect) {
        FromItem fromItem = plainSelect.getFromItem();
        fromItem.accept(DataShieldVisitorFactory.SELECT_FROM_PART);

        List<Join> joins = plainSelect.getJoins();
        if (CollectionUtils.isEmpty(joins)) {
            return;
        }

        joins.forEach(this::visitJoin);
    }

    private void visitJoin(Join join) {
        join.getRightItem().accept(DataShieldVisitorFactory.SELECT_JOIN_PART);
    }
}

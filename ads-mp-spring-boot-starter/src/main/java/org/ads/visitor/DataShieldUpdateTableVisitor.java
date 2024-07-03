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

import org.ads.AdsManager;
import org.ads.util.SpringContextUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitorAdapter;
import net.sf.jsqlparser.statement.update.Update;

public class DataShieldUpdateTableVisitor extends FromItemVisitorAdapter {
    private final Update parent;

    public DataShieldUpdateTableVisitor(Update parent) {
        this.parent = parent;
    }


    @Override
    public void visit(Table table) {
        String name = table.getName();
        AdsManager adsManager = SpringContextUtil.getBean(AdsManager.class);
        if (adsManager.shouldByPassShieldForTable(name)) {
            return;
        }

        adsManager.processUpdate(table, () -> parent);
    }
}

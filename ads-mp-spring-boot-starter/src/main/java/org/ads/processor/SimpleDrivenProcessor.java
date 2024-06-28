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

package org.ads.processor;

import org.ads.exception.AdsProcessingException;
import org.ads.provider.AssetsProvider;
import org.ads.provider.ProvidedValueType;
import org.ads.util.DataShieldSqlHelper;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.update.Update;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SimpleDrivenProcessor extends AbstractDataShieldProcessor {

    @Override
    public void processSelect(Table table, Supplier<PlainSelect> parentSelectNode) {
        AssetsProvider assetsProvider = adsContext.getAssetsProvider(table.getName());
        List<Object> list = assetsProvider.provideReadOnlyValueList();

        if (CollectionUtils.isEmpty(list)) {
            DataShieldSqlHelper.addNoPermissionExpressionToSelectWhere(parentSelectNode);
            return;
        }

        DataShieldSqlHelper.addSelectShieldExpressionToWhere(table,
                parentSelectNode,
                adsContext.getDataShield(table.getName()).getColumnName(),
                wrapReadOnlyValueList(assetsProvider));
    }

    @Override
    public void processUpdate(Table table, Supplier<Update> parentUpdateNode) {
        AssetsProvider assetsProvider = adsContext.getAssetsProvider(table.getName());
        List<Object> list = assetsProvider.provideWriteOnlyValueList();
        if (CollectionUtils.isEmpty(list)) {
            throw new AdsProcessingException("update on table is not permitted, because you have empty permitted assets");
        }

        DataShieldSqlHelper.addUpdateShieldExpressionToWhere(table,
                parentUpdateNode,
                adsContext.getDataShield(table.getName()).getColumnName(),
                wrapWriteOnlyValueList(assetsProvider));
    }

    @Override
    public void processDelete(Table table, Supplier<Delete> parentDeleteNode) {
        AssetsProvider assetsProvider = adsContext.getAssetsProvider(table.getName());
        List<Object> list = assetsProvider.provideWriteOnlyValueList();

        if (CollectionUtils.isEmpty(list)) {
            throw new AdsProcessingException("delete on table is not permitted, because you have empty permitted assets");
        }

        DataShieldSqlHelper.addDeleteShieldExpressionToWhere(table,
                parentDeleteNode,
                adsContext.getDataShield(table.getName()).getColumnName(),
                wrapWriteOnlyValueList(assetsProvider));
    }

    private List<Expression> wrapReadOnlyValueList(AssetsProvider assetsProvider) {
        return wrapValueList(assetsProvider.provideReadOnlyValueList(), assetsProvider.valueType());
    }

    private List<Expression> wrapWriteOnlyValueList(AssetsProvider assetsProvider) {
        return wrapValueList(assetsProvider.provideWriteOnlyValueList(), assetsProvider.valueType());
    }

    private List<Expression> wrapReadAndWriteValueList(AssetsProvider assetsProvider) {
        return wrapValueList(assetsProvider.provideReadAndWriteValueList(), assetsProvider.valueType());
    }

    private List<Expression> wrapValueList(List<Object> valueList, ProvidedValueType valueType) {
        List<Expression> expressionList;

        switch (valueType) {
            case LONG:
                expressionList = valueList.stream()
                        .map(val -> new LongValue().withValue((Long) val))
                        .collect(Collectors.toList());
                break;
            case INT:
                expressionList = valueList.stream()
                        .map(val -> new LongValue().withValue((Integer) val))
                        .collect(Collectors.toList());
                break;
            default:
                throw new AdsProcessingException("value type is not supported currently: " + valueType);
        }

        return expressionList;
    }
}

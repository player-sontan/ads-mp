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

package org.ads.util;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.update.Update;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class DataShieldSqlHelper {

    public static void addSelectShieldExpressionToWhere(Table table, Supplier<PlainSelect> parent, String columnName, List<Expression> inList) {
        OrExpression orExpression = new OrExpression();
        IsNullExpression isNullExpression = new IsNullExpression();
        isNullExpression.setLeftExpression(new Column().withTable(table).withColumnName(columnName));
        orExpression.setLeftExpression(isNullExpression);

        InExpression subInExpression = new InExpression();
        Column left = new Column().withTable(table).withColumnName(columnName);
        subInExpression.setLeftExpression(left);
        ExpressionList rightExpressionList = new ExpressionList();
        rightExpressionList.addExpressions(inList);
        subInExpression.setRightItemsList(rightExpressionList);

        orExpression.setRightExpression(subInExpression);

        Expression parentWhere = parent.get().getWhere();
        if (Objects.isNull(parentWhere)) {
            parent.get().setWhere(orExpression);
        } else {
            AndExpression andExpression = new AndExpression();
            andExpression.setLeftExpression(parentWhere);
            andExpression.setRightExpression(new Parenthesis(orExpression));
            parent.get().setWhere(andExpression);
        }
    }

    public static void addNoPermissionExpressionToSelectWhere(Supplier<PlainSelect> parent) {
        LongValue left = new LongValue().withValue(1L);
        LongValue right = new LongValue().withValue(0L);
        EqualsTo equalsTo = new EqualsTo().withLeftExpression(left).withRightExpression(right);
        Expression parentWhere = parent.get().getWhere();
        if (Objects.isNull(parentWhere)) {
            parent.get().setWhere(equalsTo);
        } else {
            AndExpression andExpression = new AndExpression();
            andExpression.setLeftExpression(parentWhere);
            andExpression.setRightExpression(equalsTo);
            parent.get().setWhere(andExpression);
        }
    }

    public static void addUpdateShieldExpressionToWhere(Table table, Supplier<Update> parent, String columnName, List<Expression> inList) {

        OrExpression orExpression = new OrExpression();
        IsNullExpression isNullExpression = new IsNullExpression();
        isNullExpression.setLeftExpression(new Column().withTable(table).withColumnName(columnName));
        orExpression.setLeftExpression(isNullExpression);

        InExpression subInExpression = new InExpression();
        Column left = new Column().withTable(table).withColumnName(columnName);
        subInExpression.setLeftExpression(left);
        ExpressionList rightExpressionList = new ExpressionList();
        rightExpressionList.addExpressions(inList);
        subInExpression.setRightItemsList(rightExpressionList);

        orExpression.setRightExpression(subInExpression);

        Expression parentWhere = parent.get().getWhere();
        if (Objects.isNull(parentWhere)) {
            parent.get().setWhere(orExpression);
        } else {
            AndExpression andExpression = new AndExpression();
            andExpression.setLeftExpression(parentWhere);
            andExpression.setRightExpression(new Parenthesis(orExpression));
            parent.get().setWhere(andExpression);
        }
    }

    public static void addDeleteShieldExpressionToWhere(Table table, Supplier<Delete> parent, String columnName, List<Expression> inList) {
        OrExpression orExpression = new OrExpression();
        IsNullExpression isNullExpression = new IsNullExpression();
        isNullExpression.setLeftExpression(new Column().withTable(table).withColumnName(columnName));
        orExpression.setLeftExpression(isNullExpression);

        InExpression subInExpression = new InExpression();
        Column left = new Column().withTable(table).withColumnName(columnName);
        subInExpression.setLeftExpression(left);
        ExpressionList rightExpressionList = new ExpressionList();
        rightExpressionList.addExpressions(inList);
        subInExpression.setRightItemsList(rightExpressionList);

        Expression parentWhere = parent.get().getWhere();
        if (Objects.isNull(parentWhere)) {
            parent.get().setWhere(orExpression);
        } else {
            AndExpression andExpression = new AndExpression();
            andExpression.setLeftExpression(parentWhere);
            andExpression.setRightExpression(new Parenthesis(orExpression));
            parent.get().setWhere(andExpression);
        }
    }
}

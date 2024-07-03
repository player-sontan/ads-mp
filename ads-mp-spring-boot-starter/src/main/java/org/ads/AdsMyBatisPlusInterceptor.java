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

package org.ads;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.ads.exception.AdsRuntimeException;
import org.ads.util.SpringContextUtil;
import org.ads.visitor.DataShieldVisitorFactory;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class AdsMyBatisPlusInterceptor extends JsqlParserSupport implements InnerInterceptor {
    Logger log = LoggerFactory.getLogger(AdsMyBatisPlusInterceptor.class);

    @Override
    public boolean willDoUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
            return true;
        }

        AdsManager adsManager = SpringContextUtil.getBean(AdsManager.class);
        if (adsManager.shouldByPassShield()) {
            return true;
        }

        BoundSql boundSql = ms.getBoundSql(parameter);
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(mpBs.sql());
        } catch (JSQLParserException e) {
            throw new AdsRuntimeException("sql statement grammar may has mistakes");
        }
        if (ms.getSqlCommandType() == SqlCommandType.UPDATE) {
            Update update = (Update) statement;
            adsManager.preprocessBeforeModifying(ms.getId(), update.getTable().getName());
        } else if (ms.getSqlCommandType() == SqlCommandType.DELETE) {
            Delete delete = (Delete) statement;
            adsManager.preprocessBeforeModifying(ms.getId(), delete.getTable().getName());
        } else {

        }
        return true;
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        AdsManager adsManager = SpringContextUtil.getBean(AdsManager.class);
        if (adsManager.shouldByPassShield()) {
            return;
        }
        BoundSql boundSql = sh.getBoundSql();
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(parserSingle(mpBs.sql(), null));
        if (log.isDebugEnabled()) {
            log.debug("ads processed sql: {}", mpBs.sql());
        }
    }

    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        delete.getTable().accept(DataShieldVisitorFactory.deleteTableVisitorWithParent(delete));
    }

    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        update.getTable().accept(DataShieldVisitorFactory.updateTableVisitorWithParent(update));
    }

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            selectBody.accept(DataShieldVisitorFactory.SELECT);
        } else if (selectBody instanceof SetOperationList) {
            // nothing to do
        }
    }

    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        // nothing to do
    }
}


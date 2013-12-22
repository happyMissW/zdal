/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.parser.result.DefaultSqlParserResult;
import com.alipay.zdal.parser.result.SqlParserResult;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLParserOfOracleWithInsertTest.java, v 0.1 2012-5-29 ����10:43:14 xiaoqing.zhouxq Exp $
 */
public class SQLParserOfOracleWithInsertTest {
    private static final String   ORACLE_INSERT        = "insert into users (id, gmt_create,name) values(?,now(), ?)";

    private static final Object[] ORACLE_INSERT_ARGS   = new Object[] { 100, "zhouxiaoqing" };

    private static final String   ORACLE_INSERT_NOBIND = "insert into users (id, gmt_create,name) values(22,now(), 'xiaoqing.zhouxq')";

    private static final String   PATITION_NAME        = "name";

    /**
     * ���԰󶨲���ʱ���жϲ�ֹ����Ƿ���ȷ.
     */
    //    @Test
    public void testParseWithPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(ORACLE_INSERT, false);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(Arrays
            .asList(ORACLE_INSERT_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(ORACLE_INSERT_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME);
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(ORACLE_INSERT_ARGS), partinationSet);
        Assert.assertEquals(1, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
            Assert.assertEquals("zhouxiaoqing", entry.getValue().getValue());
        }
    }

    /**
     * ���԰󶨲����ǣ��������ֶβ���sql����У���ֹ���ᱨ��.
     */
    //    @Test(expected = SqlParserException.class)
    public void testParserWithoutPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(ORACLE_INSERT, false);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(Arrays
            .asList(ORACLE_INSERT_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(ORACLE_INSERT_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME + 1);
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(ORACLE_INSERT_ARGS), partinationSet);
        Assert.assertEquals(1, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
            Assert.assertEquals("zhouxiaoqing", entry.getValue().getValue());
        }
    }

    /**
     * ���԰󶨲���ʱ���ж϶���ֶεĲ�ֹ����Ƿ���ȷ.
     */
    //    @Test
    public void testParserWithMultiPartinations() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(ORACLE_INSERT, false);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(Arrays
            .asList(ORACLE_INSERT_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(ORACLE_INSERT_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("id");
        partinationSet.add(PATITION_NAME);
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(ORACLE_INSERT_ARGS), partinationSet);

        Assert.assertEquals(2, patitions.size());

        Comparative idCompa = patitions.get("id");
        Assert.assertEquals(Comparative.Equivalent, idCompa.getComparison());
        Assert.assertEquals(100, idCompa.getValue());

        Comparative nameCompa = patitions.get(PATITION_NAME);
        Assert.assertEquals(Comparative.Equivalent, nameCompa.getComparison());
        Assert.assertEquals("zhouxiaoqing", nameCompa.getValue());
    }

    /**
     * ���Էǰ󶨲���ʱ���жϵ����ֶεĲ�ֹ����Ƿ���ȷ.
     */
    //    @Test
    public void testParserWithNoBindPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(ORACLE_INSERT_NOBIND, false);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(null));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(null));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("id");
        Map<String, Comparative> partitions = parserResult.getComparativeMapChoicer()
            .getColumnsMap(null, partinationSet);
        Assert.assertEquals(1, partitions.size());
        for (Entry<String, Comparative> entry : partitions.entrySet()) {
            Assert.assertEquals("id", entry.getKey());
            Assert.assertEquals(22, entry.getValue().getValue());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
        }
    }

    /**
     * ���Էǰ󶨲���ʱ���жϵ����ֶεĲ�ֹ����Ƿ���ȷ.
     */
    @Test
    public void testParserWithNoBindPartination1() {
        SQLParser sqlParser = new DefaultSQLParser();
        String sql = "insert into tradecore08.fund_flow_info_08 values('BO20130513014163608201','20881020107848240156','20881020107848240156','FREEZE','500','coupon','COUPON_ID=12','08-7�� -11 03.22.42.000000 ����','08-7�� -11 03.22.42.000000 ����') ";
        SqlParserResult parserResult = sqlParser.parse(sql, false);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(null));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(null));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("id");
        Map<String, Comparative> partitions = parserResult.getComparativeMapChoicer()
            .getColumnsMap(null, partinationSet);
        Assert.assertEquals(1, partitions.size());
        for (Entry<String, Comparative> entry : partitions.entrySet()) {
            Assert.assertEquals("id", entry.getKey());
            Assert.assertEquals(22, entry.getValue().getValue());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
        }
    }
}
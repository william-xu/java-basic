package com.xwl41.common.basic.helper.db;

import com.xwl41.common.basic.helper.db.conf.DbConfig;
import com.xwl41.common.basic.helper.db.conf.MySQLConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MySQLHelper extends AbstractDbHelper{

    private MySQLHelper(){
        this.config = new MySQLConfig().setPort(3306);
    }

    public static MySQLHelper newInstance(){
        return new MySQLHelper();
    }

    @Override
    public Map<String, String> getTableColumns(Connection conn, String tableSchema, String tableName) throws SQLException {
        Supplier<String> querySqlSupplier = ()-> "SELECT COLUMN_NAME, DATA_TYPE FROM information_schema.COLUMNS " +
                " WHERE TABLE_SCHEMA = '" + tableSchema + "' AND TABLE_NAME = '" + tableName + "' " +
                " ORDER BY ORDINAL_POSITION";
        return DbHelper.getTableColumns(conn, querySqlSupplier);
    }

    @Override
    public Map<String, String> getTableColumns(String tableName) throws SQLException {
        try(Connection conn = getConnection()){
            return getTableColumns(conn, config.getDbName(), tableName);
        }
    }

    @Override
    public Map<String, String> getTableColumns(Connection conn, String tableName) throws SQLException {
        return getTableColumns(conn, config.getDbName(), tableName);
    }

    @Override
    public Map<String, String> getTableColumns(String tableSchema, String tableName) throws SQLException {
        String tempUrl = MessageFormat.format(config.getUrlTemplate(), tableSchema);
        try(Connection conn = getConnection(tempUrl)){
           return getTableColumns(conn, tableSchema, tableName);
        }
    }

    @Override
    protected DbConfig assembleDbConfig(String host, Integer port, String username, String password, String dbName) {
        return new MySQLConfig(host, port, username, password, dbName);
    }

}

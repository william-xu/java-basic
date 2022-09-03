package com.xwl41.common.basic.helper.db;

import com.xwl41.common.basic.helper.db.conf.DbConfig;
import com.xwl41.common.basic.helper.db.conf.H2Config;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class H2Helper extends AbstractDbHelper {

    private H2Helper() {
        this.config = new H2Config();
    }

    public static H2Helper newInstance() {
        return new H2Helper();
    }

    @Override
    public Map<String, String> getTableColumns(Connection conn, String tableSchema, String tableName) throws SQLException {
        Supplier<String> queryStrSupplier = () -> "SELECT COLUMN_NAME, DATA_TYPE FROM information_schema.COLUMNS " +
                " WHERE TABLE_CATALOG = '" + tableSchema.toUpperCase() + "' " +
                " AND TABLE_NAME = '" + tableName.toUpperCase() + "' " +
                " ORDER BY ORDINAL_POSITION";
        return DbHelper.getTableColumns(conn, queryStrSupplier);
    }

    @Override
    public Map<String, String> getTableColumns(String tableName) throws SQLException {
        try (Connection conn = getConnection()) {
            return getTableColumns(conn, config.getDbName(), tableName);
        }
    }

    @Override
    public Map<String, String> getTableColumns(String tableSchema, String tableName) throws SQLException {
        String tempUrl = MessageFormat.format(config.getUrlTemplate(), tableSchema);
        try (Connection conn = getConnection(tempUrl)) {
            return getTableColumns(conn, tableSchema, tableName);
        }
    }

    @Override
    protected DbConfig assembleDbConfig(String host, Integer port, String username, String password, String dbName) {
        return new H2Config(host, port, username, password, dbName);
    }
}

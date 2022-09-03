package com.xwl41.common.basic.helper.db;

import com.xwl41.common.basic.helper.db.conf.DbConfig;

import javax.sql.DataSource;
import java.sql.*;

public abstract class AbstractDbHelper implements DbHelper {
    protected DbConfig config;

    //////////////////////////////////////////////////////////////////////////////
    //    Initialization  初始化
    //////////////////////////////////////////////////////////////////////////////
    @Override
    public void init(String host, Integer port, String username, String password, String dbName) {
        this.config = assembleDbConfig(host, port, username, password, dbName);
    }

    @Override
    public void init(String host, String username, String password, String dbName) {
        this.config = assembleDbConfig(host, null, username, password, dbName);
    }

    @Override
    public void init(DbConfig dbConfig) {
        this.config = dbConfig;
    }

    @Override
    public DbConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(DbConfig config) {
        this.config = config;
    }

    //////////////////////////////////////////////////////////////////////////////
    //    Get Connection 获取数据库连接
    //////////////////////////////////////////////////////////////////////////////
    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    }

    protected Connection getConnection(String newUrl) throws SQLException {
        return getConnection(newUrl, config.getUsername(), config.getPassword());
    }

    @Override
    public Connection getConnection(String url, String username, String password) throws SQLException {
        try {
            Class.forName(config.getDbType().driverClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(url, username, password);
    }

    //////////////////////////////////////////////////////////////////////////////
    //    Get Datasource  获取数据源
    //////////////////////////////////////////////////////////////////////////////
    @Override
    public DataSource getDataSource(String newUrl) {
        return DbHelper.getDataSource(newUrl, config.getUsername(), config.getPassword());
    }

    @Override
    public DataSource getDataSource() {
        return DbHelper.getDataSource(config.getUrl(), config.getUsername(), config.getPassword());
    }

    @Override
    public ResultSet executeQuery(String queryStr) throws SQLException {
        try(Connection conn = getConnection();
            ResultSet rs = conn.createStatement().executeQuery(queryStr))   {
            return rs;
        }
    }

    @Override
    public boolean execute(String sqlStr) throws SQLException {
        try(Connection conn = getConnection();
            Statement stmt = conn.createStatement()){
            return stmt.execute(sqlStr);
        }
    }

    @Override
    public int executeUpdate(String updateSqlStr) throws SQLException {
        try(Connection conn = getConnection();
            Statement stmt = conn.createStatement()){
            return stmt.executeUpdate(updateSqlStr);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    //    abstract methods  抽象方法
    //////////////////////////////////////////////////////////////////////////////
    /**
     * 组装数据库配置类实例
     * @param host
     * @param port
     * @param username
     * @param password
     * @param dbName
     * @return
     */
    protected abstract DbConfig assembleDbConfig(String host, Integer port, String username, String password, String dbName);

}

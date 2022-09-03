package com.xwl41.common.basic.helper.db;

import com.xwl41.common.basic.helper.db.conf.DbConfig;
import com.xwl41.common.basic.util.CoreUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.xwl41.common.basic.util.CoreUtil.REFLECT.*;


public interface DbHelper {
    //////////////////////////////////////////////////////////////////////////////
    //    Initialization  初始化和设置数据库配置
    //////////////////////////////////////////////////////////////////////////////
    void init(DbConfig dbConfig);

    void init(String host, Integer port, String username, String password, String dbName);

    void init(String host, String username, String password, String dbName);

    DbConfig getConfig();

    void setConfig(DbConfig config);

    //////////////////////////////////////////////////////////////////////////////
    //    Get Connection 获取数据库连接
    //////////////////////////////////////////////////////////////////////////////
    Connection getConnection() throws SQLException;

    Connection getConnection(String url, String username, String password) throws SQLException;

    //////////////////////////////////////////////////////////////////////////////
    //    Get Datasource  获取数据源
    //////////////////////////////////////////////////////////////////////////////
    /**
     * 获取数据源
     * @return      数据源对象
     */
    DataSource getDataSource();

    /**
     * 根据指定url获取数据源
     * @param url       数据库连接url
     * @return          数据源对象
     */
    DataSource getDataSource(String url);

    //////////////////////////////////////////////////////////////////////////////
    //    execute sql  执行sql
    //////////////////////////////////////////////////////////////////////////////

    ResultSet executeQuery(String queryStr) throws SQLException;

    boolean execute(String sqlStr) throws SQLException;

    int executeUpdate(String updateSqlStr) throws SQLException;

    //////////////////////////////////////////////////////////////////////////////
    //    get Table columns and data types  获取表字段-字段类型映射
    //////////////////////////////////////////////////////////////////////////////
    /**
     * 获取数据库表的字段-字段类型映射
     * @param tableName     数据库表名称
     * @return              字段-字段类型Map映射
     * @throws SQLException     SQL异常
     */
    Map<String, String> getTableColumns(String tableName) throws SQLException;

    Map<String, String> getTableColumns(Connection conn, String tableName) throws SQLException;

    Map<String, String> getTableColumns(String tableSchema, String tableName) throws SQLException;

    Map<String, String> getTableColumns(Connection conn, String tableSchema, String tableName) throws SQLException;

    //////////////////////////////////////////////////////////////////////////////
    //    static methods  静态方法
    //    1.创建HikariConfig实例 -- 根据数据连接url、用户名称和密码创建HikariConfig实例
    //    2.获取数据源实例 -- 根据数据连接url、用户名称和密码获取数据源实例
    //    3.获取数据库连接 -- 根据数据库配置实例 获取数据库连接
    //    4.获取字段-字段类型映射 -- 根据数据库连接、查询SQL语句提供器 获取字段-字段类型映射
    //    5.将结果集一条记录设置到指定java对象实例中（参数为结果集、java对象实例，实例字段列表）
    //    6.将结果集一条记录设置到指定java对象实例中（需要结果集、java对象实例）
    //    7.将结果集结果列表转换为java对象列表（参数为结果集、java对象类）
    //    4.
    //////////////////////////////////////////////////////////////////////////////

    static HikariConfig createHikariConfig(String url, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setJdbcUrl(url);
        return config;
    }

    static DataSource getDataSource(String url, String username, String password) {
        return new HikariDataSource(createHikariConfig(url, username, password));
    }

    /**
     * 根据指定数据库配置，获取数据库连接的静态方法
     * @param config        数据库配置实例
     * @return
     * @throws SQLException SQL异常
     */
    static Connection getConnection(DbConfig config) throws SQLException{
        try {
            Class.forName(config.getDbType().driverClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    }

    /**
     * 执行查询sql语句
     * @param conn
     * @param queryStr
     * @return
     * @throws SQLException
     */
    static ResultSet executeQuery(Connection conn, String queryStr) throws SQLException {
        return conn.createStatement().executeQuery(queryStr);
    }

    /**
     * 执行指定sql语句
     * @param conn
     * @param sqlStr
     * @return
     * @throws SQLException
     */
    static boolean execute(Connection conn, String sqlStr) throws SQLException {
        return conn.createStatement().execute(sqlStr);
    }

    /**
     * 执行指定更新语句
     * @param conn
     * @param updateSqlStr
     * @return
     * @throws SQLException
     */
    static int executeUpdate(Connection conn, String updateSqlStr) throws SQLException {
        return conn.createStatement().executeUpdate(updateSqlStr);
    }

    /**
     * 获取数据库表字段-字段类型映射的静态方法
     * @param conn              数据库连接对象
     * @param querySqlSupplier  查询数据库表字段信息的sql供应函数
     * @return                  字段-字段类型映射
     * @throws SQLException     SQL异常
     */
    static Map<String, String> getTableColumns(Connection conn, Supplier<String> querySqlSupplier) throws SQLException{
        Map<String, String> columnsMap = new LinkedHashMap<>();
        String queryStr = querySqlSupplier.get();
        try(ResultSet rs = conn.createStatement().executeQuery(queryStr)){
            while(rs.next()){
                columnsMap.put(rs.getString(1), rs.getString(2));
            }
        }catch (SQLException e){
            throw e;
        }
        return columnsMap;
    }

    static <T> void setRsDataToObject(ResultSet rs, T instance, List<String> fields) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int counter = 1, columnCount = metaData.getColumnCount();
        while (counter <= columnCount) {
            String columnName = CoreUtil.STRING.toCamelCase(metaData.getColumnName(counter).toLowerCase());
            String columnType = metaData.getColumnTypeName(counter);
            if (fields.contains(columnName)) {
                Object value;
                if (columnType.equalsIgnoreCase("datetime")
                        || columnType.equalsIgnoreCase("timestamp")) {
                    value = rs.getTimestamp(counter).toLocalDateTime();
                } else {
                    value = rs.getObject(counter);
                }
                setFieldValue(instance, columnName, value);
            }
            counter++;
        }
    }

    /**
     * 将结果集
     * @param rs
     * @param instance
     * @return
     * @param <T>
     * @throws SQLException
     */
    static <T> T setRsDataToObject(ResultSet rs, T instance) throws SQLException {
        if (rs == null || instance == null) {
            return null;
        }
        if(rs.next()){
            setRsDataToObject(rs, instance, getDeclaredFields(instance));
        }
        return instance;
    }

    static <T> List<T> resultToList(ResultSet rs, Class<? extends  T> clazz) throws SQLException {
        if (rs == null || clazz == null) {
            return null;
        }
        T instance = newInstance(clazz);
        if(instance == null){
            return null;
        }
        List<String> fields = getDeclaredFields(instance);
        int columnCount = rs.getMetaData().getColumnCount();
        List<T> resultList = new LinkedList<>();
        while(rs.next()){
            instance = newInstance(clazz);
            resultList.add(instance);
            //设值
            setRsDataToObject(rs, instance, fields);
        }
        return resultList;
    }


}

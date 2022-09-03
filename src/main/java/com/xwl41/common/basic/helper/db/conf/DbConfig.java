package com.xwl41.common.basic.helper.db.conf;

import com.xwl41.common.basic.constant.DBType;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class DbConfig {

    public static final String jdbcPrefix = "jdbc";
    public static final String colon = ":";
    protected String host;
    protected Integer port;
    protected String username;
    protected String password;
    protected String url;
    protected String urlTemplate;

    protected String urlSuffix;
    protected DBType dbType;
    //实际连接时使用的数据库
    protected String dbName;
    protected Charset charsets = StandardCharsets.UTF_8;

    public String getHost() {
        return host;
    }

    public DbConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public DbConfig setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DbConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DbConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUrl() {
        url = buildUrl(true);
        return url;
    }

    public DbConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUrlTemplate() {
        urlTemplate = buildUrl(false);
        return urlTemplate;
    }

    public DbConfig setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
        return this;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public DbConfig setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
        return this;
    }

    public DBType getDbType() {
        return dbType;
    }

    public DbConfig setDbType(DBType dbType) {
        this.dbType = dbType;
        return this;
    }

    public String getDbName() {
        return dbName;
    }

    public DbConfig setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public Charset getCharsets() {
        return charsets;
    }

    public DbConfig setCharsets(Charset charsets) {
        this.charsets = charsets;
        return this;
    }

    protected abstract String buildUrl(boolean withDbName);


}

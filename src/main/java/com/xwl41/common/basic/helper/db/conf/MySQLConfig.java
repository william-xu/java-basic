package com.xwl41.common.basic.helper.db.conf;

import com.xwl41.common.basic.constant.DBType;

public class MySQLConfig extends DbConfig {
    private Boolean allowMultiQueries = true;
    private Boolean useSSL = false;
    private Boolean useUnicode = true;
    private Boolean allowPublicKeyRetrieval = true;
    private String serverTimezone = "GMT%2B8";

    public MySQLConfig() {
        setDbType(DBType.MySQL);
    }

    public MySQLConfig(String host, Integer port, String username, String password, String dbName) {
        this(host, username, password, dbName);
        setPort(port == null ? 3306 : port);
    }

    public MySQLConfig(String host, String username, String password, String dbName) {
        this();
        this.host = host;
        this.username = username;
        this.password = password;
        this.dbName = dbName;
        this.port = this.port == null ? 3306 : this.port;
    }


    public Boolean getAllowMultiQueries() {
        return allowMultiQueries;
    }

    public MySQLConfig setAllowMultiQueries(Boolean allowMultiQueries) {
        this.allowMultiQueries = allowMultiQueries;
        return this;
    }

    public Boolean getUseSSL() {
        return useSSL;
    }

    public MySQLConfig setUseSSL(Boolean useSSL) {
        this.useSSL = useSSL;
        return this;
    }

    public Boolean getUseUnicode() {
        return useUnicode;
    }

    public MySQLConfig setUseUnicode(Boolean useUnicode) {
        this.useUnicode = useUnicode;
        return this;
    }

    public Boolean getAllowPublicKeyRetrieval() {
        return allowPublicKeyRetrieval;
    }

    public MySQLConfig setAllowPublicKeyRetrieval(Boolean allowPublicKeyRetrieval) {
        this.allowPublicKeyRetrieval = allowPublicKeyRetrieval;
        return this;
    }

    public String getServerTimezone() {
        return serverTimezone;
    }

    public MySQLConfig setServerTimezone(String serverTimezone) {
        this.serverTimezone = serverTimezone;
        return this;
    }

    @Override
    protected String buildUrl(boolean withDbName) {
        return jdbcPrefix + colon + dbType.code() + colon + "//" + host + colon + port +
                "/" + (withDbName ? dbName : "{0}") + "?" + (useSSL ? "" : "useSSL=false") +
                (useUnicode ? "&useUnicode=true" : "") + "&serverTimezone=" + serverTimezone +
                "&characterEncoding=" + charsets.name();
    }
}

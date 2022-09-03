package com.xwl41.common.basic.helper.db.conf;

import com.xwl41.common.basic.constant.DBType;

public class H2Config extends DbConfig {

    public H2Config() {
        setDbType(DBType.H2);
    }

    public H2Config(String host, Integer port, String username, String password, String dbName) {
        this(host, username, password, dbName);
        this.port = port;
    }

    public H2Config(String host, String username, String password, String dbName) {
        this();
        this.host = host;
        this.username = username;
        this.password = password;
        this.dbName = dbName;
    }

    private String connectionMode = "mem";

    @Override
    protected String buildUrl(boolean withDbName) {
        return jdbcPrefix + colon + dbType.code() + colon + connectionMode +
                colon + (withDbName ? dbName : "{0}");
    }

    public String getConnectionMode() {
        return connectionMode;
    }

    public void setConnectionMode(String connectionMode) {
        this.connectionMode = connectionMode;
    }
}

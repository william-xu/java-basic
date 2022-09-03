package com.xwl41.common.basic.constant;

import static com.xwl41.common.basic.constant.DbConstants.*;

public enum DBType {
        MySQL(DB_TYPE_MYSQL, DRIVER_CLASS_NAME_MYSQL),
        Oracle(DB_TYPE_ORACLE, DRIVER_CLASS_NAME_ORACLE),
        DB2(DB_TYPE_DB2, DRIVER_CLASS_NAME_DB2),
        H2(DB_TYPE_H2  ,DRIVER_CLASS_NAME_H2),
        MSSQLServer(DB_TYPE_MSSQLSERVER, DRIVER_CLASS_NAME_MSSQLSERVER),
        Sybase(DB_TYPE_SYBASE, DRIVER_CLASS_NAME_SYBASE),
        Informix(DB_TYPE_INFORMIX, DRIVER_CLASS_NAME_INFORMIX),
        PostgreSQL(DB_TYPE_POSTGRESQL, DRIVER_CLASS_NAME_POSTGRESQL);


        public String driverClassName() {
            return driverClassName;
        }

        public String code() {
            return code;
        }

        private final String code;
        private final String driverClassName;

        DBType(String code, String driverClassName) {
            this.code = code;
            this.driverClassName = driverClassName;
        }
    }
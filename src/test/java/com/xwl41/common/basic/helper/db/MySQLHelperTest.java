package com.xwl41.common.basic.helper.db;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MySQLHelperTest {

    @Test
    public void testGetTableColumn() {
        DbHelper helper = MySQLHelper.newInstance();
        helper.init("localhost", "xwl", "password", "demo");
        assertThrows(SQLException.class, () -> helper.getTableColumns("demo", "user_info"));
    }
}

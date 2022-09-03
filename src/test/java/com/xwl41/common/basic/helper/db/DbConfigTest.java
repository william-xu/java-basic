package com.xwl41.common.basic.helper.db;

import com.xwl41.common.basic.helper.db.conf.MySQLConfig;
import org.junit.jupiter.api.Test;

public class DbConfigTest {

    @Test
    public void testMySQLDbConfig(){
        MySQLConfig dbConfig = new MySQLConfig("106.52.182.246","xwl","xwl@Dev.3306","demo");
        System.out.println(dbConfig.getUrl());
    }

}

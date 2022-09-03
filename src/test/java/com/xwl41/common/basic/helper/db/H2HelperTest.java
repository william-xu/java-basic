package com.xwl41.common.basic.helper.db;

import com.xwl41.common.basic.util.CoreUtil;
import lombok.Data;
import lombok.ToString;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class H2HelperTest {
    static final String createUserInfoTableSql = "CREATE TABLE user_info " +
            "( " +
            "    user_id     bigint   auto_increment    NOT NULL COMMENT '用户ID', " +
            "    username    varchar(50)  UNIQUE  NOT NULL COMMENT '用户姓名', " +
            "    num_col    decimal(10,4)  DEFAULT NULL COMMENT '小数', " +
            "    email       varchar(50)  UNIQUE   NOt NULL COMMENT '邮箱地址', " +
            "    password    varchar(255) NOT NULL DEFAULT '' COMMENT '用户密码', " +
            "    create_by   varchar(50)    NOT NULL COMMENT '创建人员user_id', " +
            "    create_time datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "    PRIMARY KEY (user_id) " +
            ");";

    @Data
    @ToString
    static class UserInfo{
        Long userId;
        String username;
        String email;
        String password;
        BigDecimal numCol;
        String createBy;
        LocalDateTime createTime;
    }

    @Test
    @Order(1)
    public void testGetTableColumn() throws SQLException {
        DbHelper helper = H2Helper.newInstance();
        helper.init("","sa","","demo1");
        helper.execute(createUserInfoTableSql);
        System.out.println(helper.getTableColumns("user_info"));
    }

    @Test
    public void testSetValue() throws SQLException {
        DbHelper helper = H2Helper.newInstance();
        helper.init("","sa","","demo2");
        helper.execute(createUserInfoTableSql);
        helper.execute(genUserInfoInsertSql(1L,"xwl","xwl@test.com"));
        ResultSet rs = helper.executeQuery("select * from user_info");
        UserInfo u = new UserInfo();
        DbHelper.setRsDataToObject(rs, u);
        System.out.println(u);
    }

    @Test
    public void testResultToList() throws SQLException {
        DbHelper helper = H2Helper.newInstance();
        helper.init("","sa","","demo3");
        helper.execute(createUserInfoTableSql);
        helper.execute(genUserInfoInsertSql(2L,"leon","leon@test.com"));
        helper.execute(genUserInfoInsertSql(3L,"william","will@test.com"));
        ResultSet rs = helper.executeQuery("select * from user_info");
        List<UserInfo> userInfoList = DbHelper.resultToList(rs, UserInfo.class);
        System.out.println(userInfoList);
    }

    private static String genUserInfoInsertSql(Long id, String userName, String email){
      return "INSERT INTO user_info(user_id, username, email, num_col, password, create_by, create_time)  " +
                "VALUES (" + id + ", '" + userName +"', '" + email + "'," +
              " 99.9999,'$2a$10$hF6lFRAaLXlqZQlKmxn9/utSRKNL8RWV0EaUSFfScjwwzzgDgE3fO', 1, '2022-01-09 15:33:10');";
    }



}

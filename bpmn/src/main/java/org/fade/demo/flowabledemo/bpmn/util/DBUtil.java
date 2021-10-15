package org.fade.demo.flowabledemo.bpmn.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.setting.dialect.Props;

/**
 * @author fade
 * @date 2021/10/13
 */
public class DBUtil {

    private static final Props PROPS = new Props("db.properties");

    public static String getJavaJdbcUrl() {
//        String jdbcUrl = PROPS.getProperty("ui-url");
        String jdbcUrl = PROPS.getProperty("java-url");
        Assert.notBlank(jdbcUrl, "jdbcUrl is illegal");
        return jdbcUrl;
    }

    public static String getUiJdbcUrl() {
        String jdbcUrl = PROPS.getProperty("ui-url");
        Assert.notBlank(jdbcUrl, "jdbcUrl is illegal");
        return jdbcUrl;
    }

    public static String getMemoryJdbcUrl() {
        String jdbcUrl = PROPS.getProperty("memory-url");
        Assert.notBlank(jdbcUrl, "jdbcUrl is illegal");
        return jdbcUrl;
    }

    public static String getUsername() {
        String username = PROPS.getProperty("username");
        Assert.notBlank(username, "username is illegal");
        return username;
    }

    public static String getPassword() {
        String password = PROPS.getProperty("password");
        Assert.notNull(password, "password can not be null");
        return password;
    }

}

package org.fade.demo.flowabledemo.bpmn.constructs.task.usertask;

import java.util.Arrays;
import java.util.List;

/**
 * @author fade
 * @date 2021/10/24
 */
public class FakeLdapService {

    private static final List<String> RESOURCES = Arrays.asList("kermit", "gonzo", "fozzie");

    public String findManagerForEmployee(String employee) {
        if (RESOURCES.contains(employee)) {
            return RESOURCES.get(RESOURCES.indexOf(employee));
        }
        throw new RuntimeException("用户不存在");
    }

    public List<String> findAllSales() {
        return RESOURCES;
    }

}

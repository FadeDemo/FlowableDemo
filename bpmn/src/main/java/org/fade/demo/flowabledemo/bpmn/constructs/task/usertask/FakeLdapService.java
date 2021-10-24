package org.fade.demo.flowabledemo.bpmn.constructs.task.usertask;

import java.util.Arrays;
import java.util.List;

/**
 * @author fade
 * @date 2021/10/24
 */
public class FakeLdapService {

    public String findManagerForEmployee(String employee) {
        return "Kermit The Frog";
    }

    public List<String> findAllSales() {
        return Arrays.asList("kermit", "gonzo", "fozzie");
    }

}

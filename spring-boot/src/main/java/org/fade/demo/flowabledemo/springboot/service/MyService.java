package org.fade.demo.flowabledemo.springboot.service;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fade
 * @date 2021/10/07
 */
@Service
public class MyService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Transactional(rollbackFor = Throwable.class)
    public void startProcess() {
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

}

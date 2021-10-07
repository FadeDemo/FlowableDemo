package org.fade.demo.flowabledemo.springboot;

import com.alibaba.druid.pool.DruidDataSource;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @author fade
 * @date 2021/10/07
 */
@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(final RepositoryService repositoryService,
                                  final RuntimeService runtimeService,
                                  final TaskService taskService,
                                  DataSource dataSource) {
        return strings -> {
            System.out.println("Number of process definitions : "
                    + repositoryService.createProcessDefinitionQuery().count());
            System.out.println("Number of tasks : " + taskService.createTaskQuery().count());
            runtimeService.startProcessInstanceByKey("oneTaskProcess");
            System.out.println("Number of tasks after process start: "
                    + taskService.createTaskQuery().count());
            System.out.println("Is dataSource instance of DruidDataSource: " + (dataSource instanceof DruidDataSource));
        };
    }

}

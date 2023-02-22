package com.zwx.learn;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author zcq
 * @Date 2022/8/18 9:19
 **/
@SpringBootTest
public class ActDemandStudyTest {
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;

    /**
     * 初始化act表
     */
    @Test
    public void init() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        defaultProcessEngine.close();
    }



    /**
     * 部署流程图
     */
    @Test
    public void testDeploy() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment = defaultProcessEngine.getRepositoryService()
                .createDeployment()
                .name("需求管理")
                .category("需求类别")
                .addClasspathResource("demand.bpmn")
                .deploy();
        System.out.println("部署Id:"+deployment.getId()+"部署name:"+deployment.getName());
    }


    /**
     * 启动流程
     */
    @Test
    public void startDemandFlow() {
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("submitterName","zcq");
        variables.put("poList", Arrays.asList("zxl","zwx"));
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("需求管理", "2", variables);
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("zcq").list();
        for (Task task : tasks) {
            taskService.complete(task.getId());
        }
        System.out.println("流程实例ID:"+processInstance.getId()+"流程实例名称："+processInstance.getName());
    }


    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
      /*  List<Task> tasks = taskService.createTaskQuery().taskAssignee("12212212121").list();
        HashMap<String, Object> variables = new HashMap<>();
       variables.put("outcome", 1);
       List<String> list = new ArrayList<>();
        list.add("122121");

        list.add("12212212121");

        variables.put("groupleave", list);
//        variables.put("pmleave", "jiangyue");
          variables.put("agree",0);
//        variables.put("day",4);
//        variables.put("managerleave","zhangbo");
        for (Task task : tasks) {
            taskService.complete(task.getId(), variables);
        }*/
        taskService.complete("2515");
    }






}

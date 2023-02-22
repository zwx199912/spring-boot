package com.zwx.learn;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author zcq
 * @Date 2022/8/18 9:19
 **/
@SpringBootTest
public class ActAessenmtStudyTest {
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private ProcessEngine processEngine;


    /**
     * 部署绩效流程图
     */
    @Test
    public void testDeploy() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment = defaultProcessEngine.getRepositoryService()
                .createDeployment()
                .name("绩效考核")
                .addClasspathResource("okHaveParam/achievement.bpmn")
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



    @Test
    public void getActUsers() {
        String processInstanceId = "5001";
        List<HistoricTaskInstance> hpis = historyService//与历史数据（历史表）相关的Service
                .createHistoricTaskInstanceQuery()//创建历史任务实例查询
                .processInstanceId(processInstanceId)
                .taskName("需求提交")//
                .list();

        for (HistoricTaskInstance hpi : hpis) {

            System.out.println(hpi.getId()+"    "+hpi.getProcessDefinitionId()+"    "+hpi.getStartTime()+"    "+hpi.getEndTime()+"     "+hpi.getDurationInMillis());
        }

    }






}

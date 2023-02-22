package com.zwx.learn.activit;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.testng.annotations.Test;

import java.util.*;

public class Achievement {
    private ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
    //部署流程
    @Test
    public void deployProcess() {
        RepositoryService repositoryService = engine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()   // 创建部署
                .addClasspathResource("achievement.bpmn") // 加载资源文件
                .name("绩效考核") //流程名称
                .deploy();

        System.out.println("流程部署ID:"+deployment.getId());
        System.out.println("流程部署Name:"+deployment.getName());



    }
    //启动流程
    @Test
    public void startProcess(){
        RuntimeService runtimeService = engine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>();
        //绩效考核组长人选
        variables.put("personnelList", Arrays.asList("江月"));
        variables.put("curPersonnelName", "江月");

        variables.put("assigneeList", Arrays.asList("朱孝龙"));
        variables.put("masterList", Arrays.asList("聂超"));
        variables.put("assigneeOneList", Arrays.asList("朱孝龙"));

        variables.put("personnelMatterList", Arrays.asList("江月"));

        variables.put("minister", "樊高峰");
        variables.put("director", "张博");
        variables.put("personnelMatterOneList", Arrays.asList("江月"));

        variables.put("assigneeTwoList",  Arrays.asList("朱孝龙"));
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("绩效考核",variables);
        System.out.println("流程启动成功:" + processInstance.getId() + "   " + processInstance.getProcessDefinitionId() + "  "
                + processInstance.getProcessInstanceId());
    }
    //待办任务
    @Test
    public void findTask(){
        TaskService taskService = engine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("江月").list();
        if(null!=taskList&&taskList.size()>0){
            for (Task task : taskList) {
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务创建时间:"+task.getCreateTime());
                System.out.println("任务委派人:"+task.getAssignee());
                System.out.println("流程实例ID:"+task.getProcessInstanceId());
            }
        }
    }
    //办理任务
    @Test
    public void completeTask(){
        TaskService taskService = engine.getTaskService();

        Map<String, Object> variables = new HashMap<>();
//        variables.put("vmreject", "0");
//        variables.put("rejectUserList", Arrays.asList("樊高峰"));
        taskService.complete("77509",variables);
        System.out.println("任务完成");
    }
}

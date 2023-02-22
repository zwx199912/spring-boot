package com.zwx.learn;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@SpringBootTest
public class ActLeaveStudyTest {
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private HttpServletResponse response;

    /**
     * 初始化act表
     */
    @Test
    public void init() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        defaultProcessEngine.close();
    }


    /**
     * 绩效部署流程图
     */
    @Test
    public void testDeploy() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment = defaultProcessEngine.getRepositoryService()
                .createDeployment()
                .name("绩效考核")
                .addClasspathResource("lastest/achievement.bpmn")
                .deploy();
        System.out.println("部署Id:"+deployment.getId()+"部署name:"+deployment.getName());
    }


    /**
     * 补卡部署流程图
     */
    @Test
    public void testDeploy12() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment = defaultProcessEngine.getRepositoryService()
                .createDeployment()
                .name("补卡流程")
                .addClasspathResource("lastest/remedyCard.bpmn")
                .deploy();
        System.out.println("部署Id:"+deployment.getId()+"部署name:"+deployment.getName());
    }

     /**
     * 请假部署流程图
     */
    @Test
    public void testDeploy1() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment = defaultProcessEngine.getRepositoryService()
                .createDeployment()
                .name("请假管理")
                .addClasspathResource("lineUpdate/leave.bpmn")
                .deploy();
        System.out.println("部署Id:"+deployment.getId()+"部署name:"+deployment.getName());
    }


    /**
     * 需求部署流程图
     */
    @Test
    public void testDeploy3() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment = defaultProcessEngine.getRepositoryService()
                .createDeployment()
                .name("需求管理")
                .addClasspathResource("useFul/demand.bpmn")
                .deploy();
        System.out.println("部署Id:"+deployment.getId()+"部署name:"+deployment.getName());
    }

    /**
     * 启动流程
     */
    @Test
    public void startFlow() {
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("userleave", "cqzhu");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("请假管理", "1", variables);
        System.out.println("流程实例ID:"+processInstance.getId()+"流程实例名称："+processInstance.getName());
    }


    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("12212212121").list();
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
        }
    }


    @Test
    public void xml() throws IOException {
  /*      InputStream inputStream = formService.getProcessDefineResource(processDefineId, 1);

        byte[] bytes = new byte[1024];
        response.setContentType("text/xml");

        try {
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId("需求管理:1:4").singleResult();
        String resourceName = processDefinition == null?"":processDefinition.getResourceName();
        try(InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName)){
            byte[] data = IOUtils.toByteArray(resourceAsStream);
            System.out.println(new String(data, StandardCharsets.UTF_8));
        }
    }






}

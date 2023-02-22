package com.zwx.learn;

import com.zwx.learn.test.SenDMessageToMq;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
@Slf4j
public class TestController {


    @Value("${demand.time.SuspendAdvent}")
    private List<String> suspendAdvent;

    private static final Logger LOGGER = LoggerFactory.getLogger(SenDMessageToMq.class);

    @Value("${mq.topic.test}")
    private String leaveTopic;

    @Autowired
    private RocketMQTemplate rocketMqTemplate;

    @Test
    public void sendToMQ() {
        try {
            String mq  = leaveTopic;
            rocketMqTemplate.sendOneWay(mq,"1212");
            LOGGER.info("发送消息给MQ成功");
        } catch (Exception e) {
            LOGGER.error("发送邮件失败", e);
        }
    }
















    @Test
    public void thread() throws InterruptedException {
        if(suspendAdvent.size()>0){
            log.info(" 开始定时任务");
            /**
             * 顾名思义，这种类型线程池线程数量是固定的。
             * 如果线程数量设置为n，则任何时刻该线程池最多只有n个线程处于运行状态。
             * 当线程池中处于饱和运行状态时，再往线程池中提交的任务会被放到执行队列中。
             * 如果线程池处于不饱和状态，线程池也会一直存在，
             * 直到ExecuteService 的shutdown方法被调用，线程池才会被清除。
             */
            int i = suspendAdvent.size();
            ExecutorService executorService = Executors.newFixedThreadPool(i);
            //线程计数器,就是线程的个数
            CountDownLatch countDownLatch = new CountDownLatch(3);
            List<String> list =  new ArrayList<>();
            try {
                //一个线程池,三个线程,执行三个任务,只有当这三个任务执行完以后,主线程才会执行
                for (int j = 0; j < i ; j++) {
                    executorService.execute(new Runnable() {  //不是异步的没有返回值
                        @Override
                        public void run() {
                           // countDownLatch.countDown();
                            for (int i = 0; i <=100 ; i++) {
                                list.add(String.valueOf(i));
                            }
                            countDownLatch.countDown();
                        }
                    });
                }
                //主线程阻塞,如果线程池之间也是需要前后结果的话,应该也是阻塞的
                countDownLatch.await(300,TimeUnit.HOURS); //只有当countDown变为0的时候,主线程才不是阻塞的
            } finally {
                executorService.shutdown();
                executorService.awaitTermination(1, TimeUnit.SECONDS);
            }
            System.out.println(list.size());
        }
    }
    private static  ThreadPoolExecutor executor = new  ThreadPoolExecutor(20,25,100L,
            TimeUnit.SECONDS,new LinkedBlockingQueue<>(),new ThreadPoolExecutor.CallerRunsPolicy());

    @Test
    public void CallAbleThread() throws InterruptedException {
        if (suspendAdvent.size() > 0) {
            log.info(" 开始定时任务");
            List<Future> futures = new ArrayList<>();
            int i = suspendAdvent.size();
            List<String> list =  new ArrayList<>();
            for (int j = 0; j < i; j++) {
                Future future = executor.submit(() -> {
                    try {
                        for (int x = 0; x <=100 ; x++) {
                            list.add(String.valueOf(x));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                futures.add(future);
            }
            //等待线程结束
            for (Future future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(list.size());
        }
    }
}

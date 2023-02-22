package com.zwx.learn.learnHttp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 **/
@RestController
@Slf4j
public class TestController {

    @Value("${server.port}")
    private String port;

    @Value("${demand.time.SuspendAdvent}")
    private List<String> suspendAdvent;

    @RequestMapping("/test")
    public String test() {

        System.out.println(suspendAdvent.size());
        return suspendAdvent.get(0) +"-------------------------------"+suspendAdvent.get(1)+ port;
}
    /**
     * 可缓存无界线程池测试
     * 当线程池中的线程空闲时间超过60s则会自动回收该线程，核心线程数为0
     * 当任务超过线程池的线程数则创建新线程。线程池的大小上限为Integer.MAX_VALUE，
     * 可看做是无限大。
     */

    /**
     * 创建固定线程数量的线程池测试
     * 创建一个固定大小的线程池，该方法可指定线程池的固定大小，对于超出的线程会在LinkedBlockingQueue队列中等待
     * 核心线程数可以指定，线程空闲时间为0
     */

    /*@Test
    public void futureTest() {

        // 注意使用 ExecutorService 而非 Executor
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Future<String> future = executorService.submit(() -> {
            //此处为Callable接口实现
            // 业务代码
            System.out.println("执行异步任务...");
            return "返回执行结果";
        });

        try {
            //判断异步线程是否执行完成
            if (future.isDone())
                System.out.println("线程执行完成1");

            //获取异步线程的执行结果,若未完成将阻塞
            String result = future.get();
            System.out.println(result);

            if (future.isDone())
                System.out.println("线程执行完成2");
        } catch (Exception e) {
        }
    }

    public static class Tasker implements Callable<String> {

        @Override
        public String call() throws Exception {
            return "hello";
        }
    }

    @Test
    public void test11112211() throws InterruptedException, ExecutionException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        List<Future<String>> futures = new ArrayList<Future<String>>();
        Future<String> res = null;
        for(int i=0;i<5;i++){
            res = threadPool.submit(new Tasker());
            futures.add(res);
        }
        threadPool.shutdown();
        for(Future<String> future:futures){
            System.out.println(future.get());
        }

    }

    @Test
    public void thread() throws InterruptedException {
        if(suspendAdvent.size()>0){
            log.info(" 开始定时任务");
            *//**
             * 顾名思义，这种类型线程池线程数量是固定的。
             * 如果线程数量设置为n，则任何时刻该线程池最多只有n个线程处于运行状态。
             * 当线程池中处于饱和运行状态时，再往线程池中提交的任务会被放到执行队列中。
             * 如果线程池处于不饱和状态，线程池也会一直存在，
             * 直到ExecuteService 的shutdown方法被调用，线程池才会被清除。
             *//*
            int i = suspendAdvent.size();
            ExecutorService executorService = Executors.newFixedThreadPool(i);
            //线程计数器,就是线程的个数
            CountDownLatch countDownLatch = new CountDownLatch(3);
            List<String>  list =  new ArrayList<>();
            try {
                //一个线程池,三个线程,执行三个任务,只有当这三个任务执行完以后,主线程才会执行
                for (int j = 0; j < i ; j++) {
                    executorService.execute(new Runnable() {  //不是异步的没有返回值
                        @Override
                        public void run() {
                            countDownLatch.countDown();
                            for (int i = 0; i <=100 ; i++) {
                                list.add(String.valueOf(i));
                            }

                        }
                    });
                }
                //主线程阻塞,如果线程池之间也是需要前后结果的话,应该也是阻塞的
                countDownLatch.await(); //只有当countDown变为0的时候,主线程才不是阻塞的
            } finally {
                executorService.shutdown();
            }

            System.out.println(list.size());

        }




   *//*     ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(3); //线程计数器,就是线程的个数
        try {
            executorService.execute(new Runnable() {  //不是异步的没有返回值
                @Override
                public void run() {
                    countDownLatch.countDown();
                    for (int i = 0; i <=100000 ; i++) {
                        System.out.println("111");
                    }

                }
            });
            //一个线程池,三个线程,执行三个任务,只有当这三个任务执行完以后,主线程才会执行
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(0);
                        countDownLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i <=100000 ; i++) {
                        System.out.println("222");
                    }
                }
            });
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(0);
                        countDownLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i <=100000 ; i++) {
                        System.out.println("33333");
                    }
                }
            });
            //主线程阻塞,如果线程池之间也是需要前后结果的话,应该也是阻塞的
            countDownLatch.await(); //只有当countDown变为0的时候,主线程才不是阻塞的
        } finally {
            executorService.shutdown();
        }*//*
    }

*/
    public static void main(String[] args) {
        Set<String> s1= new HashSet<>();
        s1.add("1");
        s1.add("2");
        s1.add("3");
        s1.add("4");
        Set<String> s2= new HashSet<>();
        s2.add("5");
        s2.add("6");
        s2.add("7");
        s2.add("8");


        s1.addAll(s2);
        System.out.println("211212");
    }
}


package com.zwx.learn.learnHttp;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1.线程池执行不同的业务逻辑.
 * 2.获取每个任务执行的结果
 * 3.最后的结果会用到之前的结果
 */
public class SearchUserListDTO {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(3); //线程计数器,就是线程的个数
        try {
            executorService.execute(new Runnable() {  //不是异步的没有返回值
                @Override
                public void run() {
                    try {
                        Thread.sleep(0);
                        countDownLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
        }
    }
}
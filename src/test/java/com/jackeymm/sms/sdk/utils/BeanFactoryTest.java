package com.jackeymm.sms.sdk.utils;

import com.jackeymm.sms.sdk.config.BeanFactory;

public class BeanFactoryTest {

    public static void main(String[] args){
        getEhcacheIsSynchronized();
        getHttpUtilIsSynchronized();
    }

    public static void getEhcacheIsSynchronized(){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 100; i++){
                    System.out.println("t1 : " + i + BeanFactory.getEhcacheInstance().toString());
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 100; i++){
                    System.out.println("t2 : " + i + BeanFactory.getEhcacheInstance().toString());
                }
            }
        });

        t1.start();
        t2.start();

    }

    public static void getHttpUtilIsSynchronized(){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 100; i++){
                    System.out.println("~t2 : " + i + BeanFactory.getHttpUtilInstance().toString());
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 100; i++){
                    System.out.println("~t2 : " + i + BeanFactory.getHttpUtilInstance().toString());
                }
            }
        });

        t1.start();
        t2.start();

    }
}

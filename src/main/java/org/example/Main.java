package org.example;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        //Благодаря CountDownLatch в классе DataBase, ни один поток не запустится, пока не отработает этот
        DataBase.CreateTable();

        new Thread(new Runnable() {
            public void run() {
                DataBase.GetAverageMag();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                DataBase.GraphAverageMag();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                DataBase.GetMaxDeep();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                DataBase.GraphMaxDeep();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                DataBase.GraphAverageEartshake();
            }
        }).start();
    }
}





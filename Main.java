package org.example;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) throws SQLException {
        //Благодаря CountDownLatch в классе DataBase, ни один поток не запустится, пока не отработает этот
        DataBase.CreateTable();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        executorService.execute(DataBase::GraphAverageEartshake);
        executorService.execute(DataBase::GetAverageMag);
        executorService.execute(DataBase::GraphAverageMag);
        executorService.execute(DataBase::GetMaxDeep);
        executorService.execute(()-> DataBase.GraphMaxDeep());
    }
}




package org.example;


public class Main {
    public static void main(String[] args) {
        String path = "/home/luigi/Descargas/2019-Oct.csv";
        String delimiter = ",";

        Long initialTime = System.currentTimeMillis();
        LoadDataBase loadDataBase = new LoadDataBase();
        loadDataBase.loadData(path,delimiter);
        Long endTime = System.currentTimeMillis();
        System.out.println((endTime - initialTime)/1000);


    }
}
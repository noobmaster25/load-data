package org.example;


import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        LoadDataBase loadDataBase = new LoadDataBase();

        String path = "/home/luigi/Descargas/data.csv";
        String delimiter = ",";
        Long initialTime = System.currentTimeMillis();


        loadDataBase.loadData(path,delimiter);
        Long endTime = System.currentTimeMillis();

        System.out.println("Segundos: "+(endTime - initialTime)/1000);


    }
}
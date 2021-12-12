package com.bomb;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Dummy {

    private String file;
    private BufferedWriter writer;


    public Dummy(String path) throws IOException {
        this.file = path;
    }
    //1,395,864,371.2
    public void write() throws IOException {
        Path path = Paths.get(file);
        writer = new BufferedWriter(new FileWriter(file));
        int log = 0;
//        int desiredSize = 1395864371;
        int desiredSize = 1395864371;
        long size = 0l;
        while (size < desiredSize){
            writer.write(0);
            size = Files.size(path);
            writer.flush();
            System.out.println("Size is: " + size);
        }
        System.out.println("DONE");
        writer.close();
    }









}

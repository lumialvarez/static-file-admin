package com.lmalvarez.fileadmin.utils;

import com.lmalvarez.tools.system.OperativeSystem;
import com.lmalvarez.fileadmin.model.FileItem;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
    public static final String MAIN_PATH = OperativeSystem.isWindows()? "C:\\res" : "/var/www/res";
    public static final String MAIN_EXTERNAL_URL = "https://resources.lmalvarez.com";

    public static String findParentPath(String path){
        File folder = new File(path);
        return folder.getParentFile().getPath();
    }

    public static boolean isRootPath(String path){
        File folder = new File(path);
        File rootFolder = new File(MAIN_PATH);
        return folder.getPath().equals(rootFolder.getPath());
    }

    public static List<FileItem> readFolder(String path){
        List<FileItem> list = new ArrayList<>();
        File folder = new File(path);
        folder.getPath();


        for (final File fileEntry : folder.listFiles()) {
            FileItem fileItem = new FileItem();
            if (fileEntry.isDirectory()) {
                fileItem.setFolder(true);
                fileItem.setFileName(fileEntry.getName());
                fileItem.setRelativePath(findRelativeURL(fileEntry));
                try {
                    fileItem.setSize(folderSize(fileEntry));
                } catch (Exception ex){
                    fileItem.setSize(-1);
                }
                fileItem.setAbsolutePath(fileEntry.getPath());
            } else {
                fileItem.setFolder(false);
                fileItem.setFileName(fileEntry.getName());
                fileItem.setRelativePath(findRelativeURL(fileEntry));
                fileItem.setUrl(findAbsoluteWebURL(fileEntry.getPath()));
                fileItem.setSize(FileUtils.sizeOf(fileEntry));
                fileItem.setAbsolutePath(fileEntry.getPath());
            }
            list.add(fileItem);
        }
        return list;
    }

    public static String findRelativeURL(String filePath){
        return findRelativeURL(new File(filePath));
    }

    private static String findRelativeURL(File file){
        File rootFolder = new File(MAIN_PATH);
        return file.getPath().replace(rootFolder.getPath(), "");
    }

    private static String findAbsoluteWebURL(String path){
        File rootFolder = new File(MAIN_PATH);
        String ruta = path.replace(rootFolder.getPath(), "");
        ruta = ruta.replace("\\","/");
        String webUrl = MAIN_EXTERNAL_URL + ruta;
        return webUrl;
    }

    public static boolean deleteFile(String path){
        File folder = new File(path);
        return folder.delete();
    }

    public static boolean createFolder(String path){
        File folder = new File(path);
        return folder.mkdir();
    }

    public static boolean createFile(String path, String fileName, InputStream in) throws IOException {
        OutputStream out = new FileOutputStream(new File(path + FileSystems.getDefault().getSeparator() + fileName));
        int read = 0;
        byte[] bytes = new byte[1024];
        while ((read = in.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        in.close();
        out.flush();
        out.close();
        return true;
    }

    public static long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }

    public static void setPermission(String filePath) throws IOException{
        File file = new File(filePath);
        setPermission(file);
    }

    public static void setPermission(File file) throws IOException{
        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);

        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);

        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);

        Files.setPosixFilePermissions(file.toPath(), perms);
    }
}

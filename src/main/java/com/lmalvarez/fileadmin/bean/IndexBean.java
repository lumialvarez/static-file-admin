/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmalvarez.fileadmin.bean;

import com.lmalvarez.fileadmin.utils.Utils;
import com.lmalvarez.fileadmin.model.FileItem;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author lmalvarez
 */
@Named
@SessionScoped
@Getter
@Setter
public class IndexBean implements Serializable {
    private String currentPath;
    private List<FileItem> list;
    private FileItem currentFile;

    private String nameNewFolder;

    @PostConstruct
    public void init() {
        currentPath = Utils.MAIN_PATH;
        list = Utils.readFolder(currentPath);
    }

    public boolean isCarpetaPrincipal(){
        return Utils.isRootPath(currentPath);
    }

    public void cargarRuta(String path){
        currentPath = path;
        list = Utils.readFolder(currentPath);
    }

    public void cargarRutaAtras(){
        if(!Utils.isRootPath(currentPath)) {
            currentPath = Utils.findParentPath(currentPath);
            list = Utils.readFolder(currentPath);
        }
    }

    public void eliminarArchivo(FileItem fileItem){
        if(fileItem.isFolder() && fileItem.getSize() > 0){
            showWarn("La carpeta contiene archivos, no puede ser eliminada");
        } else {
            boolean eliminado = Utils.deleteFile(fileItem.getAbsolutePath());
            if (eliminado) {
                showInfo("'" + fileItem.getFileName() + "' Eliminado correctamente");
            } else {
                showWarn("'" + fileItem.getFileName() + "' No eliminado");
            }
            list = Utils.readFolder(currentPath);
        }
    }

    public void nuevaCarpeta(String folder){
        String filePath = currentPath + FileSystems.getDefault().getSeparator() + folder;
        boolean created = Utils.createFolder(filePath);
        if(created){
            nameNewFolder = "";
            showInfo("Carpeta '" + folder + "' creada correctamente");
            try {
                Utils.setPermission(filePath);
            } catch (Exception e) {
                showWarn("Carpeta creada pero con error de permisos: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showWarn("Carpeta no creada");
        }
        cargarRuta(currentPath);
    }

    public void seleccionarArchivo(FileItem file){
        currentFile = file;
    }

    public void handleFilesUpload(FileUploadEvent event) {
        UploadedFile file  = event.getFile();
        try {
            Utils.createFile(currentPath, file.getFileName(), file.getInputStream());
            list = Utils.readFolder(currentPath);
            showInfo(file.getFileName() + " is uploaded.");
            try {
                Utils.setPermission(currentPath + FileSystems.getDefault().getSeparator() + file.getFileName());
            } catch (Exception e) {
                showWarn("Archivo subido pero con error de permisos: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error " + e.getMessage());
            showError("Error " + e.getMessage());
        }
    }

    public StreamedContent getDownloadValue(String filePath) throws Exception {
        StreamedContent download;
        File file = new File(filePath);
        FileInputStream input = new FileInputStream(file);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

        download = DefaultStreamedContent.builder().name(file.getName())
                .contentType(externalContext.getMimeType(file.getName()))
                .stream(() -> input)
                .build();
        return download;
    }

    public void showInfo(String message) {
        addMessage(FacesMessage.SEVERITY_INFO, "Info Message", message);
    }

    public void showWarn(String message) {
        addMessage(FacesMessage.SEVERITY_WARN, "Warn Message", message);
    }

    public void showError(String message) {
        addMessage(FacesMessage.SEVERITY_ERROR, "Error Message", message);
    }


    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public String getNameNewFolder() {
        return nameNewFolder;
    }

    public void setNameNewFolder(String nameNewFolder) {
        this.nameNewFolder = nameNewFolder;
    }
}

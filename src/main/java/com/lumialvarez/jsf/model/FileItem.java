package com.lumialvarez.jsf.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileItem {
    private String fileName;
    private boolean folder;
    private long size;
    private String relativePath;
    private String url;
    private String absolutePath;
}

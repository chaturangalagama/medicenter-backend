package com.ilt.cms.api.entity.file;

import lombok.*;

import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FileMetadataEntity {
    
    private String fileId;
    private String name;
    private String fileName;
    private String uploader;
    private String clinicId;
    private String type;
    private long size;
    private String description;

    public String decodedFileIdValue() {
        return new String(Base64.getDecoder().decode(fileId.getBytes()));
    }

}

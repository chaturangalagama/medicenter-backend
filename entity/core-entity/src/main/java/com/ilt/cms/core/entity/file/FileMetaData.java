package com.ilt.cms.core.entity.file;

import com.lippo.commons.util.CommonUtils;

import java.util.Base64;

public class FileMetaData {

    private String fileId;
    private String name;
    private String fileName;
    private String uploader;
    private String clinicId;
    private String type;
    private long size;
    private String description;


    public FileMetaData() {
    }

    /**
     * Stores the given value
     *  @param fileId      : Converts the ID to Base64 Encoded value
     * @param name
     * @param fileName
     * @param uploader
     * @param clinicId
     * @param type
     * @param size
     * @param description
     */
    public FileMetaData(String fileId, String name, String fileName, String uploader, String clinicId, String type, long size,
                        String description) {
        this.fileId = Base64.getEncoder().encodeToString(fileId.getBytes());
        this.name = name;
        this.fileName = fileName;
        this.uploader = uploader;
        this.clinicId = clinicId;
        this.type = type;
        this.size = size;
        this.description = description;
    }

    /**
     * Stores the given value
     * @param name
     * @param fileName
     * @param uploader
     * @param clinicId
     * @param description
     */
    public FileMetaData(String name, String fileName, String uploader, String clinicId, String description) {
        this.name = name;
        this.fileName = fileName;
        this.uploader = uploader;
        this.clinicId = clinicId;
        this.description = description;
    }

    public boolean areParmetersValid(){
        return CommonUtils.isStringValid(name,fileName,uploader,clinicId);
    }


    public String getFileName() {
        return fileName;
    }

    public String getUploader() {
        return uploader;
    }

    public String getClinicId() {
        return clinicId;
    }

    public String getType() {
        return type;
    }

    public long getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public String decodedFileIdValue() {
        return new String(Base64.getDecoder().decode(fileId.getBytes()));
    }

    public String getFileId() {
        return fileId;
    }

    public String getName() {
        return name;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FileMetaData{" +
                "fileId='" + fileId + '\'' +
                ", name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", uploader='" + uploader + '\'' +
                ", clinicId='" + clinicId + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", description='" + description + '\'' +
                '}';
    }
}

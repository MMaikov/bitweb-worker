package com.example.bitwebworker;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "upload_metadata")
public class UploadMetadata {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(length = 256, nullable = false)
    private String filename;

    @Column(length = 20, nullable = false)
    private String status;

    @Column
    private String result;

    public UploadMetadata() {
    }

    public UploadMetadata(UUID id, String filename, String status) {
        this.id = id;
        this.filename = filename;
        this.status = status;
        this.result = null;
    }

    public UploadMetadata(UUID id, String filename, String status, String result) {
        this.id = id;
        this.filename = filename;
        this.status = status;
        this.result = result;
    }

    public UUID getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

package com.example.bitwebworker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UploadMetadataRepository extends JpaRepository<UploadMetadata, UUID> {
}

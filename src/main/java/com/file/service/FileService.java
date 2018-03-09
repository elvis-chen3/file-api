package com.file.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.file.model.Metadata;

public interface FileService {

	void init();

	void store(MultipartFile file) throws IOException;

	Resource loadAsResource(Metadata metadata);

	void deleteAll();

	Metadata[] loadAllMetadata();

	Metadata loadMetadataById(Long id);

	Path load(String id, String filename);

	Metadata getMetadataByFilename(String filename);

	List<Metadata> getTotalNumOfFiles();

}

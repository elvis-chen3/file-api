package com.file.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.file.config.StorageProperties;
import com.file.exceptions.StorageException;
import com.file.exceptions.StorageFileNotFoundException;
import com.file.model.Metadata;
import com.file.repository.MetadataRepository;
import com.file.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	private final Path rootLocation;
	
	private MetadataRepository metadataDAO;

	@Autowired
	public FileServiceImpl(StorageProperties properties, MetadataRepository metadataDAO) {
		this.rootLocation = Paths.get(properties.getLocation());
		this.metadataDAO = metadataDAO;
	}

	@Override
	public void store(MultipartFile file) throws IOException {
		String contentType = file.getContentType();
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		long size = file.getSize();

		Metadata metadata = new Metadata();
		metadata.setContentType(contentType);
		metadata.setFileName(filename);
		metadata.setFileSize(String.valueOf(size));
		
		Date today = Calendar.getInstance().getTime();
		metadata.setDate(today);
		
		System.out.println("****** FILENAME ****** - " + filename);
		System.out.println("****** FILESIZE ****** - " + size);
		System.out.println("****** CONTENTTYPE ****** - " + contentType);


		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory " + filename);
			}

			Metadata save = metadataDAO.save(metadata);
			long id = save.getId();
			
			Path directory = this.rootLocation.resolve(String.valueOf(id));
			if (!Files.exists(directory)) {
				Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
				FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions
						.asFileAttribute(permissions);

				try {
					Files.createDirectory(this.rootLocation.resolve(String.valueOf(id)), fileAttributes);
				} catch (IOException e) {
					System.err.println(e);
				}
			}

			Files.copy(file.getInputStream(), directory.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

	@Override
	public Metadata[] loadAllMetadata() {

		Iterable<Metadata> iterable = metadataDAO.findAll();
		Iterator<Metadata> itr = iterable.iterator();
		List<Metadata> list = new ArrayList<>();

		while (itr.hasNext()) {
			Metadata metadata = itr.next();
			list.add(metadata);
		}

		return list.toArray(new Metadata[list.size()]);

	}

	@Override
	public Metadata loadMetadataById(Long id) {
		return metadataDAO.findOne(id);
	}

	@Override
	public Path load(String id, String filename) {
		return rootLocation.resolve(id).resolve(filename);
	}
	
	@Override
	public Resource loadAsResource(Metadata metadata) {
		String filename = metadata.getFileName();
		String id = String.valueOf(metadata.getId());
		try {
			Path file = load(id, filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public Metadata getMetadataByFilename(String filename) {
		return metadataDAO.findByFileName(filename);
	}
	
	@Override
	public List<Metadata> getTotalNumOfFiles() {
		return metadataDAO.getListOfMetadataFromLastHour();
	}

}

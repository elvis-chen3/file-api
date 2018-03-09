package com.file.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.file.model.Metadata;
import com.file.service.FileService;

@RestController
public class FileApiController {

	private final FileService fileService;

	@Autowired
	public FileApiController(FileService fileService) {
		this.fileService = fileService;
	}

	@RequestMapping("/hello")
	public String hello() {
		return "hello";
	}

	/**
	 * A representation of an uploaded file received in a multipart request. The
	 * file contents are either stored in memory or temporarily on disk. In
	 * either case, the user is responsible for copying file contents to a
	 * session-level or persistent store as and if desired. The temporary
	 * storage will be cleared at the end of request processing.
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/file", method = RequestMethod.POST)
	public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
			throws IOException {

		fileService.store(file);

		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/";
	}

	@RequestMapping(value = "/files", method = RequestMethod.GET)
	public Metadata[] listAllFileInfo() {
		return fileService.loadAllMetadata();
	}

	@RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
	public Metadata listFileInfoById(@PathVariable("id") long id) {
		return fileService.loadMetadataById(id);
	}

	@RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
	public String loadFileContentById(@PathVariable("id") long id) throws IOException {

		Metadata metadata = fileService.loadMetadataById(id);

		Resource resource = fileService.loadAsResource(metadata);

		InputStream inputStream = resource.getInputStream();

	    StringBuilder textBuilder = new StringBuilder();
	    try (Reader reader = new BufferedReader(new InputStreamReader
	      (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
	        int c = 0;
	        while ((c = reader.read()) != -1) {
	            textBuilder.append((char) c);
	        }
	    }
		
		return textBuilder.toString();
	}
	
	@RequestMapping(value = "/fileId", method = RequestMethod.GET)
	public Metadata getFileIdByFilename(@RequestParam("filename") String filename) {
		Metadata metadata = fileService.getMetadataByFilename(filename);
		return metadata;
	}
	
	@RequestMapping(value = "/poll", method = RequestMethod.GET)
	public Metadata[] getFileNum() {
		List<Metadata> metadata = fileService.getTotalNumOfFiles();
		return metadata.toArray(new Metadata[metadata.size()]);
	}

}

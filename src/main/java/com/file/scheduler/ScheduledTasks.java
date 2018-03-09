package com.file.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.file.model.Metadata;
import com.file.service.FileService;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	private FileService fileService;

	@Autowired
	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	@Scheduled(cron = "0 0 * * * *")
	public void pollForFileByLastHour() {
		log.info("The time is now {}", dateFormat.format(new Date()));
		
		List<Metadata> metadataList = fileService.getTotalNumOfFiles();
		Metadata[] metadataArr = metadataList.toArray(new Metadata[metadataList.size()]);
		
		for (Metadata metadata : metadataArr) {
			log.info("Metadata - {}", metadata.toString());
		}
	}
}

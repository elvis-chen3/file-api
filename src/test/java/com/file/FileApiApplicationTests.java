package com.file;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.file.config.StorageProperties;
import com.file.exceptions.StorageException;
import com.file.model.Metadata;
import com.file.repository.MetadataRepository;
import com.file.service.impl.FileServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FileApiApplicationTests {

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testMockGetSingleFile() {

		MetadataRepository mockRepository = mock(MetadataRepository.class);

		Metadata metadata = new Metadata();
		metadata.setFileName("test");
		when(mockRepository.findOne(Long.valueOf(1))).thenReturn(metadata);

		FileServiceImpl fileService = new FileServiceImpl(new StorageProperties(), mockRepository);
		assertEquals("test", fileService.loadMetadataById(Long.valueOf(1)).getFileName());
	}

	@Test
	public void testMockGetAllFiles() {

		MetadataRepository mockRepository = mock(MetadataRepository.class);
		ArrayList<Metadata> list = new ArrayList<Metadata>();
		list.add(new Metadata());
		list.add(new Metadata());

		when(mockRepository.findAll()).thenReturn(list);

		Iterator<Metadata> itr = mockRepository.findAll().iterator();
		int size = 0;
		while (itr.hasNext()) {
			itr.next();
			size++;
		}

		assertEquals(2, size);

	}

	@Test(expected = StorageException.class)
	public void testMockStoreFileWithIllegalName() throws IOException {

		Path path = Paths.get("src/test/resource/te..st.txt");
		String name = "te..st.txt";
		String originalFileName = "te..st.txt";
		String contentType = "text/plain";

		byte[] content = null;
		try {
			content = Files.readAllBytes(path);
		} catch (final IOException e) {
		}

		MultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content);

		MetadataRepository mockRepository = mock(MetadataRepository.class);
		FileServiceImpl fileService = new FileServiceImpl(new StorageProperties(), mockRepository);
		fileService.store(multipartFile);
	}
}

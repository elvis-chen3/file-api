package com.file.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.file.model.Metadata;

@Repository
public interface MetadataRepository extends CrudRepository<Metadata, Long> {

	public Metadata findByFileName(String fileName);

	@Query("SELECT m FROM Metadata m WHERE DATEDIFF('SECOND',  CURRENT_TIMESTAMP(), DATEADD('HOUR', 1, m.created)) > 0")
	public List<Metadata> getListOfMetadataFromLastHour();
}

package ru.otusproject.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otusproject.model.GcSendXmlTb;
import ru.otusproject.model.TlgSendGcTb;

@Repository
public interface GcSendXmlTbRepository extends CrudRepository<GcSendXmlTb, Long> {
}

package ru.otusproject.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otusproject.model.TlgSendGcTb;

import java.util.List;

@Repository
public interface TlgSendGcTbRepository extends CrudRepository<TlgSendGcTb, Long> {

    List<TlgSendGcTb> findTlgSendGcTbByTlgStatusIsAndRowStateIs(Integer tlgStatus, Integer rowState);
}

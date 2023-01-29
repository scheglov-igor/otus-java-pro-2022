package ru.otusproject.service.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otusproject.model.TlgSendGcTb;
import ru.otusproject.repo.TlgSendGcTbRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TlgSendGcTbServiceImpl implements TlgSendGcTbService {
    private static final Logger log = LoggerFactory.getLogger(TlgSendGcTbServiceImpl.class);

    private final TlgSendGcTbRepository tlgSendGcTbRepository;

    public TlgSendGcTbServiceImpl(TlgSendGcTbRepository tlgSendGcTbRepository) {
        this.tlgSendGcTbRepository = tlgSendGcTbRepository;
    }

    /*
    @Override
    @Transactional
    public Client saveClient(Client client) {
        var savedClient = clientRepository.save(client);
        log.info("saved client: {}", savedClient);
        return savedClient;
    }
    */

    public Optional<TlgSendGcTb> getTlgSendGcTb(long id) {
        var tlgSendGcTbclientOptional = tlgSendGcTbRepository.findById(id);
        log.info("TlgSendGcTb: {}", tlgSendGcTbclientOptional);
        return tlgSendGcTbclientOptional;
    }

//    @Transactional
    public TlgSendGcTb saveTlgSendGcTb(TlgSendGcTb tlgSendGcTb) {
        TlgSendGcTb savedTlgSendGcTb = tlgSendGcTbRepository.save(tlgSendGcTb);
        return savedTlgSendGcTb;
    }

    public List<TlgSendGcTb> findAllNeedSend(){
        var tlgSendGcTbList = tlgSendGcTbRepository.findTlgSendGcTbByTlgStatusIsAndRowStateIs(0, 0);
        log.info("tlgSendGcTbList:{}", tlgSendGcTbList);
        return tlgSendGcTbList;
    }


/*
    @Override
    public List<Client> findAll() {
        var clientList = new ArrayList<Client>();
        clientRepository.findAll().forEach(clientList::add);
        log.info("clientList:{}", clientList);
        return clientList;
    }

     */
}

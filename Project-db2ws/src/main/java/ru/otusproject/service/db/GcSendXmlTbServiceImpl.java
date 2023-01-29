package ru.otusproject.service.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otusproject.model.GcSendXmlTb;
import ru.otusproject.repo.GcSendXmlTbRepository;

import java.util.Optional;

@Service
public class GcSendXmlTbServiceImpl implements GcSendXmlTbService {
    private static final Logger log = LoggerFactory.getLogger(GcSendXmlTbServiceImpl.class);

    private final GcSendXmlTbRepository gcSendXmlTbRepository;

    public GcSendXmlTbServiceImpl(GcSendXmlTbRepository gcSendXmlTbRepository) {
        this.gcSendXmlTbRepository = gcSendXmlTbRepository;
    }


    @Override
    @Transactional
    public GcSendXmlTb save(GcSendXmlTb gcSendXmlTb) {
        var savedClient = gcSendXmlTbRepository.save(gcSendXmlTb);
        log.info("saved gcSendXmlTb: {}", savedClient);
        return savedClient;
    }


    public Optional<GcSendXmlTb> getGcSendXmlTb(long id) {
        var gcSendXmlTbclientOptional = gcSendXmlTbRepository.findById(id);
        log.info("GcSendXmlTb: {}", gcSendXmlTbclientOptional);
        return gcSendXmlTbclientOptional;
    }
}

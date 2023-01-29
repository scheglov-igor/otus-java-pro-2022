package ru.otusproject.service.db;




import ru.otusproject.model.GcSendXmlTb;

import java.util.Optional;

public interface GcSendXmlTbService {

    public GcSendXmlTb save(GcSendXmlTb gcSendXmlTb);
    public Optional<GcSendXmlTb> getGcSendXmlTb(long id);

}

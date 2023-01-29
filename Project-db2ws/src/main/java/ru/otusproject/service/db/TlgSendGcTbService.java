package ru.otusproject.service.db;



import ru.otusproject.model.TlgSendGcTb;

import java.util.List;
import java.util.Optional;

public interface TlgSendGcTbService {

    //TlgSendGcTb saveClient(Client client);

    Optional<TlgSendGcTb> getTlgSendGcTb(long id);

    TlgSendGcTb saveTlgSendGcTb(TlgSendGcTb tlgSendGcTb);

    List<TlgSendGcTb> findAllNeedSend();
}

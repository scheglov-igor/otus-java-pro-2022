package ru.otusproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otusproject.enums.FunctionType;
import ru.otusproject.enums.ServiceType;
import ru.otusproject.exception.ManualStopException;
import ru.otusproject.model.TlgSendGcTb;
import ru.otusproject.helper.Result;
import ru.otusproject.service.db.TlgSendGcTbService;

import java.util.List;

/**
 * основной сервис для связи всех частей в единый механизм
 *
 */
@Service
public class SenderServiceImpl implements SenderService{

    private static final Logger logger = LoggerFactory.getLogger(SenderServiceImpl.class);
    private final WsService wsService;
    private final TlgSendGcTbService tlgSendGcTbService;

    public SenderServiceImpl(WsServiceImpl wsService, TlgSendGcTbService tlgSendGcTbService) {
        this.wsService = wsService;
        this.tlgSendGcTbService = tlgSendGcTbService;
    }

    /**
     * запускаем поиск еще не отправленных
     */
    @Override
    public void start() {
        List<TlgSendGcTb> tlgSendGcTbList = tlgSendGcTbService.findAllNeedSend();
        for (TlgSendGcTb tlgSendGcTb: tlgSendGcTbList) {
            send(tlgSendGcTb);
        }
    }

    @Override
    public void send(Long id) {
        logger.info("start sending tlg with id={}", id);

        var tlgSendGcTbOptional = tlgSendGcTbService.getTlgSendGcTb(id);
        var tlgSendGcTb = tlgSendGcTbOptional.orElseThrow(
//TODO do alert
                () -> new RuntimeException("no row in DB! id=" + id)
        );
        logger.info("tlg: {}", tlgSendGcTb);
        send(tlgSendGcTb);
    }


//TODO тут бы их всех в очередь выстроить...

    public void send (TlgSendGcTb tlgSendGcTb) {
        try {
            logger.info("start checking tlg...");

            if(tlgSendGcTb == null) {
                throw new ManualStopException("STOP by TlgSendGcTb is null");
            }
            if (tlgSendGcTb.getRowState() == null || !tlgSendGcTb.getRowState().equals(0)) {
                throw new ManualStopException("STOP by ROW_STATE: " + tlgSendGcTb.getRowState());
            }
            if (tlgSendGcTb.getTlgStatus() != null && !tlgSendGcTb.getTlgStatus().equals(0)) {
                throw new ManualStopException("STOP by TLG_STATUS: " + tlgSendGcTb.getTlgStatus());
            }
            if (tlgSendGcTb.getTlgWebservice() == null) {
                throw new ManualStopException("STOP by Webservice is null");
            }

            ServiceType serviceType = ServiceType.AgreedRoutes;
            System.out.println("!!! tlgSendGcTb.getTlgWebservice() = " + tlgSendGcTb.getTlgWebservice());
            FunctionType functionType = FunctionType.valueOf(tlgSendGcTb.getTlgWebservice());
            System.out.println("!!! functionType = " + functionType);

            if(functionType == null) {
                throw new ManualStopException("STOP by no FunctionType: " + tlgSendGcTb.getTlgWebservice());
            }

            logger.info("start sending ws");


            Result result = wsService.sendWrappedXml(serviceType, functionType, tlgSendGcTb.getId(), tlgSendGcTb.getTlgText());

            logger.debug("tlgSendGcTb = {}", tlgSendGcTb);
            logger.debug("result = {}", result.toStringFull());



            tlgSendGcTb.setGcSasId(result.getId());

            //TODO как в result.toString??
            StringBuilder sbErrorMessage = new StringBuilder();

            if (result.getResultErrorMessage() != null) {
                sbErrorMessage.append(result.toString());
            }

            if (result.getErrorCode() != null) {
                sbErrorMessage.append(result.toString());

                Integer errorId = result.getErrorCode();
                tlgSendGcTb.setGcSasErrorcode(errorId);
            }

            if (result.getFault() != null && result.getFault().trim() != "") {
                sbErrorMessage.append(result.getFault());
            }

            if (sbErrorMessage.length() > 0) {
                tlgSendGcTb.setGcSasErrorMessage(sbErrorMessage.toString());
            }

            tlgSendGcTb.setTlgStatus(result.getStatus());
            tlgSendGcTb.setTlgResult(result.createTlgResult());

            System.out.println("tlgSendGcTb = " + tlgSendGcTb);
            //update
            tlgSendGcTbService.saveTlgSendGcTb(tlgSendGcTb);

        } catch (ManualStopException e) {
            //TODO do alert
            logger.info("manual stop: {}", e.getMessage());
        }

    }
}

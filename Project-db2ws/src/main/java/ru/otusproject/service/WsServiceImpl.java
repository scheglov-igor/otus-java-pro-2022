package ru.otusproject.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ru.otusproject.enums.FunctionType;
import ru.otusproject.enums.ServiceType;
import ru.otusproject.exception.ManualStopException;
import ru.otusproject.model.*;
import ru.otusproject.helper.Result;
import ru.otusproject.service.db.GcSendXmlTbService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class WsServiceImpl implements WsService{

    private static final Logger logger = LoggerFactory.getLogger(WsServiceImpl.class);
    private final GcSendXmlTbService gcSendXmlTbService;

    WsServiceImpl(GcSendXmlTbService gcSendXmlTbService) {
        this.gcSendXmlTbService = gcSendXmlTbService;
    }
    @Override
    public Result sendWrappedXml(ServiceType gcService, FunctionType gcFunction, Long tlgId, String tlgTextXml) {

            logger.debug("start sending wrapped xml...");

            GcSendXmlTb gcSendXmlTb = new GcSendXmlTb(gcService, gcFunction, tlgId);

            SOAPMessage soapRequest = null;
            try {
                soapRequest = createSOAPRequest(gcFunction, tlgTextXml);
            } catch (SOAPException | IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();

                Result result = new Result(e.getMessage(), null, null, null, gcService, gcFunction);
                gcSendXmlTb.setResult(result.toString());
                gcSendXmlTb.setStatus(4);
                gcSendXmlTbService.save(gcSendXmlTb);
                return result;
            }


        SOAPMessage soapResponse = null;
            try {
                soapResponse = callSoapResponse(soapRequest, gcService, false);
            } catch (ManualStopException | SOAPException | InterruptedException e) {
                e.printStackTrace();

                Result result = new Result(e.getMessage(), null, null, null, gcService, gcFunction);
                gcSendXmlTb.setResult(result.toString());
                gcSendXmlTb.setStatus(4);
                gcSendXmlTbService.save(gcSendXmlTb);
                return result;
            }

            // try understand response
            try {
                logger.debug("### handle soapResponse...");
                SOAPBody responseSoapBody = soapResponse.getSOAPBody();
                gcSendXmlTb.setRequestXml(convertSOAPMessageToString(soapRequest));
                gcSendXmlTb.setResponseXml(convertSOAPMessageToString(soapResponse));

                Result result = new Result(responseSoapBody, gcService, gcFunction);
                System.out.println("result.toStringFull() = " + result.toStringFull());
                gcSendXmlTb.setResult(result.toString());
                gcSendXmlTb.setStatus(result.getStatus());
                gcSendXmlTbService.save(gcSendXmlTb);
                return result;
            } catch (SOAPException | IOException e) {
                e.printStackTrace();

                Result result = new Result(e.getMessage(), null, null, null, gcService, gcFunction);
                gcSendXmlTb.setResult(result.toString());

                gcSendXmlTb.setStatus(4);
                gcSendXmlTbService.save(gcSendXmlTb);
                return result;

            }







        // dbService.updateSendAgreedSegments(sendAgreedSegments, resultSAS);

        }

        public SOAPMessage createSOAPRequest(FunctionType gcFunction, String textNode) throws SOAPException, IOException, SAXException, ParserConfigurationException {
            logger.debug("create SOAPRequest {}", gcFunction);

            //TODO
            String namespace = "http://SOME/WSERVICE";
//            String namespace = "http://GKOVD/CBD2";

            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            MimeHeaders headers = soapMessage.getMimeHeaders();
            // TODO
            headers.addHeader("SOAPAction", namespace + "/" + gcFunction);
            // TODO
//TODO              String loginPassword = GcSender.gcLogin + ":" + GcSender.gcPassword;
            String loginPassword = "wsusr:wspwd";
            // headers.addHeader("Authorization", "Basic " + new
            // String(Base64.encode(loginPassword.getBytes())));

            //TODO заменить Base64 на что-то более компактное
            headers.addHeader("Authorization", "Basic " + new String(Base64.encodeBase64(loginPassword.getBytes())));

            SOAPPart soapPart = soapMessage.getSOAPPart();

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            // envelope.addNamespaceDeclaration("cbd2", namespace);
            envelope.addNamespaceDeclaration("", namespace);

       /*     if (GcSender.testGcData) {
                SOAPHeader soapHeader = envelope.getHeader();
                SOAPElement soapHeaderElem = soapHeader.addChildElement("UserClaims", "");
                SOAPElement soapHeaderElem1 = soapHeaderElem.addChildElement("zc_id", "");
                soapHeaderElem1.addTextNode("11");
            }
*/
            // SOAP Body
            SOAPBody soapBody = envelope.getBody();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            Document document = null;

            InputStream is = new ByteArrayInputStream(textNode.getBytes());
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(is);

            soapBody.addDocument(document);

            soapMessage.saveChanges();

            logger.debug("soapRequest = " + convertSOAPMessageToString(soapMessage));

            return soapMessage;

        }


        public SOAPMessage callSoapResponse(SOAPMessage soapRequest, ServiceType gcService, Boolean isZip)
                throws ManualStopException, SOAPException, InterruptedException {

            logger.debug("send request to {} and get SoapResponse", gcService);

            String serviceGcUrl = getServiceGcUrl(gcService);
            SOAPConnection soapConnection = getSoapConnection();
            SOAPMessage soapResponse = null;
            SOAPException se = null;

            int tryCount = 1;
            int waitTime = 10;
            boolean needDownload = true;
            while (needDownload && (tryCount <= 13)) {
                try {

//                    try {
//                        System.out.println("!! Request: " + convertSOAPMessageToString(soapRequest));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }

                    soapResponse = soapConnection.call(soapRequest, serviceGcUrl);

//                    try {
//                        System.out.println("!! Response: " + convertSOAPMessageToString(soapResponse));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }

                    needDownload = false;
                } catch (SOAPException e) {
                    logger.warn(
                            "attempt #" + tryCount + " failed, waiting " + waitTime + " seconds : \n" + e.getMessage());
                    se = e;
                    tryCount++;
                    TimeUnit.SECONDS.sleep(waitTime);
                    waitTime = waitTime * 2;
                }
            }
            if (soapResponse == null) {
                throw new ManualStopException("STOP! No network for 24 hours.", se);
            }

            return soapResponse;
        }

        public String getServiceGcUrl(ServiceType serviceType) {

          //TODO  return GcSender.gcUrl + "/CBD2/1.1" + (GcSender.testGcData ? "/test/" : "/") + serviceType + ".asmx?WSDL";
//            return "http://localhost:8088/" + serviceType + ".asmx?WSDL";
            return "http://localhost:8088/mockAgreedRoutesSoap.asmx?WSDL";


        }

        public static String convertSOAPMessageToString(SOAPMessage soapMessage) throws SOAPException, IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            soapMessage.writeTo(baos);
            return baos.toString();
        }

        public static SOAPMessage convertStringToSOAPMessage(String soapString) throws SOAPException, IOException {
            InputStream is = new ByteArrayInputStream(soapString.getBytes());
            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null, is);
            return soapMessage;
        }

        public SOAPConnection getSoapConnection() throws SOAPException {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            return soapConnection;
        }

    }

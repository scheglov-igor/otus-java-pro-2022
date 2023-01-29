package ru.otusproject.service;

import ru.otusproject.enums.FunctionType;
import ru.otusproject.enums.ServiceType;
import ru.otusproject.helper.Result;

public interface WsService {
    Result sendWrappedXml(ServiceType serviceType, FunctionType functionType, Long id, String tlgText);
}

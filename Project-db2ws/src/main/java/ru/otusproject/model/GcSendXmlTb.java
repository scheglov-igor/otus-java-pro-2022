package ru.otusproject.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import ru.otusproject.enums.FunctionType;
import ru.otusproject.enums.ServiceType;

import java.sql.*;
import java.time.LocalDateTime;

@Table("gc_send_xml_tb")
@Getter
@Setter
public class GcSendXmlTb {

	@Id
	private Long id;
	private Integer rowState;
	private Timestamp dateChange;

	// name of web-service
	private ServiceType gcService;

	// name of function of web-service
	private FunctionType gcFunction;

	// FK tlg_send_gc_tb
	private Long dataId;
	private String requestXml;
	private String responseXml;
	private Integer status;
	private String result;


	public GcSendXmlTb(ServiceType gcService, FunctionType gcFunction, Long dataId) {
		this.gcService = gcService;
		this.gcFunction = gcFunction;
		this.dataId = dataId;
		this.rowState = 0;
		this.dateChange = Timestamp.valueOf(LocalDateTime.now());
	}

	public void setResult(String resultString) {
		if (resultString != null && resultString.length() > 1000) {
            resultString = resultString.substring(0, 999);
        }
		this.result = resultString;
	}

	@Override
	public String toString() {
		return "GcSendXmlTb [id=" + id + ", rowState=" + rowState + ", dateChange=" + dateChange + ", gcService="
				+ gcService + ", gcFunction=" + gcFunction + ", dataId=" + dataId
				+ ", requestXml=" + (requestXml != null && !requestXml.isEmpty())
				+ ", responseXml=" + (responseXml != null && !responseXml.isEmpty())
				+ ", status=" + status +  ", result=" + result +"]";
	}

}

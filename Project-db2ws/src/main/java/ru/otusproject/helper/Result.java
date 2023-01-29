package ru.otusproject.helper;

import java.io.IOException;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.otusproject.enums.FunctionType;
import ru.otusproject.enums.ServiceType;
import ru.otusproject.exception.ActiveRstrareaException;
import ru.otusproject.exception.AgreedSegmentException;

public class Result {

	String fault;

	Integer errorCode;

	Long id;
	Boolean hasResult = false;

	String resultErrorMessage;

	FunctionType functionType;
	ServiceType serviceType;

	public Result(String fault, Integer errorCode, Long id, String resultErrorMessage, ServiceType serviceType,
			FunctionType functionType) {
		super();
		this.fault = fault;
		this.errorCode = errorCode;
		this.id = id;
		this.resultErrorMessage = resultErrorMessage;
		this.serviceType = serviceType;
		this.functionType = functionType;

	}

	public String toStringFull() {
		return "Result [fault=" + fault + ", errorCode=" + errorCode + ", id=" + id + ", resultErrorMessage="
				+ resultErrorMessage + ", functionType=" + functionType + ", serviceType=" + serviceType
				+ ", toString()=" + toString() + "]";
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		if (fault != null && fault.length() > 0) {
			sb.append("FAULT = " + fault);
		}

		if (errorCode != null) {
			if (sb.length() > 0) {
				sb.append("; ");
			}
			sb.append("ERRORCODE = " + errorCode + " (");
			if ((functionType == FunctionType.SendAgreedSegment)
					|| (functionType == FunctionType.DeleteAgreedSegment)) {
				sb.append(AgreedSegmentException.getErrorDescription(errorCode));

			} else if ((functionType == FunctionType.SendActiveRstrArea)
					|| (functionType == FunctionType.DeleteActiveRstrArea)) {
				sb.append(ActiveRstrareaException.getErrorDescription(errorCode));
			}
			// getErrorDescription();
			sb.append(")");
		}

		if (resultErrorMessage != null && resultErrorMessage.length() > 0) {
			if (sb.length() > 0) {
				sb.append("; ");
			}
			sb.append(resultErrorMessage);
		}

		return (sb.length() > 0) ? sb.toString() : null;
	}

	public String createTlgResult() {

		if (id != null) {
			return id.toString();
		}
		return "";

	}

	public Boolean hasException() {
		return (fault != null && fault.length() > 0) ? true : false;
	}

	public Boolean hasError() {
		return (errorCode != null || resultErrorMessage != null) ? true : false;
	}

	public Boolean hasResult() {
		return (id != null || hasResult) ? true : false;
	}

	public Integer getStatus() {

		Status status = new Status();

		if (hasResult()) {
			status.setBite(1, 1);
		}
		if (hasException() || hasError()) {
			status.setBite(2, 1);
		}

		return status.intVal();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result + ((fault == null) ? 0 : fault.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((resultErrorMessage == null) ? 0 : resultErrorMessage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
			return false;
		if (fault == null) {
			if (other.fault != null)
				return false;
		} else if (!fault.equals(other.fault))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (resultErrorMessage == null) {
			if (other.resultErrorMessage != null)
				return false;
		} else if (!resultErrorMessage.equals(other.resultErrorMessage))
			return false;
		return true;
	}

	public Result(SOAPBody soapBody, ServiceType serviceType, FunctionType functionType)
			throws SOAPException, IOException {
		this.serviceType = serviceType;
		this.functionType = functionType;

		StringBuilder faultSB = new StringBuilder();

		// <error code="...">errorValue</error>
		NodeList errorNodes = soapBody.getElementsByTagName("error");
		for (int i = 0; i < errorNodes.getLength(); i++) {
			Node errorNode = errorNodes.item(i);
			if (errorNode != null) {
				faultSB.append("ERROR: ");
				String errorValue = errorNode.getTextContent();
				faultSB.append(errorValue);
				Node codeAttr = errorNode.getAttributes().getNamedItem("code");
				if (codeAttr != null) {
					if (faultSB.length() > 0) {
						faultSB.append(" ");
					}
					faultSB.append("(codeStr=");
					faultSB.append(codeAttr.getNodeValue());
					faultSB.append(") ");
				}
			}
		}

		// <soap:Fault>
		NodeList faultNodes = soapBody.getElementsByTagName("soap:Fault");
		for (int i = 0; i < faultNodes.getLength(); i++) {
			Node faultNode = faultNodes.item(i);
			if (faultNode instanceof Element) {
				Element faultElement = (Element) faultNode;
				if (faultNode != null) {

					NodeList faultCodeNodes = faultElement.getElementsByTagName("faultcode");
					for (int j = 0; j < faultCodeNodes.getLength(); j++) {
						Node faultCodeNode = faultCodeNodes.item(j);
						if (faultCodeNode != null) {
							if (faultSB.length() > 0) {
								faultSB.append(" ");
							}
							faultSB.append("FAULTCODE: ");
							faultSB.append(faultCodeNode.getTextContent());
						}
					}

					NodeList faultStringNodes = faultElement.getElementsByTagName("faultstring");
					for (int j = 0; j < faultStringNodes.getLength(); j++) {
						Node faultStringNode = faultStringNodes.item(j);
						if (faultStringNode != null) {
							if (faultSB.length() > 0) {
								faultSB.append(" ");
							}
							faultSB.append("FAULTSTRING: ");
							faultSB.append(faultStringNode.getTextContent());
						}
					}

					NodeList detailNodes = faultElement.getElementsByTagName("detail");
					for (int j = 0; j < detailNodes.getLength(); j++) {
						Node detailNode = detailNodes.item(j);
						if (detailNode != null && detailNode.getTextContent().length() > 0) {
							faultSB.append("DETAIL: ");
							if (faultSB.length() > 0) {
								faultSB.append(" ");
							}
							faultSB.append(detailNode.getTextContent());
						}
					}

				}
			}
		}

		Integer errorCode = null;
		Long id = null;
		Boolean includeId = true;

		String resultErrorMessage = null;

		// <result errorCode="-1">
		NodeList resultNodes = soapBody.getElementsByTagName("result");
		Node resultNode = resultNodes.item(0);

		if (resultNode != null) {

			hasResult = true;

			NamedNodeMap attributes = resultNode.getAttributes();

			Node errorCodeNode = attributes.getNamedItem("errorCode");
			if (errorCodeNode != null) {
				if (errorCodeNode.getNodeValue().length() > 0) {
					Integer thisErrorCode = Integer.valueOf(errorCodeNode.getNodeValue().trim());

					if (thisErrorCode.equals(0) && (functionType == FunctionType.DeleteAgreedSegment
							|| functionType == FunctionType.DeleteActiveRstrArea)) {
						errorCode = null;
					}

					else {
						errorCode = thisErrorCode;

						if (errorCode.equals(-20020) || errorCode.equals(-20030) || errorCode.equals(-20040)
								|| errorCode.equals(-20070) || errorCode.equals(-20080) || errorCode.equals(-20100)) {
							includeId = false;
						}

					}

				}
			}

			Node idNode = attributes.getNamedItem("id");
			if (idNode != null && includeId) {
				id = Long.valueOf(idNode.getNodeValue());
			}

			if (resultNode instanceof Element) {
				Element resulElement = (Element) resultNode;
				NodeList errorMessageNodes = resulElement.getElementsByTagName("errorMessage");
				for (int j = 0; j < errorMessageNodes.getLength(); j++) {
					Node errorMessageNode = errorMessageNodes.item(j);
					if (errorMessageNode != null && errorMessageNode.getTextContent() != null
							&& errorMessageNode.getTextContent().trim().length() > 0
							&& !errorMessageNode.getTextContent().trim().toLowerCase().contentEquals("null")) {

						resultErrorMessage = errorMessageNode.getTextContent();
					}
				}
			}
		}

		if (faultSB != null && faultSB.length() > 0) {
			this.fault = faultSB.toString();
		}
		this.errorCode = errorCode;
		this.id = id;
		this.resultErrorMessage = resultErrorMessage;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFault() {
		return fault;
	}

	public void setFault(String fault) {
		this.fault = fault;
	}

	public String getResultErrorMessage() {
		return resultErrorMessage;
	}

	public void setResultErrorMessage(String resultErrorMessage) {
		this.resultErrorMessage = resultErrorMessage;
	}

}

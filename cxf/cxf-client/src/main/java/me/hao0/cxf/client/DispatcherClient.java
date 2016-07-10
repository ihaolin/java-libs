package me.hao0.cxf.client;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;
import java.io.IOException;
import java.net.URL;

/**
 * 针对ProvicerServer的Dispatcher Client:
 * 
 * 请求：
  <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Header xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"/>
   <soap:Body>
      <create xmlns="http://service.cxf.haolin.org/">
         <arg0>
            <userename>haolin</userename>
            <password>123456</password>
            <email>576240289@qq.com</email>
            <mobile>18683443875</mobile>
         </arg0>
      </create>
   </soap:Body></soap:Envelope>
   
   响应:
   <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	   <SOAP-ENV:Header xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"/>
	   <soap:Body>
	      <create xmlns="http://service.cxf.haolin.org/">
	         <return>123</return>
	      </create>
	   </soap:Body>
   </soap:Envelope>
 */
public class DispatcherClient {

	public static void main(String[] args) throws SOAPException, IOException {
		String wsdlLocation = "http://localhost:8888/UserServiceDOMProvider?wsdl";

		QName domProvider = new QName("http://providers.cxf.haolin.org/",
				"UserServiceDOMProviderService");
		QName portName = new QName("http://providers.cxf.haolin.org/",
				"UserServiceDOMProviderPort");
		Service service = Service.create(new URL(wsdlLocation), domProvider);

		// 1.创建请求消息
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage soapRequest = factory.createMessage();

		QName userCreateQName = new QName("http://service.cxf.haolin.org/", "create");
		SOAPElement userCreateResponse = soapRequest.getSOAPBody()
				.addChildElement(userCreateQName);
		// 2. 构建User xml对象
		SOAPElement user = userCreateResponse.addChildElement("arg0");
		user.addChildElement("userename").addTextNode("haolin");
		user.addChildElement("password").addTextNode("123456");
		user.addChildElement("email").addTextNode("576240289@qq.com");
		user.addChildElement("mobile").addTextNode("18683443875");
		DOMSource domRequestMsg = new DOMSource(soapRequest.getSOAPPart());

		//以Message模式发送SOAP消息
		Dispatch<DOMSource> domMsg = service.createDispatch(portName,
				DOMSource.class, Mode.MESSAGE); 
		
		DOMSource domResponseMsg = domMsg.invoke(domRequestMsg);
		System.out.println("Client Request as a DOMSource data in MESSAGE Mode");
		soapRequest.writeTo(System.out);
		System.out.println("\n");
		System.out.println("Response from server: " + 
				domResponseMsg.getNode().getLastChild().getTextContent());
	}
}

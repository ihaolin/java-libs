package me.hao0.cxf.providers;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.Provider;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;
import java.io.IOException;

/**
 * 基于DOMSource的服务提供者
 * @author haolin
 * 
 */
@WebServiceProvider()
@ServiceMode(value = Mode.MESSAGE)
public class UserServiceDOMProvider implements Provider<DOMSource> {

	@Override
	public DOMSource invoke(DOMSource request) {
		DOMSource response = new DOMSource();
		try {
			MessageFactory factory = MessageFactory.newInstance();
			// 1. 解析请求
			// 创建构建SOAP消息
			SOAPMessage soapReq = factory.createMessage();
			soapReq.getSOAPPart().setContent(request);
			System.out.println("Incoming Client Request as a DOMSource data in MESSAGE Mode");
			soapReq.writeTo(System.out);
			
			// 获取第一个参数元素, 如create中的user对象
			Node user = soapReq.getSOAPBody().getFirstChild();
			// 获取对象属性
			NodeList userAttrs = user.getChildNodes();
			for (int i=0; i<userAttrs.getLength(); i++){
				Node attr = userAttrs.item(i);
				String attrName = attr.getNodeName();
				String attrValue = attr.getFirstChild().getNodeValue();
				System.out.println(attrName + " = " + attrValue);
			}
			
			// 2. 响应请求
			SOAPMessage resp = factory.createMessage();
			QName createUserQName = new QName("http://org.haolin.cxf.service.cxf.haolin.org/", "create");
			QName returnQName = new QName("http://org.haolin.cxf.service.cxf.haolin.org/", "return");
			SOAPElement createUserResponse = 
					resp.getSOAPBody().addChildElement(createUserQName);
			createUserResponse.addChildElement(returnQName).addTextNode("123");
			response.setNode(resp.getSOAPPart());
		} catch (SOAPException | IOException e) {
			e.printStackTrace();
		}
		return response;
	}
}

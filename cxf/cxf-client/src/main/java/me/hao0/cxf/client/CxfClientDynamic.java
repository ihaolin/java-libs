package me.hao0.cxf.client;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.service.model.*;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * 基于CXF的动态client
 */
public class CxfClientDynamic {
	public static void main(String[] args) {
		JaxWsDynamicClientFactory clientFactory = 
				JaxWsDynamicClientFactory.newInstance();
		Client client = clientFactory.createClient("http://localhost:9999/UserService?wsdl");
		
		// 1. get service
		Endpoint endpoint = client.getEndpoint();
		List<ServiceInfo> serviceInfo = endpoint.getService().getServiceInfos();
		ServiceInfo service0 = serviceInfo.get(0);
		
		// 2. get binding
		QName bindingName = new QName("http://service.cxf.haolin.org/", "UserServiceSoapBinding");
		BindingInfo binding = service0.getBinding(bindingName);
		
		// 3. opreate name
		QName opName = new QName("http://service.cxf.haolin.org/", "create");
		BindingOperationInfo opreation = binding.getOperation(opName);
		BindingMessageInfo inputMessageInfo = null;
		if (!opreation.isUnwrapped()){
			// UserService uses document literal wrapped style
			inputMessageInfo = opreation.getWrappedOperation().getInput();
		} else{
			inputMessageInfo = opreation.getUnwrappedOperation().getInput();
		}
		
		// 4. get input types
		List<MessagePartInfo> inputMessageTypes = inputMessageInfo.getMessageParts();
		if (inputMessageTypes != null && inputMessageTypes.size() > 0){
			// set arg values
			for (MessagePartInfo inputMessageType : inputMessageTypes){
				System.out.println(inputMessageType.getTypeClass());
			}
		}
		
		// 5. invoke
		//client.invoke()
	}
}

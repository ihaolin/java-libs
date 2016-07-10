package me.hao0.cxf.client;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * JaxWs 动态client
 */
public class JaxWsClientDynamic {
	public static void main(String[] args) throws Exception {
		JaxWsDynamicClientFactory clientFactory = 
				JaxWsDynamicClientFactory.newInstance();
		Client client = clientFactory.createClient("http://localhost:9999/UserService?wsdl");
		Object[] resp = client.invoke("list");
		System.out.println(resp);
	}
}

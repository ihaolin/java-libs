package test;

import org.example.receiveclaiminfofromgvs.OutType;
import org.example.receiveclaiminfofromgvs.ReceiveClaimInfoFromGVS;
import org.example.receiveclaiminfofromgvs.ReceiveClaimInfoFromGVS_Service;
import org.example.receiveclaiminfofromgvs.ZSPJHINPUT2;
import org.junit.Test;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取索赔信息测试
 */
public class ReceiveClaimInfoTest {
	
	@Test
	public void testReceiveClaimInfo() throws MalformedURLException{
//		URL wsdlLocation = new URL("http://10.135.17.72:10101/EAI/RoutingProxyService/EAI_SOAP_ServiceRoot?INT_CODE=GVS_test_009");
//		QName serviceName = 
//				new QName("http://www.example.org/ReceiveClaimInfoFromGVS/", 
//						"ReceiveClaimInfoFromGVS");
//		ReceiveClaimInfoFromGVS_Service serviceSrc = 
//				new ReceiveClaimInfoFromGVS_Service(wsdlLocation, serviceName);
		
		// use default
		ReceiveClaimInfoFromGVS_Service serviceSrc = new ReceiveClaimInfoFromGVS_Service();
		
		ReceiveClaimInfoFromGVS service = serviceSrc.getReceiveClaimInfoFromGVSSOAP();
		
		ZSPJHINPUT2 inputParam = new ZSPJHINPUT2();
		List<ZSPJHINPUT2> inputParams = new ArrayList<ZSPJHINPUT2>();
		inputParams.add(inputParam);
		
		OutType result = service.receiveClaimInfoFromGVS(inputParams);
		
		System.out.println("Z_RET: " + result.getZRET());
		System.out.println("EAIFlag: " + result.getEAIFlag());
		System.out.println("EAIMessage: " + result.getEAIMessage());
		System.out.println("EAIDetail: " + result.getEAIDetail());
		
	}
}

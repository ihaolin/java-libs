package test;

import org.example.sendclaiminfotogvs.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送索赔信息测试
 */
public class SendClaimInfoTest {

	@Test
	public void testSendClaimInfoByDate(){
		SendClaimInfoToGVS_Service serviceSrc = new SendClaimInfoToGVS_Service();
		SendClaimInfoToGVS service = serviceSrc.getSendClaimInfoToGVSSOAP();
		
		List<ZSPJHINPUT> dates = new ArrayList<ZSPJHINPUT>();
		ZSPJHINPUT date1 = new ZSPJHINPUT();
		date1.setZDATE("2014-06-26");
		dates.add(date1);
		
		List<ZSPJHOUTPUT> result =
				service.sendClaimInfoToGVS(dates, null);
		
		System.out.println(result.size());
	}
	
	@Test
	public void testSendClaimInfoByMonth(){
		SendClaimInfoToGVS_Service serviceSrc = new SendClaimInfoToGVS_Service();
		SendClaimInfoToGVS service = serviceSrc.getSendClaimInfoToGVSSOAP();
		
		List<ZSPJHINPUTMONTHLY> monthes = new ArrayList<ZSPJHINPUTMONTHLY>();
		ZSPJHINPUTMONTHLY month1 = new ZSPJHINPUTMONTHLY();
		month1.setZDATE("2014-07-13");
		monthes.add(month1);
		List<ZSPJHOUTPUT> result =
				service.sendClaimInfoToGVS(null, monthes);
		
		System.out.println(result.size());
	}
}

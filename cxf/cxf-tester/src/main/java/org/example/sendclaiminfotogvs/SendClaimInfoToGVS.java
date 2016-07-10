
package org.example.sendclaiminfotogvs;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.util.List;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "SendClaimInfoToGVS", targetNamespace = "http://www.example.org/SendClaimInfoToGVS/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SendClaimInfoToGVS {


    /**
     * 
     * @param tINPUT
     * @param tINPUT2
     * @return
     *     returns java.util.List<org.example.sendclaiminfotogvs.ZSPJHOUTPUT>
     */
    @WebMethod(operationName = "SendClaimInfoToGVS", action = "http://www.example.org/SendClaimInfoToGVS/SendClaimInfoToGVS")
    @WebResult(name = "T_OUTPUT", targetNamespace = "")
    @RequestWrapper(localName = "SendClaimInfoToGVS", targetNamespace = "http://www.example.org/SendClaimInfoToGVS/", className = "org.example.sendclaiminfotogvs.SendClaimInfoToGVS_Type")
    @ResponseWrapper(localName = "SendClaimInfoToGVSResponse", targetNamespace = "http://www.example.org/SendClaimInfoToGVS/", className = "org.example.sendclaiminfotogvs.SendClaimInfoToGVSResponse")
    public List<ZSPJHOUTPUT> sendClaimInfoToGVS(
            @WebParam(name = "T_INPUT", targetNamespace = "")
            List<ZSPJHINPUT> tINPUT,
            @WebParam(name = "T_INPUT2", targetNamespace = "")
            List<ZSPJHINPUTMONTHLY> tINPUT2);

}

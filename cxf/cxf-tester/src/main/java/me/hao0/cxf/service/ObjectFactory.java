
package me.hao0.cxf.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the me.hao0.cxf.service package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _LoadResponse_QNAME = new QName("http://service.cxf.learn.haol.org/", "loadResponse");
    private final static QName _User_QNAME = new QName("http://service.cxf.learn.haol.org/", "User");
    private final static QName _Create_QNAME = new QName("http://service.cxf.learn.haol.org/", "create");
    private final static QName _List_QNAME = new QName("http://service.cxf.learn.haol.org/", "list");
    private final static QName _Load_QNAME = new QName("http://service.cxf.learn.haol.org/", "load");
    private final static QName _CreateResponse_QNAME = new QName("http://service.cxf.learn.haol.org/", "createResponse");
    private final static QName _ListResponse_QNAME = new QName("http://service.cxf.learn.haol.org/", "listResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: me.hao0.cxf.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListResponse }
     *
     */
    public ListResponse createListResponse() {
        return new ListResponse();
    }

    /**
     * Create an instance of {@link User }
     *
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link Load }
     *
     */
    public Load createLoad() {
        return new Load();
    }

    /**
     * Create an instance of {@link CreateResponse }
     *
     */
    public CreateResponse createCreateResponse() {
        return new CreateResponse();
    }

    /**
     * Create an instance of {@link Create }
     *
     */
    public Create createCreate() {
        return new Create();
    }

    /**
     * Create an instance of {@link List }
     *
     */
    public List createList() {
        return new List();
    }

    /**
     * Create an instance of {@link LoadResponse }
     *
     */
    public LoadResponse createLoadResponse() {
        return new LoadResponse();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link LoadResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://service.cxf.learn.haol.org/", name = "loadResponse")
    public JAXBElement<LoadResponse> createLoadResponse(LoadResponse value) {
        return new JAXBElement<LoadResponse>(_LoadResponse_QNAME, LoadResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link User }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://service.cxf.learn.haol.org/", name = "User")
    public JAXBElement<User> createUser(User value) {
        return new JAXBElement<User>(_User_QNAME, User.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link Create }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://service.cxf.learn.haol.org/", name = "create")
    public JAXBElement<Create> createCreate(Create value) {
        return new JAXBElement<Create>(_Create_QNAME, Create.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link List }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://service.cxf.learn.haol.org/", name = "list")
    public JAXBElement<List> createList(List value) {
        return new JAXBElement<List>(_List_QNAME, List.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link Load }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://service.cxf.learn.haol.org/", name = "load")
    public JAXBElement<Load> createLoad(Load value) {
        return new JAXBElement<Load>(_Load_QNAME, Load.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link CreateResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://service.cxf.learn.haol.org/", name = "createResponse")
    public JAXBElement<CreateResponse> createCreateResponse(CreateResponse value) {
        return new JAXBElement<CreateResponse>(_CreateResponse_QNAME, CreateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.cxf.learn.haol.org/", name = "listResponse")
    public JAXBElement<ListResponse> createListResponse(ListResponse value) {
        return new JAXBElement<ListResponse>(_ListResponse_QNAME, ListResponse.class, null, value);
    }

}

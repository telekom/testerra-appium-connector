package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement;

import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rnhb on 11.01.2016.
 */
public class ScreenDump {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenDump.class);

    /**
     * Created by rnhb on 21.12.2015.
     *
     * @link https://docs.experitest.com/display/public/SA/GetVisualDump
     */
    public enum Type {
        NATIVE_INSTRUMENTED("Native"),
        WEB("Web"),
        NON_INSTRUMENTED("non-instrumented");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private final DocumentBuilderFactory documentBuilderFactory;
    private final DocumentBuilder documentBuilder;
    private final XPathFactory xPathFactory;
    private String rawXmlString;

    public ScreenDump(String rawXmlString) {
        this.rawXmlString = rawXmlString;
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        xPathFactory = XPathFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new TesterraRuntimeException("Failed to prepare " + this.getClass() + ".", e);
        }
    }

    private Document getDumpAsDocument() {
        try {
            return documentBuilder.parse(new InputSource(new StringReader(rawXmlString)));
        } catch (SAXException e) {
            rawXmlString = rawXmlString.replaceAll("&#(\\d*)", "$1");
            try {
                return documentBuilder.parse(new InputSource(new StringReader(rawXmlString)));
            } catch (Exception e1) {
                LOGGER.debug(e1.getMessage());
                throw new TesterraRuntimeException("Failed to parse NativeScreenDump as xml. NativeScreenDump = " + rawXmlString, e);
            }
        } catch (IOException e) {
            throw new TesterraRuntimeException("Failed to parse NativeScreenDump as xml. NativeScreenDump = " + rawXmlString, e);
        }
    }

    private XPathExpression compileXpathLocator(MobileLocator mobileLocator) {
        return compileXpathLocator(mobileLocator.getLocatorWithoutXpathPrefix());
    }

    private XPathExpression compileXpathLocator(String xPath) {
        try {
            return xPathFactory.newXPath().compile(xPath);
        } catch (XPathExpressionException e) {
            throw new TesterraRuntimeException("Failed to compile xpath \"" + xPath + "\".", e);
        }
    }

    private Object evaluateXpath(Document document, XPathExpression xPathExpression, QName returnType) {
        try {
            return xPathExpression.evaluate(document, returnType);
        } catch (XPathExpressionException e) {
            throw new TesterraRuntimeException("Failed to evaluate xPath \"" + xPathExpression + "\" against " + document, e);
        }
    }

    public Map<String, String> getAttributes(MobileLocator mobileLocator) {
        Document dumpAsDocument = getDumpAsDocument();
        XPathExpression xPathExpression = compileXpathLocator(mobileLocator);
        Object evaluate = evaluateXpath(dumpAsDocument, xPathExpression, XPathConstants.NODE);

        if (evaluate == null || !(((Node) evaluate).hasAttributes())) {
            return null;
        } else {
            HashMap<String, String> attributeMap = new HashMap<String, String>();
            Node attributes = (Node) evaluate;
            NamedNodeMap nodeAttributes = attributes.getAttributes();
            for (int i = 0; i < nodeAttributes.getLength(); i++) {
                Node item = nodeAttributes.item(i);
                String nodeName = item.getNodeName();
                String nodeValue = item.getNodeValue();
                if (nodeValue != null) {
                    nodeValue = nodeValue.trim();
                }
                attributeMap.put(nodeName, nodeValue);
            }
            return attributeMap;
        }
    }

    public int getNumberOfElements(MobileLocator mobileLocator) {
        Document dumpAsDocument = getDumpAsDocument();
        XPathExpression xPathExpression = compileXpathLocator(mobileLocator);
        Object evaluate = evaluateXpath(dumpAsDocument, xPathExpression, XPathConstants.NODE);
        if (evaluate == null) {
            return 0;
        } else {
            return ((NodeList) evaluate).getLength();
        }
    }

    public NodeList findElements(MobileLocator mobileLocator) {
        Document dumpAsDocument = getDumpAsDocument();
        XPathExpression xPathExpression = compileXpathLocator(mobileLocator.getLocatorWithoutXpathPrefix());
        Object evaluate = evaluateXpath(dumpAsDocument, xPathExpression, XPathConstants.NODE);
        if (evaluate == null) {
            return null;
        } else {
            return ((NodeList) evaluate);
        }
    }

    public String getContent(MobileLocator mobileLocator) {
        try {
            NodeList elements = findElements(mobileLocator);
            StringWriter buf = new StringWriter();
            Transformer xform = TransformerFactory.newInstance().newTransformer();
            xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            xform.setOutputProperty(OutputKeys.INDENT, "yes");
            xform.transform(new DOMSource((Node) elements), new StreamResult(buf));
            return buf.toString();
        } catch (TransformerException e) {
            LOGGER.debug(e.getMessage());
            //TODO the return statement looks suspicious
            return e.toString();
        }
    }

    public String getRawXmlString() {
        return rawXmlString;
    }
}

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import static io.restassured.RestAssured.given;

public class CalculatorTest {

    // http://www.dneonline.com/calculator.asmx

    @Test
    public void additionCalculatorTest() {

        int firstDigit = 4;
        int secondDigit = 3;

        String request =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                    "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                    "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <Add xmlns=\"http://tempuri.org/\">\n" +
            "      <intA>" + firstDigit + "</intA>\n" +
            "      <intB>" + secondDigit + "</intB>\n" +
            "    </Add>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";

        String response =
            given()
//                .auth()
//                .basic("username", "password")
                .contentType("text/xml")
                .body(request)
//                .relaxedHTTPSValidation()
            .when()
                .post("http://www.dneonline.com/calculator.asmx")
            .then()
                .statusCode(200)
                .extract()
                .response()
                .body().asString();

        Document doc = convertStringToXMLDocument(response);
        NodeList nodeList = doc.getElementsByTagName("AddResult");

        String element = nodeList.item(0).getTextContent();

        Assert.assertEquals(firstDigit + secondDigit, Integer.parseInt(element));
    }

    @Test
    public void multiplyCalculatorTest() {

        int firstDigit = 10;
        int secondDigit = 5;

        String request =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                        "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                        "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                        "  <soap:Body>\n" +
                        "    <Multiply xmlns=\"http://tempuri.org/\">\n" +
                        "      <intA>" + firstDigit + "</intA>\n" +
                        "      <intB>" + secondDigit + "</intB>\n" +
                        "    </Multiply>\n" +
                        "  </soap:Body>\n" +
                        "</soap:Envelope>";

        String response =
                given()
//                .auth()
//                .basic("username", "password")
                        .contentType("text/xml")
                        .body(request)
//                .relaxedHTTPSValidation()
                        .when()
                        .post("http://www.dneonline.com/calculator.asmx")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response()
                        .body().asString();

        Document doc = convertStringToXMLDocument(response);
        NodeList nodeList = doc.getElementsByTagName("MultiplyResult");

        String element = nodeList.item(0).getTextContent();

        Assert.assertEquals(firstDigit * secondDigit, Integer.parseInt(element));
    }

    private static Document convertStringToXMLDocument(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            return builder.parse(new InputSource(new StringReader(xmlString)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}



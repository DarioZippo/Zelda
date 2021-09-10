package zelda;

import com.thoughtworks.xstream.*;
import java.io.ByteArrayInputStream;
import java.nio.file.*;
import javafx.scene.input.KeyCode;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.w3c.dom.Document;
import org.xml.sax.*;

public class ReaderSettingsXML {

    private static final String filePathXml = "settings.xml";
    private static final String filePathXsd = "settings.xsd";
    public static final SettingsXML defaultSettings = new SettingsXML(
            new KeyAssociation(KeyCode.RIGHT, KeyCode.LEFT, KeyCode.UP, KeyCode.DOWN, KeyCode.Z, KeyCode.C, KeyCode.X),
            new ServerAddress("localhost", 9200),
            new ServerAddress("localhost", 3306),
            "root",
            "",
            5
    );

    public static SettingsXML readSettings() {
        String x = "";

        try {
            x = new String(Files.readAllBytes(Paths.get(ReaderSettingsXML.filePathXml)));
        } catch (Exception e) {
        }

        if (checkSettingsXML(x)) {
            System.out.println("Impostazioni XML ok");
            return deserializeXmlSettings(x);
        } else {
            System.out.println("Impostazioni XML DEFAULT");
            writeDefaultSettings();
            return ReaderSettingsXML.defaultSettings;
        }
    }

    private static boolean checkSettingsXML(String serializedXmlSettings) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Document d = db.parse(new InputSource(new ByteArrayInputStream(serializedXmlSettings.getBytes("utf-8"))));
            Schema s = sf.newSchema(new StreamSource(Files.newInputStream(Paths.get(ReaderSettingsXML.filePathXsd))));
            s.newValidator().validate(new DOMSource(d));
        } catch (Exception e) {
            if (e instanceof SAXException) {
                System.out.println("Errore di validazione:" + e.getMessage());
                return false;
            } else {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private static SettingsXML deserializeXmlSettings(String serializedXmlSettings) {
        XStream xs = new XStream();
        xs.setMode(XStream.NO_REFERENCES);
        return (SettingsXML) xs.fromXML(serializedXmlSettings);
    }

    private static void writeDefaultSettings() {
        XStream xs = new XStream();
        xs.setMode(XStream.NO_REFERENCES);
        String x = xs.toXML(defaultSettings);

        try {
            Files.write(Paths.get(ReaderSettingsXML.filePathXml), x.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

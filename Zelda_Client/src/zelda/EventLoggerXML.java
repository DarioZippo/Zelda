package zelda;

import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.net.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.w3c.dom.Document;
import org.xml.sax.*;

public class EventLoggerXML {

    private static java.net.InetSocketAddress serverLogAddress;

    private static final String filePathXSD = "entrataLog.xsd";

    public static final String eventDescriptionStart = "ApplicazioneAvviata";
    public static final String eventDescriptionClose = "Applicazione chiusa";

    public static final String eventDescriptionUsername = "Username inserito";
    public static final String eventDescriptionButton = "Pulsante - premuto";

    public static final String eventDescriptionStartGame = "Partita avviata";
    public static final String eventDescriptionEndGame = "Partita terminata";
    /*
    public static final String descrizioneEventoPausaPartita = "Partita messa in pausa";
    public static final String descrizioneEventoRipresaPartita = "Partita ripresa";
    */
    public static final String eventDescriptionInterruptGame = "Partita interrotta";
    public static final String eventDescriptionRecoverGame = "Partita recuperata";

    public static final String eventDescriptionKeyAssociation = "Caricate associazioni tasti personalizzate. Destra: - Sinistra: - Fuoco: - Pausa: -";
    public static final String placeholderDescription  = "-";

    public static void setServerLogAddress(String address, int port){
        System.out.println("Setta");
        EventLoggerXML.serverLogAddress = new InetSocketAddress(address, port);
    }
    
    public static void recordEvent(String eventDescription) {
        EventLogXML log = new EventLogXML(eventDescription);
        String serializedLog = serializeEventLog(log);
        if (validSerializedEventLog(serializedLog)) {
            sendSerializedEventLog(serializedLog);
        }
    }

    private static String serializeEventLog(EventLogXML eventLog) {
        XStream xs = new XStream();
        return xs.toXML(eventLog);
    }

    private static boolean validSerializedEventLog(String serializedEventLog) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Document d = db.parse(new InputSource(new ByteArrayInputStream(serializedEventLog.getBytes("utf-8"))));
            Schema s = sf.newSchema(new StreamSource(new File(filePathXSD)));
            s.newValidator().validate(new DOMSource(d));
        } catch (ParserConfigurationException | SAXException | IOException e) {
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

    private static void sendSerializedEventLog(String serializedEventLog) {
        try (
                Socket s = new Socket(serverLogAddress.getAddress(), serverLogAddress.getPort());
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());) {
            dout.writeUTF(serializedEventLog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package zelda;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.w3c.dom.Document;
import org.xml.sax.*;

public class ServerLogXML {

    private static final String xmlLogPath = "xmlLog.txt";
    private static final String entryLogXsdPath = "entrataLog.xsd";
    private static final int listeningPort = 9200;

    public static void main(String[] args) {
        try (
                ServerSocket listeningSocket = new ServerSocket(listeningPort);) {
            cycleWaitingLog(listeningSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cycleWaitingLog(ServerSocket listeningSocket) {
        while (true) {
            try {
                Socket connection = listeningSocket.accept();
                String log = receiveLog(connection);
                if (log != null) {
                    if (validEventLog(log)) {
                        recordLogOnFile(log);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String receiveLog(Socket applicationConnection) {
        try (
                DataInputStream din = new DataInputStream(applicationConnection.getInputStream());) {
            return din.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static boolean validEventLog(String logEventReceived) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Document d = db.parse(new InputSource(new ByteArrayInputStream(logEventReceived.getBytes("utf-8"))));
            Schema s = sf.newSchema(new StreamSource(new File(entryLogXsdPath)));
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

    private static void recordLogOnFile(String logEventReceived) {
        try {
            Files.write(Paths.get(xmlLogPath), (logEventReceived + "\n\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

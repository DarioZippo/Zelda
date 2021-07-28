package zelda;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import javafx.scene.control.TextField;

public class LocalCacheOperations {

    private static final String cacheFilePath = "cache.bin";
    
    public static void salvaCache(GameModel gameModel){
        CacheData cacheData = null;
        if (gameModel.getUser() != null){
            if(gameModel.isEnded() == false){
                EventLoggerXML.recordEvent(EventLoggerXML.eventDescriptionInterruptGame);
                cacheData = new CacheData(gameModel.getUser());
            }
        }
        
        try (
                    ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(cacheFilePath));) {
                oout.writeObject(cacheData);
            } catch (IOException e) {
                e.printStackTrace();
        }
    }
    
    public static void ripristinaCache(TextField loginTextField) {
        if (Files.exists(Paths.get(cacheFilePath))) {
            try (
                    ObjectInputStream oin = new ObjectInputStream(new FileInputStream(cacheFilePath));) {
                CacheData cacheData = (CacheData) oin.readObject();
                if(cacheData != null)
                    loginTextField.setText(cacheData.getUser());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
/*
    public static void salvaCache(String usernameGiocatore, ModelloGioco modelloGioco, VistaGioco vistaGioco) {
        if (usernameGiocatore != null) {
            DatiCachePartita cachePartita = null;
            if (modelloGioco.ottieniStatoPartita() != StatoPartita.Inattiva) {
                LoggerEventoXml.registraEvento(LoggerEventoXml.descrizioneEventoInterruzionePartita);
                cachePartita = costruisciCachePartita(modelloGioco, vistaGioco);
            }

            DatiCache datiCache = new DatiCache(usernameGiocatore, cachePartita);

            try (
                    ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(percorsoFileCache));) {
                oout.writeObject(datiCache);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static DatiCachePartita costruisciCachePartita(ModelloGioco modelloGioco, VistaGioco vistaGioco) {
        ArrayList<DatiCacheElementoGioco> datiProiettiliGiocatore = new ArrayList<>();
        ArrayList<DatiCacheElementoGioco> datiProiettiliAvversario = new ArrayList<>();

        for (Proiettile proiettile : vistaGioco.ottieniProiettili(Fazione.Giocatore)) {
            DatiCacheElementoGioco datiProiettile = new DatiCacheElementoGioco(Fazione.Giocatore, proiettile.getCenterX(), proiettile.getCenterY());
            datiProiettiliGiocatore.add(datiProiettile);
        }
        for (Proiettile proiettile : vistaGioco.ottieniProiettili(Fazione.Avversario)) {
            DatiCacheElementoGioco datiProiettile = new DatiCacheElementoGioco(Fazione.Avversario, proiettile.getCenterX(), proiettile.getCenterY());
            datiProiettiliAvversario.add(datiProiettile);
        }

        DatiCacheElementoGioco datiNavicellaGiocatore = new DatiCacheElementoGioco(Fazione.Giocatore,
                (vistaGioco.ottieniNavicella(Fazione.Giocatore).getBoundsInParent().getMinX() + vistaGioco.ottieniNavicella(Fazione.Giocatore).getBoundsInParent().getMaxX()) / 2,
                (vistaGioco.ottieniNavicella(Fazione.Giocatore).getBoundsInParent().getMinY() + vistaGioco.ottieniNavicella(Fazione.Giocatore).getBoundsInParent().getMaxY()) / 2);

        DatiCacheElementoGioco datiNavicellaAvversario = new DatiCacheElementoGioco(Fazione.Avversario,
                (vistaGioco.ottieniNavicella(Fazione.Avversario).getBoundsInParent().getMinX() + vistaGioco.ottieniNavicella(Fazione.Avversario).getBoundsInParent().getMaxX()) / 2,
                (vistaGioco.ottieniNavicella(Fazione.Avversario).getBoundsInParent().getMinY() + vistaGioco.ottieniNavicella(Fazione.Avversario).getBoundsInParent().getMaxY()) / 2);

        return new DatiCachePartita(modelloGioco.ottieniUsernameAvversario(),
                modelloGioco.ottieniVite(Fazione.Giocatore),
                modelloGioco.ottieniVite(Fazione.Avversario),
                datiNavicellaGiocatore,
                datiNavicellaAvversario,
                datiProiettiliGiocatore,
                datiProiettiliAvversario);
    }

    public static void ripristinaCache(XeviousVS interfaccia, ModelloGioco modelloGioco, VistaGioco vistaGioco) {
        if (Files.exists(Paths.get(percorsoFileCache))) {
            try (
                    ObjectInputStream oin = new ObjectInputStream(new FileInputStream(percorsoFileCache));) {
                DatiCache datiCache = (DatiCache) oin.readObject();
                interfaccia.impostaUsernameRecuperato(datiCache.username);
                if (datiCache.datiPartita != null) {
                    ripristinaCachePartita(datiCache.datiPartita, interfaccia, modelloGioco, vistaGioco);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void ripristinaCachePartita(DatiCachePartita datiCachePartita, XeviousVS interfaccia, ModelloGioco modelloGioco, VistaGioco vistaGioco) {
        modelloGioco.impostaRecuperoPartita(datiCachePartita.usernameAvversario, datiCachePartita.viteGiocatore, datiCachePartita.viteAvversario);
        vistaGioco.impostaNavicella(Fazione.Giocatore, datiCachePartita.navicellaGiocatore.centroX, datiCachePartita.navicellaGiocatore.centroY);
        vistaGioco.impostaNavicella(Fazione.Avversario, datiCachePartita.navicellaAvversario.centroX, datiCachePartita.navicellaAvversario.centroY);

        for (DatiCacheElementoGioco datiProiettile : datiCachePartita.proiettiliGiocatore) {
            vistaGioco.aggiungiProiettile(Fazione.Giocatore, datiProiettile.centroX, datiProiettile.centroY);
        }

        for (DatiCacheElementoGioco datiProiettile : datiCachePartita.proiettiliAvversario) {
            vistaGioco.aggiungiProiettile(Fazione.Avversario, datiProiettile.centroX, datiProiettile.centroY);
        }

        interfaccia.impostaRecuperoPartita();
    }
}
*/
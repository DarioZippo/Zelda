package zelda;

import java.io.*;
import java.nio.file.*;

public class LocalCacheOperations {

    private static final String cacheFilePath = "cache.bin";
    
    public static void saveCache(GameModel gameModel, TurnHandler turnHandler){
        CacheData cacheData = null;
        if(gameModel.isEnded() == false){
            EventLoggerXML.recordEvent(EventLoggerXML.eventDescriptionInterruptGame);
            cacheData = new CacheData(gameModel, turnHandler);
        }
                
        try (
                ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(cacheFilePath));) {
                oout.writeObject(cacheData);
            } catch (IOException e) {
                e.printStackTrace();
        }
    }
    
    public static void restoreCache(Zelda root) {
        if (Files.exists(Paths.get(cacheFilePath))) {
            try (
                ObjectInputStream oin = new ObjectInputStream(new FileInputStream(cacheFilePath));) {
                CacheData cacheData = (CacheData) oin.readObject();
                if(cacheData != null)
                    root.rebuildFromCache(cacheData);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

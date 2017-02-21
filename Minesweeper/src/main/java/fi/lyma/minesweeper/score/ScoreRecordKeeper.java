package fi.lyma.minesweeper.score;

import fi.lyma.minesweeper.logic.GameMode;

import java.io.*;
import java.util.*;

/**
 * Stores highs cores by the {@link GameMode}
 */
public class ScoreRecordKeeper implements Serializable {

    Map<GameMode, List<ScoreRecord>> records;

    private ScoreRecordKeeper() {
        records = new HashMap<>();
    }

    /**
     * Stores new {@link ScoreRecord} with {@link GameMode}
     * @param gameMode that was used
     * @param scoreRecord player's information
     */
    public void storeNewRecord(GameMode gameMode, ScoreRecord scoreRecord) {
        List<ScoreRecord> scoreRecords = records.getOrDefault(gameMode, new ArrayList<>());
        scoreRecords.add(scoreRecord);
        Collections.sort(scoreRecords);
        records.put(gameMode, scoreRecords);
    }

    /**
     * Returns all the scores by {@link GameMode}
     * @param gameMode {@link GameMode} to
     * @return list of {@link ScoreRecord}, if there are no SaveRecords with give {@link GameMode} empty list is returned
     */
    public List<ScoreRecord> getScoresByGameMode(GameMode gameMode) {
        return records.getOrDefault(gameMode, new ArrayList<>());
    }

    /**
     * Clears all the scores
     */
    public void clearRecords() {
        records.clear();
    }

    /**
     * Factory method for the class that Loads {@link ScoreRecordKeeper} from given path. If file doesn't exist or the loading fails, new {@link ScoreRecordKeeper} is returned.
     * @param path path to load from
     * @return ScoreRecordKeeper that is loaded from the file or new instance if loading fails
     */
    public static ScoreRecordKeeper loadFromFile(String path) {
        ScoreRecordKeeper keeper;
        try (
            FileInputStream stream = new FileInputStream(path);
            ObjectInputStream objectStream = new ObjectInputStream(stream)
        ){
            keeper = (ScoreRecordKeeper) objectStream.readObject();
        } catch (Exception e) {
            keeper = new ScoreRecordKeeper();
        }
        return keeper;
    }

    /**
     * Saves this instance to the given path
     * @param path path to save to
     */
    public void saveToFile(String path) {
        try (
            FileOutputStream outputStream = new FileOutputStream(path);
            ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)
        ){
            objectStream.writeObject(this);
        } catch (Exception e) {
        }
    }
}

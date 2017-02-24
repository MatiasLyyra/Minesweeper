package fi.lyma.minesweeper.score;

import fi.lyma.minesweeper.logic.GameMode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Stores highs cores by the {@link GameMode}.
 */
public class ScoreRecordKeeper implements Serializable {
    public static final String SAVE_DIR = ".minesweeper_save";
    private static final String SAVE_FILE = "minesweeper.dat";
    Map<GameMode, List<ScoreRecord>> records;

    private ScoreRecordKeeper() {
        records = new HashMap<>();
    }

    /**
     * Stores new {@link ScoreRecord} with {@link GameMode}.
     *
     * @param gameMode    that was used
     * @param scoreRecord player's information
     */
    public void storeNewRecord(GameMode gameMode, ScoreRecord scoreRecord) {
        List<ScoreRecord> scoreRecords = records.getOrDefault(gameMode, new ArrayList<>());
        scoreRecords.add(scoreRecord);
        Collections.sort(scoreRecords);
        records.put(gameMode, scoreRecords);
    }

    /**
     * Returns all the scores by {@link GameMode}.
     *
     * @param gameMode {@link GameMode} to
     * @return list of {@link ScoreRecord}, if there are no SaveRecords with give {@link GameMode} empty list is returned
     */
    public List<ScoreRecord> getScoresByGameMode(GameMode gameMode) {
        return records.getOrDefault(gameMode, new ArrayList<>());
    }

    /**
     * Clears all the scores.
     */
    public void clearRecords() {
        records.clear();
        saveToFile();
    }

    /**
     * Factory method for the class that Loads {@link ScoreRecordKeeper} from file.
     * If file doesn't exist or the loading fails, new {@link ScoreRecordKeeper} is returned.
     *
     * @return ScoreRecordKeeper that is loaded from the file or new instance if loading fails
     */
    public static ScoreRecordKeeper loadFromFile() {
        ScoreRecordKeeper keeper;
        try (
            FileInputStream stream = new FileInputStream(SAVE_DIR + File.separator + SAVE_FILE);
            ObjectInputStream objectStream = new ObjectInputStream(stream)
        ) {
            keeper = (ScoreRecordKeeper) objectStream.readObject();
        } catch (Exception e) {
            keeper = new ScoreRecordKeeper();
        }
        return keeper;
    }

    /**
     * Saves this instance to file.
     */
    public void saveToFile() {
        File saveFile = new File(SAVE_DIR);
        saveFile.mkdir();
        try (
            FileOutputStream outputStream = new FileOutputStream(SAVE_DIR + File.separator + SAVE_FILE);
            ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)
        ) {
            objectStream.writeObject(this);
        } catch (Exception e) {
        }
    }
}

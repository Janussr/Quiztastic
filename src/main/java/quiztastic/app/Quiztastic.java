package quiztastic.app;

import quiztastic.core.Board;
import quiztastic.core.Question;
import quiztastic.domain.BoardFactory;
import quiztastic.domain.Game;
import quiztastic.domain.QuestionRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Quiztastic {


    private static Quiztastic instance;

    private static Board createBoardFromFile(String file) {
        InputStream s = Quiztastic.class.getClassLoader()
                .getResourceAsStream(file);
        QuestionReader reader = new QuestionReader(new InputStreamReader(s));
        MapQuestionRepository repo = null;
        try {
            repo = MapQuestionRepository.fromQuestionReader(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new BoardFactory(repo).makeBoard();
    }

   public static Quiztastic getInstance() {
        if (instance == null) {
            Game danish = new Game(createBoardFromFile("danish_questions.tsv"), new ArrayList<>());
            Game english = new Game(createBoardFromFile("master_season1-35clean.tsv"), new ArrayList<>());
            instance = new Quiztastic(Map.of("da", danish, "en", english));

        }
        return instance;
    }

    private final Map<String, Game> games;

    private Quiztastic(Map<String, Game> games) {
        this.games = games;
    }

    public Iterable<Question> getQuestions() {
        return null;
    }

    public Board getBoard() {
        return null;
    }

    public Game getCurrentGame(String lang) {
        return games.get(lang);
    }

}

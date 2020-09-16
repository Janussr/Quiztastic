package quiztastic.domain;

import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Player;
import quiztastic.core.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game {
    private final Board board;
    private final List<Answer> answerList;


    public Game(Board board, List<Answer> answerList) {
        this.board = board;
        this.answerList = answerList;
    }

    public List<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();
        for (Board.Group g : board.getGroups()) {
            categories.add(g.getCategory());
        }
        return categories;
    }


    public String answerQuestion(int categoryNumber, int questionNumber, String answer, Player player) {
        Question q = getQuestion(categoryNumber, questionNumber);
        answerList.add(new Answer(categoryNumber, questionNumber, answer, player));
        if (q.getAnswer().equals(answer)) {
            return null;
        } else {
            return q.getAnswer();
        }
    }

    public Map<Player, Integer> getScore() {
        return null;
    }

    public String getQuestionText(int categoryNumber, int questionNumber) {
        return getQuestion(categoryNumber, questionNumber).getQuestion();
    }

    private Question getQuestion(int categoryNumber, int questionNumber) {
        return this.board.getGroups().get(categoryNumber).getQuestions().get(questionNumber);
    }

    public boolean isAnswered(int categoryNumber, int questionNumber) {
        for (Answer a : answerList) {
            if (a.hasIndex(categoryNumber, questionNumber)) {
                return true;
            }
        }
        return false;
    }

    private class Answer {
        private final int categoryNumber;
        private final int questionNumber;
        private final String answer;
        private final Player player;

        private Answer(int categoryNumber, int questionNumber, String answer, Player player) {
            this.categoryNumber = categoryNumber;
            this.questionNumber = questionNumber;
            this.answer = answer;
            this.player = player;
        }

        public boolean hasIndex(int categoryNumber, int questionNumber) {
            return this.categoryNumber == categoryNumber && this.questionNumber == questionNumber;
        }
    }
}

package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Category;
import quiztastic.core.Player;
import quiztastic.domain.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Protocol {
    private static final int counter = 0;
    private final Quiztastic quiz;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Player player;
    private final String language;

    public Protocol(BufferedReader in, PrintWriter out, Player player, String language) {
        this.in = in;
        this.out = out;
        this.player = player;
        this.language = language;
        this.quiz = Quiztastic.getInstance();
    }

    private String fetchCommand() throws IOException {
        out.print("> ");
        String word = in.readLine(); // answer a100 -> answer
        return word;
    }

    public void run() throws IOException {
        try {
            out.println("Welcome to jeopardy type d for print, h for help");
            out.flush();
            displayBoard();
            String cmd = fetchCommand();
            while (!cmd.equals("quit")) {
                switch (cmd) {
                    case "h":
                    case "help":
                        out.println("ITS FUCKING JEOPARDY, IF YOU DONT KNOW HOW TO PLAY GOOGLE IT!\nbut 4real, 'd' to draw the board, 'a' to answer. answers are entered as: Catagory Letter, Question, like so: 'B400'");
                        break;
                    case "draw":
                    case "d":
                        displayBoard();
                        break;
                    case "answer":
                    case "a":
                        String question = in.readLine();
                        String a = question.substring(0, 1).toLowerCase(); // "A100" -> "a"
                        int questionScore = Integer.parseInt(question.substring(1)); // "A100" -> 100
                        answerQuestion("abcdef".indexOf(a), questionScore);
                        break;
                    default:
                        out.println("Unknown command! '" + cmd + "' type help too see all commands");
                }
                out.flush();
                cmd = fetchCommand();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        }

    private void answerQuestion(int categoryNumber, int questionScore) throws IOException {
        String userAnswer = null;
        Game game = quiz.getCurrentGame(language);
        List<Integer> scores = List.of(100, 200, 300, 400, 500);
        int questionNumber = scores.indexOf(questionScore);
        out.println(game.getQuestionText(categoryNumber, questionNumber) + "\nEnter answer\n> ");
        out.flush();
        userAnswer = in.readLine();
        if (game.answerQuestion(categoryNumber, questionNumber, userAnswer, player) == null && userAnswer != null) {
            out.println("'" + userAnswer + "' was Correct, congrats cheater\n");
        } else if (userAnswer == null || userAnswer.isEmpty()) {
            out.print("Answer cannot be empty.. pls fix!\n");
        } else {
            out.println("Correct answer was: " + game.answerQuestion(categoryNumber, questionNumber, userAnswer, player));
            out.println("'" + userAnswer + "'   was incorrect, loser, xd\n");
        }

    }

    private void displayBoard() {
        int counter = 0;
        List<String> CategoryIds = new ArrayList<>();
        CategoryIds.add("NULL");
        CategoryIds.add("A.");
        CategoryIds.add("B.");
        CategoryIds.add("C.");
        CategoryIds.add("D.");
        CategoryIds.add("E.");
        CategoryIds.add("F.");
        Game game = quiz.getCurrentGame(language);
        List<Integer> scores = List.of(100, 200, 300, 400, 500);
        for (Category c : game.getCategories()) {
            counter++;
            out.print(CategoryIds.get(counter) + c.getName());
            out.print("  ");
        }
        out.println();
        for (int questionNumber = 0; questionNumber < 5; questionNumber++) {
            out.print("| ");
            for (int category = 0; category < 6; category++) {
                if (game.isAnswered(category, questionNumber)) {
                }
                out.print("     ");
                if (game.isAnswered(category, questionNumber)) {
                    out.print("---     ");
                } else {
                    out.print(scores.get(questionNumber));
                    out.print("     ");
                }
                out.print(" |");
            }
            out.println();
        }
    }

}

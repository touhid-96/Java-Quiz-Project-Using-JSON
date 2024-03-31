import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class Quiz {

    public static void main(String[] args) throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        String userFileLocation = "./src/main/resources/users.json";

        JSONArray usersArray = Utils.readJsonArray(userFileLocation);

        String role = "";
        boolean isLoggedIn = false;

        System.out.print("System:> Enter your username\nUser:> ");
        String _username = scanner.nextLine();
        System.out.print("System:> Enter password\nUser:> ");
        String _password = scanner.nextLine();

        for (int i=0; i<usersArray.size(); i++) {
            JSONObject userObj = (JSONObject) usersArray.get(i);
            String username = userObj.get("username").toString();
            String password = userObj.get("password").toString();

            if (_username.equals(username) && _password.equals(password)) {
                role = userObj.get("role").toString();
                isLoggedIn = true;
                break;
            }
        }

        if (isLoggedIn) {
            if (role.equals("admin")) {

                System.out.println("\nSystem:> Welcome " + _username + "! Please create new questions in the question bank.");

                addQuestion();   //"add questions" feature is written in this method

            } else if (role.equals("student")) {

                System.out.println("\nSystem:> Welcome " + _username + " to the quiz! We will throw you 10 questions.");

                generateQuiz();

            }
        } else {
            System.out.println("\nSystem:> Wrong credentials!");
        }
    }

    private static void generateQuiz() throws IOException, ParseException {
        String quiFileLocation = "./src/main/resources/quiz.json";
        JSONArray quesArray = Utils.readJsonArray(quiFileLocation);

        int questionsCount = 10;
        int score = 0;

        while (true) {
            System.out.println("Each MCQ mark is 1 and no negative marking. Are you ready? Press 's' to start.");
            if (!goAgain("Student")) break;

            //getting a shuffled index_array
            int[] shuffledQuesIndexArray = makeMeShuffledArray(quesArray.size());

            //starting quiz using the shuffled index_array
            for (int i=1; i<=questionsCount; i++) {
                JSONObject quesObj = (JSONObject) quesArray.get(shuffledQuesIndexArray[i-1]);

                System.out.println("\n[Question " + i + "] " + quesObj.get("question"));
                System.out.println("1. " + quesObj.get("option 1"));
                System.out.println("2. " + quesObj.get("option 2"));
                System.out.println("3. " + quesObj.get("option 3"));
                System.out.println("4. " + quesObj.get("option 4"));

                System.out.print("\nStudent:> ");
                String answer = new Scanner(System.in).nextLine();

                if (answer.equals(quesObj.get("answerkey").toString())) score++;
            }

            showScore(score);
            System.out.println("\nWould you like to start again? press s for start or q for quit");
            if (!goAgain("Student")) break;
        }
    }

    private static void showScore(int score) {
        if (score >= 8) System.out.println("message: Excellent! You have got "+score+" out of 10");
        else if (score >= 5) System.out.println("message: Good. You have got "+score+" out of 10");
        else if (score >= 2) System.out.println("message: Very poor! You have got "+score+" out of 10");
        else System.out.println("message: Very sorry you are failed. You have got "+score+" out of 10");
    }

    private static int[] makeMeShuffledArray(int size) {
        int[] tempArray = new int[size];
        for (int i=0; i<size; i++) {
            tempArray[i] = i;
        }

        Random random = new Random();
        for (int i=0; i<tempArray.length; i++) {
            int index = random.nextInt(tempArray.length-1);

            int temp = tempArray[index];
            tempArray[index] = tempArray[i];
            tempArray[i] = temp;
        }
        return tempArray;
    }

    private static void addQuestion() throws IOException, ParseException {
        String quiFileLocation = "./src/main/resources/quiz.json";
        JSONArray quesArray = Utils.readJsonArray(quiFileLocation);

        int ansOptionsCount = 4;
        while (true) {
            JSONObject quesObj = new JSONObject();
            System.out.print("\nSystem:> Input your question\nAdmin:> ");
            String question = new Scanner(System.in).nextLine();
            quesObj.put("question", question.trim());

            for (int i=1; i <= ansOptionsCount; i++) {
                System.out.print("System:> Input option " + i + ":\nAdmin:> ");
                String option = new Scanner(System.in).nextLine();
                quesObj.put("option " + i, option.trim());
            }

            System.out.print("System:> What is the answer key?\nAdmin:> ");
            int ansKey = new Scanner(System.in).nextInt();
            quesObj.put("answerkey", ansKey);

            quesArray.add(quesObj);    //question added
            Utils.fileWrite(quesArray, quiFileLocation);

            System.out.print("\nSystem:> Saved successfully! Do you want to add more questions? (press s for start and q for quit)");
            if (!goAgain("Admin")) break;
        }
    }

    private static boolean goAgain(String user) {
        while (true) {
            System.out.print("\n"+user+":> ");
            String _input = new Scanner(System.in).nextLine();

            if (_input.equalsIgnoreCase("q")) {
                System.out.println("\nSystem:> Quiting..");
                return false;
            }
            else if (_input.equalsIgnoreCase("s")) return true;

            else {
                System.out.print("\nSystem:> Invalid input! Press s for start and q for quit");
            }
        }
    }
}

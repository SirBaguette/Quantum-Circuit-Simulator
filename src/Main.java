import java.util.Scanner;
public class Main {
    
}

    public static void main(String[] args) {
    // [qubit number] [qubit state]
        int[][] qubitList = new int [10][10];
        Scanner scanner = new Scanner(System.in);
        System.out.println("Instructions");
        String menuOption = "";
        while (menuOption != "change qubit state" && menuOption != "create new qubit" && menuOption != "add quantum gate" && menuOption != "remove quantum gate" && menuOption != "remove qubit" && menuOption != "change quantum gate position") {
            try {
                menuOption = Scanner.nextLine();
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid phrase");
            }
            if(menuOption == "change qubit state") {
                System.out.println("Please input qubit number");
                int qNum = -1;
                while (qNum < 0 || qNum > 10) {
                    try {
                        qNum = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number");
                    }}
                int qState;
                System.out.println("Please input qubit state");
                int qState = -1;
                while (qState < 0 || qState > 4 ) {
                    try {
                        qNum = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number");
                    }}
            }
            
    }
        public void changeQState (Qubit q, int state){
            q.changeQubitState(state);
        }
}
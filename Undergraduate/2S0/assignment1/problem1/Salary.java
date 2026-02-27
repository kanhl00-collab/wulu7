import java.util.Scanner;
public class Salary {
    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);

        int t = keyboard.nextInt();
        int d = keyboard.nextInt();
        int D = keyboard.nextInt();
        int T = keyboard.nextInt();

        int salary = 0;

        // calculate salary
        if (T > t) { // work extra time
            salary = (T - t) * D + t * d;
        } else { // normal or less
            salary = T * d;
        }

        //output
        System.out.println(salary);

    }
}

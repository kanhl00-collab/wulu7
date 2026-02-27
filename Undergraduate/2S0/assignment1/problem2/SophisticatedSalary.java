package com.company;
import java.util.Scanner;
public class SophisticatedSalary {

    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);


        //init
        int t = 0;
        int T = 0;
        int d = 0;
        int D = 0;


        //assign values
        for (int i = 0; i < 4;i ++){
            String s1 = keyboard.next();
            if (s1.contains("t=")) {
                s1 = s1.substring(2);
                t = Integer.parseInt(s1);
            } else if (s1.contains("T=")) {
                s1 = s1.substring(2);
                T = Integer.parseInt(s1);
            } else if (s1.contains("d=")) {
                s1 = s1.substring(2);
                d = Integer.parseInt(s1);
            } else {
                s1 = s1.substring(2);
                D = Integer.parseInt(s1);
            }
        }


        // init salary
        int salary = 0;

        // calculate salary
        if (T > t) { // work extra time
            salary = (T - t) * D + t * d;
        } else { // normal or less
            salary = T * d;
        }



        //format output

        String s2 = salary + " Dollars";
        for (int i =0; i < s2.length(); i ++) {
            System.out.print("*");
        }
        System.out.println();
        System.out.println(s2);
        for (int i =0; i < s2.length(); i ++) {
            System.out.print("*");
        }

    }
}



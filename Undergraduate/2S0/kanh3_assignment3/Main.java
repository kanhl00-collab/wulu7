package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);

        String line = scanner.nextLine();

        //check if there are invalid characters
        String[] characters = line.split("");
        for (int i = 0; i < line.length(); i++) {
            if (characters[i].matches("[^()@&\\d]")) { //anything that is not (,),@,&,number
                System.out.println("INVALID CHARACTERS");
                return;
            }
        }

        //check if parenthesis is valid
        if (!isValid(line)) {
            System.out.println("INVALID EXPRESSION");
            return;
        }

        /*evaluate the expression*/
        //separate numbers and symbols and put them in stack
        characters = line.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)|(?<=\\D)(?=\\D)");//split at symbol-digit, digit-symbol,symbol-symbol boundaries
        List<String> chars = Arrays.asList(characters);
        Stack<String> stack = new Stack<>();
        stack.addAll(chars);

        //remove parenthesis and evaluate the expression inside the parenthesis
        try {
            removeP(stack);
        } catch (Exception e) {
            System.out.println("INVALID EXPRESSION");
            return;
        }


        //here the expression is parenthesis-free, like 1@2&34&567@8
        //so we only need to evaluate it and print the result

        try {
            getValue(stack);
        } catch (Exception e) {
            System.out.println("INVALID EXPRESSION");
            return;
        }
        String result = getValue(stack);
        if (result.matches("//D")) {
            System.out.println("INVALID EXPRESSION");
        } else {
            System.out.println(result);
        }


    }


    //find the inner and righter most parenthesis, evaluate, until there is no parenthesis
    private static void removeP(Stack<String> stack) {
        while (stack.contains("(")) {
            int lastL = stack.lastIndexOf("("); // the last index of left parenthesis
            int nextR = lastL;//initiate
            //find the index of next right parenthesis, which is a pair to left parenthesis we just found
            for (int i = lastL; i < stack.size(); i++) {
                if (stack.elementAt(i).equals(")")) {
                    break;
                } else {
                    nextR++;
                }
            }

            //invalid expression
            /*if ((nextR - lastL) % 2 == 1) {
                System.out.println("INVALID EXPRESSION");
            }*/


            //put the content inside this parenthesis into a new stack
            Stack<String> stack1 = new Stack<>();
            for (int i = lastL + 1; i < nextR; i++) {
                stack1.push(stack.elementAt(i));
            }
            //add the evaluation result of the parenthesis into the main stack
            stack.add(nextR + 1, getValue(stack1));
            //remove the content of the parenthesis including "("")"
            for (int i = lastL; i < nextR + 1; i++) {
                stack.remove(lastL);
            }

        }
    }

    //evaluate an expression without parenthesis using recursion
    static String getValue(Stack stack) {

        //base case: a number
        if (stack.size() == 1) {
            return (String) stack.elementAt(0);

        //recursion: like A@B or A@B?C?D.......... (? is @ or &)
        } else if (((String) stack.elementAt(1)).equals("@")) {
            String str1 = (String) stack.elementAt(0); //get the first number, A
            String str2 = (String) stack.elementAt(2); //get the second number, B
            String result = at(str1, str2); //get min(A,B)
            stack.add(3, result); //add min(A,B)
            stack.remove(2); //remove B
            stack.remove(1); //remove @
            stack.remove(0); //remove A
            return getValue(stack); //now min(A,B) or min(A,B)?C?D..........., then do recursion

        //recursion: like A&B or A&B?C?D......... (? is & or @)
        } else {
            String str1 = (String) stack.elementAt(0); //get A
            String str2 = (String) stack.elementAt(2); //gets B
            String result = and(str1, str2); //max(A,B)
            stack.add(3, result);
            stack.remove(2); //remove B
            stack.remove(1); //remove &
            stack.remove(0); //remove A
            return getValue(stack); //now max(A,B) or max(A,B)?C?D..........., then do recursion
        }
    }



    //define @ operator
    static String at(String str1, String str2) {
        if (Integer.parseInt(str1) < Integer.parseInt(str2)) {
            return str1;
        }
        return str2;
    }


    //define & operator
    static String and(String str1, String str2) {
        if (Integer.parseInt(str1) < Integer.parseInt(str2)) {
            return str2;
        }
        return str1;
    }


    //if parenthesis is valid
    static boolean isValid(String line) {
        Stack<Character> stack = new Stack<> ();
        for (int i = 0; i < line.length(); i ++) {
            switch (line.charAt(i)){
                case '(': stack.push('(');
                    break;
                case ')':
                    if (stack.empty())
                        return false;
                    stack.pop();
                    break;
                default:
                    break;
            }
        }
        return stack.empty();
    }



}

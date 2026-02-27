package com.company;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
public class Main {


    private static CarModel[] models = new CarModel[100];// array for models
    private static int num_models = 0;// total number of models
    private static int num_cars = 0;// total  number of cars
    private static Car[] cars = new Car[100];// array for cars


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        //initialize user input
        String user_input = "" ;


        List<Double>[] suc_trips = new ArrayList[100];// array for successful trips for each car

        //keeps getting input until the line "FINISH"
        while (!"FINISH".equals(user_input)) {

            user_input = scanner.nextLine();// get the next line


            // different cases start here
            if (user_input.startsWith("MODEL")) {
                String[] arrForModel = user_input.split(" ");// split the input
                num_models += 1;// total number of models + 1
                // add new model to the array
                models[num_models - 1] = new CarModel(arrForModel[1],
                                                      Double.parseDouble(arrForModel[2]),
                                                      Double.parseDouble(arrForModel[3]));


            } else if (user_input.startsWith("CAR")) {
                String[] arrForCar = user_input.split(" ");
                if (whichModel(arrForCar[1])!= num_models) { // in case there is no such model
                    num_cars += 1;// total number of cars + 1
                    //add new car to the array
                    cars[num_cars - 1] = new Car(models[whichModel(arrForCar[1])],
                            Integer.parseInt(arrForCar[2]));

                    // initialize an arraylist for this new car
                    suc_trips[num_cars - 1] = new ArrayList<Double>();
                }




            } else if (user_input.startsWith("TRIP")) {
                String[] arrForTrip = user_input.split(" ");
                if (whichCar(arrForTrip[1])!= num_cars) { //in case there is no such car
                    if (cars[whichCar(arrForTrip[1])].trip(Double.parseDouble(arrForTrip[2]))) { //call the trip function
                        System.out.println("Trip completed successfully for #" + arrForTrip[1]);//if true print successful
                        // add this successful trip to the array for successful trips for this car
                        suc_trips[whichCar(arrForTrip[1])].add(Double.parseDouble(arrForTrip[2]));

                    } else {//if false print no fuel
                        System.out.println("Not enough fuel for #" + arrForTrip[1]);
                    }
                }


            } else if (user_input.startsWith("REFILL")) {
                String[] arrForRefill = user_input.split(" ");
                cars[whichCar(arrForRefill[1])].refill();// call the refill function



            } else if (user_input.startsWith("LONGTRIP")){
                String[] arrForLongtrip = user_input.split(" ");
                int result = 0; // initialize the answer for successful trips greater than given distance
                //count the number of distance of successful trips greater than given distance
                for (int i = 0; i < suc_trips[whichCar(arrForLongtrip[1])].size(); i++) {
                    if (suc_trips[whichCar(arrForLongtrip[1])].get(i) > Double.parseDouble(arrForLongtrip[2])) {
                        result += 1;
                    }
                }
                //print out the result
                System.out.println("#" + arrForLongtrip[1] + " made " + result + " trips longer than " +arrForLongtrip[2]);
            }
        }
    }

    // return the index of the model whose name is given
    private static int  whichModel(String model_name) {
        for (int i = 0; i < num_models; i ++) {
            if (model_name.equals(models[i].getName())) {
                return i;
            }
        }
        //exception
        return num_models;// if can't find model name in the array
    }


    // return the index of the car whose plate number is given
    private static int whichCar(String car_number) {
        for (int i = 0; i < num_cars; i ++) {
            if (Integer.parseInt(car_number) == (cars[i].getPlateNumber())) { //find the exact car
                return i;
            }
        }
        //exception
        return num_cars;//if can't find plate number in the array
    }


}

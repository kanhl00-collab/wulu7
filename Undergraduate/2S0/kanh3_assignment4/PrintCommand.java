import java.text.ParseException;

class PrintCommand extends Command {
    private String entityType;
    private String queryType;
    private String queryValue;

    PrintCommand(String[] tokens) {
        super();
        entityType = tokens[1];
        queryType = tokens[2];
        queryValue = tokens[3];
    }

    @Override
    void run(Database database) {
        //PRINT CUSTOMER ... ...
        if (entityType.equals("CUSTOMER"))
            runPrintCustomer(database);
        //PRINT PLAN ... ...
        else if (entityType.equals("PLAN"))
            runPrintPlan(database);
        else {
            throw new BadCommandException("Bad print command.");
        }
    }

    private void runPrintCustomer(Database database) {
        //PRINT CUSTOMER TOTAL_CLAIMED ...
        if (queryType.equals("TOTAL_CLAIMED")) {
            System.out.println("Total amount claimed by " + database.getCustomer(queryValue).getName() +
                    " is " + database.totalClaimAmountByCustomer(queryValue));
        //PRINT CUSTOMER TOTAL_RECEIVED ...
        } else if (queryType.equals("TOTAL_RECEIVED")) {
            System.out.println("Total amount received by " + database.getCustomer(queryValue).getName() +
                            " is " + database.totalReceivedAmountByCustomer(queryValue));
        }
    }

    //TODO
    private void runPrintPlan(Database database) {
        //PRINT PLAN NUM_CUSTOMERS ...
        if (queryType.equals("NUM_CUSTOMERS")) {
            System.out.println("Number of customers under " + database.getPlan(queryValue).getName() +
                    " is " + database.numberOfCustomers(queryValue));
         //PRINT PLAN TOTAL_PAYED_TO_CUSTOMERS ...
        } else if (queryType.equals("TOTAL_PAYED_TO_CUSTOMERS")) {
            System.out.println("Total amount payed under " + database.getPlan(queryValue).getName() +
                    " is " + database.totalPayedAmountByPlan(queryValue));
        }
    }
}

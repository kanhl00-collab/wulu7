import java.util.ArrayList;

class Database {
    //generate empty database
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Home> homes = new ArrayList<>();
    private ArrayList<Car> cars = new ArrayList<>();
    private ArrayList<Plan> plans = new ArrayList<>();
    private ArrayList<Contract> contracts = new ArrayList<>();
    private ArrayList<Claim> claims = new ArrayList<>();

    /*
    methods to update database
     */
    void insertHome(Home home) {
        homes.add(home);
    }

    void insertCar(Car car) {
        cars.add(car);
    }

    void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    void insertPlan(Plan plan) {
        plans.add(plan);
    }

    void insertClaim(Claim claim) {
        claims.add(claim);
    }

    void insertContract(Contract contract) {
        contracts.add(contract);
    }

    /*
    methods to get objects by name
     */
    Plan getPlan(String name) {
        //search for every plan in arrayList until the wanted one
        for (Plan plan : plans) {
            if (plan.name.equals(name))
                return plan;
        }
        return null;
    }

    Customer getCustomer(String name) {
        for (Customer customer : customers) {
            if (customer.getName().equals(name))
                return customer;
        }
        return null;
    }

    Contract getContract(String name) {
        for (Contract contract : contracts) {
            if (contract.getContractName().equals(name))
                return contract;
        }
        return null;
    }

    String getPlanByHome(Home home) {
        for (Contract contract : contracts) {
            if (home.getOwnerName().equals(contract.getCustomerName()))
                return contract.getPlanName();
        }
        return null;
    }

    String getPlanByCar(Car car) {
        for (Contract contract : contracts) {
            if (car.getOwnerName().equals(contract.getCustomerName()))
                return contract.getPlanName();
        }
        return null;
    }

    /**
     * There is at most one home owned by each person.
     */
    Home getHome(String ownerName) {
        for (Home home : homes) {
            if (home.getOwnerName().equals(ownerName))
                return home;
        }
        return null;
    }

    /**
     * There is at most one car owned by each person.
     */
    Car getCar(String ownerName) {
        for (Car car : cars) {
            if (car.getOwnerName().equals(ownerName))
                return car;
        }
        return null;
    }

    long totalClaimAmountByCustomer(String customerName) {
        long totalClaimed = 0;
        for (Claim claim : claims) {
            // customer who signed the contract == customer who claimed
            if (getContract(claim.getContractName()).getCustomerName().equals(customerName))
                totalClaimed += claim.getAmount();
        }
        return totalClaimed;
    }

    long totalReceivedAmountByCustomer(String customerName) {
        long totalReceived = 0;
        for (Claim claim : claims) {
            //the contract which the claim belongs to
            Contract contract = getContract(claim.getContractName());
            //if it is the right customer
            if (contract.getCustomerName().equals(customerName)) {
                //only add if claim is successful
                if (claim.wasSuccessful()) {
                    long deductible = getPlan(contract.getPlanName()).getDeductible();
                    //if amount claimed < deductible, return 0
                    totalReceived += Math.max(0, claim.getAmount() - deductible);
                }
            }
        }
        return totalReceived;
    }

    long numberOfCustomers(String planName) {
        long totalNumber = 0;
        //start from contract
        for (Contract contract : contracts) {
            // the plan on the contract is the plan we want
            if (contract.getPlanName().equals(planName)) {
                totalNumber++;
            }
        }
        //start from customer to avoid duplication
        /*for (Customer customer : customers) {
            boolean hasPlan = false;
            for (Contract contract : contracts) {
                //search for contracts with customer's name on it
                if (contract.getCustomerName().equals(customer.getName())) {
                    //check which plan it is
                    if (contract.getPlanName().equals(planName)) {
                        //this is the plan we want
                        hasPlan = true;
                    }
                }
            }
            if (hasPlan) {totalNumber++;}
        }
        *
         */
        return totalNumber;
    }

    long totalPayedAmountByPlan(String planName) {
        long totalPayed = 0;
        for (Claim claim : claims) {
            Contract contract = getContract(claim.getContractName());
            if (contract.getPlanName().equals(planName)) {
                if (claim.wasSuccessful()) {
                    long deductible = getPlan(contract.getPlanName()).getDeductible();
                    totalPayed += Math.max(0, claim.getAmount() - deductible);
                }
            }
        }
        return totalPayed;
    }
}

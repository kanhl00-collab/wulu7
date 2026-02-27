import java.util.ArrayList;
import java.util.Stack;

class Database {
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Home> homes = new ArrayList<>();
    private ArrayList<Car> cars = new ArrayList<>();
    private ArrayList<Plan> plans = new ArrayList<>();
    private ArrayList<Contract> contracts = new ArrayList<>();
    private ArrayList<Claim> claims = new ArrayList<>();
    private ArrayList<Company> companies = new ArrayList<>();

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

    void insertCompany(Company company) {
        companies.add(company);
    }

    Plan getPlan(String name) {
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

    // adds function for company

    // Company getCompany(String name) {
    //    for (Company company : companies) {
    //        if (company.getCompanyName().equals(name))
    //            return company;
    //    }
    //    return null;
    //}

    ArrayList<Company> getCompaniesByOwnerName(String ownerName) {
        ArrayList<Company> result = new ArrayList<>();
        Stack<Company> stack = new Stack<>();
        // finds companies that directly belong to the customer
        for (Company company : companies) {
            if (company.getOwnerName().equals(ownerName)) {
                result.add(company);
                stack.push(company);
                // System.out.println("Direct:" + company.getCompanyName());
            }
        }
        // finds companies that indirectly belong to the customer
        while (! stack.empty()) {
            Company currentCompany = stack.pop();
            // System.out.println("Current:" + currentCompany.getCompanyName());
            for (Company company : companies) {
                // System.out.println("Searched:" + company.getCompanyName());
                if (! result.contains(company)) {
                    if (company.getOwnerName().equals(currentCompany.getCompanyName())) {
                        result.add(company);
                        stack.push(company);
                    }
                }
            }
        }
        return result;
    }

    long getWealthByOwnerName(String ownerName) {
        long wealth = 0;
        // total home wealth
        for (Home home : getHomesByOwnerName(ownerName)) {
            wealth += home.getValue();
        }
        // total car wealth
        for (Car car : getCarsByOwnerName(ownerName)) {
            wealth += car.getValue();
        }
        // total company wealth
        for (Company company : getCompaniesByOwnerName(ownerName)) {
            wealth += company.getValue();
        }
        return wealth;
    }

    /* This function has been updated to output a list
    of homes rather than a single home. In other words,
    an owner may own multiple homes.
     */
    ArrayList<Home> getHomesByOwnerName(String ownerName) {
        ArrayList<Home> result = new ArrayList<>();
        for (Home home : homes) {
            if (home.getOwnerName().equals(ownerName))
                result.add(home);
        }
        return result;
    }


    /* This function has been updated to output a list
    of homes rather than a single home. In other words,
    an owner may own multiple homes.
     */
    ArrayList<Car> getCarsByOwnerName(String ownerName) {
        ArrayList<Car> result = new ArrayList<>();
        for (Car car : cars) {
            if (car.getOwnerName().equals(ownerName))
                result.add(car);
        }
        return result;
    }


    long totalClaimAmountByCustomer(String customerName) {
        long totalClaimed = 0;
        for (Claim claim : claims) {
            if (getContract(claim.getContractName()).getCustomerName().equals(customerName))
                totalClaimed += claim.getAmount();
        }
        return totalClaimed;
    }

    long totalReceivedAmountByCustomer(String customerName) {
        long totalReceived = 0;
        for (Claim claim : claims) {
            Contract contract = getContract(claim.getContractName());
            if (contract.getCustomerName().equals(customerName)) {
                if (claim.wasSuccessful()) {
                    long deductible = getPlan(contract.getPlanName()).getDeductible();
                    totalReceived += Math.max(0, claim.getAmount() - deductible);
                }
            }
        }
        return totalReceived;
    }

    public Insurable getCarByPlateNumber(String insurableID) {
        for (Car car : cars) {
            if (car.getPlateNumber().equals(insurableID))
                return car;
        }
        return null;
    }

    public Insurable getHomeByPostalCode(String insurableID) {
        for (Home home : homes) {
            if (home.getPostalCode().equals(insurableID))
                return home;
        }
        return null;
    }
}

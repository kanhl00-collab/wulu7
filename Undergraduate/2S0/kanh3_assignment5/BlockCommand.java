import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

class BlockCommand extends Command {
    private String blockType;
    private HashMap<String, Tag> tags = new HashMap<>();
    private HashMap<String, Tag> tags2 = new HashMap<>();

    BlockCommand(String blockType) {
        this.blockType = blockType;
    }

    /*
    Cause range criterion only appears in the plan section, a less expansive way is to change something regarding the plan and
    its child class instead of changing all the data structure(HashMap). Here a simple way is to add another HashMap to store the
    range boundary when reading commands, and add parameter to the classes(Plan, HomePlan, CarPlan) when using data. See changes
    made to these classes.
     */
    void addTag(Tag tag) {
        // if tag name already in HashMap
        if (tags.containsKey(tag.getName())) {
            // if relation is the same
            if (tag.getRelation() == tags.get(tag.getName()).getRelation()) {
                // replace
                tags.put(tag.getName(), tag);
            }
            // add another tag
            else {
                tags2.put(tag.getName(), tag);
            }
        }
        else {tags.put(tag.getName(), tag);}
    }

    String getBlockType() {
        return blockType;
    }

    @Override
    void run(Database database) throws ParseException {
        if (blockType.equals(Customer.inputTag)) {
            database.insertCustomer(new Customer(tags));
        } if (blockType.equals(Home.inputTag)) {
            database.insertHome(new Home(tags));
        } if (blockType.equals(Car.inputTag)) {
            database.insertCar(new Car(tags));
        } if (blockType.equals(Claim.inputTag)) {
            Claim claim = new Claim(tags);
            database.insertClaim(claim);
            if (processClaim(claim, database)) {
                claim.setSuccessful(true);
                System.out.println("Claim on " + Utils.formattedDate(claim.getDate())
                        + " for contract " + claim.getContractName() + " was successful.");
            } else {
                claim.setSuccessful(false);
                System.out.println("Claim on " + Utils.formattedDate(claim.getDate())
                        + " for contract " + claim.getContractName() + " was not successful.");
            }
        } if (blockType.equals(Contract.inputTag)) {
            database.insertContract(new Contract(tags));
        } if (blockType.equals(HomePlan.inputTag)) {
            // adding tags2
            database.insertPlan(new HomePlan(tags, tags2));
        } if (blockType.equals(CarPlan.inputTag)) {
            // adding tags2
            database.insertPlan(new CarPlan(tags, tags2));
        } if (blockType.equals(Company.inputTag)) {
            database.insertCompany(new Company(tags));
        }
    }

    private boolean processClaim(Claim claim, Database database) {
        Contract contract = database.getContract(claim.getContractName());
        Customer customer = database.getCustomer(contract.getCustomerName());
        Plan plan = database.getPlan(contract.getPlanName());
        Insurable insurable = plan.getInsuredItem(claim.getInsurableID(), database);

        /* If INSURABLE_ID was not found or was from the wrong type (car/home).
        For example, if INSURABLE_ID corresponds to a car but the plan
        corresponds to a home then insurable would be null. */
        if (insurable == null)
            return false;

        // If the claimed item is not owned by the owner.
        if (!insurable.getOwnerName().equals(customer.getName()))
            return false;

        // If the claimed amount is higher than covered by the plan.
        if (plan.getMaxCoveragePerClaim() < claim.getAmount())
            return false;

        // If the claim date is not in the contract period.
        if (claim.getDate().after(contract.getEndDate()) || claim.getDate().before(contract.getStartDate()))
            return false;

        // If the customer was not eligible.
        if (!plan.isEligible(customer, claim.getDate(), database))
            return false;

        return plan.isEligible(insurable, claim.getDate());
    }
}

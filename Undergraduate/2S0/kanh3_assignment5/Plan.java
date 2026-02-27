import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

abstract class Plan {
    String name;
    long premium;
    long maxCoveragePerClaim;
    long deductible;
    RangeCriterion customerAgeCriterion = new RangeCriterion();
    RangeCriterion customerIncomeCriterion = new RangeCriterion();
    RangeCriterion customerWealthCriterion = new RangeCriterion();

    Plan(HashMap<String, Tag> tags, HashMap<String, Tag> tags2) {
        name = tags.get("NAME").getValue();
        premium = Integer.parseInt(tags.get("PREMIUM").getValue());
        maxCoveragePerClaim = Integer.parseInt(tags.get("MAX_COVERAGE_PER_CLAIM").getValue());
        deductible = Integer.parseInt(tags.get("DEDUCTIBLE").getValue());

        if (tags.get("CUSTOMER.AGE") != null) {
            customerAgeCriterion.addCriterion(tags.get("CUSTOMER.AGE"));
        }
        // another criterion
        if (tags2.get("CUSTOMER.AGE") != null) {
            customerAgeCriterion.addCriterion(tags2.get("CUSTOMER.AGE"));
        }
        if (tags.get("CUSTOMER.INCOME") != null) {
            customerIncomeCriterion.addCriterion(tags.get("CUSTOMER.INCOME"));
        }
        // another criterion
        if (tags2.get("CUSTOMER.INCOME") != null) {
            customerIncomeCriterion.addCriterion(tags2.get("CUSTOMER.INCOME"));
        }
        if (tags.get("CUSTOMER.WEALTH") != null) {
            customerWealthCriterion.addCriterion(tags.get("CUSTOMER.WEALTH"));
        }
        if (tags2.get("CUSTOMER.WEALTH") != null) {
            customerWealthCriterion.addCriterion(tags2.get("CUSTOMER.WEALTH"));
        }

    }

    abstract boolean isEligible(Insurable insurable, Date date);

    abstract ArrayList<? extends Insurable> getInsuredItems(Customer customer, Database database);

    abstract Insurable getInsuredItem(String insurableID, Database database);

    boolean isEligible(Customer customer, Date currentDate, Database database) {
        // Extracting the age of the customer
        LocalDate localCurrentDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localBirthDate = customer.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long age = localCurrentDate.getYear() - localBirthDate.getYear();
        // Checking the exact age of customer
        if (localCurrentDate.getDayOfYear() < localBirthDate.getDayOfYear())
            // if not exceeding the exact date, then subtract 1 from age
            age--;
        // Checking if the age is in the range.
        if (!customerAgeCriterion.isInRange(age))
            return false;
        // Checking if the wealth is in the range.
        if (!customerWealthCriterion.isInRange(database.getWealthByOwnerName(customer.getName())))
            return false;
        // Checking if the income is in the range.
        return customerIncomeCriterion.isInRange(customer.getIncome());
    }

    String getName() {
        return name;
    }

    long getPremium() {
        return premium;
    }

    long getMaxCoveragePerClaim() {
        return maxCoveragePerClaim;
    }

    long getDeductible() {
        return deductible;
    }
}

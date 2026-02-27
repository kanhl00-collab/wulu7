
import java.text.ParseException;
import java.util.HashMap;

class Company {
    private String ownerName;
    private String companyName;
    private long value;

    static final String inputTag = "COMPANY";

    Company(HashMap<String, Tag> tags) throws ParseException {
        ownerName = tags.get("OWNER_NAME").getValue();
        companyName = tags.get("COMPANY_NAME").getValue();
        value = Long.parseLong(tags.get("VALUE").getValue());
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public long getValue() {
        return value;
    }

    public static String getInputTag() {
        return inputTag;
    }
}


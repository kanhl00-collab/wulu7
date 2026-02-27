import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

class Car extends Insurable {

    private String make;
    private String model;
    private Date purchaseDate;
    private long mileague;

    static final String inputTag = "CAR";

    //gets value according to input
    Car(HashMap<String, Tag> tags) throws ParseException {
        super(tags);
        make = tags.get("MAKE").getValue();
        model = tags.get("MODEL").getValue();
        purchaseDate = Utils.convertDate(tags.get("PURCHASE_DATE").getValue());
        mileague = Long.parseLong(tags.get("MILEAGE").getValue());
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public long getMileague() {
        return mileague;
    }

    public static String getInputTag() {
        return inputTag;
    }
}


package enums;

public enum StatisticsAttribute {

    CLIENT_NAME("client_name", false),
    ORDER_DATE("order_date", false),
    STATUS("status", false),
    CATEGORIES("categories", true);

    private final String jsonKey;
    private final boolean isComplex;

    StatisticsAttribute(String jsonKey, boolean isComplex) {
        this.jsonKey = jsonKey;
        this.isComplex = isComplex;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public boolean isComplex() {
        return isComplex;
    }
}
package nitpicksy.qrservice.enumeration;

public enum PaymentStatus {
    NEW("new"),
    PENDING("pending"),
    CONFIRMING("confirming"),
    PAID("paid"),
    INVALID("invalid"),
    EXPIRED("expired"),
    CANCELED("canceled"),
    REFUNDED("refunded");

    public final String name;

    private PaymentStatus(String name) {
        this.name = name;
    }
}

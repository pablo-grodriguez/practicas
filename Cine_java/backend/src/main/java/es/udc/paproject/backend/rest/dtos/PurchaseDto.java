package es.udc.paproject.backend.rest.dtos;

public class PurchaseDto {

    private Long id;
    private long datePurchase;
    private String title;
    private int quantity;
    private float price;
    private long dateSession;
    private boolean delivered;

    public PurchaseDto() {
    }

    public PurchaseDto(Long id, long datePurchase, String title, int quantity, float price, long dateSession, boolean delivered) {
        this.id = id;
        this.datePurchase = datePurchase;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
        this.dateSession = dateSession;
        this.delivered = delivered;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDatePurchase() {
        return datePurchase;
    }

    public void setDatePurchase(long datePurchase) {
        this.datePurchase = datePurchase;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getDateSession() {
        return dateSession;
    }

    public void setDateSession(long dateSession) {
        this.dateSession = dateSession;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}

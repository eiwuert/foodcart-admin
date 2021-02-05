package de.habibhaidari.foodcart.model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static de.habibhaidari.foodcart.constant.FormatConstants.TIME_FORMAT;

public class Order {

    private int id;

    // TODO ENUM
    private State state;

    @SerializedName("refund_rate")
    private Rate refundRate;

    @SerializedName("created_at")
    private Date createdAt;


    @SerializedName("updated_at")
    private Date updatedAt;


    @SerializedName("deleted_at")
    private Date deletedAt;


    @SerializedName("method_id")
    private int methodId;
    private Method method;
    private String notes;

    @SerializedName("reference_order")
    private Order referenceOrder;


    @SerializedName("user_id")
    private int userId;
    private int delivery;
    private User user;
    private ArrayList<Position> positions;

    private String name;
    private String phone;
    private String street;
    private Postcode postcode;
    private String floor;
    private int postcode_id;

    private Rate rate;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public int getPostcode_id() {
        return postcode_id;
    }

    public void setPostcode_id(int postcode_id) {
        this.postcode_id = postcode_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Postcode getPostcode() {
        return postcode;
    }

    public void setPostcode(Postcode postcode) {
        this.postcode = postcode;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public Order getReferenceOrder() {
        return referenceOrder;
    }

    public void setReferenceOrder(Order referenceOrder) {
        this.referenceOrder = referenceOrder;
    }

    @SuppressLint("DefaultLocale")
    public String getDeliveryFormatted() {
        if (getDelivery() < 0) {
            return "";
        }
        return String.format(TIME_FORMAT, getDelivery() / 60, getDelivery() % 60);
    }

    public int getTotal() {
        int subtotal = getSubtotal();
        if (getRate() != null) {
            subtotal += getRate().getCosts();
        }
        if (getRefundRate() != null) {
            subtotal -= getRefundRate().getCosts();
        }

        return subtotal;
    }

    public int getSubtotal() {
        int subtotal = 0;
        for (Position p : getPositions()) {
            subtotal += p.getTotal();
        }
        return subtotal;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRefundRate(Rate refundRate) {
        this.refundRate = refundRate;
    }

    public Rate getRefundRate() {
        return refundRate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getMethodId() {
        return methodId;
    }

    public void setMethodId(int methodId) {
        this.methodId = methodId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Position> positions) {
        this.positions = positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum State {
        NEW("NEW"),
        PENDING_PAYMENT("PENDING_PAYMENT"),
        SEEN("SEEN"),
        PROCESSING("PROCESSING"),
        DELIVERING("DELIVERING"),
        CANCELED("CANCELED"),
        CLOSED("CLOSED");

        State(String state) {
        }

        public String getName() {
            switch (this) {
                case NEW:
                    return "Neu";
                case PENDING_PAYMENT:
                    return "Zahlung ausstehend";
                case PROCESSING:
                    return "Zubereitung";
                case DELIVERING:
                    return "Lieferung";
                case CANCELED:
                    return "Storniert";
                case SEEN:
                    return "Gesehen";
                default:
                    return "Abgeschlossen";
            }
        }
    }
}
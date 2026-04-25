package models;

import java.time.LocalDate;
import enums.PaymentMethod;
import exceptions.InvalidDataException;

public class Invoice {
    private int invoiceId;
    private Reservation reservation;
    private double totalAmount;
    private PaymentMethod paymentMethod;
    private LocalDate paymentDate;

    public Invoice() {
    }

    public Invoice(int invoiceId, Reservation reservation, double totalAmount,
                   PaymentMethod paymentMethod, LocalDate paymentDate) {
        setInvoiceId(invoiceId);
        setReservation(reservation);
        setTotalAmount(totalAmount);
        setPaymentMethod(paymentMethod);
        setPaymentDate(paymentDate);
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        if (invoiceId <= 0) {
            throw new InvalidDataException("Invoice ID must be greater than 0.");
        }
        this.invoiceId = invoiceId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        if (reservation == null) {
            throw new InvalidDataException("Reservation cannot be null.");
        }
        this.reservation = reservation;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        if (totalAmount < 0) {
            throw new InvalidDataException("Total amount cannot be negative.");
        }
        this.totalAmount = totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new InvalidDataException("Payment method cannot be null.");
        }
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        if (paymentDate == null) {
            throw new InvalidDataException("Payment date cannot be null.");
        }
        this.paymentDate = paymentDate;
    }

    public void printInvoiceInfo() {
        System.out.println("Invoice Information:");
        System.out.println("Invoice ID: " + invoiceId);
        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println("Guest Username: " + reservation.getGuest().getUsername());
        System.out.println("Total Amount: " + totalAmount);
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Payment Date: " + paymentDate);
    }
}
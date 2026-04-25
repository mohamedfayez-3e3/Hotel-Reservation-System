package interfaces;

import enums.PaymentMethod;

public interface Payable {
    void checkout(int reservationId, PaymentMethod paymentMethod);
}
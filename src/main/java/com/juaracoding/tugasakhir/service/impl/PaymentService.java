package com.juaracoding.tugasakhir.service.impl;

import com.juaracoding.tugasakhir.dto.validasi.BookingRequestDTO;
import com.juaracoding.tugasakhir.enums.Currency;
import com.juaracoding.tugasakhir.enums.PaymentMethod;
import com.juaracoding.tugasakhir.enums.PaymentStatus;
import com.juaracoding.tugasakhir.model.Booking;
import com.juaracoding.tugasakhir.model.Payment;
import com.juaracoding.tugasakhir.repository.PaymentRepository;
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

//    @Autowired
//    private Config midtransConfig;

    @Transactional
    public Payment createPendingPayment(Booking booking, BookingRequestDTO bookingRequestDTO) {
        Payment payment = Payment.builder()
                .booking(booking)
                .paymentStatus(PaymentStatus.PENDING) // Set status PENDING saat reservasi dibuat
                .paymentMethod(null) // Biarkan null, karena user akan memilih nanti
                .totalPrice(bookingRequestDTO.getTotalPrice()) // Gunakan harga dari reservasi
                .currency(Currency.IDR) // Default ke IDR, bisa diubah sesuai kebutuhan
                .build();

        return paymentRepository.save(payment);
    }

    public String createTransaction(Long bookingId, String email, Double totalPrice) {
//        MidtransSnapApi snapApi = new MidtransSnapApi(midtransConfig);
        Config coreApiConfigOptions = Config.builder()
                .setServerKey("SB-Mid-server-lNuXHFIfHeBizgYcY41-lPL_")
                .setClientKey("SB-Mid-client-NoPw1aIkOsI_LrGJ")
                .setIsProduction(false)
                .build();
        MidtransSnapApi snapApi = new ConfigFactory(coreApiConfigOptions).getSnapApi();

        // Membuat parameter transaksi
        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", "ORDER-" + bookingId);
        transactionDetails.put("gross_amount", totalPrice);

        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("email", email);

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("transaction_details", transactionDetails);
        transaction.put("customer_details", customerDetails);

        try {
            JSONObject snapResponse = snapApi.createTransaction(transaction);
            return snapResponse.getString("token"); // Ambil Snap Token dari response
        } catch (MidtransError e) {
            e.printStackTrace();
            return null;
        }
    }
}

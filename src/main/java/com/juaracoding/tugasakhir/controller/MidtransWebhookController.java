package com.juaracoding.tugasakhir.controller;

import com.juaracoding.tugasakhir.service.impl.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class MidtransWebhookController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleMidtransWebhook(@RequestBody Map<String, Object> payload) {
            Long orderId = Long.parseLong((String) payload.get("order_id"));
            String transactionStatus = (String) payload.get("transaction_status");
            String paymenMethod = (String) payload.get("payment_type");

            // Update status pembayaran di database
            paymentService.updatePaymentStatus(orderId, transactionStatus, paymenMethod);
        return ResponseEntity.ok("Payment status updated.");
    }

}

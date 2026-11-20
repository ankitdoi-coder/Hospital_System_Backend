package com.ankit.HealthCare_Backend.Payments;

import com.razorpay.RazorpayClient;
import com.razorpay.Order;
import com.razorpay.Utils;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RazorpayService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    public Map<String, Object> createOrder(int amountInRupees, String currency) throws RazorpayException {
        log.info("Creating Razorpay order for amount: {} {}, currency: {}", amountInRupees, "INR", currency);
        //client means our account of razor pay 
        RazorpayClient client = new RazorpayClient(key, secret);

        JSONObject orderRequest = new JSONObject();
        //convert rupee in Paise
        int amountPaise = amountInRupees * 100; // Razorpay accepts amount in paise
        orderRequest.put("amount", amountPaise);
        orderRequest.put("currency", currency == null ? "INR" : currency);
        orderRequest.put("receipt", "rcpt_" + System.currentTimeMillis());
        orderRequest.put("payment_capture", 1);
        Order order = client.orders.create(orderRequest);

        Map<String, Object> resp = new HashMap<>();
        resp.put("id", order.get("id"));
        resp.put("amount", order.get("amount"));
        resp.put("currency", order.get("currency"));
        resp.put("status", order.get("status"));
        // expose public key to frontend to initialize Razorpay checkout
        resp.put("key", key);

        log.debug("Razorpay order created: {}", resp);
        return resp;
    }

    public void verifySignature(Map<String, String> params) throws RazorpayException {
        log.info("Verifying Razorpay signature for payload keys: {}", params.keySet());
        JSONObject json = new JSONObject(params);
        Utils.verifyPaymentSignature(json, secret);
        log.info("Razorpay signature verified successfully");
    }
}

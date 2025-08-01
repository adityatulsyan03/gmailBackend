package com.example.gmailDemo.controller;

import com.example.gmailDemo.model.EmailRequest;
import com.example.gmailDemo.service.GmailService;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import java.util.Base64;

@RestController
@RequestMapping("/api/gmail")
@CrossOrigin(origins = "*")
public class GmailController {

    @Autowired
    private GmailService gmailService;

    @GetMapping("/auth")
    public ResponseEntity<Map<String, String>> authenticate() {
        try {
            String authUrl = gmailService.getAuthorizationUrl();
            return ResponseEntity.ok(Map.of("auth_url", authUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/callback")
    public String handleCallback(@RequestParam String code) {
        try {
            String accessToken = gmailService.exchangeCodeForToken(code);
            return "<html><script>window.opener.postMessage({access_token:'" + accessToken + "'},'*');window.close();</script></html>";
        } catch (Exception e) {
            return "<html><script>window.opener.postMessage({error:'" + e.getMessage() + "'},'*');window.close();</script></html>";
        }
    }

    @GetMapping("/list")
    public List<Map<String, String>> listEmails() throws Exception {
        Gmail gmail = gmailService.getGmailService();
        ListMessagesResponse response = gmail.users().messages().list("me").setMaxResults(10L).execute();
        List<Map<String, String>> emails = new ArrayList<>();

        if (response.getMessages() == null) return emails;
        for (Message message : response.getMessages()) {
            Message fullMessage = gmail.users().messages().get("me", message.getId()).execute();
            Map<String, String> emailInfo = new HashMap<>();
            emailInfo.put("id", fullMessage.getId());
            for (MessagePartHeader header : fullMessage.getPayload().getHeaders()) {
                if ("Subject".equalsIgnoreCase(header.getName())) {
                    emailInfo.put("subject", header.getValue());
                } else if ("From".equalsIgnoreCase(header.getName())) {
                    emailInfo.put("from", header.getValue());
                } else if ("Date".equalsIgnoreCase(header.getName())) {
                    emailInfo.put("date", header.getValue());
                }
            }
            
            // Extract body content
            String body = extractBody(fullMessage.getPayload());
            emailInfo.put("body", body != null ? body : "No body content");
            
            System.out.println("Email Info: " + emailInfo);
            
            emails.add(emailInfo);
        }
        return emails;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest req) throws Exception {
        Gmail gmail = gmailService.getGmailService();
        MimeMessage message = createEmail(req.getTo(), "me", req.getSubject(), req.getBody());
        Message msg = createMessageWithEmail(message);
        gmail.users().messages().send("me", msg).execute();
        return ResponseEntity.ok(Map.of("status", "sent"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        try {
            gmailService.clearCredentials();
            return ResponseEntity.ok(Map.of("status", "logged out"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Utility methods
    private MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
    
    private String extractBody(MessagePart payload) {
        if (payload.getBody() != null && payload.getBody().getData() != null) {
            return new String(Base64.getUrlDecoder().decode(payload.getBody().getData()));
        }
        if (payload.getParts() != null) {
            for (MessagePart part : payload.getParts()) {
                String body = extractBody(part);
                if (body != null) return body;
            }
        }
        return null;
    }
}
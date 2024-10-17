package com.example.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.InvalidCredentialsException;
import com.example.exception.MessageValidationException;
import com.example.exception.UserNotFoundException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account registeredAccount = accountService.register(account);
        return ResponseEntity.ok().body(registeredAccount);
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account loggedInAccount = accountService.login(account);
        return ResponseEntity.ok().body(loggedInAccount);
    }

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {

        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.ok(createdMessage);

    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages() {

        return ResponseEntity.ok().body(messageService.getAllMessages());
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessameById(@PathVariable int messageId) {
        return ResponseEntity.ok().body(messageService.getMessageById(messageId));
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        int result = messageService.deleteMessageById(messageId);
        if (result == 1) {
            return ResponseEntity.ok().body(1);
        } else {
            return ResponseEntity.ok().body(null);
        }
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@RequestBody Map<String, String> requestBody,
            @PathVariable int messageId) {
        String messageText = requestBody.get("messageText");

        int rowsAffected = messageService.updateMessageById(messageId, messageText);

        if (rowsAffected == 1) {
            return ResponseEntity.ok().body(1);
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByUserId(@PathVariable int accountId) {
        return ResponseEntity.ok().body(messageService.getAllMessagesByUserId(accountId));
    }

    @ExceptionHandler({
            UsernameAlreadyExistsException.class,
            InvalidCredentialsException.class,
            UserNotFoundException.class,
            MessageValidationException.class
    })
    public ResponseEntity<String> handleIllegalArgumentException(RuntimeException e) {
        if (e instanceof UsernameAlreadyExistsException) {
            return ResponseEntity.status(409).body(e.getMessage());
        } else if (e instanceof InvalidCredentialsException) {
            return ResponseEntity.status(401).body(e.getMessage());
        } else if (e instanceof UserNotFoundException || e instanceof MessageValidationException) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } else {
            return ResponseEntity.badRequest().body("Unexpected error occurred.");
        }

    }
}

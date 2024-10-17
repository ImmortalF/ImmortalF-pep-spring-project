package com.example.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.MessageValidationException;
import com.example.exception.UserNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {

        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()
                || message.getMessageText().length() > 255) {
            throw new MessageValidationException("Message text must be provided and cannot exceed 255 characters.");
        }
        if (!accountRepository.existsById(message.getPostedBy()))
            throw new UserNotFoundException("Posted by user does not exist.");

        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int id) {
        return messageRepository.findById(id).orElse(null);
    }

    public int deleteMessageById(int id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public int updateMessageById(int id, String newMessageText) {
        {
            if (!messageRepository.existsById(id)) {
                return 0;
            }

            if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
                return 0;
            }

            Message message = messageRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Message not found"));
            message.setMessageText(newMessageText);
            messageRepository.save(message);

            return 1;
        }

    }

    public List<Message> getAllMessagesByUserId(int id) {
        return messageRepository.findAllByPostedBy(id);
    }

}

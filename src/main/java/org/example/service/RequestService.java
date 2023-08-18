package org.example.service;

import com.pengrad.telegrambot.model.Update;
import org.example.entity.Request;
import org.example.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;

@Service
public class RequestService {
    private final RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public void create(Update update, Matcher matcher) {

    }

    public List<Request> requestList() {
        return requestRepository.findAll();
    }
}
package com.example.bitwebworker;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Component
public class TextQueueListener {

    @Autowired
    private ObjectMapper objectMapper;

    private final UploadMetadataRepository metadataRepository;

    public TextQueueListener(UploadMetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    @RabbitListener(queues = "textQueue")
    public void receiveMessage(String message) {

        TextUploadMessage textUploadMessage;
        try {
            textUploadMessage = objectMapper.readValue(message, TextUploadMessage.class);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Received message from textQueue: " + textUploadMessage.getUuid() + " " + textUploadMessage.getFileName());

        String[] words = getWords(textUploadMessage.getContent());
        Map<String, Integer> wordFrequency = getWordFrequency(words);

        String result;
        try {
            result = objectMapper.writeValueAsString(wordFrequency);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        UploadMetadata uploadMetadata = new UploadMetadata(textUploadMessage.getUuid(), textUploadMessage.getFileName(), "done", result);
        metadataRepository.save(uploadMetadata);
    }

    private static String[] getWords(String text) {
        return text.strip().split("\\s");
    }

    private static Map<String, Integer> getWordFrequency(String[] words) {
        Map<String, Integer> wordFrequency = new HashMap<>();
        for (String word : words) {
            if (wordFrequency.containsKey(word)) {
                int oldFreq = wordFrequency.get(word);
                oldFreq += 1;
                wordFrequency.put(word, oldFreq);
            } else {
                wordFrequency.put(word, 1);
            }
        }
        return wordFrequency;
    }
}

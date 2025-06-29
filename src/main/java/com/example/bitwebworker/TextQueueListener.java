package com.example.bitwebworker;

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

        TextUploadMessage textUploadMessage = ExceptionHandler.runUnchecked(() -> objectMapper.readValue(message, TextUploadMessage.class));

        System.out.println("Received message from textQueue: " + textUploadMessage.getUuid() + " " + textUploadMessage.getFileName());

        String[] words = getWords(textUploadMessage.getContent());
        Map<String, Integer> wordFrequency = getWordFrequency(words);

        String result = ExceptionHandler.runUnchecked(() -> objectMapper.writeValueAsString(wordFrequency));

        metadataRepository.save(new UploadMetadata(textUploadMessage.getUuid(), textUploadMessage.getFileName(), "done", result));
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

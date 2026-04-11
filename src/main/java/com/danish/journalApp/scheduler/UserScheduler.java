package com.danish.journalApp.scheduler;

import com.danish.journalApp.config.GeminiConfig;
import com.danish.journalApp.entity.JournalEntry;
import com.danish.journalApp.entity.User;
import com.danish.journalApp.enums.JournalType;
import com.danish.journalApp.repository.UserRepositoryImpl;
import com.danish.journalApp.services.EmailService;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    EmailService emailService;
    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private GeminiConfig geminiConfig;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSaMail() {
        List<User> users = userRepository.getUserForSA();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<JournalEntry> weeklyJournals = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .filter(x -> x.getJournalType().equals(JournalType.Productivity))
                    .collect(Collectors.toList());

            String journalText = weeklyJournals.stream()
                    .map(x -> "Date: " + x.getDate() + "\n" +
                            "Title: " + x.getTitle() + "\n" +
                            "Content: " + x.getContent() + "\n" +
                            "Type: " + x.getJournalType() + "\n" +
                            "---")
                    .collect(Collectors.joining("\n"));

            GenerateContentResponse response =
                    geminiConfig.client.models.generateContent("gemini-2.5-flash", journalText, geminiConfig.instruction);

            emailService.sendMail(user.getEmail(),"Weekly Productivity Report", response.text());
        }
    }
}

package com.telegrambot.marketplace.service;

import com.telegrambot.marketplace.dto.Answer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@AllArgsConstructor
@NoArgsConstructor
public class SendMessageBuilder {
    private SendMessage sendMessage;

    public SendMessageBuilder chatId(Long chatId) {
        this.sendMessage.setChatId(chatId);
        return this;
    }

    public SendMessageBuilder message(String message) {
        this.sendMessage.setText(message);
        return this;
    }

    public Answer build() throws Exception {
        if(sendMessage.getChatId() == null)
            throw new Exception("Id must be not null");

        Answer answer = new Answer();
        answer.setBotApiMethod(sendMessage);

        return answer;
    }
}

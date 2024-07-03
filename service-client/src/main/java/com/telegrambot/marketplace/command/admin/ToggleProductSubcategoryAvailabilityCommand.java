package com.telegrambot.marketplace.command.admin;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.ProductSubcategoryService;
import com.telegrambot.marketplace.service.handler.CommandHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ToggleProductSubcategoryAvailabilityCommand implements Command {

    private final ProductSubcategoryService productSubcategoryService;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/togglesubcategory";
    }

    @Override
    @SneakyThrows
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        if (!UserType.ADMIN.equals(user.getPermissions())) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("You do not have permission to toggle subcategory availability.")
                    .build();
        }

        String[] args = update.getArgs().getFirst().split(" ");
        if (args.length < 1) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Usage: /togglesubcategory <subcategory>")
                    .build();
        }

        String subcategoryName = args[0];
        ProductSubcategory subcategory = productSubcategoryService.findByName(subcategoryName);
        if (subcategoryName == null) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Subcategory not found.")
                    .build();
        }

        subcategory.setAllowed(!subcategory.isAllowed());
        productSubcategoryService.save(subcategory);

        String status = subcategory.isAllowed() ? "available" : "unavailable";
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Subcategory " + subcategoryName + " is now " + status + ".")
                .build();
    }
}

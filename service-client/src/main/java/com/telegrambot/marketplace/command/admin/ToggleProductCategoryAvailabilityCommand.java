package com.telegrambot.marketplace.command.admin;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.ProductCategoryService;
import com.telegrambot.marketplace.service.handler.CommandHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ToggleProductCategoryAvailabilityCommand implements Command {

    private final ProductCategoryService productCategoryService;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/togglecategory";
    }

    @Override
    @SneakyThrows
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        if (!UserType.ADMIN.equals(user.getPermissions())) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("You do not have permission to toggle category availability.")
                    .build();
        }

        String[] args = update.getArgs().getFirst().split(" ");
        if (args.length < 1) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Usage: /togglecategory <category>")
                    .build();
        }

        String categoryName = args[0];
        ProductCategory category = productCategoryService.findByName(categoryName);
        if (category == null) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Category not found.")
                    .build();
        }

        category.setAllowed(!category.isAllowed());
        productCategoryService.save(category);

        String status = category.isAllowed() ? "available" : "unavailable";
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Category " + categoryName + " is now " + status + ".")
                .build();
    }
}

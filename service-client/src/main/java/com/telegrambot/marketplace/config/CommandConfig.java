package com.telegrambot.marketplace.config;

import com.telegrambot.marketplace.command.admin.StatisticsCommand;
import com.telegrambot.marketplace.command.admin.add.AddCityCommand;
import com.telegrambot.marketplace.command.admin.add.AddCountryCommand;
import com.telegrambot.marketplace.command.admin.add.AddDistrictCommand;
import com.telegrambot.marketplace.command.admin.add.AddProductCategoryCommand;
import com.telegrambot.marketplace.command.admin.add.AddProductCommand;
import com.telegrambot.marketplace.command.admin.add.AddProductSubcategoryCommand;
import com.telegrambot.marketplace.command.admin.availability.ToggleCityAvailabilityCommand;
import com.telegrambot.marketplace.command.admin.availability.ToggleCountryAvailabilityCommand;
import com.telegrambot.marketplace.command.admin.availability.ToggleDistrictAvailabilityCommand;
import com.telegrambot.marketplace.command.admin.availability.ToggleProductAvailabilityCommand;
import com.telegrambot.marketplace.command.admin.availability.ToggleProductCategoryAvailabilityCommand;
import com.telegrambot.marketplace.command.admin.availability.ToggleProductSubcategoryAvailabilityCommand;
import com.telegrambot.marketplace.command.user.basket.BasketCommand;
import com.telegrambot.marketplace.command.user.basket.BuyBasketCommand;
import com.telegrambot.marketplace.command.user.basket.DeleteOrderCommand;
import com.telegrambot.marketplace.command.user.basket.DeleteOrdersCommand;
import com.telegrambot.marketplace.command.user.basket.OrdersCommand;
import com.telegrambot.marketplace.command.user.pathway.CategoryCommand;
import com.telegrambot.marketplace.command.user.pathway.CityCommand;
import com.telegrambot.marketplace.command.user.pathway.CountryCommand;
import com.telegrambot.marketplace.command.user.pathway.DistrictCommand;
import com.telegrambot.marketplace.command.user.pathway.OrderCommand;
import com.telegrambot.marketplace.command.user.pathway.ProductCommand;
import com.telegrambot.marketplace.command.user.pathway.StartCommand;
import com.telegrambot.marketplace.command.user.pathway.SubcategoryCommand;
import com.telegrambot.marketplace.command.user.profile.PasswordCommand;
import com.telegrambot.marketplace.command.user.profile.ProfileInfoViewCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfig {

    @Bean
    public CommandHandler commandHandler(
            final AddCountryCommand addCountryCommand,
            final AddCityCommand addCityCommand,
            final AddDistrictCommand addDistrictCommand,
            final AddProductCategoryCommand addProductCategoryAdminCommand,
            final AddProductSubcategoryCommand addProductSubcategoryAdminCommand,
            final AddProductCommand addProductCommand,
            final ToggleCountryAvailabilityCommand toggleCountryAvailabilityCommand,
            final ToggleCityAvailabilityCommand toggleCityAvailabilityCommand,
            final ToggleDistrictAvailabilityCommand toggleDistrictAvailabilityCommand,
            final ToggleProductCategoryAvailabilityCommand toggleProductCategoryAvailabilityCommand,
            final ToggleProductSubcategoryAvailabilityCommand toggleProductSubcategoryAvailabilityCommand,
            final ToggleProductAvailabilityCommand toggleProductAvailabilityCommand,
            final StatisticsCommand statisticsCommand,
            final StartCommand startCommand,
            final BasketCommand basketCommand,
            final BuyBasketCommand buyBasketCommand,
            final DeleteOrderCommand deleteOrderCommand,
            final DeleteOrdersCommand deleteOrdersCommand,
            final OrdersCommand ordersCommand,
            final CountryCommand countryCommand,
            final CityCommand cityCommand,
            final DistrictCommand districtCommand,
            final CategoryCommand categoryCommand,
            final SubcategoryCommand subcategoryCommand,
            final ProductCommand productCommand,
            final OrderCommand orderCommand,
            final PasswordCommand passwordCommand,
            final ProfileInfoViewCommand profileInfoViewCommand) {

        CommandHandler handler = new CommandHandler();

        // Register user commands
        handler.registerUserCommand("/start", startCommand);
        handler.registerUserCommand("/basket_", basketCommand);
        handler.registerUserCommand("/buyBasket_", buyBasketCommand);
        handler.registerUserCommand("/delete_order_", deleteOrderCommand);
        handler.registerUserCommand("/delete_orders_", deleteOrdersCommand);
        handler.registerUserCommand("/orders_", ordersCommand);
        handler.registerUserCommand("/category_", categoryCommand);
        handler.registerUserCommand("/subcategory_", subcategoryCommand);
        handler.registerUserCommand("/subcategory_", productCommand);
        handler.registerUserCommand("/country_", countryCommand);
        handler.registerUserCommand("/city_", cityCommand);
        handler.registerUserCommand("/district_", districtCommand);
        handler.registerUserCommand("TEXT", orderCommand);
        handler.registerUserCommand("TEXT", passwordCommand);
        handler.registerUserCommand("/profile_", profileInfoViewCommand);

        // Register admin commands
        handler.registerAdminCommand("/admin_add_product_category_", addProductCategoryAdminCommand);
        handler.registerAdminCommand("/admin_add_product_subcategory_", addProductSubcategoryAdminCommand);
        handler.registerAdminCommand("/admin_add_product_", addProductCommand);
        handler.registerAdminCommand("/admin_add_country_", addCountryCommand);
        handler.registerAdminCommand("/admin_add_city_", addCityCommand);
        handler.registerAdminCommand("/admin_add_district_", addDistrictCommand);
        handler.registerAdminCommand("/admin_toggle_country_availability_", toggleCountryAvailabilityCommand);
        handler.registerAdminCommand("/admin_toggle_city_availability_", toggleCityAvailabilityCommand);
        handler.registerAdminCommand("/admin_toggle_district_availability_", toggleDistrictAvailabilityCommand);
        handler.registerAdminCommand("/admin_toggle_category_availability_",
                toggleProductCategoryAvailabilityCommand);
        handler.registerAdminCommand("/admin_toggle_subcategory_availability_",
                toggleProductSubcategoryAvailabilityCommand);
        handler.registerAdminCommand("/admin_toggle_product_availability_", toggleProductAvailabilityCommand);
        handler.registerAdminCommand("/admin_statistics_", statisticsCommand);

        // Add other commands as needed

        return handler;
    }
}

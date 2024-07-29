Telegram Bot for Online Shop
This project is a Telegram bot designed for managing an online shop. It uses various technologies and frameworks, including Spring Boot, JPA, Hibernate, PostgreSQL, and integrates with the Telegram API. The bot allows users to interact with an online marketplace, manage their orders, view products, and more.

Table of Contents
Getting Started
Prerequisites
Installation
Configuration
Usage
Project Structure
Contributing
License
Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

Prerequisites
Java 11 or higher
Docker (optional, for running PostgreSQL)
PostgreSQL
Installation
Clone the repository:

sh
Copy code
git clone https://github.com/Khvn001/tg_bot.git
cd tg_bot
Build the project using Gradle:

sh
Copy code
./gradlew build
Run PostgreSQL using Docker:

sh
Copy code
docker run --name tg_bot_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=postgres -p 5432:5432 -d postgres
Apply database migrations:

sh
Copy code
./gradlew flywayMigrate
Configuration
Configure the bot by setting the necessary environment variables or by editing the application.yml file located in service-client/src/main/resources.

BOT_NAME: The name of your Telegram bot.
BOT_TOKEN: The token provided by BotFather.
POSTGRES_JDBC_URL: The JDBC URL for your PostgreSQL database.
POSTGRES_USERNAME: The username for your PostgreSQL database.
POSTGRES_PASSWORD: The password for your PostgreSQL database.
AWS_S3_ACCESS_KEY: The access key for your AWS S3 bucket.
AWS_S3_SECRET_KEY: The secret key for your AWS S3 bucket.
Example application.yml snippet:

yaml
Copy code
spring:
datasource:
url: jdbc:postgresql://localhost:5432/postgres
username: postgres
password: postgres
bot:
name: BotName
token: BotToken
aws:
s3:
accessKey: AWS_S3_ACCESS_KEY
secretKey: AWS_S3_SECRET_KEY
Usage
Run the application:

sh
Copy code
./gradlew bootRun
The bot will start and connect to Telegram. You can interact with it using the Telegram app.

Project Structure
The project is divided into several packages, each responsible for different functionalities of the bot.

Core Packages
com.telegrambot.marketplace: Contains the main application class and configuration files.
com.telegrambot.marketplace.command: Handles various bot commands and updates.
com.telegrambot.marketplace.config: Contains configuration classes for the bot, JPA, and other services.
com.telegrambot.marketplace.dto: Data transfer objects used for communication between different layers of the application.
com.telegrambot.marketplace.entity: JPA entities representing the database structure.
com.telegrambot.marketplace.repository: Spring Data JPA repositories for accessing the database.
com.telegrambot.marketplace.service: Service layer classes for business logic and interaction with repositories.
Main Classes and Files
TelegramBotApplication.java: The main class for starting the Spring Boot application.
BotConfig.java: Configuration class for the bot's settings.
CommandConfig.java: Configuration class for registering commands.
JpaConfig.java: Configuration class for JPA and Hibernate settings.
application.yml: Configuration file for the application properties.
Contributing
Contributions are welcome! Please read the CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

License
This project is licensed under the MIT License - see the LICENSE file for details.


Developers: 
Pessy Israeli
Shira Giladi

A full-stack web platform (React + Spring Boot + PostgreSQL) for interactive driving theory learning. The system combines quizzes, trivia, and memory games with leaderboards and progress tracking to create a fun, gamified study experience.
Here you can find a User Guide – Functional Description:


Project Structure:
frontend    # React
backend     # Spring Boot Java
README.md   #  Installation instructur

IDE- Visual Studio Code- https://code.visualstudio.com

For Frontend:
Node.js + npm- https://nodejs.org/en

For Backend:
Java (JDK 17 or higher)
Maven (version 3.6 or higher)
PostgreSQL (version 12 or higher)

Follow these steps to run the website locally on your computer:

1. Create a folder on your computer
2. Open CMD from the folder you opened
3. Link to clone repository: https://github.com/shiragildi1/EZDrive.git

Add necessary files:
1. add file named ".env" in the Client
   In the file put: REACT_APP_GOOGLE_CLIENT_ID=250713696752-4k026jggdvnitarn3efqudooqignqc68.apps.googleusercontent.com 
2. add file named ".env" in teh Server
   In the file put: GOOGLE_CLIENT_ID="250713696752-4k026jggdvnitarn3efqudooqignqc68.apps.googleusercontent.com
3. add an API key in the ChatBotQuestionService.java file in order to receive proper responses from our bot.

How to run front:
1. Open integreted terminal
1. Run: npm install
2. Run: npm start
   
How to run back:
Run DB:
1. In file application.properties add your password hear: spring.datasource.password=### (line 8)
1. Open postgreSql
3. cd Project_EZDrive/Server-> Right click on EzDriveApplication.java -> Run Java
4. The tables will be refreshed and created in the DB.
   
The backend API will be available at: http://localhost:8080

Application URLs:
Frontend: http://localhost:3000
API (Swagger): http://localhost:8080/swagger-ui.html

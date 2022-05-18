# AuctionApp
Auction app for AtlantBH internship. \
AuctionApp is a web application where you can look through available products and, if registered, bid for products of interest or even post them.
***
App: https://auction-abh-app.herokuapp.com/
***

## Structure and requirements
Backend directory contains Rest API created as **Spring Boot** project. For it to work correctly it needs Environment variables set on your OS. They consist of Facebook and Google OAuth2 clients, Mail, Postgres database and link to frontend and itself. \
Install git, Java Development Kit, Maven and PostgreSQL.

Frontend directory contains **ReactJs** project. For this application you need git and Node installed. Create .env file in source directory and define REACT_APP_API_URL environment variable that will enable connection to server host. 


## How to run locally
Navigate to the backend folder and run **mvn spring-boot:run**. Originally application would be available on http://localhost:8080, but you can set up application.yaml file configuration according to your needs.

Navigate to the frontend folder and run **npm install**. Run **npm start** and it will be available on http://localhost:3000 in a browser, unless you specify a different port.


## Deployment
Do not forget to set everything up, as mentioned above, prior to deployment.

* create 2 apps on Heroku;
* set buildpack for subdirectories and then for java and react in respective apps;
* create Heroku postgres database on server side;
* create config variables in place of environmen variables in both apps;
* deploy;

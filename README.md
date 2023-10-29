# Readers' Paradise: Your Bookish Heaven 🌄
- Welcome to Readers' Paradise - Your Bookish Heaven! 📚🏰
- This application is a library and order management system for a bookstore.

  [Why Focus on This Application?](#why-focus-on-this-application?)


## Project Technology Stack: 💻🔧📚

* Programming Language : ***Java***
* Framework: Spring Boot
* Spring Security and JWT: For authentication and security
* Springdoc OpenAPI: For OpenAPI documentation
* Logging System: Log4j2
* Testing: JUnit
* Database: MySQL
* Liquibase: For database version management
* Lombok: For boilerplate code generation
* MapStruct: For mapper generation
* Email: ***javax.mail***
* Testcontainers: For integration tests with the H2 test database

## What is it for? 🔍
This application is designed for bookstores or libraries to manage their collection of books, categories, and user orders.

## Why Focus on This Application?
This application is the perfect tool for bookstore or library owners who want a simple and effective way to manage their book collection,
user orders, and shopping cart inventory. It makes the process of purchasing and searching for books more convenient and organized for users.
Therefore, this application provides many useful features for managing the book business and makes it easier for users to search for and order books.

### Users can utilize this application for: 📝
1. Retrieving a list of all library books with pagination support.
2. Obtaining detailed book information using its unique identifier.
3. Searching for books by title.
4. Searching for books based on various parameters such as title, author, or genre.
5. Getting a list of all available book categories.
6. Accessing detailed category information using its unique identifier.
7. Retrieving a list of books belonging to a specific category.
8. Creating a new order.
9. Receiving a list of all items in a specific order.
10. Retrieving details of a specific order item using order and item identifiers.
11. Adding a new product to a user's shopping cart.
12. Obtaining the contents of a user's shopping cart.
13. Updating the quantity of a product in the shopping cart.
14. Removing a product from the shopping cart.
15. Searching for books by title, author, or ISBN.
16. Filtering books by categories.

# User Registration : 📌 
1. Use the POST method:
```code
/api/auth/register
```
Example : 
```json
{
"email": "john.doe@example.com",
"password": "securePassword123",
"repeatPassword": "securePassword123",
"firstName": "John",
"lastName": "Doe",
"shippingAddress": "123 Main St, City, Country"
}
```
The expected response will include the user's identifier and other user-related information.
<img width="1423" alt="registration" src="https://github.com/RostyslavOnysh/book-store/assets/98691406/3af7b34d-3eca-4471-9561-6fa3a895d299">


2. User Authentication: 📌 
* Use the POST method:
```code 
/api/auth/login.
```
Example :
```json
{
"email": "john.doe@example.com",
"password": "securePassword123"
}
```
***The expected response will contain an access token that needs to be used for further requests.***
<img width="1370" alt="login" src="https://github.com/RostyslavOnysh/book-store/assets/98691406/505fb9c0-ba2f-47d9-9cb5-b5260731213e">

## Example Requests:

**Request to search for books by an author:** 📌 
* Method: **GET**
* URL: [http://localhost:8088/api/books/search?author=Taras%20Hryhorovych%20Shevchenko](http://localhost:8088/api/books/search?author=Taras%20Hryhorovych%20Shevchenko)
* In this example, you are searching for books by the author "Taras Hryhorovych Shevchenko." The response to this request will contain a list of books that match your author query.
<img width="1370" alt="by author" src="https://github.com/RostyslavOnysh/book-store/assets/98691406/170f49b8-edb4-40e1-bc12-1d281db8e725">

**Request to view all categories (GET: /api/categories):**  📌 
* Method: **GET**
* URL: [http://localhost:8088/api/categories](http://localhost:8088/api/categories)
* This request allows you to retrieve a list of all book categories in your application.

**Request to view a specific category by its unique identifier (GET: /api/categories/{id}):**  📌 
* Method: **GET**
* URL: [http://localhost:8088/api/categories/{id}](http://localhost:8088/api/categories/{id})
* You can specify the unique identifier of the category in the URL, and this request will return detailed information about that category.

**Request to get a list of books in a specific category (GET: /api/categories/{id}/books):**  📌 
* Method: **GET**
* URL: [http://localhost:8088/api/categories/{id}/books](http://localhost:8088/api/categories/{id}/books)
* In this example, you specify the unique identifier of the category in the URL, and this request will return a list of books that belong to that category.

**Request to get a user's cart (GET: /api/cart):**  📌 
* Method: **GET**
* URL: [http://localhost:8088/api/cart](http://localhost:8088/api/cart)
* This request allows you to retrieve the user's shopping cart along with the list of items in the cart. It returns detailed information about the cart and the items in it.

**Request to add an item to the user's cart (POST: /api/cart):**  📌 
* Method: **POST**
* URL: [http://localhost:8088/api/cart](http://localhost:8088/api/cart)
* Example JSON request body:
```json
{
  "bookId": 2,
  "quantity": 5
}
```
* This request adds the specified item in the specified quantity to the user's cart.
  <img width="1370" alt="Screenshot 2023-10-29 at 15 22 03" src="https://github.com/RostyslavOnysh/book-store/assets/98691406/c7d6639f-f4de-45fc-8fd9-e963cae9879f">


**Request to update the quantity of an item in the user's cart (PUT: /api/cart/cart-items/{cartItemId}):** 📌 
* Method: PUT
* URL: http://localhost:8088/api/cart/cart-items/{cartItemId}
* Example JSON request body:
```json
{
"quantity": 10
}
```
* This request updates the quantity of a specific item in the user's cart.
<img width="1370" alt="Screenshot 2023-10-29 at 15 23 58" src="https://github.com/RostyslavOnysh/book-store/assets/98691406/096183a7-ce88-48c1-90b4-c4a4bd84f4b2">


**Request to remove an item from the user's cart (DELETE: /api/cart/cart-items/{cartItemId}):** 📌 
* Method: DELETE
* URL: http://localhost:8088/api/cart/cart-items/{cartItemId}
* This request deletes a specific item from the user's cart based on its unique identifier



# Access to Additional Features for ADMIN: ⚠️
If a user has the role ADMIN, they can utilize additional functionalities such as creating a new book, editing,
and deleting an existing book. 
These requests will have endpoints /api/books/ and /api/books/{id} for creating, editing, and deleting, respectively.
* Utilizing the Access Token:
* After successful authentication, you will receive an access token.
* This access token should be included in the header of each request that requires authentication.
* It will be used to verify the user's rights and grant access to specific features.

# Key Features 🔑
1. User Authentication and Authorization:
* The project provides user authentication based on their email and password using JSON Web Tokens (JWT).
* Spring Security is used to configure application security and protect resources.
2. User Registration:
* Users can register, providing information including email and password.
3. JWT Token Generation
* JWT tokens are used for secure user authentication and access control to protected resources.
4. Email Validation:
* Implemented email format validation and verification for user emails.
5. Logging:
* Log4j2 is used for logging actions and events within the application.
6. Springdoc OpenAPI:
* Springdoc OpenAPI is used for automated API documentation generation.
7. Password Encryption:
* BCrypt is used for secure storage and comparison of user passwords.
8. Security Management:
* Security configuration using Spring Security to ensure data confidentiality and integrity.
9. Integration Tests:
* Integration tests with Testcontainers to check component interactions, including interactions with the database.
10. Data Validation:
* Data validation is used, including email format checking and password matching.
11. Pagination Support for book and order listings.
12. Ability to search for books using various criteria (title, author, genre, etc.).
13. Management of book categories and their detailed information.
14. Creation and management of user orders.
15. Handling the contents of a user's shopping cart and updating the quantity of items in the cart.


#  Email and Password Validation 🔧

The project includes email validation and password matching. For email validation, a custom annotation @Email is used, and for password matching, @MatchPassword annotation is used. Details can be found in respective classes.

# EmailValidator 🔨

The EmailValidator class is used for email validation. The @Email annotation is used to mark methods that accept email addresses. The validate() method is used for email address validation.

# PasswordMatcher ✒️

The PasswordMatcher class is used for password matching. The @MatchPassword annotation is used to mark methods that accept passwords. The matches() method is used for comparing two passwords.


# Requirements ♨️
Before starting work on the project, make sure you have the following components installed:

- 🔺 Java Development Kit (JDK) version 11 or higher.
- 🔺 Maven (4.0.0): Tool for project building and dependency management.
- 🔺 Git
- 🔺 MySQL: Database used for data storage.
- 🔺 Properly configured pom.xml file containing the following dependencies :
*  For the detailed pom.xml file, you can find it in the project folder 📕.


## How to Install  🔧 
Follow these steps to install and run the project:
1. Clone the repository:
   ```shell
   git clone git@github.com:RostyslavOnysh/book-store.git
   ```


# Project Launch: 📡
* Ensure that Docker is installed on your system.
You can configure the database parameters in the .env file. Make sure you have provided the correct information, such as the username, password, database name, and so on.
* Open a terminal and navigate to the root directory of your project.
* Run the application using Docker Compose. Use the following command:
```bash
docker-compose up
```
* This command will start containers for your application and the database in Docker.
* After a successful launch, you can interact with your application's API. You can use tools like ***Postman or Swagger*** to interact with the API and verify its functionality.

  # Using Swagger
* User Registration
1. Open Postman or another tool for sending HTTP requests.
2. Create a POST request to /auth/register to register a new user. In the request body, provide the necessary information, such as email, password, first name, last name, and shipping address etc.
3. Send the request, and the user will be registered.

* Using Swagger
After successfully registering a user, you are ready to use Swagger for documenting and testing your API. You can follow these steps:
1. Start your application and navigate to the URL where you have set up Swagger. This typically looks like http://your_server/api/swagger-ui/.
2. You may be prompted for a login and password to authenticate in Swagger. Enter the login you used during registration (usually your email address), along with the corresponding password.
3. Now you can use Swagger to view and test various endpoints of your API. Swagger provides a convenient interface for making requests and checking the functionality of your application.


# Contact ♨️📬
Feel free to contact the author for any questions or feedback:
- Author: [Rostyslav](https://www.linkedin.com/in/rostyslav-onyshchenko-7ab348281/)
# License ☑️
This project is distributed under the MIT License. Feel free to modify and adapt it to your needs.

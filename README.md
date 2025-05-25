# Price Comparator
## Technical stack 
- Java
- Spring Boot
- PostgreSQL
- Maven

## Brief overview of project structure
- Main.java -> Entry point of the application
- controller/ -> REST API controllers
- dto/ -> Data Transfer Objects
- model/ -> Entity/model classes
- repository/ -> Data access layer
- service/ -> Business logic layer
- resources/data -> CSV files containing product data

## Instructions on how to build and run the application
To build and run this application, ensure PostgreSQL is installed and running locally. Then:  
1. Create a database named "PriceComparator".  
2. In the src/main/resources/application.properties file, update the following lines with your local PostgreSQL credentials:  
spring.datasource.url=jdbc:postgresql://localhost:5432/PriceComparator  
spring.datasource.username=postgres  
spring.datasource.password=parola  

To test the alert functionality, I have already inputted in application.properties a dummy gmail account that works for testing.  

## How to test the implemented features
To use the implemented features, use Postman or a similar tool. These are the instructions on how to test each functionality:  

### Daily Shopping Basket Monitoring
Send a POST request to the following URL: http://localhost:8080/api/basket/optimize  
The POST request should have a similar body to the following:  
```json
[
    {
        "productId": "P006",
        "quantity": 1
    },
        {
        "productId": "P062",
        "quantity": 3
    },
        {
        "productId": "P001",
        "quantity": 2
    }
]
 ```

### Best Discounts
Send a GET request to the following URL: http://localhost:8080/api/discounts/best

### New Discounts
Send a GET request to the following URL: http://localhost:8080/api/discounts/new

### Dynamic Price History Graphs
To simply get the data points for a product, send a GET request to: http://localhost:8080/api/price-history?productId=P001.  
 The product id can be changed to the product of interest. Or, to also have a filter on, send the following GET request:  
  http://localhost:8080/api/price-history?productId=P001&store=lidl

### Product Substitutes & Recommendations
Here is an example GET request. The productId parameter can be changed to see recommendations for another product.  
 http://localhost:8080/api/product-substitutes?productId=P003

### Custom Price Alert
To set a target price for a product, for a certain user, send a POST request to the following URL:  
http://localhost:8080/api/price-alerts  
The POST request should have a body similar to this:
```json
{
  "userEmail": "jajov38269@nomrista.com",
  "targetPrice": 1.9,
  "product": {
    "id": "P001"
  }
}
 ```

You can modify the userEmail field to any email address that you would like to receive the alert on. I used a temporary email.

After the POST request has been sent, the alert has been saved. To trigger the alert, you must send another POST request to manually add another price entry. The newly added price entry should have a price lower than the target price set by the user. Here is an example below:

Send POST request to:  
 http://localhost:8080/api/products/P001/add-price-entry  
With the following body:  
```json
{
  "store": "kaufland",
  "date": "2025-05-24",
  "price": 1.88
}
 ```



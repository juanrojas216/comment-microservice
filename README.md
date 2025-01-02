Microservice for an ecommerce system.

Made using Kotlin, RabbitMQ and MySQL.

Allows user to post, read and rate comments with API calls and RabbitMQ queues. Waits before posting comment to check if the user has bought the product, using both synchronous and asynchronous methods.
Allows backoffice administrator to review and moderate comments.

# LinkDIn

## About
This is a web application that implements a social media service similar to LinkedIn. This project is an assignment for the course "Web Technologies" of the dept. Informatics & Telecommunications of National Kapodistrian University of Athens.

## Frameworks used

### Backend

* [Spring MVC / Spring Boot](https://spring.io/): With this framework we implemented a RESTful API that handles all the requests of the application.
* [Spring JPA](https://spring.io/projects/spring-data-jpa) / [Hibernate](https://hibernate.org/): These frameworks are responsible for the I/O with the database.
* [MySQL](https://www.mysql.com/): The RDBMS of this web application is MySQL.

### Frontend
* [TypeScript](https://www.typescriptlang.org/)/[Angular  5](https://angular.io/): We have implemented the user interface in Angular, split in components (i.e. navbar.component, post.component etc.) 
* [Bootstrap 4](http://getbootstrap.com/): The user interface is styled with the help of Bootstrap framework. Also responsive view is supported.
* HTML 5/CSS (nothing to say here...)

##

### Post Recommendation Algorithm

The post recommendation service executes an algorithm that is based in k-NN classification. More specific, the algorithm fetches the 3 most recent public posts per user based on similar user activity(interests & comments).

##

### Job Recommendation Algorithm

Each user's job advert page contains job adverts that are based on the personal information of the user. After the stopwords have been filtered out, the algorithm selects the ads that have the most similar content with the user's info.

##

### TLS
We used a cerificate that is signed from [Let's Encrypt]() in order to encrypt all the traffic between the clients and the server.

##

### Post Visibility
Even though it was not in the assignment description, we have added a special feature in posts. All posts can be either public or private. With the attribute 'public' the post can be visible from everyone. On the contrary, with the attribute 'private' the post is visible only within the network of the user.

##

### Admin tools
The administrators can see everyone's activity, even if it's private. Also they have the option to export to XML file all the users' information and activity.

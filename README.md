<h1 align="center">
  Tomateto API - Social Media REST API for Tomateto
</h1>
<br>
<p align="center">
  <img src="https://user-images.githubusercontent.com/69242299/183031100-0f3d44ac-9899-4581-bdf4-d629829391d3.png" alt="tomateto-logo" />
</p>
<br>

<p align="center">
  <a href="https://tomateto.com">
    <img src="https://img.shields.io/badge/azure-running-blue" alt="azure-badge">
  </a>
  <a href="https://tomateto.com">
    <img src="https://img.shields.io/badge/demo-online-brightgreen" alt="demo-badge">
  </a>
  <a href="https://tomateto.com">
    <img src="https://img.shields.io/badge/version-1.0.2--beta-green" alt="version-badge">
  </a>
</p>

## Introduction

This is a social media REST API written in Java and powered by Spring Boot. It provides endpoints such as creating a new post, liking a post, following a user, etc. This API acts as a bridge between the Tomateto React web app and the PostgreSQL database, where all user, post, and comment data are being stored. Hosted with Azure App Service within a Docker container.

This is the back-end side of Tomateto. To learn more about the front-end side, click [here](https://github.com/adamscript/tomateto-react).

### Demo
Here is the working live demo : <https://tomateto.com>

### Built With
- [Java](https://www.java.com/en/)
- [Spring Boot](https://spring.io/)
- [Docker](https://www.docker.com/)
- [Azure App Service](https://azure.microsoft.com/en-us/services/app-service/)
- [Azure Database for PostgreSQL](https://azure.microsoft.com/en-us/services/postgresql/)

## Features

### API Request ([example code](https://github.com/adamscript/tomateto-api/blob/main/src/main/java/com/adamscript/tomatetoapi/controllers/PostController.java))
This API supports methods to list, insert, edit, delete, and many more that are specific to a particular resource. The following file identifies methods for `Post` resources: [Example code](https://github.com/adamscript/tomateto-api/blob/main/src/main/java/com/adamscript/tomatetoapi/services/PostService.java)

### Custom Response Object ([example code](https://github.com/adamscript/tomateto-api/blob/main/src/main/java/com/adamscript/tomatetoapi/helpers/handler/Response.java))
This object is used to handle bad requests made by users that could happen when the user modified the front end web app code through the browser developer tools to try to bypass a request validator (like trying to edit a post created by another user). The following JSON structure shows the format of a response:

```
{
    "message": "Success!",
    "code": 0,
    "items": {
        "id": 1,
        "content": "First!",
        "photo": null,
        "date": "2022-07-26T17:52:22Z",
        "likesCount": 2,
        "commentsCount": 1,
        "user": {
            "displayName": "Adam Darmawan",
            "id": "U2k7x5pBMXMleCHRqfRTYZkQlmD3",
            "avatar": {
                "small": "https://someurl",
                "extraSmall": "https://someurl",
                "default": "https://someurl",
                "medium": "https://someurl"
            },
            "username": "adamscript"
        },
        "isEdited": false,
        "isLiked": true,
        "isMine": true
    }
}
```

The following table identifies response codes and messages that the API returns. 

[Example code](https://github.com/adamscript/tomateto-api/blob/main/src/main/java/com/adamscript/tomatetoapi/helpers/service/ServiceStatus.java)

| Code | Message |
| ---- | ------- |
| General |
| 0 | Success! |
| User |
| 100 | User not found |
| 101 | Username already exists |
| 102 | User does not exist (Invalid User ID) |
| 103 | Username can't be empty |
| 104 | Name can't be empty |
| 105 | You can't follow yourself |
| 106 | You can't follow non existing user |
| 107 | You can't follow if you do not exist |
| 108 | User already followed |
| 109 | User not followed |
| Post |
| 200 | Post not found |
| 201 | Post does not exist (Invalid Post ID) |
| 202 | Post's user can't be empty |
| 203 | Post's content can't be empty |
| 204 | Post already liked |
| 205 | Post not liked |
| Comment |
| 300 | Comment not found |
| 301 | Comment does not exist (Invalid Comment ID) |
| 302 | Comment's user can't be empty |
| 303 | Comment's post can't be empty |
| 304 | Comment's content can't be empty |
| 305 | Comment already likedd |
| 306 | Comment not liked |
| Error |
| 400 | An unknown error occured |
| 401 | You are not authorized for this action |

### Data Transfer Object ([example code](https://github.com/adamscript/tomateto-api/blob/main/src/main/java/com/adamscript/tomatetoapi/models/dto/PostContentDTO.java))
This is mainly used to solve the infinite recursion problem that happens when the requested data (in example, all top posts) contains a relation with another entity (in example, the user who created the posts). It also helps reduce the number of calls needed so the server can respond with all of the required data at once (in example, content of the post, name of the user who created the post, and like status of the post) without having to make separate calls for each data.

### Instant Search ([example code](https://github.com/adamscript/tomateto-api/blob/main/src/main/java/com/adamscript/tomatetoapi/services/SearchService.java))
The app will index every data that is stored in the database to find data based on search queries faster so users will be able to get responses instantly. This feature is powered by Hibernate Search and Apache Lucene.

## Testing
Test cases were written to make sure the app runs as exactly as expected outside of the development environment. These tests include unit testing, integration testing, controller testing, and repository testing. The test cases were written using JUnit and Mockito for mocking.

[Example code](https://github.com/adamscript/tomateto-api/blob/main/src/test/java/com/adamscript/tomatetoapi/services/PostServiceUnitTest.java)

## CI/CD
Continuous integration is triggered when a pull request was made. It will automatically run the tests to make sure the app will run as expected after being integrated to the main branch.

Continuous deployment is triggered when a new tag is pushed to the repository. It will add some configurations to the app, then it will build and push a new container to GItHub Container Registry, and finally it will deploy the container to Azure App Service which will trigger the resource to restart.

The CI/CD workflow is being run by GitHub Actions.

[Example code](https://github.com/adamscript/tomateto-api/blob/main/.github/workflows/deploy.yml)

## Container
This app is running within a Docker container with Eclipse Temurin JRE 17 as the base image and hosted on Azure App Service and GitHub Container Registry.

[Example code](https://github.com/adamscript/tomateto-api/blob/main/Dockerfile)

## Azure
This app is currently running utilizing two Azure resources:
- Azure App Service
This resource hosts the REST API and provides access to the API endpoints.
- Azure Database for PostgreSQL
This resource stores the data that can be requested through the API.

## Reflection
Through this project, my main goal was to learn how to write my own REST API and the React app to interact it with. I also wanted to use this opportunity to learn about cloud, containers, testing, and CI/CD.
Initially I wanted to build it as microservices but I figured it was a bit too ambitious for what I was trying to build with the amount of time that I had, so I built it as a monolithic app instead. 

If I had more time, I would add these :
- More comprehensive testing (it already covers many edge cases, but there are still a few that I wish I could add if I had more time)
- Pagination, which could be useful if the server needs to handle many GET requests at once. Will add this when absolutely needed.
- Error handling, right now it can only handle bad requests made by users, not errors that is caused by the app itself.

## Support
If you are having issues or found a bug, feel free to [open an issue](https://github.com/adamscript/tomateto-react/issues). Also if you have questions about the project, feel free to reach out at: <adam@adamscript.com>.

## Sources
These articles helped me while building this project. Check them out!
- [Jackson – Bidirectional Relationships](https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion)
- [Best Practices for REST API Error Handling](https://www.baeldung.com/rest-api-error-handling-best-practices)
- [Spring Data JPA: Query Projections](https://thorben-janssen.com/spring-data-jpa-query-projections)
- [Testing MVC Web Controllers with Spring Boot and @WebMvcTest](https://reflectoring.io/spring-boot-web-controller-test)
- [Full-Text Search with Hibernate Search and Spring Boot](https://reflectoring.io/hibernate-search)
- [9 Tips for Containerizing Your Spring Boot Code](https://www.docker.com/blog/9-tips-for-containerizing-your-spring-boot-code)
- [10 best practices to build a Java container with Docker](https://snyk.io/blog/best-practices-to-build-java-containers-with-docker)

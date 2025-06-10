# WebCrawler

**WebCrawler** is a Java-based project developed as part of the *Clean Code* course at **AAU Klagenfurt**.  

---

## Functionality
- Allows concurrent crawling of selected websites up to a chosen depth
- The starting website URL and crawling depth are entered by a user via console
- Exceptions, which occur during execution, are written to a log file "exceptions.log" for consistency
- Details of crawled websites are stored in a report "Web_Crawler_Report.md".

---

## Requirements

- **JDK 21** (ensure it is set as your SDK)
- **Gradle** (uses JVM toolchain 21)
- **JUnit 5**
- **Mockito 5**

---

## Dependencies

The project uses the following libraries:

### Main Dependencies
- [`jsoup`](https://jsoup.org/) - `org.jsoup:jsoup:1.15.4` (for HTML parsing)

### Testing Dependencies
- `org.junit.jupiter:junit-jupiter-api:5.10.2` (for unit testing)
- `org.junit.jupiter:junit-jupiter-engine:5.10.2` (test runtime engine)
- `org.mockito:mockito-core:5.18.0` (for mocking in tests)

---

### Build the project
````
./gradlew build
```` 
### Run Tests
````
./gradlew test
````
---
### License
This project is open-source and free to use.
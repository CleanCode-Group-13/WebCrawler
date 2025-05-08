# WebCrawler

**WebCrawler** is a Java-based project developed as part of the *Clean Code* course at **AAU Klagenfurt**.  
---

## Requirements

- **JDK 21** (ensure it is set as your SDK)
- **Gradle** (uses JVM toolchain 21)

---

## Dependencies

The project uses the following libraries:

### Main Dependencies
- [`jsoup`](https://jsoup.org/) - `org.jsoup:jsoup:1.15.4` (for HTML parsing)
- [`deepl-java`](https://www.deepl.com/docs-api) - `com.deepl.api:deepl-java:1.1.0` (for optional translation features)

### Testing Dependencies
- `org.junit.jupiter:junit-jupiter-api:5.9.2` (for unit testing)
- `org.junit.jupiter:junit-jupiter-engine:5.9.2` (test runtime engine)
- `org.mockito:mockito-core:5.2.0` (for mocking in tests)

> The project uses **JUnit Platform** for running tests.

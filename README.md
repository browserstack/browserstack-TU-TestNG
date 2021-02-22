1. Basic test run locally
`mvn test -P local`

2. Test using Page Object Model
`mvn test -P local-pom`

3. Data-driven test
`mvn test -P local-data-driven`

4. Data-driven test external file
`mvn test -P local-data-driven-external`

5. Tests run on Browserstack
`mvn test -P browserstack`

6. Tests run on Browserstack (with location India)
`mvn test -P browserstack-geo`

7. Tests run parallel on local browsers
`mvn test -P local-parallel`

8. Tests run on Docker Selenium Grid
Make sure Docker Selenium Grid is running, by executing `docker-compose up -d` from the `/docker` folder.

   `mvn test -P docker`

9. Tests run parallel on Docker Selenium Grid
Make sure to scale your Docker Selenium Grid with `docker-compose scale chromenode=3`.

   `mvn test -P docker-parallel`

10. Tests run parallel on Browserstack
`mvn test -P browserstack-parallel`

## Generate Report
`mvn allure:report`

The report is located in `target/site`

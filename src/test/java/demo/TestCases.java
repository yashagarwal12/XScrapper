package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.io.File;
import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
        ChromeDriver driver;
        Wrappers wrap = new Wrappers();

        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
         */

        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */
        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();
        }

        @Test
        public void testCase01() throws InterruptedException {
                System.out.println("-------------------testCase01 started----------------------");
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                driver.get("https://www.scrapethissite.com/pages/");

                WebElement hockey_teams = driver.findElement(By.partialLinkText("Hockey Teams"));
                wrap.clicks(hockey_teams);

                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));
                ArrayList<Map<String, Object>> final_list = new ArrayList<>();
                ObjectMapper mapper = null;
                for (int i = 1; i <= 4; i++) {
                        WebElement pages = driver
                                        .findElement(By.xpath(
                                                        "//ul[@class='pagination']/li[" + i + "]/a"));
                        wrap.clicks(pages);
                        Thread.sleep(2000);

                        List<WebElement> list = driver
                                        .findElements(By.xpath(
                                                        "//table/tbody/tr[@class='team']/td[contains(@class,'pct text')]"));

                        for (int j = 1; j <= list.size(); j++) {
                                float win_perc = Float.parseFloat(driver
                                                .findElement(By
                                                                .xpath("//table/tbody/tr[@class='team'][" + (j)
                                                                                + "]/td[contains(@class,'pct text')]"))
                                                .getText());
                                if (win_perc < 0.400) {
                                        mapper = new ObjectMapper();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("Epoch Time of Scrape", System.currentTimeMillis() / 1000);
                                        map.put("Team Name",
                                                        driver.findElement(
                                                                        By.xpath("//table/tbody/tr[@class='team']["
                                                                                        + (j) + "]/td[@class='name']"))
                                                                        .getText());
                                        map.put("Year",
                                                        driver.findElement(
                                                                        By.xpath("//table/tbody/tr[@class='team']["
                                                                                        + (j) + "]/td[@class='year']"))
                                                                        .getText());
                                        map.put("Win %", win_perc);
                                        final_list.add(map);
                                }

                        }

                }

                wrap.createJsonFile(final_list, mapper, "hockey-team-data.json");
                System.out.println("-------------------testCase01 Ended----------------------");
        }

        @Test
        public void testCase02() throws InterruptedException {
                System.out.println("-------------------testCase02 started----------------------");
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                driver.get("https://www.scrapethissite.com/pages/");

                WebElement oscar = driver.findElement(By.partialLinkText("Oscar Winning Films"));
                wrap.clicks(oscar);

                wait.until(ExpectedConditions.presenceOfElementLocated(
                                By.xpath("//h1[contains(text(),'Oscar Winning Films: AJAX and Javascript')]")));

                List<WebElement> years = driver
                                .findElements(By.xpath(
                                                "//h3[contains(text(),'Choose a Year to View Films')]//following-sibling::a"));
                ArrayList<Map<String, Object>> final_list = new ArrayList<>();
                ObjectMapper mapper = null;
                for (int i = 1; i <= years.size(); i++) {
                        wrap.scrollToView(driver.findElement(By
                                        .xpath("//h3[contains(text(),'Choose a Year to View Films')]//following-sibling::a["
                                                        + (i) + "]")),
                                        driver);
                        wrap.clicks(driver.findElement(By
                                        .xpath("//h3[contains(text(),'Choose a Year to View Films')]//following-sibling::a["
                                                        + (i) + "]")));

                        // wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));
                        Thread.sleep(5000);
                        for (int j = 1; j <= 5; j++) {
                                boolean isWinner = false;
                                mapper = new ObjectMapper();
                                Map<String, Object> map = new HashMap<>();

                                map.put("Epoch Time of Scrape", System.currentTimeMillis() / 1000);
                                map.put("Year", driver.findElement(By.xpath(
                                                "//div[@class='container']//h3[contains(text(),'Choose a Year to View Films')]/following-sibling::a["
                                                                + (i) + "]"))
                                                .getText());
                                map.put("Title", driver.findElement(
                                                By.xpath("//div[@class='container']//table/tbody/tr[" + (j)
                                                                + "]/td[@class='film-title']"))
                                                .getText());
                                map.put("Nomination",
                                                driver.findElement(By.xpath(
                                                                "//div[@class='container']//table/tbody/tr[" + (j)
                                                                                + "]/td[@class='film-nominations']"))
                                                                .getText());
                                map.put("Awards", driver.findElement(
                                                By.xpath("//div[@class='container']//table/tbody/tr[" + (j)
                                                                + "]/td[@class='film-awards']"))
                                                .getText());
                                try {
                                        driver.findElement(By.xpath("//div[@class='container']//table/tbody/tr[" + (j)
                                                        + "]/td[@class='film-best-picture']"));
                                        isWinner = true;
                                        map.put("isWinner", isWinner);
                                } catch (Exception E) {
                                        isWinner = false;
                                        map.put("isWinner", isWinner);
                                }

                                final_list.add(map);
                        }

                }

                wrap.createJsonFile(final_list, mapper, "oscar-winner-data.json");

                String userDir = System.getProperty("user.dir");
                File file = new File(userDir + "\\output\\oscar-winner-data.json");
                Assert.assertTrue(file.exists() && file.length() != 0);
                System.out.println("-------------------testCase02 Ended----------------------");

        }

        @AfterTest
        public void endTest() {
                driver.close();
                driver.quit();

        }
}
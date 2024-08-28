package demo.wrappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class Wrappers {
    public void clicks(WebElement ele) {
        try {
            ele.click();
        } catch (Exception E) {
            E.printStackTrace();
        }

    }

    public void scrollToView(WebElement ele, WebDriver driver) {
        try {
            ((JavascriptExecutor) driver).executeScript("argument[0].scrollToView(false);", ele);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public void createJsonFile(ArrayList<Map<String, Object>> final_list, ObjectMapper mapper, String file_name) {
        try {
            String employeePrettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(final_list);
            System.out.println(employeePrettyJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String userDir = System.getProperty("user.dir");

        // Writing JSON on a file
        try {

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(userDir + "\\output\\" + file_name), final_list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

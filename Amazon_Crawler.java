package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Amazon_Crawler {

    public static void main(String[] args) throws InterruptedException {
        // !! Set up ChromeDriver path, change that for your environment !!
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Marti\\Desktop\\chromedriver.exe");

        // Launch Chrome browser
        WebDriver driver = new ChromeDriver();

        // Open Amazon website on full screen
        driver.manage().window().maximize();
        driver.get("https://www.amazon.com/ref=nav_bb_logo");

        // Wait to be load
        Thread.sleep(5000);

        try {
            // Find the "Shop By Department" dropdown menu by Xpath and click it
            WebElement shopByDepartment = driver.findElement(By.xpath("/html/body/div[1]/header/div/div[4]/div[1]/a/i"));
            shopByDepartment.click();

            // Wait for the submenu to be load
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Get the Department links by classname
            List<WebElement> departmentLinks = driver.findElements(By.className("nav-item"));
            Date timestamp = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd__HH_mm_ss");
            String resultFileName = dateFormat.format(timestamp) + "_Results.txt";

            // Create a BufferedWriter to write results to a text file
            BufferedWriter writer = new BufferedWriter(new FileWriter(resultFileName));

            for (WebElement link : departmentLinks) {
                String linkUrl = link.getAttribute("href");
                String linkText = link.getText();
                String status = checkLinkStatus(linkUrl);

                writer.write(linkUrl + ", " + linkText + ", " + status);
                writer.newLine();
            }

            // Close the writer and driver
            writer.close();
            driver.quit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to verify the status of a link
    private static String checkLinkStatus(String url) {
        try {
            java.net.HttpURLConnection.setFollowRedirects(false);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (responseCode == 200) ? "OK" : "Dead link";
        } catch (IOException e) {
            e.printStackTrace();
            return "Dead link";
        }
    }
}
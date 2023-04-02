package org.sdet.qa;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.text.Collator;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Epic("Практикум 1")
public class LoginTest {
    public ChromeOptions option = new ChromeOptions();
    public WebDriver driver;
    public void start() {
        option.addArguments("--remote-allow-origins=*");
        System.setProperty("webdriver.chrome.driver", ConfProps.getProperty("chromedriver"));
        driver = new ChromeDriver(option);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    @Feature("Регистрация пользователя")
    @Story("Успешная регистрация")
    @Test
    public void Case1() {
        start();
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
        WebElement Submit = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[1]/button[1]"));
        Submit.click();
        WebElement TxtBoxContent = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[1]/input"));
        TxtBoxContent.sendKeys("standard_user");
        WebElement Password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[2]/input"));
        Password.sendKeys("secret_sauce");
        WebElement Postcode = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[3]/input"));
        Postcode.sendKeys("111111");
        WebElement AddCustomer = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/button"));
        AddCustomer.click();

        Alert alert = driver.switchTo().alert();
        String text = alert.getText();
        Assert.assertTrue("Test", text.indexOf("Customer added successfully with customer id", 0) != -1);
        alert.accept();
        driver.close();
        driver.quit();
    }
    @Feature("Сортировка")
    @Story("По возрастанию")
    @Test
    public void Case2() {
        start();
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager/list");
        List<WebElement> list_before = driver.findElements(By.cssSelector("tr > .ng-binding:nth-child(1)"));
        List<String> testList = new ArrayList<String>();
        for (int i = 0; i < list_before.size(); i++) {
            testList.add(list_before.get(i).getText());
        }
        Collections.sort(testList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Collator collator = Collator.getInstance(new Locale("ru", "RU")); //Тут вводим свою локаль
                collator.setStrength(Collator.PRIMARY);//
                return collator.compare(o1, o2);
            }
        });
        // клик для сортировки
        WebElement SortNames = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/table/thead/tr/td[1]/a"));
        SortNames.click();
        SortNames.click();
        List<WebElement> list_after = driver.findElements(By.cssSelector("tr > .ng-binding:nth-child(1)"));
        List<String> newList = new ArrayList<String>();
        for (int i = 0; i < list_after.size(); i++) {
            newList.add(list_after.get(i).getText());
        }
        Assert.assertEquals("Test 2", testList, newList);
        driver.close();
        driver.quit();
    }


    @Feature("Фильтрация")
    @Story("Фильтрация по 5 кейсам")
    @Test
    public void Case3() {
        start();
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager/list");
        List<WebElement> listBefore = driver.findElements(By.cssSelector("tr"));
        List<String> testList = new ArrayList<String>();

        for (int i = 1; i < listBefore.size(); i++) {
            testList.add(listBefore.get(i).findElement(By.cssSelector("td:nth-child(1)")).getText() + "|" +
                    listBefore.get(i).findElement(By.cssSelector("td:nth-child(2)")).getText() + "|" +
                    listBefore.get(i).findElement(By.cssSelector("td:nth-child(3)")).getText() + "|" +
                    listBefore.get(i).findElement(By.cssSelector("td:nth-child(4)")).getText()
            );
        }

        String[] query = new String[5];  // Строка запроса
        query[0] = "101";
        query[1] = "55";
        query[2] = "Harry";
        query[3] = "E55656";
        query[4] = "u";
        for (int queryIndex = 0; queryIndex < query.length; queryIndex++) {

            List<String> rowsBefore = new ArrayList<String>();

            Integer j = 0;
            for (String x : testList) {
                if (x.indexOf(query[queryIndex], 0) != -1) {
                    rowsBefore.add(x);
                }

            }

            WebElement TxtBoxContent = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/form/div/div/input"));
            TxtBoxContent.clear();
            TxtBoxContent.sendKeys(query[queryIndex]);

            List<WebElement> listAfter = driver.findElements(By.cssSelector("tr"));

            List<String> newList = new ArrayList<String>();

            for (int i = 1; i < listAfter.size(); i++) {
                newList.add(listAfter.get(i).findElement(By.cssSelector("td:nth-child(1)")).getText() + "|" +
                        listAfter.get(i).findElement(By.cssSelector("td:nth-child(2)")).getText() + "|" +
                        listAfter.get(i).findElement(By.cssSelector("td:nth-child(3)")).getText() + "|" +
                        listAfter.get(i).findElement(By.cssSelector("td:nth-child(4)")).getText()
                );
            }

            List<String> rowsAfter = new ArrayList<String>();
            Integer k = 0;
            for (String x : newList) {
                if (x.indexOf(query[queryIndex], 0) != -1) {
                    rowsAfter.add(x);
                }

            }
            Assert.assertEquals("Test 2", rowsBefore, rowsAfter);
           }
        driver.close();
        driver.quit();
    }
}
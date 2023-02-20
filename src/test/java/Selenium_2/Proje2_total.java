package Selenium_2;

import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Proje2_total {

    @Test
    public void proje2() throws IOException, InterruptedException {
 //s1-s2
        WebDriver driver = new ChromeDriver();
        driver.get("http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx");
        driver.manage().window().maximize();
///s3
         WebElement userName =  driver.findElement(By.xpath("//input[@name = 'ctl00$MainContent$username']"));
userName.sendKeys("Tester");

        WebElement passWord =  driver.findElement(By.xpath("//input[@name = 'ctl00$MainContent$password']"));
        passWord.sendKeys("test");

        WebElement login =  driver.findElement(By.xpath("//input[@name = 'ctl00$MainContent$login_button']"));
        login.click();
//s4
        WebElement order =  driver.findElement(By.xpath("//a[@href = 'Process.aspx']"));
        order.click();

        //s5-chosing

            WebElement product = driver.findElement(By.xpath("//select[@name='ctl00$MainContent$fmwOrder$ddlProduct']"));
product.click();

            List<WebElement> choseOptions = driver.findElements(By.xpath("//select[@name='ctl00$MainContent$fmwOrder$ddlProduct']/option"));//xpath custom
            int choseIndex = (int) (Math.random() * choseOptions.size());
            choseOptions.get(choseIndex).click();


        //  s5-------Enter a random product quantity between 1 and 100
        int quantity = (int) (Math.random() * 100) + 1;
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtQuantity")).sendKeys(Integer.toString(quantity));


        // Click on Calculate and verify that the Total value is correct
        driver.findElement(By.cssSelector("input[type='submit'][value='Calculate']")).click();
        int expectedTotal = (quantity < 10) ? quantity * 100 : (int) (quantity * 100 * 0.92);
        Thread.sleep(1000);
        WebElement actualTotalText = driver.findElement(By.name("ctl00$MainContent$fmwOrder$txtTotal"));
        System.out.println(actualTotalText.getText());

        //s6-s10
        Path reader = Path.of("src/test/java/Selenium_2/MOCK_DATA.csv");

        List<String[]> dataRows = Files.readAllLines(reader) // read all lines from file
                .stream() // convert to stream
                .skip(1) // skip the header row
                .map(line -> line.split(",")) // split each line by comma
                .collect(Collectors.toList()); // collect into a list of String arrays

        // Get a random row of data from the list
        Random random = new Random();
        String[] randomDataRow = dataRows.get(random.nextInt(dataRows.size()));

        // Use the data from the row to enter customer information
        WebElement name1 = driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtName"));
        name1.sendKeys(randomDataRow[0]);
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox2")).sendKeys(randomDataRow[1]);
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox3")).sendKeys(randomDataRow[2]);
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox4")).sendKeys(randomDataRow[3]);
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox5")).sendKeys(randomDataRow[4]);

        //s11
        List<WebElement> cardTypes = driver.findElements(By.name("ctl00$MainContent$fmwOrder$cardList"));
        int index = (int) (Math.random() * cardTypes.size());
        cardTypes.get(index).click();

        //s- 12 Enter a random card number based on the selected card type
        String cardNumber = "";
        int randomCardTypeIndex = 0;
        switch (randomCardTypeIndex) {
            case 0: // Visa
                cardNumber = "4" + new Faker().number().digits(15);
                break;
            case 1: // MasterCard
                cardNumber = "5" + new Faker().number().digits(15);
                break;
            case 2: // American Express
                cardNumber = "3" + new Faker().number().digits(14);
                break;
        }
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox6")).sendKeys(cardNumber);

        // s-13 Enter a valid expiration date (newer than the current date)
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox1")).sendKeys("12/23");

        // s-14 Click on the Process button
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_InsertButton")).click();

        // s-15 Verify that the "New order has been successfully added" message appears on the page
        String successMessage = "New order has been successfully added.";
        Assert.assertTrue(driver.getPageSource().contains(successMessage));

        // s-16 Click on the View All Orders link
        driver.findElement(By.linkText("View all orders")).click();

        // s-17 Verify that the placed order details appear on the first row of the orders table

        WebElement ordersTable = driver.findElement(By.id("ctl00_MainContent_orderGrid"));
        WebElement firstRow = ordersTable.findElement(By.xpath("//table[@id='ctl00_MainContent_orderGrid']/tbody/tr[2]"));
        String actualName = firstRow.findElement(By.xpath("//*[@id=\"ctl00_MainContent_orderGrid\"]/tbody/tr[2]/td[2]")).getText();
        String street = firstRow.findElement(By.xpath("//*[@id=\"ctl00_MainContent_orderGrid\"]/tbody/tr[2]/td[6]")).getText();
        String city = firstRow.findElement(By.xpath("//*[@id=\"ctl00_MainContent_orderGrid\"]/tbody/tr[2]/td[7]")).getText();
        String state = firstRow.findElement(By.xpath("//*[@id=\"ctl00_MainContent_orderGrid\"]/tbody/tr[2]/td[8]")).getText();
        String zip = firstRow.findElement(By.xpath("//*[@id=\"ctl00_MainContent_orderGrid\"]/tbody/tr[2]/td[9]")).getText();

        Assert.assertEquals(actualName,randomDataRow[0]);
        Assert.assertEquals(street,randomDataRow[1]);
        Assert.assertEquals(city, randomDataRow[2]);
        Assert.assertEquals(state, randomDataRow[3]);
        Assert.assertEquals(zip, randomDataRow[4]);

        // s-18 Log out of the application
        //driver.findElement(By.id("ctl00_logout")).click();

        // Quit the driver
       // driver.quit();

    }
}

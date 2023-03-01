package Selenium_Project_3.Cargurus;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CarGurus_Test_Proje_3 {
    @Test
    public void carGurus() throws InterruptedException, IOException {

//step 1 - Navigate to cargurus.com - Step 2 - Click on Buy Used

        WebDriver driver = new ChromeDriver();
        driver.get("https://www.cargurus.com/");
        driver.findElement(By.xpath("//label[@class=\"ft-homepage-search__tabs__used-car \"]")).click();

//Step 3  - Verify that the default selected option in Makes dropdown is All Makes (Use Assert methods for all verifications

        Thread.sleep(1000);
        WebElement selectedOptionMakes = new Select(driver.findElement(By.xpath("//select[@id=\"carPickerUsed_makerSelect\"]"))).getFirstSelectedOption();
        Assert.assertEquals(selectedOptionMakes.getText(), "All Makes", "Default selected option");

        // Step 4 In Makes dropdown, choose Lamborghini
        Thread.sleep(2000);
        new Select(driver.findElement(By.xpath("//select[@id=\"carPickerUsed_makerSelect\"]"))).selectByVisibleText("Lamborghini");


// Step 5  Verify that the default selected option in Models dropdown is All Models
        WebElement selectedOptionMakes1 = new Select(driver.findElement(By.xpath("//select[@id='carPickerUsed_modelSelect']"))).getFirstSelectedOption();
        Assert.assertEquals(selectedOptionMakes1.getText(), "All Models", "Default selected option");

// Step 6 Verify that Models dropdown options are [All Models, Aventador, Huracan, Urus, 400GT, Centenario, Countach, Diablo, Espada, Gallardo, Murcielago]
        List<String> expectedModels = List.of("All Models", "Aventador", "Huracan", "Urus", "400GT",
                "Centenario", "Countach", "Diablo", "Espada", "Gallardo", "Murcielago");
        List<WebElement> modelsOptions = selectedOptionMakes1.findElements(By.xpath("//select[@id='carPickerUsed_modelSelect']//option"));
        List<String> actualModels = new ArrayList<>();
        for (WebElement option : modelsOptions) {
            actualModels.add(option.getText());
        }
        Assert.assertEquals(actualModels, expectedModels);

// Step 7  In Models dropdown, choose Gallardo
        new Select(driver.findElement(By.xpath("//select[@id='carPickerUsed_modelSelect']"))).selectByVisibleText("Gallardo");

// Step 8 Enter 22031 for zip and hit search
        Path reader = Path.of("src/test/java/Selenium_Project_3/Cargurus/MOCK_DATA.csv");

        List<String[]> dataRows = Files.readAllLines(reader) // read all lines from file
                .stream()
                .skip(1)
                .map(line -> line.split(","))
                .collect(Collectors.toList());

        // Get a random row of data from the list
        Random random = new Random();
        String[] randomDataRow = dataRows.get(random.nextInt(dataRows.size()));
        driver.findElement(By.xpath("//input[@id=\"dealFinderZipUsedId_dealFinderForm\"]")).sendKeys(randomDataRow[0]);
//        driver.findElement(By.xpath("//input[@id=\"dealFinderZipUsedId_dealFinderForm\"]")).sendKeys("22031");
//
        driver.findElement(By.xpath("//input[@id=\"dealFinderForm_0\"]")).click();

// Step 9 Verify that there are 15 search results, excluding the first sponsored result
        List<WebElement> searchResults = driver.findElements(By.xpath("//a[@data-cg-ft='car-blade-link'][not(contains(@href, 'FEATURED'))]"));
        Assert.assertEquals(15, searchResults.size());

// Step 10 Verify that all 15 result's title text contains "Lamborghini Gallardo"
        for (WebElement searchResult : searchResults) {
            String resultTitle = searchResult.getText();
            Assert.assertTrue(resultTitle.contains("Lamborghini Gallardo"));
        }

// Step 11  From the dropdown on the left corner choose “Lowest price first” option and verify that all 15 results are sorted from lowest to highest. You should exclude the first result since it will not be a part of sorting logic. To verify correct sorting, collect all 15 prices into a list, create a copy of it and sort in ascending order and check the equality of the sorted copy with the original
        Select sortDropdown = new Select(driver.findElement(By.xpath("//select[@id='sort-listing']")));
        sortDropdown.selectByVisibleText("Lowest price first");
        List<WebElement> priceElements = driver.findElements(By.xpath("//span[@class='cg-dealFinder-result-stats']/div[2]/div"));
        List<Integer> prices = new ArrayList<>();
        for (WebElement priceElement : priceElements) {
            prices.add(Integer.parseInt(priceElement.getText().replaceAll("[$,]", "")));
        }
        List<Integer> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices);
        Assert.assertEquals(prices, sortedPrices, "lowest price failed");

// Step 12  From the dropdown menu, choose “Highest mileage first” option and verify that all 15 results are sorted from highest to lowest. You should exclude the first result since it will not be a part of sorting logic
        sortDropdown.selectByVisibleText("Highest mileage first");
        List<WebElement> mileageElements = driver.findElements(By.xpath("//span[@class='cg-dealFinder-result-stats']/div[1]/div"));
        List<Integer> mileages = new ArrayList<>();
        for (WebElement mileageElement : mileageElements) {
            mileages.add(Integer.parseInt(mileageElement.getText().replaceAll("[,]", "")));
        }
        //Integer.parseInt(text.substring(0, text.indexOf(" ")).replace(",", "")));
        List<Integer> sortedMileages = new ArrayList<>(mileages);
        Collections.sort(sortedMileages, Collections.reverseOrder());
        Assert.assertEquals(mileages, sortedMileages, "highest mileage failed");

        // Step 13 On the left menu, click on Coupe AWD checkbox and verify that all results on the page contains “Coupe AWD"

        Thread.sleep(4000);
        driver.findElement(By.xpath("//fieldset[5]//ul[1]//li[1]//label[1]//p[1]")).click();


        List<WebElement> resultTitleElements = driver.findElements(By.xpath("//a[@data-cg-ft='car-blade-link'][not(contains(@href, 'FEATURED'))]//h4[contains(@class, 'cg-listingDetail-model')]"));
        for (WebElement resultTitleElement : resultTitleElements) {
            String resultTitle = resultTitleElement.getText().toLowerCase();
            Assert.assertTrue(resultTitle.contains("lamborghini gallardo") && resultTitle.contains("coupe awd"));
        }
//Distanse

        new Select(driver.findElement(By.xpath("//select[@id=\"select-filter\"]"))).selectByVisibleText("Nationwide");

//color
        Thread.sleep(3000);
        driver.findElement(By.xpath("//fieldset[6]//ul[1]//li[9]//label[1]//p[1]")).click();

        //interior color
        Thread.sleep(3000);
        driver.findElement(By.xpath("//fieldset[7]//ul[1]//li[1]//label[1]//p[1]")).click();

// Step 14  Click on the last result (get the last result dynamically, i.e., your code should click on the last result regardless of how many results are there

        Thread.sleep(3000);
        List<WebElement> resultListElements = driver.findElements(By.xpath("//img[@class='C6f2e2 bmTmAy']"));
        WebElement lastCheck =  resultListElements.get(resultListElements.size()-1);
        lastCheck.click();

// Step 15 Once you are in the result details page go back to the results page and verify that the clicked result has “Viewed” text on it
        Thread.sleep(3000);
        driver.navigate().back();
        String viewedText = driver.findElement(By.xpath("//div[@class='HWinWE x1gK4I']")).getText();
        Assert.assertTrue(viewedText.contains("Viewed"));


        //driver.close();
    }
}



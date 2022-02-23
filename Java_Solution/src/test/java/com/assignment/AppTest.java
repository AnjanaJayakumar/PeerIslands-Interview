package com.assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    App app;

    @Before
    public void setUp() {
        this.app = new App();
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void input1Test() {
        JSONParser jsonParser = new JSONParser();
        String output = "";

        try (FileReader reader = new FileReader("input1.json")) {

            Object jsonQuery = jsonParser.parse(reader);

            output = App.parseJSONQuery((JSONObject) jsonQuery);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("[INFO]: Failing input1.json",
                "SELECT * FROM iris",
                output);
    }

    @Test
    public void input2Test() {
        JSONParser jsonParser = new JSONParser();
        String output = "";

        try (FileReader reader = new FileReader("input2.json")) {

            Object jsonQuery = jsonParser.parse(reader);

            output = App.parseJSONQuery((JSONObject) jsonQuery);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("[INFO]: Failing input2.json",
                "SELECT * FROM iris WHERE Species = \'Iris-versicolor\'",
                output);
    }

    @Test
    public void input3Test() {
        JSONParser jsonParser = new JSONParser();
        String output = "";

        try (FileReader reader = new FileReader("input3.json")) {

            Object jsonQuery = jsonParser.parse(reader);

            output = App.parseJSONQuery((JSONObject) jsonQuery);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("[INFO]: Failing input3.json",
                "SELECT Species, PetalWidthCm, PetalWidthCm * 2 FROM iris WHERE Species = \'Iris-versicolor\' AND PetalWidthCm BETWEEN 1.3 AND 1.8 OR Species = \'Iris-setosa\' AND PetalLengthCm IN (1.4,1.6)",
                output);
    }

    @Test
    public void input4Test() {
        JSONParser jsonParser = new JSONParser();
        String output = "";

        try (FileReader reader = new FileReader("input4.json")) {

            Object jsonQuery = jsonParser.parse(reader);

            output = App.parseJSONQuery((JSONObject) jsonQuery);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("[INFO]: Failing input4.json",
                "SELECT Categories.CategoryName, Categories.Description, Products.ProductName FROM Categories inner join Products ON Categories.CategoryID = Products.ProductID WHERE Products.ProductName LIKE \'c%\' AND Categories.CategoryName IN ( \'Beverages\', \'Condiments\')",
                output);
    }
}

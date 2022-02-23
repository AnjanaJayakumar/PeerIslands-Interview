package com.assignment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Hello world!
 *
 */
public class App {

    private static Operation parseOperations(JSONObject operationQuery) {

        // Get the operations
        Operation operation = new Operation();

        // Make the operation
        operation.setOperator((String) operationQuery.get("operator"));
        operation.setFieldName((String) operationQuery.get("fieldName"));
        operation.setFieldValues((ArrayList<Object>) operationQuery.get("fieldValue"));

        return operation;
    }

    private static JoinOperation parseJoinOperations(JSONObject joinOperationQuery) {

        // Get the join operations
        JoinOperation joinOperation = new JoinOperation();

        // Make the operation
        String joinType = (String) joinOperationQuery.get("joinType");
        if (!joinType.isEmpty()) {
            joinOperation.setJoinType(joinType);
        }
        joinOperation.setOperator((String) joinOperationQuery.get("operator"));
        joinOperation.setField1((String) joinOperationQuery.get("field1"));
        joinOperation.setField2((String) joinOperationQuery.get("field2"));
        joinOperation.setJoinTable((String) joinOperationQuery.get("joinTableName"));

        return joinOperation;
    }

    public static String parseJSONQuery(JSONObject jsonQuery) {

        // Make the Operators
        JSONArray jsonOperations = (JSONArray) jsonQuery.get("operations");
        ArrayList<Operation> operations = new ArrayList<>();

        for (Object jsonOperation : jsonOperations) {
            operations.add(parseOperations((JSONObject) jsonOperation));
        }

        // Make Join Operators
        JSONArray jsonJoinOperations = (JSONArray) jsonQuery.get("joinOperations");
        ArrayList<JoinOperation> joinOperations = new ArrayList<>();
        for (Object jsonJoinOperation : jsonJoinOperations) {
            joinOperations.add(parseJoinOperations((JSONObject) jsonJoinOperation));
        }

        // Get the JSONQuery
        JSONQuery query = new JSONQuery(
                (String) jsonQuery.get("type"),
                (ArrayList<String>) jsonQuery.get("columns"),
                (ArrayList<String>) jsonQuery.get("joiners"),
                (String) jsonQuery.get("table_name"),
                operations,
                joinOperations);

        // Build Query
        return query.parseSQLQuery();
    }

    public static void main(String[] args) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("input4.json")) {

            Object jsonQuery = jsonParser.parse(reader);

            System.out.println(parseJSONQuery((JSONObject) jsonQuery));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

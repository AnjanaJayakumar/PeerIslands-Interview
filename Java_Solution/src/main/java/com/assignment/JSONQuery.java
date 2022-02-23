package com.assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class Operation {

    // Defining Operations
    public String operator;
    public String fieldName;
    public ArrayList<Object> fieldValues;
    public String miniQuery;

    // Private variables
    private String[] supportedLogicalOperators = {
            "IN",
            "BETWEEN",
            "LIKE",
    };
    private String[] supportedComparisonOperators = {
            "=", ">", "<", ">=", "<=", "<>"
    };

    public Operation() {
    }

    // NOTE: Define PRIVATE Functions
    private String betweenParser() throws Exception {

        if (this.fieldValues.size() != 2) {
            throw new Exception(
                    "[ERROR]: BETWEEN Operation expects only 2 fieldValues, got " + this.fieldValues.size());
        } else {
            // Proceed with the miniquery
            miniQuery = this.fieldName + " BETWEEN " + this.fieldValues.get(0) + " AND " + this.fieldValues.get(1);
            return miniQuery;
        }
    }

    private String inParser() throws Exception {
        if (this.fieldValues.size() < 1) {
            throw new Exception("[ERROR]: IN Operation expects atleast 1 fieldValue, got " + this.fieldValues.size());
        } else {
            miniQuery = this.fieldName + " IN (";
            // Proceed with the miniquery
            for (int i = 0; i < this.fieldValues.size(); ++i) {
                Object value = this.fieldValues.get(i);
                if (value instanceof Long) {
                    miniQuery += (Long) value;
                } else if (value instanceof Double) {
                    miniQuery += (Double) value;
                } else if (value instanceof String) {
                    miniQuery += " \'" + (String) value + "\'";
                } else {
                    throw new Exception("[ERROR]: IN Parser encountered Unsupported type: " + value.getClass());
                }
                if (i != (this.fieldValues.size() - 1)) {
                    miniQuery += ",";
                }
            }
            // Close the bracket
            miniQuery += ")";
            return miniQuery;
        }
    }

    private String likeParser() {
        return this.fieldName + " LIKE " + "\'" + this.fieldValues.get(0) + '\'';
    }

    private String comparisonParser() {
        Object value = this.fieldValues.get(0);
        String miniQuery = this.fieldName + " " + this.operator + " ";
        if (value instanceof String) {
            miniQuery += "\'" + (String) value + "\'";
        } else {
            miniQuery += (String) value;
        }

        return miniQuery;
    }

    // NOTE: Define PUBLIC Functions
    // Setter Functions
    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldValues(ArrayList<Object> fieldValues) {
        this.fieldValues = fieldValues;
    }

    @Override
    public String toString() {
        if (miniQuery == null) {
            String output = "Operator: " + this.operator + "\n";
            output += "fieldName: " + this.fieldName + "\n";
            output += "fieldValues: " + this.fieldValues.toString();
            return output;
        } else {
            return this.miniQuery;
        }
    }

    public String miniQueryBuider() {

        List<String> logicalList = Arrays.asList(this.supportedLogicalOperators);
        List<String> comparisonList = Arrays.asList(this.supportedComparisonOperators);

        // Handle Operations
        try {
            if (logicalList.contains(this.operator.toUpperCase())) {
                switch (this.operator.toUpperCase()) {
                    case "BETWEEN":
                        this.miniQuery = this.betweenParser();
                        break;
                    case "LIKE":
                        this.miniQuery = this.likeParser();
                        break;
                    case "IN":
                        this.miniQuery = this.inParser();
                        break;
                    default:
                        System.out.println(this.operator.toUpperCase());
                        throw new Exception("[ERROR]: The heck is this?!!!");
                    // break;
                }
            } else if (comparisonList.contains(this.operator)) {
                this.miniQuery = this.comparisonParser();
            } else {
                throw new Exception("[ERROR]: Unsupported Operation given: " + this.operator);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return this.miniQuery;
    }
}

class JoinOperation {
    String joinType;
    String operator;
    String field1;
    String field2;
    String joinTable;

    // NOTE: Define Public
    public JoinOperation() {
    }

    // Define Setter functions
    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public void setJoinTable(String joinTable) {
        this.joinTable = joinTable;
    }

}

public class JSONQuery {

    public String mainOperation;
    public ArrayList<String> columns;
    public ArrayList<String> joiners;
    public String tableName;
    public ArrayList<Operation> operations;
    public ArrayList<JoinOperation> joinOperations;
    private String[] supportedComparisonOperators = {
            "=", ">", "<", ">=", "<=", "<>"
    };

    // NOTE: Define Constructor
    public JSONQuery(
            String mainOperation,
            ArrayList<String> columns,
            ArrayList<String> joiners,
            String tableName,
            ArrayList<Operation> operations,
            ArrayList<JoinOperation> joinOperations) {

        // Handle the constructor
        this.mainOperation = mainOperation;
        this.columns = columns;
        this.joiners = joiners;
        this.tableName = tableName;
        this.operations = operations;
        this.joinOperations = joinOperations;
    }

    public String parseSQLQuery() {

        String query = "";
        List<String> comparisonList = Arrays.asList(this.supportedComparisonOperators);
        query += this.mainOperation;
        query += " ";
        for (int i = 0; i < this.columns.size(); ++i) {
            query += columns.get(i);

            if (i != this.columns.size() - 1)
                query += ", ";
        }
        query += " FROM " + this.tableName;

        // Checking for WHERE conditions
        if (operations.size() == 0 && joinOperations.size() == 0) {
            return query;
        }

        // Proceed with WHERE
        if (joinOperations.size() > 0) {

            // Handling only one join functionality for now.
            JoinOperation joinOperation = joinOperations.get(0);

            // Check if operator is a comparison operator
            if (comparisonList.contains(joinOperation.operator)) {
                if (joinOperation.joinType == null) {
                    query += ", " + joinOperation.joinTable + " WHERE ";
                    query += joinOperation.field1 + " " + joinOperation.operator + " " + joinOperation.field2;
                } else {
                    query += " " + joinOperation.joinType + " " + joinOperation.joinTable + " ON ";
                    query += joinOperation.field1 + " " + joinOperation.operator + " " + joinOperation.field2;
                }
            }
        }

        if (operations.size() == 0) {
            return query;
        }

        if (joinOperations.size() > 0 && joinOperations.get(0).joinType == null) {
            query += " AND ";
        } else {
            query += " WHERE ";
        }
        String joiner;
        Iterator<String> joinerIterator = joiners.iterator();
        for (Operation operation : this.operations) {
            query += operation.miniQueryBuider();
            if (joinerIterator.hasNext()) {
                joiner = joinerIterator.next();
                query += " " + joiner + " ";
            }
        }

        return query;
    }
}

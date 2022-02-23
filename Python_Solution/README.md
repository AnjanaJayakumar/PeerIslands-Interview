# Python JSON to SQL Parser

**Author:** Anjana Jayakumar <br>
**Version:** 0.1-alpha <br>
**Date Published:** 23/02/2022 (DD/MM/YYYY)

## Introduction

This is a basic Python SQL Parser that takes an input.json file of the desired format and converts it into a SQL Query.


## Requirements

This program is built and tested in Windows 10 Environment. The basic software
requirements are as follows:

- Python 3.8.4

## Expected JSON Schema

The Following is the expected JSON Schema for this program to work. Future iterations will relax these constraints.

```json
{
    "type": "SELECT", // Main Operation
    "columns": [  // List of Columns to Select
        "*"
    ],
    "joiners": [], // List of AND, OR Operators
    "table_name": "iris", // Table Name
    "operations": [], // List of operations
    "joinOperations": [] // List of Join Operations
}
```

An example complicated SQL Query is as follows:
```sql
SELECT Categories.CategoryName, Categories.Description, Products.ProductName FROM Categories inner join Products ON Categories.CategoryID = Products.ProductID WHERE Products.ProductName LIKE 'c%' AND Categories.CategoryName IN ( 'Beverages', 'Condiments')
```

The JSON Version following my Schema would be as follows:

```json
{
    "type": "SELECT",
    "columns": [
        "Categories.CategoryName",
        "Categories.Description",
        "Products.ProductName"
    ],
    "joiners": [
        "AND"
    ],
    "table_name": "Categories",
    "operations": [
        {
            "operator": "LIKE",
            "fieldName": "Products.ProductName",
            "fieldValue": [
                "c%"
            ]
        },
        {
            "operator": "IN",
            "fieldName": "Categories.CategoryName",
            "fieldValue": [
                "Beverages",
                "Condiments"
            ]
        }
    ],
    "joinOperations": [
        {
            "joinType": "inner join",
            "operator": "=",
            "field1": "Categories.CategoryID",
            "field2": "Products.ProductID",
            "joinTableName": "Products"
        }
    ]
}
```

## Executing the Project

1. Open a terminal in the current folder and run `python solution.py`. This program will prompt you to enter the path of the `.json` input file. A sample JSON file is provided as `input.json` in the same folder.
2. Following this, the output SQL query for the JSON provided will be printed in the console window.


## Thank you
Thank you for the wonderful opportunity to work on this beautiful yet challenging problem statement. The solution implemented here is just a simple solution. Most SQLParsing techniques use some type of trees to construct a query tree which is pruned and optimized to generate the final query string. However, due to the time constraint, I was not able to implement such a arduous solution and went ahead with a simpler solution. I thank you for considering my application and am eager to discuss all my solutions with peerIslands via the Interview Process. Thank you. 

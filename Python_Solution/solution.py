# import the necessary libraries
# import pyodbc
import json
from typing import Dict, List, Callable, Tuple

__author__ = "Anjana Jayakumar"
__version__ = "0.1-alpha"
__description__ = """
This is a simple python JSON to SQL parser program which takes 
a .json file following the JSON schema given in README.md, and
converts it to a valid SQL query.
"""
__date_published__ = "23/02/2022 (DD/MM/YYYY)"


class QueryBuilder:
    def __init__(self, json_file: str):
        self.json_file = json_file
        self.comparison_opns = ['=', '>', '<', '>=', '<=', '<>']
        self.logical_opns = ['AND', 'ALL', 'ANY', 'BETWEEN',
                             'EXISTS', 'IN', 'LIKE', 'NOT', 'OR', 'SOME', '']

    def get_values(self) -> Tuple:
        """Function to get the JSON values

        Returns:
            Tuple: values in the JSON
        """
        joiners = self.json_file.get('joiners')
        operations = self.json_file.get('operations')
        columns = self.json_file.get('columns')
        type = self.json_file.get('type')
        table_name = self.json_file.get("table_name")
        join_operations = self.json_file.get("joinOperations")
        return joiners, operations, columns, type, table_name, join_operations

    def between_solver(self, field_name: str, field_value: List) -> str:
        """Function that returns the query substring for BETWEEN operation

        Args:
            field_name (str): name of the column 
            field_value (List): values 

        Returns:
            str: substring for BETWEEN 
        """
        if len(field_value) != 2:
            raise ValueError
        query = f"{field_name} BETWEEN {field_value[0]} AND {field_value[1]}"
        return query

    def in_solver(self, field_name: str, field_value: List) -> str:
        """Function that returns the query substring for IN operation

        Args:
            field_name (str): name of the column 
            field_value (List): values 

        Returns:
            str: substring for IN 
        """
        if len(field_value) < 1:
            raise ValueError

        query = f" {field_name} IN ("
        # iterate through the list of values and add it to the query substring
        for val in range(len(field_value)):
            value = field_value[val]
            if isinstance(value, str):
                query += f" '{value}',"
            else:
                query += f" {value},"
        query = query[:-1]    # remove the last comma from the substring
        query += ')'
        return query

    def like_solver(self, field_name: str, field_value: List) -> str:
        """Function that returns the query substring for LIKE operation

        Args:
            field_name (str): name of the column 
            field_value (List): values 

        Returns:
            str: substring for LIKE 
        """
        value = field_value[0]
        query = f"{field_name} LIKE '{value}'"
        return query

    def sql_builder(self) -> str:
        """Main method to parse the JSON and return the final query

        Raises:
            Exception: raise when the given operator is not present in the list of pre-defined operators

        Returns:
            str: final query after parsing the JSON
        """
        # get the values
        joiners, operations, columns, type, table_name, join_operations = self.get_values()

        if not operations:
            operations = []
        if not join_operations:
            join_operations = []

        # start the query
        query = type

        # go through the list of columns
        if columns and len(columns) > 0:
            for col_name in columns:
                query += col_name + ','
            query = query[:-1]    # remove the last comma

        else:
            query += " * "

        # add the main table name
        query += f" FROM {table_name}"

        # return the query if join_operations AND operations is empty
        if (join_operations and len(join_operations) == 0) and (operations and len(operations) == 0):
            return query

        try:
            # check if there are any joins defined
            if join_operations and len(join_operations) > 0:
                join_operations = join_operations[0]
                field_1 = join_operations['field1']
                field_2 = join_operations['field2']
                join_table = join_operations['joinTableName']
                operator = join_operations['operator']
                join_type = join_operations['joinType']

                # check if the operator is a comparison operator
                if join_operations['operator'] in self.comparison_opns:
                    if join_operations['joinType'] == "":
                        query += f", {join_table} WHERE {field_1} {operator} {field_2}"
                    else:
                        query += f" {join_type} {join_table} ON  {field_1} {operator} {field_2}"
                else:
                    raise ValueError

            # return the query if operations is empty
            if len(operations) == 0:
                return query

            if len(join_operations) > 0:
                if join_type == "":
                    query += " AND "
            else:
                query += " WHERE "
            ctr = 0

            # iterate through the list of operations
            for operation in operations:
                operator = operation.get('operator')
                field_value = operation.get('fieldValue')
                field_name = operation.get('fieldName')

                # check if the operator is a comparison operator
                if operator in self.comparison_opns:
                    if isinstance(field_value, str):
                        query += f" {field_name} {operator} '{field_value}'"
                    else:
                        query += f" {field_name} {operator} {field_value}"

                # check if the operator is a logical operator
                elif isinstance(operator, str):
                    operator = operator.upper()
                    if operator in self.logical_opns:
                        if operator == "BETWEEN":
                            query += self.between_solver(field_name,
                                                         field_value)
                        elif operator == "IN":
                            query += self.in_solver(field_name, field_value)
                        elif operator == "LIKE":
                            query += self.like_solver(field_name, field_value)
                    else:
                        raise ValueError

                else:
                    raise ValueError

                # add the joiner between multiple operations
                if ctr+1 < len(operations):
                    query += f" {joiners[ctr]} "
                    ctr += 1

            return query

        except Exception as e:
            print("[ERROR]: The program made an owie :(")
            print(e)


# connect to a SQL database
# conn = pyodbc.connect('Driver={SQL Server};'
#                       'Server=LENOVO-PC\SQLEXPRESS;'
#                       'Database=Northwind;'
#                       'Trusted_Connection=yes;')


# get the input json file path:
json_path = input("Enter the path of the JSON file: ")

# parse the json
try:
    if (json_path[-5:] != '.json'):
        raise ValueError

    with open(json_path) as file:
        json_file = json.load(file)

    query_builder = QueryBuilder(json_file=json_file)
    query = query_builder.sql_builder()
    print("\nSQL Query: \n", query)

except ValueError as e:
    print("[ERROR]: The file should have a .json extension")

except Exception as e:
    print("[ERROR]: The program made an owie :(")
    print("[ERROR]: ", e)

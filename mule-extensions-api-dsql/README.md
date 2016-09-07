# Mule Extensions API Dsql

Provides a set of classes and interfaces to manage and translate DSQL (Datasense Query Language) queries.

## Building this module

This module depends on an [ANLTR](http://www.antlr.org/) grammar that is used to generate a set of classes
that are used to parse DSQL queries, those classes are not uploaded to the repository, meaning that the code 
won't compile right away. 

Running `mvn clean compile` will trigger the classes generation on the `validate` phase.

## Examples

### Parsing a DSQL query from a String

Using the DsqlParser you can create a DsqlQuery object that is java representation of a DSQL query.

    String query = "dsql:select * from addresses order by name desc";
    DsqlQuery dsqlQuery = DsqlParser.getInstance().parse(query);   

### Translating a DSQL query 

Once you have a parsed DsqlQuery object, you can use a custom QueryTranslator to translate from a DSQL
to the Native Query Language of your API domain. 

    QueryTranslator translator = new NativeQueryTranslator();
    String nativeQuery = dsqlQuery.translate(trasnlator);


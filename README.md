# Berlin Election Explorer 

This is a browser based app that can compute a result for each of berlin's election districts from a given formula. It is based on Spring framework and Vaadin to generate the UI. The data is populated from a berlin open data csv file into a mongoDb instance at startup. 
It was created as a PoC to explore various techniques of the Spring framework, like SpEL (Spring Expression Language) and GWT based Vaadin. This is considered work in progress. Any contribution is welcome.

## setup

- Run a mongoDb instance, e.g. with docker 

`docker run --net=host mongo`

- Start the application e.g. by using maven

`mvn spring-boot:run`


## usage

The User can define variables and therefore more complex queries can be created. Variable definitions are computed in order of appearance in the user interface. Enter the formula into the field entitled with "Data Destillery" and press Enter to trigger the computation. The result is then available in the table next to the csv content.


### syntax

Use Spring Expression Language syntax, see the [docs](http://docs.spring.io/autorepo/docs/spring-framework/4.0.0.RC2/spring-framework-reference/html/expressions.html)

There is one empty variable definition by default. '+' adds as many as needed.

If referencing a table column header from the csv, please refer to it with a preceeding hash and use uppercase.

example: `#WAHLBERECHTIGTE_INSGESAMT`

E.g., to compute the percentage for a party of all people allowed to elect:

`#BIG / (#WAHLBERECHTIGTE_INSGESAMT + 1) * 100` *(adding 1 to avoid error dividing by zero)*


### defining variables

- use the definition fields to define your variables, e.g.

`INHABITANTS = 3652957`

- use it later in your formula with preceeding hash

`#WAHLBERECHTIGTE_INSGESAMT / #INHABITANTS`


### election data

Currently election data is hardcoded for the Wahl [Berliner Abgeordnetenhaus 2016](https://www.wahlen-berlin.de/wahlen/be2016/afspraes/index.html) and only the "Erststimmen" are taken into account. There are also several tweaks applied to the csv file.


### features missing

- select election year, type and vote type
- make comparisons / timelines

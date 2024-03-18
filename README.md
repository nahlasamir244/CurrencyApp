# Currency Conversion App

multi module android demo for currency conversion using fixer API

1. App module : for simplicity holds the presentation layer and navigation logic
2. Core modules : holdes the shared logic and utils for each layer 
3. Model module : holds the common dtos and classes for the app 

## Architecture pattern: 
MVI model view intent single activity architecture following the Data-Domain-Presentation clean architecture and the UDF unidirectional data flow pattern with repository pattern for data as a layer over different offline/online data sources

## Libraries and dependencies:
1. Constraint Layout for flexible relative positioning and sizing of views
2. Support for different English/Arabic local
3. Coroutines and flow for asynchronous operations (networking or data store operations)
4. data binding
5.  Navigation component for handling transitions between fragments
6.  Dagger-Hilt for dependency injection
7.  Retrofit/okhttp for networking

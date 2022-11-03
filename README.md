# compose

# Project structure

The project have the following structure:
 - app - app module
 - core 
   - domain - set of basic functionality and abstractions for the domain layer 
   - ui - set of basic functionality and abstractions for the ui layer 
   - features - set of basic functionality and abstractions for the feature managment
 - features
   - splash - splash feature, just to illistarte internal navigation
   - main - main screen feature

# Libarries

 The project uses the following libraries
  - compose
  - compose-navigation
  - retrofit
  - coil
  - hilt
  
# Architecture   
Project meet SOLID principles. UI Lauer uses MVVM approach.
The project contains of set isolated features which are should meet some requirements:
 - Each feature isolated into specific module
 - Each feature should use dagger hilt for DI
 - Each feature provided public interface for building navigation graph.
 - Each feature should not have depependencies from other features. Dependencies should be organised as DI dependency which are defined in that feature.
 
 

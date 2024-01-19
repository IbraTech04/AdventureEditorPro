# AdventureEditorPro
CSC207H5 Final Project - Fall 2023

*Created by Ibrahim Chehab, Themba Dube, Faizaan Bhemat, and Abigail Yanku*

AdventureEditorPro is the ultimate adventure game development tool. It allows you to create your own adventure games which comply with the Assignment 2 format from CSC207H5. It also allows you to edit existing adventure games with ease, without ever having to touch a text file!

## Preface
### What is AdventureEditorPro?
AdventureEditorPro is a JavaFX application that allows you to create and edit adventure games. It is designed to be easy to use, and is fully accessible. It is also designed to be used by everyone, and is fully keyboard-navigable. It is the ultimate adventure game development tool, and is the perfect companion to Assignment 2!

### Why did we make AdventureEditorPro?
For those of you who have taken CSC207H5 in the fall of 2023, you will recall the consistent theme of "Adventure Games" throughout the course. You may also recall the trouble you went through to debug Assignments one and two, due to the difficulty of compiling valid game files by hand. Well, that's exactly why we made AdventureEditorPro! We wanted to make it easier for students to create and edit adventure games, and to make it easier for them to debug their games. 

### How long did AdventureEditorPro take to make?
Roughly a month and a half. We worked on this project in parallel with the rest of our coursework, balancing both the planning, documentation, and actual development of the project with our other courses. 

## Features
### Create New Games
Want to create your own adventures to use with Assignment 2? No problem! AdventureEditorPro allows you to create new games from scratch without ever touching a text file! Add rooms and objects, assign images and paths, and export to an A2-compliant folder structure with the click of a button!

### Edit Existing Games
AdventureEditorPro also allows you to edit existing games with ease. Simply load the game from a folder, and you can edit it just like you would a new game. You can add new rooms and objects, edit existing ones, and even delete them if you want! All without ever touching a text file!

### Visualize Paths
Having trouble solving your game? Are you stuck in a loop? AdventureEditorPro can help! The all-new PassageCycle system allows for features like the PathVisualizer, allowing you to easily preview your entire game at a glance with the click of a button! Easily find infinite loops, dead-ends, and more!

### Edit Rooms and Objects
Easily add and remove rooms and objects, including their corresponding images, descriptions, and more! You can even add and remove exits between rooms, and add and remove objects from rooms! All without ever touching a text file!

### Easily edit passages
Don't like the feel of a game? Want to make it harder? Well look no further! The PassageCycle system allows for instantaneous and intuitive passage editing! Simply click on a passage to edit it, and let AdventureEditorPro do the rest! You can even add and remove passages with ease!

### Fully Accessible - Designed for Everyone!
Accessibility was a central focus when developing AdventureEditorPro. As such, you can find several accessibility features built-in to the application. For instance, our default color pallate is not only appealing to the eye, but is also made with contrast in mind. You'll find all the colors throughout AdventureEditorPro to have a contrast ratio of at least 4.5:1, making it easy to see for everyone! We also support native screen-reader support, and even have TTS functionality built-in! Features like these make AdventureEditorPro the perfect compliment to Assignment 2!

## Project Breakdown
### Model - Ibrahim Chehab and Themba Dube
The model package contains all the classes that are used to represent the game. This includes the game itself, the rooms, the items, and the characters. The model package also contains the classes that are used to read and write the game to a file. 

The model package is a modified version of the Assignment 2 model, providing several QoL improvements such as the PassageCycle (replacing the PassageTable), improved data structures (for improved efficiency), and more! Otherwise, it remains equivalent to the Assignment 2 model, even sharing the same public-facing API. The only new functionality that was added was saving the current model to the A2-compliant folder structure, instead of serializing it to a file.

### View - Abigail Yanku and Faizaan Bhemat
The view is all-new for AdventureEditor. It was built from the ground up to provide a user-friendly interface for creating and editing adventure games. It is built using JavaFX, and is designed to be intuitive and easy to use. It also implements several accessibility features, such as a high-contrast color pallate, native screen-reader support, and TTS functionality.

### Controller - Ibrahim Chehab and Themba Dube
The controller is the glue that holds the model and the view together. It is responsible for handling user input, and updating the model and view accordingly. It is also responsible for saving and loading games to and from files.

## Installation
### Prerequisites
- Java 20 or higher
- JavaFX 
- FreeTTS
- JUnit 5
- IntelliJ IDEA (recommended)

### Installation Instructions
1. Clone the repository by running `git clone https://github.com/IbraTech04/AdventureEditorPro.git` in your terminal.
2. Open the project in IntelliJ IDEA (or your preferred IDE).
3. Add the JavaFX and FreeTTS libraries to the project.
4. Run the project.
5. ???
6. Profit!

## Academic Integrity Reminder 
If you are a student at the University of Toronto taking CSC207H, please remember that **you are responsible for following the University's [Academic Integrity Policy](https://governingcouncil.utoronto.ca/secretariat/policies/code-behaviour-academic-matters-january-1-2021).** You are reminded that copying any code from this repository **without proper citation** constitutes plagiarism and may result in an academic offense being raised against you. Should you find yourself in a situation where you are tempted to copy code from this repository, please take a step back and consider using course resources such as Office Hours or Piazza for assistance instead.

If you are *not* a student at the University of Toronto, please remember that you are responsible for following the Academic Integrity Policy of your institution. You are reminded that copying any code from this repository without proper citation may constitute plagiarism. You are encouraged to consult your institution's Academic Integrity Policy for further guidance.

Remember that copying code blindly is not only academically dishonest, but also counterproductive to your learning. If you are struggling with a concept, please reach out to your instructor or TA for help. They are there to help you, and will be more than happy to do so! This repository is intended to be used as a reference, not as a source of code to copy. Please use it responsibly.
# Assignment 1
Read input file for jeopardy categories, points, questions, and answers. Create a jeopardy game that is played through the console. 
## Organization
1. My program is split into five classes: Main, Game, Team, Question and FinalJeopardy. The Main class is what reads the input file and runs the entire program. The Game class deals with everything after the Main class starts the game, from prompting team names and asking questions. The Team class is for the Team object, which contains various variables such as String name, int totalPoints, etc. and getters for them. The Question and FinalJeopardy classes are for Question objects and the FinalJeopardy object. These hold String variables for the question and answer. 

### Notes
1. My input.txt file is also included in the project folder. This was used for testing purposes.
2. The jeopardy game does not allow teams with total points with 0 points or less to participate in final jeopardy.
3. If all teams have negative points by final jeopardy, the program still determines the "winner" based on the negative points.
# Talio - the task organizer application

## Description of project

"Talio" is the application in which users can create lists (such as "TODO" or "DONE"), in which cards (tasks) can be created.

## Group members

| Profile Picture                                                | Name | Email |
|----------------------------------------------------------------|---|---|
| <img src="docs/readme_profile_photos/Jan.jpg" width="50">      | Jan Maris | J.W.Maris@student.tudelft.nl |
| <img src="docs/readme_profile_photos/aldas.jpg" width="50">    | Aldas Lenkšas | A.Lenksas@student.tudelft.nl |
| <img src="docs/readme_profile_photos/aiste.jpg" width="50">    | Aistė Macijauskaitė | A.Macijauskaite@student.tudelft.nl |
| <img src="docs/readme_profile_photos/leonardo.JPG" width="50"> | Leonardo Marcuzzi | L.Marcuzzi@student.tudelft.nl |
| <img src="docs/readme_profile_photos/rebecca.jpg" width="50">  | Rebecca Andrei | N.R.Andrei@student.tudelft.nl |

## How to run it

*Note*: All team members were using Windows as their operating system, IntelliJ IDEA as their editor, and 
Java SDK 17 as their Java SDK version.
The steps below are written assuming the same environment.

1. Clone the repository from GitLab. If cloning with SSH, this can be done with the following command:
```
git clone git@gitlab.ewi.tudelft.nl:cse1105/2022-2023/teams/oopp-team-79.git
```

2. Open the project (which is cloned folder `oopp-team-79`) as 
the IntelliJ IDEA project.

3. To run the server, `server.Main` should be run. The full path of this class is `server/src/main/java/server/Main.java`.
If the server was run correctly, there should be a message `Application is running!` when going to `http://localhost:8080/`.

4. To run the client, `client.Main` should be run. The full path of this class is `client/src/main/java/client/Main.java`. Note that the client is using JavaFX, so the application should
be run with VM options that are found in `client/README.md` file.

*Note*: both running the server and running the client can be done by first
creating IntelliJ run configuration (don't forget to add required VM options
to the Client application run configuration). 
This way, it is easier to run the application.

## How to contribute to it

We are not expecting any contributions from the open source community anymore, as this 
project was made for the course at the university, and currently we are not planning
to continue it.

## Copyright / License (opt.)

This project was created for the course CSE1105 OOP Project
(2022/23 Q3) at the Delft University of Technology.

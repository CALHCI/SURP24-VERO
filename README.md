# VERO

**Virtual Environment for Robot Operation (VERO)** is an application that processes user input from a **Meta Quest 3 headset** and converts it into commands for controlling a **UR cobot**.

<br>

### Dashboard

The dashboard module is an application with two elements
- ControlPanel is a Java Desktop application that starts the server, displays the x and y coordinates in real-time on screen, allows the user to alter the starting positions.
- HeadsetSimulator is a server streaming sample x and y coordinates to the dashboard (draws 3/4ths of a circle).
- Server is the websocket server which recieves x and y coordinates from the VR headset and translates them into 3d space for the cobot arm.


### VR MQTT Project
- A unity project which tracks user hands and streams them to an MQTT broker which can be accessed by the Java Control Panel app


## To Do List

- [X] :one: Modify the class Subscriber.java to receive the VR data stream (the method run).
- [X] :two: Modify the class Blackboard.java to do the math processing needed per value received (create a new method and call it in add before adding point to the data structure points.
- [X] :three: Create instructions for the robot (it is OK to just print them on console or use the logger.
- [X] :four: Try the instructions feeding them manually to the UR cobot.

## Stage 1

- [ ] :one: Send stream via websocket to robot.
- [ ] :two: Send commands to the robot.


## Stage 2

- [ ] :one: Paper and Video for the HRI conference.

## Setup
1. Run a clean maven install
2. Run `controlPanel/Main.java` to run the dashboard
3. Configure the origin, delta X, and delta Y robot coordinates in `Settings > Configure`
4. Start server by clicking `Server > Start Server`
5. Run `headsetSimulator/Main.java` to run the simulation


## Unity Project Setup
1. Install Unity Hub and Unity
2. From within Unity Hub, make sure you have Unity version 2022.3.42f1 installed
3. Clone this repository to Finder/File Explorer
4. Open Unity Hub, in Projects tab click Add > Add project from disk
5. Select the "Unity MQTT Proj" folder from this repository
6. Open scene called "MQTT3dTestProj" in Assets folder

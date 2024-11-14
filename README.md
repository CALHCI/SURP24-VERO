# VERO

**Virtual Environment for Robot Operation (VERO)** is an application that processes user input from a **Meta Quest 3 headset** and converts it into commands for controlling a **UR cobot**.

<br>

## Modules
### Dashboard

The dashboard module is an application with two elements
- ControlPanel is a Java Desktop application that starts the server, displays the x and y coordinates in real-time on screen, allows the user to alter the starting positions.
- HeadsetSimulator is a server streaming sample x and y coordinates to the dashboard (draws 3/4ths of a circle).
- Server is the websocket server which recieves x and y coordinates from the VR headset and translates them into 3d space for the cobot arm.


### VR WS Project
- A unity script which uses XR to display a canvas which sends the translated coordinate out via websockets (WIP)

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


> [!IMPORTANT]  
> Add Unity project install guide here when ready.

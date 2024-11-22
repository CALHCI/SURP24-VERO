# VERO

**Virtual Environment for Robot Operation (VERO)** is an application that processes user input from a **Meta Quest 3 headset** and converts it into commands for controlling a **UR cobot**.

> [!NOTE]  
> Removing the instructions about how to clone a Github repository assuming you all are knowledgeable on that.

<br>

### Dashboard

The dashboard module is an application with two elements
- Headset simulator is a server streaming random x and y coordinates to the dashboard.
- Dashboard is a Java Desktop application that displays the x and y coordinates in real-time on screen.


## To Do List

> [!NOTE]  
> Add who is responsible below.

- [ ] :one: Modify the class Subscriber.java to receive the VR data stream (the method run). - **Who?**
- [ ] :two: Modify the class Blackboard.java to do the math processing needed per value received (create a new method and call it in add before adding point to the data structure points. - **Who?**
- [ ] :three: Create instructions for the robot (it is OK to just print them on console or use the logger. - **Who?**
- [ ] :four: Try the instructions feeding them manually to the UR cobot. - **Who?**

## Stage 1

- [ ] :one: Send stream via websocket to robot.
- [ ] :two: Send commands to the robot.


## Stage 2

- [ ] :one: Paper and Video for the HRI conference.

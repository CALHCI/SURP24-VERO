# VERO

**Virtual Environment for Robot Operation (VERO)** is an application that processes user input from a **Meta Quest 3 headset** and converts it into commands for controlling a **UR cobot**.

> [!NOTE]  
> Removing the instructions to clone a github repository assuming you are knowledgeable on that.


## Modules

This are the modules in this repository as today.1

### Dashboard

The dashboard module is an application with two elements
- Headset simulator is a server streaming random x and y coordinates to the dashboard.
- Dashboard is a Java Desktop application that displays the x and y coordinates in real-time on screen.

### WebSockets

> [!NOTE]  
> Tell me what this is.


## To Do List

> [!NOTE]  
> Add who is responsible below.

- [ ] Modify the class Subscriber.java to receive the VR data stream (the method run). - **Who?**
- [ ] Modify the class Blackboard.java to do the math processing needed per value received (create a new method and call it in add before adding point to the data structure points. - **Who?**
- [ ] Create instructions for the robot (it is OK to just print them on console or use the logger. - **Who?**
- [ ] Try the instructions feeding them manually to the UR cobot. - **Who?**

## Stage 1

- [ ] Send stream via websocket to robot.
- [ ] Send commands to the robot.


## Stage 2

- [ ] Paper and Video for the HRI conference.

# VR-UR

VR-UR is an application that processes user input from a **Meta Quest 3 headset** and converts it into commands for controlling a **UR cobot**.

> [!NOTE]  
> Removing the instructions to clone a github repository assuming you are knowledgeable on that.


## Stage 1

> [!NOTE]  
> Add responsible below.

- [ ] Convert VR information to usable format. (x, y) - **create a folder/package or link it here**
- [ ] Recieve raw data websocket stream. - **a simple socket or MQTT could work**
- [ ] Clean stream.
- [ ] Use threading, buffering, and interpolation to prevent backlog of actions (if delay or stutter is present).
- [X] Convert VR stream into robot information stream.
- [ ] Send stream via websocket to robot.
- [ ] Handle robot commands. - **create a folder/package or link it here**
- [ ] Paper and Video for the HRI conference.

## Stage 2
- [ ] talk to you later

## Stage 3
#### Talk with Javier of course but premptively...
- [ ] Monolithic to modular via a microservice structure communicating using a message broker (Springboot and RabbitMQ) - **check Mosqutto MQTT**

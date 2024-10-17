# VR-UR
### VR-UR is an application that processes user input from a VR headset and converts it into commands for controlling a robotic arm to do some basic painting.
* Be sure to follow Git Flow: make a "feature-<feature-name>" branch when developing which merges into the "develop" branch when done; Merge to "release" branch when ready for bugfixing; Merge to "main" only when ready to deploy.

### Installation
```bash
   git clone https://github.com/CALHCI/VR-UR.git
   cd VR-UR
   # There's a way to use git flow built in?
```

## Stage 1
#### Deadline: (12/6/24)
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

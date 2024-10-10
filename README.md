# VR-UR
VR-UR is an application that processes user input from a VR headset and converts it into commands for controlling a robotic arm to do some basic painting.

## Stage 1
#### Deadline: (12/6/24)
- [ ] Convert VR information to usable format. (x, y) works for now. *Seperate repo*
- [ ] Recieve raw data websocket stream.
- [ ] Clean stream.
- [ ] Convert VR stream into robot information stream.
- [ ] Handle interpolation between packets to prevent backlog of actions.
- [ ] Send stream via websocket to robot.
- [ ] Handle robot commands. *Seperate repo*
- [ ] Make a writeup for grants

## Stage 2
#### Talk with Javier of course but premptively...
- [ ] Monolithic to modular via a microservice structure communicating using a message broker (Springboot and RabbitMQ)

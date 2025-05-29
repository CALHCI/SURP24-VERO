# VERO

**Virtual Environment for Robot Operation (VERO)** is an application that processes user input from a **Meta Quest 3 headset** and converts it into commands for controlling a **UR cobot**.

### Dashboard

The dashboard module is an application with two elements

-   ControlPanel is a Java Desktop application that displays the MQTT x and y coordinates in real-time on screen, creates robot commands, sends commands via socket, and allows the user to alter the arm's starting positions.
-   CircleSimulator sends x and y coordinates via MQTT to the dashboard (draws an identical circle).
-   DrawingSimulator sends x and y coordinates via MQTT to the dashboard (allows the user to draw and see drawn points).
-   Server is the MQTT server which receives x and y coordinates from the dashboard and translates them into 3d space for the cobot arm.

### VRUR_Unity

-   Unity-based VR app that tracks precise hand ray hit points on a virtual canvas.
-   Data is streamed to an MQTT broker, allowing collaboration with Java app for visualization and translation to robot commands.

## Setup

1. Run a clean maven install.
2. Run `controlPanel/Main.java` to run the dashboard.
3. Configure the origin, delta X, and delta Y robot coordinates in `Settings > Configure`.
4. Start server by clicking `Server > Start Server`.
5. Sideload `final.apk` or build it yourself.
6. (Optionally) Run `circleSimulator/Main.java` or `drawingSimulator/Main.java` to run a simulation of the VR data.

## Unity Build Guide

1. Install Unity Hub.
2. From within Unity Hub, install Unity version 2022.3.42f1 and add the "Android Build Support" module.
3. Open Unity Hub, add the cloned folder.
4. Open scene `Assets/Scenes/SampleScene.unity`.
5. (Optionally) Install "Meta Quest Link" and plug in headset to run the live "play mode". You might need the "Meta Quest Developer Hub" app to enable auto link.
6. Switch to Android build and just build.

## VR Use Guide

1. Enable developer mode on your Quest.
2. Sideload the apk using "SideQuest" or "Meta Quest Developer Hub".
3. Ensure connection to the internet.
4. Run the apk found in the "Unknown Sources" tab.

-   Move the panel by gripping the sides of the panel (finicky with hand gestures).
-   Draw with the trigger or by pinching while pointing at the panel.

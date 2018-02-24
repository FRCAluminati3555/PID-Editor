# PID-Editor
## Hello There! ##
  This is a PID Editor program that was written for the talon srx. This will let you edit the PID values in real time and watch the data in a graph as well, without having to reupload code everytime to test the constants. 

## How To Use ##
  To use this, there is just a tiny bit of code to change. In the robot package, Robot.java, you need to change / add CANTalon monitors that are the ids of the talons you plan to use. Next, in the networking package, Server.java, there are two static variables, host and port, use the ip you plan to use (they are labeled accordingly) or supply the one you are going to use. The same goes for the port. Once that is all set up, upload the project to the rio. Next, go to the Editor package, PIDLauncher.java, and run it. This is the client that allows you to edit properties of the talon. Keep in mind that it is likely that you need to upload the robot project after closing the client. 

  Once the client has started and the robot is communicating, enable the robot. Then input the id of the talon you wish to edit. There are two components, the PIDEditor and the Graph. The editor is the UI that allows you to change the values, Graph is well... the graph. Here there is also load and export UI. This will save the locations of the UI elements currently on the screen. NOTE this will not save the values in the editor, read on to see how that works. Load UI will load in the UI elements that are saved and have there ids registered in the monitor manager (the robot.java part where you changed the ids of the talon monitors). 
  
  When using the graph, right click on it to select what you want to watch. Additionally, there is an export option. This will export an excel file to your desktop of the data from this graph. 
  
   For the editor, there is also and export button. This will save the essential data of the editor to a file to read from later. When an editor is created it will load the values of the file that has the same id number, if there is one. 

## Issues ##
Feel free to pose issues here on the github post if you find a bug, just keep in mind this is in development and is not complete.

# FRC 2015 Tote Computer Vision

This software was used to look for the vision targets that were on the side of the totes (gamepieces). The robot was positioned so as to drive past them during autonomous mode, then stop once lined up with the tote and pick it up from the side.

### Hardware:
This software runs on an Nvidia Jetson TK1 devkit that has an Axis IP camera attached to it. The Jetson is then connected through an ethernet switch to the NI RoboRIO (robot's controller).

### How does it work?
An explanation of vision targets is available here: https://docs.wpilib.org/en/latest/docs/software/vision-processing/introduction/target-info-and-retroreflection.html

The vision targets used on the side of the totes are similar to those in the article. The software tracks the horizontal position of the vision target and sends it back to the RoboRIO for further processing.

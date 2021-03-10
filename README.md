# FlinkRPiDrone

### Introduction

Apache Flink is a framework and distributed processing engine for stateful computations over unbounded and bounded data streams. Flink has been designed to run in all common cluster environments, perform computations at in-memory speed and at any scale. Because of its ability to handle streaming of data, Apache Flink was chosen as a framework for the drones.

Apache Flink is deployed on a Raspberry Pi cluster, where the master node is on the ground and the worker nodes are on the drones. The master node submits and assigns jobs to the worker nodes that are on-board of the drones. The worker nodes then gather sensor data through Navio2 and stream to the framework of our choice.

The drones are equipped with a Raspberry Pi 4 and Emlid's Navio2, an autopilot HAT with various sensors, such as accelerometer, gyroscope, magnetometer, and GPS, that will send data through Apache Flink. The drones are also equipped with a Raspberry Pi camera module that will stream the video at their respective IP address at port 8000.

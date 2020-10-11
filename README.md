# Cerebrum

## A Mouse/Keyboard Sharing Tool

For some backstory, this was a project that I started in order to dive deeper into learning the capabilities of [JavaFX](https://openjfx.io/). 
I stumbled upon the `Robot` class that simulates basic user input and sought to have some fun with it. 

![Tool](https://github.com/albertbregonia/MiniChat/blob/master/img/demo.jpg?raw=true "Demo")

**Cerebrum** is a Java-based portable tool that is paired with the [Netty API](https://netty.io/) to create a low latency, network-based, 
mouse/keyboard sharing tool. It is named **Cerebrum** as it can act as a hub for mouse/keyboard input for up to 10 devices. 

The main purpose is to allow for a user to be able to connect other computers together and eliminate the need for extra keyboards and mice. 
***Thus overall, increasing productivity.***

#### If you know how to port forward, you can ***also*** use this as a remote desktop software if paired with a 3rd-party screen-sharing application.

### Note: If you are to use this software, you must:
- Understand the basic concepts of networking such as IP addresses and ports.
- Know that this software works best where the scaling of the host window matches that of the client. 

## Main Features:
1. Configurable to be a server or client.
2. Easy to use GUI.
3. Cross Platform - Able to run on Windows, Mac OS X and Linux as long as a [JRE 1.8+](https://www.java.com/en/) is available.
4. Able to control a maximum of 10 simulataneous devices.

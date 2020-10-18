# Cerebrum

## A Mouse/Keyboard Sharing Tool

For some backstory, this was a project that I started in order to dive deeper into learning the capabilities of [JavaFX](https://openjfx.io/). 
I stumbled upon the `Robot` class that simulates basic user input and sought to have some fun with it. 

![Tool](https://github.com/albertbregonia/Cerebrum/blob/main/src/main/resources/demo.png?raw=true "Demo")

**Cerebrum** is a Java-based portable tool that is paired with the [Netty API](https://netty.io/) to create a low latency, network-based, 
mouse/keyboard sharing tool. It is named **Cerebrum** as it can act as a hub for mouse/keyboard input for up to 10 devices. 

The main purpose is to allow for a user to be able to connect other computers together and eliminate the need for extra keyboards and mice. 
***Thus overall, increasing productivity.***

#### If you know how to port forward, you can ***ALSO*** use this as a remote desktop software if paired with a 3rd-party screen-sharing application.

## Requirements:
- Understand the basic concepts of networking such as IP addresses and ports.
- Understand that this software works best where the scaling of the host window matches that of the client. 

## Main Features:
1. Easy to use, simple GUI.
2. Able to control up to maximum of 10 simulataneous devices.
3. Cross Platform - Able to run on Windows, Mac OS X and Linux as long as a [JRE/JDK 10+](https://www.oracle.com/java/technologies/java-archive-javase10-downloads.html) is available.

## RELEASE NOTES AS OF OCT. 17
- Keyboard input and scrolling is fully functional, 100% accuracy; ***EXCEPT: OS-Exclusive Key Combinations (ie. CTRL+ALT+DEL)***
- No scrolling functionality (besides mouse wheel click-based scrolling)
- Currently only works with Java 10+ although it is compiled for Java 8...

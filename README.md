# Cerebrum

## A Mouse/Keyboard Sharing Tool

For some backstory, this was a project that I started in order to dive deeper into learning the capabilities of [JavaFX](https://openjfx.io/). 
I stumbled upon the `Robot` class that simulates basic user input and sought to have some fun with it. 

![Tool](https://github.com/albertbregonia/Cerebrum/blob/main/src/main/resources/demo.png?raw=true "Demo")

**Cerebrum** is a Java-based portable tool that is paired with the [Netty API](https://netty.io/) to create a low latency, network-based, 
mouse/keyboard sharing tool. It is named **Cerebrum** as it can act as a hub for mouse/keyboard input for up to 10 devices.

*If you modify the code you can attempt to connect more than 10 devices. I merely set the limit at 10 to keep the settings slider intelligible.*

The main purpose is to allow for a user to be able to connect other computers together and eliminate the need for extra keyboards and mice. 
***Thus overall, increasing productivity.***

#### If you know how to port forward, you can ***ALSO*** use this as a remote desktop software if paired with a 3rd-party screen-sharing application.

## Requirements:
- Understand the basic concepts of networking such as IP addresses and ports.
- Understand the basic concepts of screen resolutions and window scaling.
- Understand that this software works best where the scaling of the host window matches that of the client. 

## Main Features:
1. Easy to use, simple GUI.
2. Able to control up to maximum of 10 simulataneous devices.
3. Cross Platform - Able to run on Windows, Mac OS X and Linux as long as a [JRE/JDK 10+](https://www.oracle.com/java/technologies/java-archive-javase10-downloads.html) is available.

## How to Use:
  #### If you are using the software locally:
  1. Decide what computer will be the *host* or `master` and what port you would like to use.
  2. Run the software on said computer and click `Connect`. The settings menu will load.
  3. In the Settings Menu: 
      - Check the `Host` CheckBox.
      - Type *Server* in the `IP Address` section.
      - Enter your desired port in the `Port` section.
      - Slide the bar to the desired number of computers you would like to connect. 
      - Click `Save`.
  4. Once the `Connection Status` becomes a blue check, run the software on the remote devices and click `Connect`. The settings menu will load.
  5. In the Settings Menu:
      - Enter in the IPv4 of the `master` in the `IP Address` section.
      - Enter your previously chosen port in the `Port` section.
      - Click `Save`.
  6. Wait a few seconds, and you should see a translucent cyan window pop up on the `master` with the title being either your device name or an IP Address. This window is the control panel for that respective device. 
      ### ***Ensure that you scale this window to match that of the remote device.***
      In this control panel, all keyboard and mouse data performed on this window will be sent to the respective remote device. That is to say, clicking anywhere on the panel will correspond to a click on the respective remote device at that location (if scaled properly).
  7. Once finished, Click `Disconnect` to stop the server or click on the names of each device on the `Live Connections` window to disconnect them.
  #### If you are using the software remotely: ***aka using this as a remote desktop***
  1. Follow the same steps described above, but ensure that the `master` is port-forwarded and that the client enters the external [IP Address](https://whatsmyip.org/) of the `master`.


## RELEASE NOTES AS OF OCT. 17
- Keyboard input and scrolling is fully functional, 100% accuracy; ***EXCEPT: OS-Exclusive Key Combinations (ie. CTRL+ALT+DEL)***
- No scrolling functionality (besides mouse wheel click-based scrolling)
- Currently only works with Java 10+ although it is compiled for Java 8...

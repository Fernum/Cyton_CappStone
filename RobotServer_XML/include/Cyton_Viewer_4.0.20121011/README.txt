Release notes for Actin SE Cyton Control Software v4.0

The default port name for the Cyton device defaults to 'any' on windows, which means the first available usb2dynamixel port and /dev/ttyUSB0 on linux.  It can be changed within the cytonViewer in the "Edit->Configure Cyton Hardware..." menu item.

In order for the Cyton hardware to pass its initialization tests it needs to confirm that the configuration file is in sync with the stored (EEPROM) values within the servos.  This can be done by running 'cytonSetup --write' from the installation bin directory.  Running the 'cytonSetup' command again without the '--write' argument will verify that the configuration has been written and should say 'Cyton configuration check successful.'

Platform specific notes:

Windows
-------
If you need to install a serial port driver for the supplied hardware, there is an installer executable located in the contrib directory - "CDM20600.exe"

For best performance during assistive mode, it is recommended that you recude the latency timer in the serial port driver.  You can do this from the Device Manager.  The complete path is:

Administrative Tools -> Computer Management -> Device Manager -> Ports (COM & LPT) USB Serial Port -> Properties -> Port Settings -> Advanced -> Latency Timer

The default value is 16ms.  Set this value to 1ms.


Linux (all distros)
-------------------

Serial port permissions
-----------------------
In order to properly communicate with the Cyton hardware, the user needs to have read and write permission to the serial device.  In certain cases simply setting read and write access to the device is sufficient.
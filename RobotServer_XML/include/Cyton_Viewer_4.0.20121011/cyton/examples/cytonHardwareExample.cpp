//     Copyright (c) 2008-2010 Energid Technologies. All rights reserved. ////
//
// Filename:    cytonHardwareExample.cpp
//
// Description: Example program to exercise the hardwareInterface class
//              of the Cyton arm.
//
// Contents:    
//
/////////////////////////////////////////////////////////////////////////
#ifdef WIN32
#  include <windows.h>
#else
#  include <unistd.h>
#  include <string.h>
#  define Sleep(ms) usleep(1000*ms)
#endif

#include <cytonHardwareInterface.h>
#include <iostream>


/////////////////////////////////////////////////////////////////////////
// Function:     testJoints
// Description:  Perform a series of tests on the hardware.  It is
//               currently setup to perform the following tasks independently
//               for each joint:
//               1.  Set joint to its minimum joint angle
//               2.  Set joint to its maximum joint angle
//               3.  Set joint to its minimum joint angle
//               4.  Set joint to its initialization joint angle
//
// I/O:          Returns EcTrue on success or EcFalse on failure.
//
// Notes:        In each case the parameters are queried from the
//               configuration file.
/////////////////////////////////////////////////////////////////////////
EcBoolean
testJoints
   (
   cyton::hardwareInterface& hw
   )
{
   // This gives a velocity of around 0.3 radians per second when travelling
   // over 180 degrees.
   const EcReal deltaTime = 10.0; // 10 seconds

   // This method will set the internal routines to go from one position to
   // another in this much time.  It is a convenience routine in lieu of
   // specifying a joint velocity on a per-joint basis.
   hw.setVelocityFromDeltaTime(deltaTime);
   // Give a pretty conservative wait time (2x)
   const EcU32 waitTimeInMS = EcU32(2 * deltaTime * 1000);

   const size_t numJoints = hw.numJoints();
   if(!numJoints)
   {
      std::cerr << "Invalid configuration. No joints available.\n";
      return EcFalse;
   }
   
   std::cout << "Starting hardware joint test with " << numJoints << " joints\n";
   EcRealVector jointAngle(numJoints), minAngles(numJoints), initAngles(numJoints, 0.0), maxAngles(numJoints);

   // Pull information from configuration
   std::cout << "Reading angles from configuration.\n";
   if(!hw.getJointStates(minAngles, cyton::MinAngle))
   {
      std::cerr << "Unable to get minimum angles from getJointStates.\n";
      return EcFalse;
   }
   if(!hw.getJointStates(maxAngles, cyton::MaxAngle))
   {
      std::cerr << "Unable to get maximum angles from getJointStates.\n";
      return EcFalse;
   }

   // Set all joints to their init value.
   std::cout << "Setting all joints to init(0) angles.\n";
   if(!hw.setJointCommands(initAngles) ||
      !hw.waitUntilCommandFinished(waitTimeInMS))
   {
      std::cerr << "Problem setting initialize angles.\n";
      return EcFalse;
   }
   Sleep(1000); // Pause for a second to visually verify finished cmd.

   // Start from init setting.
   jointAngle = initAngles;

   // Reduce the Shoulder Pitch Angle Limits to 90 degree on each side so
   // that the arm's gripper doesn't hit the ground.
   minAngles[1] = -1.57079;
   maxAngles[1] =  1.57079;
   if(numJoints == 16) // Bi-handed configuration
   {
      minAngles[8] = -1.57079;
      maxAngles[8] =  1.57079;
   }
   // Cycle through each joint individually and go to min, then max, min
   // again and then back to init position.
   for(EcU32 ii=0; ii<numJoints; ++ii)
   {
      jointAngle[ii] = minAngles[ii];
      std::cout << "Setting joint " << ii << " to min angle.\n";
      if(!hw.setJointCommands(jointAngle) ||
         !hw.waitUntilCommandFinished(waitTimeInMS)) // Let the hardware achieve its position.
      {
         std::cerr << "Problem setting min angle for joint " << ii << ".\n";
         return EcFalse;
      }
      Sleep(1000); // Pause for a second to visually verify finished cmd.

      jointAngle[ii] = maxAngles[ii];
      std::cout << "Setting joint " << ii << " to max angle.\n";
      if(!hw.setJointCommands(jointAngle) ||
         !hw.waitUntilCommandFinished(waitTimeInMS)) // Let the hardware achieve its position.
      {
         std::cerr << "Problem setting max angle for joint " << ii << ".\n";
         return EcFalse;
      }
      Sleep(1000); // Pause for a second to visually verify finished cmd.

      jointAngle[ii] = minAngles[ii];
      std::cout << "Setting joint " << ii << " to min angle.\n";
      if(!hw.setJointCommands(jointAngle) ||
         !hw.waitUntilCommandFinished(waitTimeInMS)) // Let the hardware achieve its position.
      {
         std::cerr << "Problem setting min angle for joint " << ii << ".\n";
         return EcFalse;
      }
      Sleep(1000); // Pause for a second to visually verify finished cmd.

 
      jointAngle[ii] = initAngles[ii];
      std::cout << "Setting joint " << ii << " to init angle.\n";
      if(!hw.setJointCommands(jointAngle) ||
         !hw.waitUntilCommandFinished(waitTimeInMS)) // Let the hardware achieve its position.
      {
         std::cerr << "Problem setting init angle for joint " << ii << ".\n";
         return EcFalse;
      }
      Sleep(1000); // Pause for a second to visually verify finished cmd.
   }

   std::cout << "Successfully finished hardware joint test.\n";
   return EcTrue;
}


int
main
   (
   int argc,
   char *argv[]
   )
{
   EC_DEBUG_FLAGS;

   cyton::hardwareInterface hardware("cytonPlugin", "cytonConfig.xml");

   if((argc > 1 && !strcmp(argv[1], "-h")) || argc > 2)
   {
      std::cout << "Usage: " << argv[0] << " [-h] [port]\n";
      std::cout << "\t-h : Usage information.\n\n";
      // Attempt to retrieve available serial ports.
      EcStringVector ports = hardware.availablePorts();
      const size_t numPorts = ports.size();
      if(numPorts)
      {
         std::cout << "Where port can be one of:\n";
         for(size_t ii=0; ii<numPorts; ++ii)
         {
            std::cout << "\t" << ports[ii] << "\n";
         }
         std::cout << "\n This overrides the previously configured serial port.\n";
      }
      return -1;
   }

   if(argc == 2)
   {
      hardware.setPort(argv[1]);
   }

   if(!hardware.init())
   {
      std::cerr << "Problem initializing cyton hardwareInterface.\n";
      return -1;
   }

   // Success in initialization.  Start hardware tests.
   if(testJoints(hardware))
   {
      std::cout << "All Tests passed.\n";
      return 0;
   }
   std::cerr << "Test failed.\n";
   return -1;
}

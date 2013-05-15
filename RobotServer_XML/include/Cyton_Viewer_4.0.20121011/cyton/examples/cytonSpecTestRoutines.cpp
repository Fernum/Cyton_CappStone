//     Copyright (c) 2008-2010 Energid Technologies. All rights reserved. ////
//
// Filename:    cytonSpecTestRoutines.cpp
//
// Description: 
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

//------------------------------------------------------------------------------
// Convenience function to print a vector of joint values.
//------------------------------------------------------------------------------
static std::ostream&
operator<<(std::ostream& os, const EcRealVector &vec)
{
   size_t num = vec.size();
   if(num)
   {
      for(size_t ii=0; ii<num-1; ++ii)
      {
         os << vec[ii] << ", ";
      }
      os << vec[num-1];
   }
   return os;
}

//------------------------------------------------------------------------------
// Perform joint limit test on the hardware.  It is currently setup to move each
// joint between minimum and maximum limits and then read joint values from 
// hardware when limits are reached.
//
// Notes:        In each case the parameters are queried from the
//               configuration file.
//------------------------------------------------------------------------------
EcBoolean jointLimitTest(cyton::hardwareInterface& hw)
{
   const EcU32 waitTimeInMS = 25000; // 25 second max wait time
   const size_t numJoints = hw.numJoints();
   if(!numJoints)
   {
      std::cerr << "Invalid configuration. No joints available.\n";
      return EcFalse;
   }
   
   std::cout << "Starting hardware joint test with " << numJoints << " joints\n";
   EcRealVector jointAngle(numJoints), minAngles(numJoints), initAngles(numJoints,0.0), maxAngles(numJoints);

   // Pull information from configuration
   std::cout << "Reading angles from configuration.\n";
   if(!hw.getJointStates(minAngles, cyton::MinAngle))
   {
      std::cerr << "Unable to get minimum angles.\n";
      return EcFalse;
   }
   if(!hw.getJointStates(maxAngles, cyton::MaxAngle))
   {
      std::cerr << "Unable to get maximum angles.\n";
      return EcFalse;
   }

   // Set all joints to their init value.
   std::cout << "Setting all joints to 0 angles.\n";
   // Passing 0's for velocity makes it move at the setup speed.
   if(!hw.setJointCommands(initAngles, initAngles))
   {
      std::cerr << "Problem setting zero angles.\n";
      return EcFalse;
   }
   hw.waitUntilCommandFinished(waitTimeInMS);
   Sleep(1000); // Pause for a second to visually verify finished cmd.

   // Start from init setting.
   jointAngle = initAngles;
   EcRealVector jointValuePrint(numJoints);

   // Reduce the Shoulder Pitch Angle Limits to 90 degree each side 
   // we dont want the arm to hit the ground
   minAngles[1] = -1.57079;
   maxAngles[1] = 1.57079;

   hw.setVelocityFromDeltaTime(3.0); // Move from point to point in 3 seconds

   // Cycle through each joint individually and go to min, max and then init
   // Joint values are read and displayed once the joint reaches its limit
   for(EcU32 ii=0; ii<numJoints; ++ii)
   {
      jointAngle[ii] = minAngles[ii];
      std::cout << "\nSetting joint " << ii << " to min angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting min angle for joint " << ii << ".\n";
         return EcFalse;
      }
      hw.waitUntilCommandFinished(waitTimeInMS); // Let the hardware achieve its position.

      hw.getJointStates(jointValuePrint);
      std::cout<<"\n--Joint Values--\n" << jointValuePrint << std::endl;

      Sleep(1000); // Pause for a second to visually verify finished cmd.

      jointAngle[ii] = maxAngles[ii];
      std::cout << "\nSetting joint " << ii << " to max angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting max angle for joint " << ii << ".\n";
         return EcFalse;
      }
      hw.waitUntilCommandFinished(waitTimeInMS); // Let the hardware achieve its position.

      hw.getJointStates(jointValuePrint);
      std::cout<<"\n--Joint Values--\n" << jointValuePrint << std::endl;

      Sleep(1000); // Pause for a second to visually verify finished cmd.

      jointAngle[ii] = initAngles[ii];
      std::cout << "\nSetting joint " << ii << " to init angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting init angle for joint " << ii << ".\n";
         return EcFalse;
      }
      hw.waitUntilCommandFinished(waitTimeInMS); // Let the hardware achieve its position.

      hw.getJointStates(jointValuePrint);
      std::cout<<"\n--Joint Values--\n" << jointValuePrint << std::endl;

      Sleep(1000); // Pause for a second to visually verify finished cmd.
   }
   return EcTrue;
}


//------------------------------------------------------------------------------
// Performs joint velocity test on the hardware.  It is currently setup to move
// a joint at different velocities and read back the joint velocity for each
// joint directly from the hardware and display the results.
//
// Notes:        In each case the parameters are queried from the
//               configuration file.
//------------------------------------------------------------------------------
EcBoolean jointVelocityTest(cyton::hardwareInterface& hw)
{
   const EcU32 waitTimeInMS = 25000; // 25 second max wait time
   const size_t numJoints = hw.numJoints();
   EcRealVector jointValuesRead(numJoints);
   if(!numJoints)
   {
      std::cerr << "Invalid configuration. No joints available.\n";
      return EcFalse;
   }
   
   std::cout << "Starting hardware joint test with " << numJoints << " joints\n";
   EcRealVector jointAngle(numJoints), minAngles(numJoints), initAngles(numJoints,0.0), maxAngles(numJoints);

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
   std::cout << "Setting all joints to zero angles.\n";
   if(!hw.setJointCommands(initAngles, initAngles))
   {
      std::cerr << "Problem setting zero angles.\n";
      return EcFalse;
   }
   hw.waitUntilCommandFinished(waitTimeInMS);
   Sleep(1000); // Pause for a second to visually verify finished cmd.

   // Start from init setting.
   jointAngle = initAngles;

   // Here we are checking out the 3rd joint of the arm.
   // The joint is made to move to max and min limits and velocity is read back from the joints.
   hw.setVelocityFromDeltaTime(1.0); // 1 second to move
   for(EcU32 ii=2; ii<3; ++ii)
   {
      jointAngle[ii] = minAngles[ii];
      std::cout << "\nMoving joint " << ii << " to min angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting min angle for joint " << ii << ".\n";
         return EcFalse;
      }
      // read joint velocity

      hw.getJointStates(jointValuesRead, cyton::JointVelocity);
      std::cout<<"\n--Joint Velocity--\n" << jointValuesRead << std::endl;
      Sleep(1500); // Pause for a second to visually verify finished cmd.

      hw.setVelocityFromDeltaTime(1.5); // 1.5 second to move
      jointAngle[ii] = maxAngles[ii];
      std::cout << "\nMoving  joint " << ii << " to max angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting max angle for joint " << ii << ".\n";
         return EcFalse;
      }
      // read joint velocity
      hw.getJointStates(jointValuesRead, cyton::JointVelocity);
      std::cout<<"\n--Joint Velocity--\n" << jointValuesRead << std::endl;
      Sleep(1500); // Pause for a second to visually verify finished cmd.

      hw.setVelocityFromDeltaTime(0.6); // 0.6 second to move
      jointAngle[ii] = initAngles[ii];
      std::cout << "\nMoving  joint " << ii << " to init angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting init angle for joint " << ii << ".\n";
         return EcFalse;
      }
      // read joint velocity
      hw.getJointStates(jointValuesRead, cyton::JointVelocity);
      std::cout<<"\n--Joint Velocity--\n" << jointValuesRead << std::endl;
      Sleep(1500); // Pause for a second to visually verify finished cmd.
   }
   return EcTrue;
}


//------------------------------------------------------------------------------
// Performs reach test on the hardware.  It is currently setup to stretch the
// arm in four directions.
// Notes:        In each case the parameters are queried from the
//               configuration file.   
//------------------------------------------------------------------------------
EcBoolean reachTest(cyton::hardwareInterface& hw)
{
   const EcU32 waitTimeInMS = 25000; // 25 second max wait time
   const size_t numJoints = hw.numJoints();
   if(!numJoints)
   {
      std::cerr << "Invalid configuration. No joints available.\n";
      return EcFalse;
   }
   
   std::cout << "Starting hardware joint test with " << numJoints << " joints\n";
   EcRealVector jointAngle(numJoints), minAngles(numJoints), initAngles(numJoints,0.0), maxAngles(numJoints);

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
   std::cout << "Setting all joints to zero angles.\n";
   if(!hw.setJointCommands(initAngles, initAngles))
   {
      std::cerr << "Problem setting zero angles.\n";
      return EcFalse;
   }
   hw.waitUntilCommandFinished(waitTimeInMS);
   Sleep(1000); // Pause for a second to visually verify finished cmd.

   // Start from init setting.
   jointAngle = initAngles;

   EcRealVector jointValuePrint(numJoints);

   // Reduce the Shoulder Pitch Angle Limits to 90 degree each side 
   // we dont want the arm to hit the ground
   minAngles[1] = -1.57079;
   maxAngles[1] = 1.57079;

   hw.setVelocityFromDeltaTime(2.0);
   // We are just moving a single joint during this test.
   // The spin joint in the middle is moved.
   for(EcU32 ii=1; ii<2; ++ii)
   {
      jointAngle[ii] = minAngles[ii];
      std::cout << "Setting joint " << ii << " to min angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting min angle for joint " << ii << ".\n";
         return EcFalse;
      }

      Sleep(5000); // Pause for a second to visually verify finished cmd.

      jointAngle[ii] = maxAngles[ii];
      std::cout << "Setting joint " << ii << " to max angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting max angle for joint " << ii << ".\n";
         return EcFalse;
      }

      Sleep(5000); // Pause for a second to visually verify finished cmd.

      jointAngle[ii] = initAngles[ii];
      std::cout << "Setting joint " << ii << " to init angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting init angle for joint " << ii << ".\n";
         return EcFalse;
      }

      Sleep(3000); // Pause for a second to visually verify finished cmd.

      // now turn base joint of the arm by 90 degree
      jointAngle[ii] = initAngles[ii];
      jointAngle[0] = 1.57079;//90 degree
      std::cout << "Setting joint " << ii << " to init angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting init angle for joint " << ii << ".\n";
         return EcFalse;
      }

      Sleep(5000); // Pause for a second to visually verify finished cmd.

      jointAngle[ii] = minAngles[ii];
      std::cout << "Setting joint " << ii << " to min angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting min angle for joint " << ii << ".\n";
         return EcFalse;
      }

      Sleep(5000); // Pause for a second to visually verify finished cmd.
      jointAngle[ii] = maxAngles[ii];
      std::cout << "Setting joint " << ii << " to max angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting max angle for joint " << ii << ".\n";
         return EcFalse;
      }

      Sleep(5000); // Pause for a second to visually verify finished cmd.

      jointAngle[ii] = initAngles[ii];
      jointAngle[0] = 0.0;//back to inital pose
      std::cout << "Setting joint " << ii << " to init angle.\n";
      if(!hw.setJointCommands(jointAngle))
      {
         std::cerr << "Problem setting init angle for joint " << ii << ".\n";
         return EcFalse;
      }

      Sleep(2000); // Pause for a second to visually verify finished cmd.
   }
   return EcTrue;
}


//------------------------------------------------------------------------------
int main(int argc, char *argv[])
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

   while(1)
   {
      std::cout << "\nCyton Specification Test Routine\n"
                << "Test List:\n"
                << "1.Joint Limit Test\n"
                << "2.Joint Velocity Test\n"
                << "3.Reach Test\n"
                << "Any other key quits\n"
                << "\nPlease enter your choice(1/2/3):";
      char choice;
      std::cin >> choice;
      EcBoolean retVal;

      switch(choice)
      {
      case '1': // Joint Limit test routine
         retVal = jointLimitTest(hardware);
         std::cout << "\nJoint limit test finished.\n";
         break;

      case '2': // Joint Velocity test routine
         retVal = jointVelocityTest(hardware);
         std::cout << "\nJoint velocity test finished.\n";
         break;

      case '3': // Reach test routine
         retVal = reachTest(hardware);
         std::cout << "\nReach test finished.\n";
         break;

      default:
         std::cout << "Exiting\n";
         return 0;
      }

      if(retVal)
      {
         std::cout << "Test completed successfully.\n";
      }
      else
      {
         std::cerr << "Test failed.\n";
      }
   }

   return 0;
}

//     Copyright (c) 2008-2012 Energid Technologies. All rights reserved. ////
//
// Filename:    cytonControlExample.cpp
//
// Description: Example program to exercise the controlInterface class
//              to facilitate controlling the Cyton arm.
//
//------------------------------------------------------------------------------
#include <actinSE/Array3.h>
#include <actinSE/ControlSystem.h>
#include <actinSE/EndEffector.h>
#include <cytonHardwareInterface.h>
#include <iomanip>
#include <iostream>

using namespace actinSE;

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
// Consolidated example that positions the end-effector and then pushes the
// calculated joint values to the hardware. It sets an initial location and
// tells it to move to the new locations.  It is given a timestep of 0.1 sec
// (10Hz) and an overall time of 20 seconds to complete the moves.
// Returns EcTrue on success and EcFalse on failure.
//------------------------------------------------------------------------------
EcBoolean
testControlAndHardware
   (
   actinSE::ControlSystem& control,
   cyton::hardwareInterface& hardware
   )
{
   EcRealVector jointAngles, jointRates;
   
   // Pull the starting position.
   EndEffectorVector eeVec;
   if(!control.getParam<ControlSystem::EndEffectors>(eeVec))
   {
      std::cerr << "Problem getting EE vector.\n";
      return EcFalse;
   }

   // For this test, we want to select the proper end-effector for movement.
   // We will choose the first EE that is not a linear-constraint, which should
   // work for all cases.
   EcInt32 eeIndex = -1;
   for(size_t ii=0,eeSize=eeVec.size(); ii<eeSize; ++ii)
   {
      if(eeVec[ii].endEffectorType() != EndEffector::LinearConstraintEndEffector)
      {
         eeIndex = ii;
         break;
      }
   }
   if(eeIndex == -1)
   {
      // Couldn't find EE that would work. Bail
      std::cerr << "Could'n't find end-effector to use for test.\n";
      return EcFalse;
   }
   // Get a handle to the EE we are going to use.
   EndEffector& ee = eeVec[eeIndex];


   // Will return EcFalse if not linked with rendering version of lib.
   EcBoolean withRendering = control.setParam<ControlSystem::Rendering>(EcTrue);

   // Move arm to known location before moving in end-effector space.
   EcU32 numJoints = hardware.numJoints();
   EcRealVector zeroAngles(numJoints, 0.0); // Zero angles for all

   // Move to init (zero) position.  Giving a properly-sized velocity vector
   // of 0 values will cause the MaxSetupVelocityInHWUnits XML value to be
   // used.  It defaults to 20, which for this hardware is about 0.32 rad/sec.
   // This gives a good, slow, sane value to use instead of calculating
   // something ourselves.  A 180 degree rotation at this rate would take
   // approximately 10 seconds.  We give the hardware a little more just in
   // case it is really twisted.
   std::cout << "Moving arm to init (zero) angle position.\n";
   if(!hardware.setJointCommands(zeroAngles, zeroAngles) ||
      !hardware.waitUntilCommandFinished(20000)) // wait up to 20 seconds
   {
      std::cerr << "Unable to move arm to zero angle position.\n";
      return EcFalse;
   }

   // Get state of hardware and set that to our control system.
   if(!hardware.getJointStates(jointAngles))
   {
      std::cerr << "Unable to get joint state of hardware\n";
      return EcFalse;
   }
   std::cout << "Initial state of hardware = " << jointAngles << std::endl;
   EcBoolean passed = control.setParam<ControlSystem::JointAngles>(jointAngles);

   EcReal currentTime = 0.0; // time counter for this pass

   // 3 arbitrary points to move the arm
   Array3Vector point;
   point.push_back(Array3( 0.15, -0.10, 0.16));
   point.push_back(Array3(-0.22, -0.06, 0.13));
   point.push_back(Array3(-0.01,  0.16, 0.52));

   for(size_t ii=0; passed && ii<point.size(); ++ii)
   {
      Array3 actualPos;
      passed &= ee.getParam<EndEffector::ActualPose>(actualPos);
      std::cout << "Starting EE position: " << actualPos << std::endl;

      // Generate a new desired end-effector position.  We are just
      // passing in position information, since we are dealing with a
      // point end-effector (ignores orientation).
      passed &= ee.setParam<EndEffector::DesiredPose>(point[ii]);
      std::cout << "Desired EE position: " << point[ii] << std::endl;

      const EcReal totalTime = currentTime + 20.0; // Give 20s for each point
      const EcReal timeStep = 0.1;   // 10Hz update

      // Run until we get where we want to go, or it took too long.
      while(passed && !actualPos.approxEq(point[ii], 1e-7) && currentTime <= totalTime)
      {
         // Calculate a new state at the given time.
         passed &= control.calculateToNewTime(currentTime);

         // Calculate our current time.
         currentTime += timeStep;

         // Pull updated joint angles after calculating.
         passed &= control.getParam<ControlSystem::JointAngles>(jointAngles);
         passed &= control.getParam<ControlSystem::JointVelocities>(jointRates);
         // Pass them directly to hardware
         passed &= hardware.setJointCommands(jointAngles, jointRates);

         // Get EE position for loop check.
         passed &= ee.getParam<EndEffector::ActualPose>(actualPos);

         // Print out the results
         std::cout << "Time = " << std::setw(2) << currentTime
#if _DEBUG
                   << ", Current pos (" << actualPos << ")"
#endif      
                   << ", Joint angles = ";
         for(size_t anglesize=jointAngles.size(),jj=0; jj<anglesize; ++jj)
         {
            std::cout << std::setw(9) << jointAngles[jj] << " ";
         }
         std::cout << std::endl;
      }
      if(currentTime < totalTime)
      {
         std::cout << "Achieved desired position in " << currentTime
                   << " seconds\n";
      }
      else
      {
         std::cout << "Did not achieve desired position after " << totalTime
                   << " seconds.  Current pos = (" << actualPos << ")\n";      
      }

      // Pause for 2 seconds before moving on to next position.
      EcSLEEPMS(2000);
   }

   if(!passed)
   {
      std::cerr << "Premature exit at time = " << currentTime << std::endl;
      return EcFalse;
   }

   return EcTrue;
}


//------------------------------------------------------------------------------
int main(int argc, char* argv[])
{
   EC_DEBUG_FLAGS;

   ControlSystem control;
   if(!control.loadFromFile("cyton.ecz"))
   {
      std::cerr << "Problem loading Cyton control system file.\n";
      return -1;
   }
   cyton::hardwareInterface hardware("cytonPlugin", "cytonConfig.xml");
   if(!hardware.init())
   {
      std::cerr << "Problem initializing Cyton hardwareInterface.\n";
      return -1;
   }
   
   // create and run the example
   if(testControlAndHardware(control, hardware))
   {
      std::cout << "Test passed.\n";
      return 0;
   }
   std::cout << "Test failed.\n";
   return -1;
}

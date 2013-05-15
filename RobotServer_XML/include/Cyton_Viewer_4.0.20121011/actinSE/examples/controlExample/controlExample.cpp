//     Copyright (c) 2009-2010 Energid Technologies. All rights reserved. ////
//
// Filename:    controlExample.cpp
//
// Description: Use case example which demonstrates some of the capabilities
//              of the actinSE::ControlSystem class.
//
/////////////////////////////////////////////////////////////////////////
#include <actinSE/ControlSystem.h>
#include <actinSE/EndEffector.h>
#include <actinSE/CoordinateSystemTransformation.h>
#include <iostream>
#include <iomanip>

using namespace actinSE;

int main(int argc, char** argv)
{
   // Verify command-line arguments
   if(argc != 2)
   {
      std::cerr << "Usage: " << argv[0] << " <simulation file>\n";
      return 1;
   }

   ControlSystem system;
   if(!system.loadFromFile(argv[1]))
   {
      std::cerr << "Invalid simulation file (" << argv[1] << ")\n";
      return 1;
   }

   // Get the loaded EEs.
   EndEffectorVector eeVec;
   if(!system.getParam<ControlSystem::EndEffectors>(eeVec))
   {
      std::cerr << "Problem getting EE vector.\n";
      return 1;
   }

   const size_t eeVecSize = eeVec.size();
   if(!eeVecSize)
   {
      std::cerr << "No end-effectors found in this file.\n";
      return 1;
   }

   // Will return EcFalse if not linked with rendering version of lib.
   EcBoolean withRendering = system.setParam<ControlSystem::Rendering>(EcTrue);

   // Pull and use the last EE.
   EndEffector& ee = eeVec[eeVecSize-1];
   CoordinateSystemTransformation coord;
   ee.getParam<EndEffector::ActualPose>(coord);
   Array3 actualPos = coord.translation();
   std::cout << "Starting position: " << actualPos << std::endl;

   Array3 offset(0.0,0.1,0.1);
   Array3 pos = actualPos - offset;
   
   coord.setTranslation(pos);
   ee.setParam<EndEffector::DesiredPose>(coord);
   std::cout << "Desired position: " << pos << std::endl;
   
   EcRealVector angles;
   const EcReal timeStep = 0.1; // 10Hz update
   const EcReal totalTime = 10.0; // 10 seconds total time

   EcReal currentTime = 0.0;
   EcU32 count = 0;
   
   // Run until we get where we want to go, or it took too long.
   while(!actualPos.approxEq(pos, 1e-7) && currentTime <= totalTime)
   {
      // Calculate our current time.
      currentTime = count*timeStep;
      count++;
      
      // Calculate a new state at the given time.
      system.calculateToNewTime(currentTime);
      if(withRendering)
      {
         // Pause to see effects in render window
         EcSLEEPMS(200);
      }
      // Pull updated joint angles after calculating.
      system.getParam<ControlSystem::JointAngles>(angles);

      // Get EE position for loop check.
      ee.getParam<EndEffector::ActualPose>(coord);
      actualPos = coord.translation();

      // Print out the results
      std::cout << "Time = " << std::setw(2) << currentTime
#if _DEBUG
                << ", Current pos (" << actualPos << ")"
#endif      
                << ", Joint angles = ";
      for(size_t anglesize=angles.size(),jj=0; jj<anglesize; ++jj)
      {
         std::cout << std::setw(9) << angles[jj] << " ";
      }
      std::cout << std::endl;
   }

   if(currentTime < totalTime)
   {
      std::cout << "Achieved desired position in " << currentTime << " seconds (" << count << " steps)\n";
   }
   else
   {
      std::cout << "Did not achieve desired position after " << totalTime
                << " seconds.  Current pos = (" << actualPos << ")\n";      
   }

   if(withRendering)
   {
      // Pause to see effects in render window before exiting
      EcSLEEPMS(2000);
   }
   
   return 0;
}

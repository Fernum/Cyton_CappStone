//     Copyright (c) 2008-2010 Energid Technologies. All rights reserved. ////
//
// Filename:    cytonControllerExample.cpp
//
// Description: Example program to exercise the controlInterface class
//              to facilitate controlling the Cyton arm.
//
// Contents:    class cytonControlExample
//
/////////////////////////////////////////////////////////////////////////
#include <actinSE/CoordinateSystemTransformation.h>
#include <cytonController/cytonController.h>
#include <iostream>

using namespace actinSE;
using namespace cyton;

int main(int argc, char* argv[])
{
   EC_DEBUG_FLAGS;

   CytonController controller;
   controller.setPluginFile("cytonPlugin");
   controller.setSimulationFile("cyton.ecz");
   controller.setEndEffectorIndex(2);
   controller.setTolerance(0.003);
   controller.setRenderingMode(EcTrue);
   controller.setHardwareMode(EcTrue);
   if(!controller.init())
   {
      std::cerr << "Problem initializing Cyton controller.\n";
      return -1;
   }

   const EcReal inchToMeter=0.0254;
   Array3Vector points;
   points.push_back(Array3(-10, 11, 7)*inchToMeter);
   points.push_back(Array3(-10, 11, 5)*inchToMeter);
   points.push_back(Array3(-10, 11, 7)*inchToMeter);
   points.push_back(Array3( 10, 11, 7)*inchToMeter);
   points.push_back(Array3( 10, 11, 5)*inchToMeter);
   points.push_back(Array3( 10, 11, 7)*inchToMeter);
   points.push_back(Array3(-12, 11, 7)*inchToMeter);
   points.push_back(Array3(-12, 11, 5)*inchToMeter);
   points.push_back(Array3(-12, 11, 7)*inchToMeter);

   for(EcU32 ii=0; ii<points.size(); ++ii)
   {
      std::cout << "Moving to point " << ii << " at: " 
                << points[ii][0] << ", "
                << points[ii][1] << ", "
                << points[ii][2] << std::endl;
      if(!controller.moveTo(points[ii], 30))
      {
         std::cerr << "Can't reach goal.\n";
         const Array3& actualPoint = controller.lastActualPoint();
         std::cerr << "Current point: "
                   << actualPoint[0] << ", "
                   << actualPoint[1] << ", "
                   << actualPoint[2] << std::endl;
         break;
      }
      // pause 2 seconds
      EcSLEEPMS(2000);
   }

   controller.reset();
   controller.shutdown();

   return 0;
}

#include <actinSE/ControlSystem.h>
#include <actinSE/EndEffector.h>
#include <actinSE/CoordinateSystemTransformation.h>
#include <iostream>
#include <iomanip>
#include <fstream>


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

   // Will return EcFalse if not linked with rendering version of lib.
   EcBoolean withRendering = system.setParam<ControlSystem::Rendering>(EcTrue);

    const size_t eeVecSize = eeVec.size();
   // Pull and use the last EE.
   EndEffector& ee = eeVec[eeVecSize-1];
   CoordinateSystemTransformation coord;
   ee.getParam<EndEffector::ActualPose>(coord);
   Array3 actualPos = coord.translation();

   EcReal totalTime;

   std::cout<<" \n\n|-----------Enter The starting position (x,y,z) value ----------|\n";
   std::cout<<"|\t\t\t\t\t\t\t\t|\n";
   std::cout<<"|Init pos = " << actualPos[0] << ", "
            << actualPos[1] << ", "
            << actualPos[2] << "\t\t\t|\n";
   std::cout<<"|Enter The starting position  x value :::\t\t\t|";
   std::cin>>actualPos[0];
   std::cout<<"|Enter The starting position  y value :::\t\t\t|";
   std::cin>>actualPos[1];
   std::cout<<"|Enter The starting position  z value :::\t\t\t|";
   std::cin>>actualPos[2];
   std::cout << "|  Starting position(x,y,z)\t"<<"\t:->\t"
             << actualPos[0] << ", "
             << actualPos[1] << ", "
             << actualPos[2] << "\t\t|";
   std::cout<<" \n|---------------------------------------------------------------|";

  
   Array3 Pos;// = coord.translation();

   std::cout<<" \n|----------Enter The destination position (x,y,z) value --------|\n";
   std::cout<<"|\t\t\t\t\t\t\t\t|\n";
   std::cout<<"|Enter The starting position  x value :::\t\t\t|";
   std::cin>>Pos[0];
   std::cout<<"|Enter The starting position  y value :::\t\t\t|";
   std::cin>>Pos[1];
   std::cout<<"|Enter The starting position  z value :::\t\t\t|";
   std::cin>>Pos[2];
   std::cout<<"|Enter The time to reach(sec)\t\t:\t\t\t|";
   std::cin>>totalTime;
   std::cout << "|  Destination position(x,y,z)\t"<<"\t:->\t"
             << Pos[0] << ", "
             << Pos[1] << ", "
             << Pos[2] << "\t\t|";
   std::cout<<" \n|---------------------------------------------------------------|\n";

   ee.setParam<EndEffector::DesiredPose>(Pos);

   std::ofstream inf;
   inf.open("actinSE.txt");
   
   EcReal currentTime = 0.0;
   EcU32 count = 0;
   EcRealVector angles;
   const EcReal timeStep = 0.1; // 10Hz update
   
   // Run until we get where we want to go, or it took too long.
   while(!actualPos.approxEq(Pos, 1e-7) && currentTime <= totalTime)
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
      inf << "Time = " << std::setw(2) << currentTime
          << ", Current pos ("
          << actualPos[0] << ", "
          << actualPos[1] << ", "
          << actualPos[2] << ")"
          << ", Joint angles = ";
      for(size_t anglesize=angles.size(),jj=0; jj<anglesize; ++jj)
      {
         inf << std::setw(9) << angles[jj] << " ";
      }
      inf << std::endl;
   }

   if(currentTime < totalTime)
   {
      std::cout << "Achieved desired position in " << currentTime << " seconds (" << count << " steps)\n";
   }
   else
   {
      std::cout << "Did not achieve desired position after " << totalTime
                << " seconds.  Current pos = (" << actualPos[0]
                << ", " << actualPos[1] << ", " << actualPos[2] << ")\n";      
   }
}

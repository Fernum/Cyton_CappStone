//     Copyright (c) 2009-2010 Energid Technologies. All rights reserved. ////
//
// Filename:    main.cpp
//
// Description: Use case example which describes the control system and
//              end effectors available in the loaded file.  Uses the
//              actinSE interface.
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

   // Describe the current simulation file.





   //--------------------------------------------------------------------------
   // Print out DH Parameters
   //--------------------------------------------------------------------------
   Array3Vector dhvec = system.param<ControlSystem::CraigDHParameters, Array3Vector>();
   std::cout << "DH Parameters\n"
             << "Link#     A         alpha       D\n"
             << "-----------------------------------\n";
   for(size_t size=dhvec.size(),ii=0; ii<size; ++ii)
   {
      std::cout << std::setw( 3) << ii << ":  "
                << std::setprecision(5)
                << std::setw(10) << dhvec[ii][0] << " "
                << std::setw(10) << dhvec[ii][1] << " "
                << std::setw(10) << dhvec[ii][2] << std::endl;
   }

   Array3 pos;
   Orientation orient;
   CoordinateSystemTransformation coord;
   EcRealVector jointAngles;

   //--------------------------------------------------------------------------
   // Print out joint positions. Both joint angle and absolute position
   //--------------------------------------------------------------------------
   const EcU32 numJoints = system.param<ControlSystem::NumJoints, EcU32>();
   std::cout << "\n" << numJoints << " joints found.\n"
             << "Actual joint positions:\n";

   system.getParam<ControlSystem::JointAngles>(jointAngles);
   for(EcU32 ii=0; ii<numJoints; ++ii)
   {
      system.getParam<ControlSystem::JointPose>(ii, pos);
      std::cout << "\tJoint " << ii << ": " << jointAngles[ii] << ", pos = " << pos << std::endl;
   }

   //--------------------------------------------------------------------------
   // Print out End-Effector information
   //--------------------------------------------------------------------------
   EndEffectorVector eeVec = system.param<ControlSystem::EndEffectors, EndEffectorVector>();
   const size_t eesize = eeVec.size();
   std::cout << "\n" << eesize << " end-effectors found.\n";

   for(size_t ii=0; ii<eesize; ++ii)
   {
      EndEffector& ee = eeVec[ii];
      EcU32 doc = ee.param<EndEffector::DegreesOfConstraint, EcU32>();
      std::cout << "EE " << ii << ": "
                << ee.name() << "(doc=" << doc << ")\n";

      EndEffector::EEStateFlags flags = ee.stateFlags();
      std::cout << "State: HardConstrained - " << (flags & EndEffector::HardConstrained ? 'Y':'N')
                << ", RelativeLink - " << (flags & EndEffector::RelativeLinkFlag ? 'Y':'N')
                << ", Attached - " << (flags & EndEffector::Attached ? 'Y':'N') << std::endl;
      if(flags & EndEffector::Attached)
      {
         EcInt32 linkIndex = static_cast<EcInt32>(ee.param<EndEffector::AttachedLink, EcU32>());
         EcString linkName = ee.param<EndEffector::AttachedLink, EcString>();
         std::cout << "\tAttached Link = " << linkIndex << ", name = " << linkName << std::endl;

	 // Some EE types don't have valid offsets (LinearConstraint)
	 // Only print if OK
	 if(ee.getParam<EndEffector::AttachedOffset>(coord))
	 {
	    orient = coord.orientation();
      
	    std::cout << "\tOffset pos=("
		      << coord.translation() << "), orient=("
		      << orient[0] << "," << orient[1] << "," << orient[2] << "," << orient[3] << ")\n";
	 }
      }
      if(flags & EndEffector::RelativeLinkFlag)
      {
         EcU32 linkIndex = ee.param<EndEffector::RelativeLink, EcU32>();
         EcString linkName = ee.param<EndEffector::RelativeLink, EcString>();
         std::cout << "Relative Link index = " << linkIndex << ", name = " << linkName << std::endl;
      }

      EcReal threshold = ee.param<EndEffector::MotionThreshold, EcReal>();
      std::cout << "Threshold value = " << threshold << std::endl;
      
      if(ee.endEffectorType() == EndEffector::LinearConstraintEndEffector)
      {
         EcReal gain = ee.param<EndEffector::Gain, EcReal>();
         std::cout << "Gain value = " << gain << std::endl;
      }

      //-----------------------------------------------------------------------
      // Print out EE Actual and desired information
      //-----------------------------------------------------------------------
      coord = ee.param<EndEffector::ActualPose, CoordinateSystemTransformation>();
      pos = coord.translation();
      orient = coord.orientation();
      std::cout << "Actual coord: pos=("
                << pos <<"), orient=("
                << orient[0] << "," << orient[1] << "," << orient[2] << "," << orient[3] << ")\n";

      coord = ee.param<EndEffector::DesiredPose, CoordinateSystemTransformation>();
      pos = coord.translation();
      orient = coord.orientation();
      std::cout << "Desired coord: pos=("
                << pos <<"), orient=("
                << orient[0] << "," << orient[1] << "," << orient[2] << "," << orient[3] << ")\n";

      EcRealVector velVec = ee.param<EndEffector::DesiredVelocity, EcRealVector>();
      const size_t size = velVec.size();
      std::cout << "Desired velocity(" << size << "): ";
      for(size_t jj=0; jj<size; ++jj)
      {
         std::cout << velVec[jj] << " ";
      }
      std::cout << std::endl << std::endl;
   }
   return 0;
}
   

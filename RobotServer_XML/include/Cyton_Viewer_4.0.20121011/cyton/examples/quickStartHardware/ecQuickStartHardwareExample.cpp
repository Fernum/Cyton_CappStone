//     Copyright (c) 2012 Energid Technologies. All rights reserved. ////
//
// Filename:    ecQuickStartHardwareExample.cpp
//
// Description: Holds the quick-start code described in the Users Guide.
//
// Contents:    class EcQuickStartHardwareExample 
//
/////////////////////////////////////////////////////////////////////////
#include "ecQuickStartHardwareExample.h"
#include <control/ecFrameEndEffector.h>
#include <control/ecIncludeControlExpressions.h>
#include <control/ecPointEndEffector.h>
#include <control/ecPosContSystem.h>
#include <control/ecVelContSystem.h>
#include <convertSystem/ecVisualizableStatedSystem.h>
#include <foundCore/ecApplication.h>
#include <hardwareInterface/cytonHardwareInterface.h>
#include <rendCore/ecRenderWindow.h>
#include <simulation/ecSysSimulation.h>
#include <xml/ecXmlObjectReaderWriter.h>


/////////////////////////////////////////////////////////////////////////
// Functions:    run()
// Description:  Executes the quick-start code.
/////////////////////////////////////////////////////////////////////////
void EcQuickStartHardwareExample::run()
{
   EcU32 ii;

   // -----------------------------------------------------------------
   // Example section #1 - Loading a simulation from a file.
   // -----------------------------------------------------------------

   // declare an error return code
   EcBoolean success;

   // add the file location to the search path
   Ec::Application::addDirectory(Ec::Application::getDataDirectory() + 
      EcString("/actinExamples"));

   // declare a filename
   EcString filename="cytonHardwareQuickstart.ecz";
   
   // declare a simulation object
   EcSystemSimulation simulationIn;

   // load the simulation from an ECZ file
   success = EcXmlObjectReaderWriter::readFromFile(simulationIn, filename);

   // declare a visualizable stated system object
   EcVisualizableStatedSystem visStatedSystem;

   simulationIn.getVisualizableStatedSystem(visStatedSystem);

   // make sure it loaded properly
   if(!success)
   {
      EcWARN("Could not load simulation.\n");
      return;
   }

   // -----------------------------------------------------------------
   // End example section #1
   // -----------------------------------------------------------------


   // -----------------------------------------------------------------
   // Example section #2 - Viewing a stated system.
   // -----------------------------------------------------------------

   // instantiate a renderer
   EcRenderWindow renderer;

   // set the size of the window
   renderer.setWindowSize(256,256);
  
   // set the system
   if(!renderer.setVisualizableStatedSystem(visStatedSystem))
   {
      return;
   }
   
   // view the system
   renderer.renderScene();

   // pause one second
   EcSLEEPMS(1000);

   // -----------------------------------------------------------------
   // End example section #2
   // -----------------------------------------------------------------

   // -----------------------------------------------------------------
   // Example section #3 - Configuring hardware
   // -----------------------------------------------------------------

   EcU32 waitTimeInMS;
   cyton::hardwareInterface hardware("cytonPlugin", "cytonConfig.xml");

   // get ports
   EcStringVector ports = hardware.availablePorts();
   const EcU32 numPorts = ports.size();
   EcBoolean foundPort=EcFalse;
   if(numPorts)
   {
      std::cout << "Tested ports:\n";
      for(EcU32 ii=0; ii<numPorts; ++ii)
      {
         std::cout << "\t" << ports[ii] << "\n";
         hardware.setPort(ports[ii].c_str());
         if(hardware.init())
         {
            foundPort=EcTrue;
            break;
         }
      }

      if(foundPort)
      {
         // This gives a velocity of 0.3 radians per second 
         const EcReal deltaTime = 10.0; // 10 seconds

         // This method will set the internal routines to go from one position to
         // another in this much time.  It is a convenience routine in lieu of
         // specifying a joint velocity on a per-joint basis.
         hardware.setVelocityFromDeltaTime(deltaTime);
         // Give a pretty conservative wait time (2x)
         waitTimeInMS = EcU32(2 * deltaTime * 1000);
      }
      else
      {
         // did not find port
         std::cout << "Cound not find valid port.\n";
         return;
      }
   }

   const EcU32 numJoints = hardware.numJoints();
   if(numJoints)
   {
      std::cout << "Found "<<numJoints<<" joints.\n";
   }
   else
   {
      std::cerr << "Invalid configuration. No joints available.\n";
      return;
   }

   EcRealVector initAngles(numJoints, 0.0);
   EcRealVector jointAngles=initAngles;


   // Set all joints to their init value.
   std::cout << "Setting all joints to init(0) angles.\n";
   if(!hardware.setJointCommands(initAngles) ||
      !hardware.waitUntilCommandFinished(waitTimeInMS))
   {
      std::cerr << "Problem setting initialize angles.\n";
      return;
   }
   EcSLEEPMS(1000); // Pause for a second to visually verify finished cmd.



   // -----------------------------------------------------------------
   // Example section #4 - Defining a position-control system.
   // -----------------------------------------------------------------

   // a variable holding the position control system
   EcPositionControlSystem posContSys;

   // set the position control system from the loaded simualtion
   posContSys=simulationIn.positionControlSystem();

   // set the stated system
   posContSys.setStatedSystem(&visStatedSystem.statedSystem());

   // -----------------------------------------------------------------
   // End example section #4
   // -----------------------------------------------------------------

   // -----------------------------------------------------------------
   // Example section #5 - Placing the end effector.
   // -----------------------------------------------------------------

   // end effector selection
   const EcU32 handIndex=2;
   // get the current offset in system coordinates
   EcCoordinateSystemTransformation
      initialPose=posContSys.actualPlacement(0,handIndex);

   // set the desired position to be offset from the current position
   // by 0.15 m along the z-axis 
   EcCoordinateSystemTransformation finalPose = initialPose;
   finalPose.outboardTransformBy(EcVector(-0.0,0.0,-0.15));
   posContSys.setDesiredPlacement(0,handIndex,finalPose);

   // a state to update and render, and an object to hold the 
   // final pose
   EcManipulatorSystemState dynamicState;
   EcCoordinateSystemTransformation calculatedFinalPose;

   // set the system
   if(!renderer.setVisualizableStatedSystem(visStatedSystem))
   {
      return;
   }
   
   // execution parameters
   EcU32 steps=100;
   EcReal simRunTime = 10.0;
   EcReal simTimeStep = simRunTime/steps;

   // move to the desired pose, and render every timestep
   for(ii=0;ii<steps;++ii)
   {
      // get the current time
      EcReal currentTime=simTimeStep*ii;

      // calculate the state at current time 
      posContSys.calculateState(currentTime,dynamicState);

      // show the manipulator in this position
      renderer.setState(dynamicState);
      renderer.renderScene();

      // set the hardware
      if(!setHardwareValues(hardware,dynamicState.positionStates()[0].jointPositions()))
      {
         std::cerr << "Problem setting angles for step " << ii << ".\n";
         return;
      }

      EcSLEEPMS(static_cast<EcU32>(1000*simTimeStep));
   }

   // check for accuracy
   calculatedFinalPose = posContSys.actualPlacementVector()[0].
      offsetTransformations()[handIndex];
   if(!calculatedFinalPose.approxEq(finalPose,1e-5))
   {
      EcWARN("Did not converge.\n");
      return;
   }

   // -----------------------------------------------------------------
   // End example section #5
   // -----------------------------------------------------------------

   // -----------------------------------------------------------------
   // Example section #6 - Moving the end effector along a path
   // -----------------------------------------------------------------

   // execution parameters
   steps=400;
   simRunTime = 10.0;
   simTimeStep = simRunTime/steps;
   EcReal radius=0.2;
   EcU32  loops=3;
   EcOrientation orient(0,0,0,1);
   EcReal startingTime=posContSys.time();

   // move to the desired pose, and render the position every 
   // time step
   for(ii=0;ii<steps;++ii)
   {
      // get the current time
      EcReal currentTime=simTimeStep*ii;

      // set the pose
      EcCoordinateSystemTransformation pose;
      pose.setOrientation(orient);
      EcReal angle=Ec2Pi*loops*currentTime/simRunTime;
      EcVector offset=radius*EcVector(cos(angle),sin(angle),0);
      pose.setTranslation(finalPose.translation()+offset);
      posContSys.setDesiredPlacement(0,handIndex,pose);

      // calculate the state at current time 
      posContSys.calculateState(
         currentTime+startingTime,dynamicState);

      // show the manipulator in this position
      renderer.setState(dynamicState);
      renderer.renderScene();

      // set the hardware
      if(!setHardwareValues(hardware,dynamicState.positionStates()[0].jointPositions()))
      {
         std::cerr << "Problem setting angles for step " << ii << ".\n";
         return;
      }

      EcSLEEPMS(static_cast<EcU32>(1000*simTimeStep));
   }

   // -----------------------------------------------------------------
   // End example section #6
   // -----------------------------------------------------------------

   // -----------------------------------------------------------------
   // Example section #7 - Creating a simulation.
   // -----------------------------------------------------------------

   // a variable holding the simulation
   EcSystemSimulation simulation;

   // reset time to zero and add the position control system and
   // visualization parameters
   posContSys.setTime(0.0);
   simulation.setFromPositionControlSystem(posContSys,
      visStatedSystem.visualizationParameters());

   // save the simulation as a plain XML file
   EcXmlObjectReaderWriter::writeToFile(simulation, "quickStartHardwareSimulation.xml");

   // save the simulation as a compressed XML file
   EcXmlObjectReaderWriter::writeToFile(simulation, "quickStartHardwareSimulation.xml.gz");

   // -----------------------------------------------------------------
   // End example section #7
   // -----------------------------------------------------------------

   // finish
   EcPRINT("Successfully completed example.\n");
}

/// set joint angles in hardware
EcBoolean EcQuickStartHardwareExample::setHardwareValues
   (
   cyton::hardwareInterface& hardware,
   const EcXmlRealVector& jointValues
   )
{
   const EcU32 numJoints = jointValues.size();

   m_JointAngles.resize(numJoints);

   // set the hardware
   for(EcU32 jj=0;jj<numJoints;++jj)
   {
      m_JointAngles[jj]=jointValues[jj];
   }

   if(!hardware.setJointCommands(m_JointAngles))
   {
      std::cerr << "Problem setting angles.\n";
      return EcFalse;
   }

   return EcTrue;
}

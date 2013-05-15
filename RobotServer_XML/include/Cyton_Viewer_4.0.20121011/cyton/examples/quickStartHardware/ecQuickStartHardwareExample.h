#ifndef EcQuickStartHardwareExample_H_
#define EcQuickStartHardwareExample_H_
//     Copyright (c) 2012 Energid Technologies. All rights reserved. ////
//
// Filename:    EcQuickStartHardwareExample.h
//
// Description: Holds the hardware quick-start code.
//
// Contents:    class EcQuickStartHardwareExample 
//
/////////////////////////////////////////////////////////////////////////
#define EC_HAVE_ACTIN
#include <hardwareInterface/cytonHardwareInterface.h>
#include <xml/ecXmlVectorType.h>

/** Holds the hardware quick-start code.
*/
class EcQuickStartHardwareExample 
{
public:
   /// run the quick-start code
   void run();

   /// set joint angles in hardware
   EcBoolean setHardwareValues
      (
      cyton::hardwareInterface& hardware,
      const EcXmlRealVector& jointValues
      );

   EcRealVector m_JointAngles;
};

#endif // EcQuickStartHardwareExample_H_

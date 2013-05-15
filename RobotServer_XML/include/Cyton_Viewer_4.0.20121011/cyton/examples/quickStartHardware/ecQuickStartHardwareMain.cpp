//     Copyright (c) 2012 Energid Technologies. All rights reserved. ////
//
// Filename:    ecQuickStartHardwareMain.cpp
//
// Description: main() for the quick-start hardware example
//
/////////////////////////////////////////////////////////////////////////
#include "ecQuickStartHardwareExample.h"
#include <xml/ecXmlWriter.h>

int main(int argc, char* argv[])
{
   EC_DEBUG_FLAGS;

   // set the MCML lanauge.
   EcXmlWriter::setDefaultLanguageSelection( EcXmlWriter::MCML_V1_0_0 );

   // create and run the example
   EcQuickStartHardwareExample example;
   example.run();

   return 0;
}

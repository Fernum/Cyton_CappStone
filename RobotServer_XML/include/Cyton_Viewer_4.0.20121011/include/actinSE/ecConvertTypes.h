#ifndef ec_ConvertTypes_H_
#define ec_ConvertTypes_H_
//     Copyright (c) 2009-2010 Energid Technologies. All rights reserved. ////
//
// Filename:    ecConvertTypes.h
//
// Description: Convenience routines to translate from one type to another.
//
/////////////////////////////////////////////////////////////////////////
#include "CoordinateSystemTransformation.h"
#include <foundCommon/ecCoordSysXForm.h>

namespace Ec
{
   inline EcVector toEC
      (
      const actinSE::Array3& v
       ) { return EcVector(v[0],v[1],v[2]); }

   inline actinSE::Array3 fromEC
      (
      const EcVector& v
      ) { return actinSE::Array3(v[0],v[1],v[2]); }

   inline EcOrientation toEC
      (
      const actinSE::Orientation& o
       ) { return EcOrientation(o[0],o[1],o[2],o[3]); }

   inline actinSE::Orientation fromEC
      (
      const EcOrientation& o
      ) { return actinSE::Orientation(o[0],o[1],o[2],o[3]); }

   inline EcCoordinateSystemTransformation toEC
      (
      const actinSE::CoordinateSystemTransformation& coord
      )
   {
      switch(coord.mode())
      {
      case actinSE::CoordinateSystemTransformation::NO_TRANSLATION: return EcCoordinateSystemTransformation(toEC(coord.orientation()));
      case actinSE::CoordinateSystemTransformation::NO_ROTATION: return EcCoordinateSystemTransformation(toEC(coord.translation()));
      case actinSE::CoordinateSystemTransformation::ARBITRARY:
      default:
         break;
      }
      return EcCoordinateSystemTransformation(toEC(coord.translation()),toEC(coord.orientation()));
   }

   inline actinSE::CoordinateSystemTransformation fromEC
      (
      const EcCoordinateSystemTransformation& coord
      )
   {
      switch(coord.mode())
      {
      case EcCoordinateSystemTransformation::NO_TRANSLATION: return actinSE::CoordinateSystemTransformation(fromEC(coord.orientation()));
      case EcCoordinateSystemTransformation::NO_ROTATION: return actinSE::CoordinateSystemTransformation(fromEC(coord.translation()));
      case EcCoordinateSystemTransformation::ARBITRARY:
      default:
         break;
      }
      return actinSE::CoordinateSystemTransformation(fromEC(coord.translation()),fromEC(coord.orientation()));
   }
} // namespace Ec

#endif // ec_ConvertTypes_H_

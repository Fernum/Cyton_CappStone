#ifndef actinSE_EndEffectorImpl_H_
#define actinSE_EndEffectorImpl_H_
//     Copyright (c) 2009-2010 Energid Technologies. All rights reserved. ////
//
// Filename:    EndEffectorImpl.h
//
// Description: Internal implementation details for actinSE::EndEffector
//              within the actinSE::ControlSystem class.
//
// Contents:    class actinSE::EndEffectorImpl
//
/////////////////////////////////////////////////////////////////////////
#include "EndEffector.h"
#include <foundCore/ecConstants.h>

// forward declarations
class EcSimulationPositionControlContainer;
class EcEndEffector;

namespace actinSE
{

// Internal implementation details for actinSE::EndEffector.
struct EndEffectorImpl
{
   /// Default constructor.
   /// @param EE   Reference to the internal Ec end-effector
   explicit EndEffectorImpl
      (
      EcEndEffector& EE
      );

   /// Copy constructor
   /// @param orig Implementation to copy from
   EndEffectorImpl
      (
      const EndEffectorImpl& orig
      );

   /// Equality operator
   /// @param  rhs       Other object to compare against
   /// @return EcBoolean Whether the comparison is successful or not
   EcBoolean operator==
      (
      const EndEffectorImpl& rhs
      ) const;

   /// Determines the actinSE equivalent of this Ec end-effector.
   /// @param  EE         Ec end-effector to lookup
   /// @return EETypeEnum end-effector type from lookup
   static EndEffector::EETypeEnum type
      (
      const EcEndEffector& EE
      );

   /// Creates a new Ec end-effector based on the type
   /// @param  type             EE type to create
   /// @return EndEffectorImpl* Handle to prepared internal pointer
   static EndEffectorImpl* newFromType
      (
      const EndEffector::EETypeEnum type
      );

  /// Sets the link offset.  If a subset of the coord can be set
  /// then it will do what it can.
  /// @param  coord     Position and orientation to set
  /// @return EcBoolean Fail if the EE doesn't support
  EcBoolean setOffset
     (
     const CoordinateSystemTransformation& coord
     );

  /// Retrieve the link offset.  If a subset of the coord can be
  /// set, then it will do what it can.
  /// @param  coord     Position and orientation to get
  /// @return EcBoolean Fail if the EE doesn't support
  EcBoolean getOffset
     (
     CoordinateSystemTransformation& coord
     ) const;

   EcEndEffector& m_EE;                          ///< Reference to Ec version of EE
   mutable EcSimulationPositionControlContainer* m_pControlSystem; ///< Attached control system
   EndEffector::EETypeEnum m_EEType;             ///< The type of EE this is
   EndEffector::EEStateFlags m_EEState;          ///< Bit flags denoting current state
   EcU32 m_EEIndex;                              ///< Index into vector of EEs.
};

} // namespace actinSE

#endif // actinSE_EndEffectorImpl_H_

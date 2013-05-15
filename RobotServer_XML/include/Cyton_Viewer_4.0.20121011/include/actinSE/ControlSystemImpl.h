#ifndef actinSE_ControlSystemImpl_H_
#define actinSE_ControlSystemImpl_H_
//     Copyright (c) 2009-2010 Energid Technologies. All rights reserved. ////
//
// Filename:    ControlSystemImpl.h
//
// Description: Internal implementation details for actinSE::Controlsystem.
//
// Contents:    struct actinSE::ControlSystemImpl
//
/////////////////////////////////////////////////////////////////////////
#include "ControlSystem.h"
#include "CoordinateSystemTransformation.h"
#include <foundCore/ecTypes.h>
#include <simulation/ecSysSimulation.h>

// forward declarations
class EcEndEffector;
class EcSimulationPositionControlContainer;
class EcStatedSystem;

namespace actinSE
{

typedef boost::shared_ptr<EndEffector> SharedEndEffector;
typedef std::vector<SharedEndEffector> SharedEndEffectorVector;
class RenderPtrs;

struct ControlSystemImpl
{
   /// Default constructor.  Zeros pointers.
   ControlSystemImpl
      (
      );

   /// Destructor.  Cleans up memory
   ~ControlSystemImpl
      (
      );

   /// Equality operator
   /// @param  rhs       Other object to compare against
   /// @return EcBoolean Whether the comparison is successful or not
   EcBoolean operator==
      (
      const ControlSystemImpl& rhs
      ) const;
   
   /// Loads and initializes internal variables.
   /// @param  fileName  File to load
   /// @return EcBoolean Success or failure of command
   EcBoolean initFromFile
      (
      const EcString& fileName
      );

   /// Create a new EndEffector from its associated Ec type.
   /// @param  ee                Ec-based end-effector used internally
   /// @return SharedEndEffector Pointer to new external object
   SharedEndEffector newEndEffectorFromEc
      (
      EcEndEffector& ee
      ) const;

   /// Set end effector vector and update status flags.
   /// @param value Vector to set
   void setSharedEndEffectors
      (
      const SharedEndEffectorVector& value
      );

   /// Recalculate state at a specified time.
   /// @param[in] timeInSeconds Time to calculate state at
   /// @return    EcBoolean     Success or failure of command
   EcBoolean calculateState
      (
      const EcReal timeInSeconds
      );

   /// Enable or disable rendering.  Only valid when compiled with rendering libraries.
   /// @param[in] renderState Desired state of rendering
   /// @return    EcBoolean   Success or failure of command.  Fails if it can't
   ///                        create a window or if not linked with render libs
   EcBoolean setRenderState
      (
      const EcBoolean renderState
      );

   /// Retrieve current state of rendering
   /// @return EcBoolean Whether rendering is enabled or disabled
   EcBoolean renderState
      (
      ) const;

   /// Variables that make up the internals of the control system.
   EcString                              m_SystemFile;     ///< Loaded control system file
   EcStatedSystem*                       m_pStatedSystem;  ///< Current stated system
   EcSystemSimulation                    m_SysSimulation;  ///< Parent simulation
   EcSimulationPositionControlContainer* m_pControlSystem; ///< Control system
   RenderPtrs*                           m_pRenderer;      ///< Rendering window
   EcBoolean                             m_Initialized;    ///< Whether implementation is loaded

   /// Cached internal representations of certain variables.
   mutable SharedEndEffectorVector        m_EEVec;       ///< actinSE version of EESet
   mutable EcRealVector                   m_JointValues; ///< Joint positions
   mutable EcRealVector                   m_JointRates;  ///< Joint velocities
   mutable CoordinateSystemTransformation m_BasePose;    ///< Base position and orientation
   mutable EcManipulatorSystemState       m_State;       ///< Current state of simulation
   mutable EcManipulatorActiveState       m_ActiveState; ///< Active state for computing
};

} // namespace actinSE

#endif // actinSE_ControlSystemImpl_H_

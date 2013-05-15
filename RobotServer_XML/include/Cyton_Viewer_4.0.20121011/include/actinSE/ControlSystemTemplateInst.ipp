//     Copyright (c) 2009-2010 Energid Technologies. All rights reserved. ////
//
// Filename:    ControlSystemTemplateInst.ipp
//
// Description: Explicit instantiations of available getParam/setParam
//              types for the actinSE::ControlSystem class.  These are the
//              only valid specialized types.
//
/////////////////////////////////////////////////////////////////////////

//------------------------------------------------------------------------------
// Provide a convenience macro to forward declare a specific parameter type.
//------------------------------------------------------------------------------
#define FWD_DECL_SPARAM(T1,T2) \
   EC_ACTINSE_TEMPLATE_DECL template EC_ACTINSE_DECL EcBoolean ControlSystem::setParam<ControlSystem::T1,T2>(const T2&)
#define FWD_DECL_GETPARAM(T1,T2) \
   EC_ACTINSE_TEMPLATE_DECL template EC_ACTINSE_DECL EcBoolean ControlSystem::getParam<ControlSystem::T1,T2>(T2&) const
#define FWD_DECL_GETPARAM1(T1,T2) \
   EC_ACTINSE_TEMPLATE_DECL template EC_ACTINSE_DECL EcBoolean ControlSystem::getParam<ControlSystem::T1,T2>(const EcU32, T2&) const
#define FWD_DECL_GREFPARAM(T1,T2) \
   EC_ACTINSE_TEMPLATE_DECL template EC_ACTINSE_DECL const T2& ControlSystem::param<ControlSystem::T1,T2>() const
#define FWD_DECL_GPARAM(T1,T2) \
   EC_ACTINSE_TEMPLATE_DECL template EC_ACTINSE_DECL T2 ControlSystem::param<ControlSystem::T1,T2>()
// Default setParam, getParam and param methods
#define FWD_DECL_SGPARAM(T1,T2) FWD_DECL_SPARAM(T1,T2); FWD_DECL_GPARAM(T1,T2); FWD_DECL_GETPARAM(T1,T2)
// Same as above, but add a const reference param method
#define FWD_DECL_SGREFPARAM(T1,T2) FWD_DECL_SGPARAM(T1,T2); FWD_DECL_GREFPARAM(T1,T2);

FWD_DECL_SGPARAM   (Rendering,         EcBoolean);
FWD_DECL_SGPARAM   (SimulationTime,    EcReal);
FWD_DECL_GPARAM    (NumJoints,         EcU32);
FWD_DECL_SGREFPARAM(JointAngles,       EcRealVector);
FWD_DECL_SGREFPARAM(JointVelocities,   EcRealVector);
FWD_DECL_GETPARAM1 (JointPose,         CoordinateSystemTransformation);
FWD_DECL_GETPARAM1 (JointPose,         Orientation);
FWD_DECL_GETPARAM1 (JointPose,         Array3);
FWD_DECL_SGREFPARAM(BasePose,          CoordinateSystemTransformation);
FWD_DECL_SGREFPARAM(BasePose,          Orientation);
FWD_DECL_SGREFPARAM(BasePose,          Array3);
FWD_DECL_SGPARAM   (EndEffectors,      EndEffectorVector);
FWD_DECL_SGREFPARAM(EndEffectors,      SharedEndEffectorVector);
FWD_DECL_GPARAM    (CraigDHParameters, Array3Vector);

#undef FWD_DECL_SPARAM
#undef FWD_DECL_GPARAM
#undef FWD_DECL_GREFPARAM
#undef FWD_DECL_SGPARAM
#undef FWD_DECL_SGREFPARAM

//     Copyright (c) 2009-2010 Energid Technologies. All rights reserved. ////
//
// Filename:    EndEffectorTemplateInst.ipp
//
// Description: Explicit instantiations of available getParam/setParam
//              types for the actinSE::EndEffector class.  These are the
//              only valid specialized types.
//
/////////////////////////////////////////////////////////////////////////

//------------------------------------------------------------------------------
// Provide a convenience macro to forward declare a specific parameter type.
//------------------------------------------------------------------------------
#define FWD_DECL_SPARAM(T1,T2) \
   EC_ACTINSE_TEMPLATE_DECL template EC_ACTINSE_DECL EcBoolean EndEffector::setParam<EndEffector::T1,T2>(const T2&)
#define FWD_DECL_GPARAM(T1,T2) \
   EC_ACTINSE_TEMPLATE_DECL template EC_ACTINSE_DECL T2 EndEffector::param<EndEffector::T1,T2>() const; \
   EC_ACTINSE_TEMPLATE_DECL template EC_ACTINSE_DECL EcBoolean EndEffector::getParam<EndEffector::T1,T2>(T2&) const
#define FWD_DECL_SGPARAM(T1,T2) FWD_DECL_SPARAM(T1,T2); FWD_DECL_GPARAM(T1,T2)

FWD_DECL_SGPARAM(HardConstraint,      EcBoolean);
FWD_DECL_GPARAM (DegreesOfConstraint, EcU32);
FWD_DECL_SGPARAM(MotionThreshold,     EcReal);
FWD_DECL_SGPARAM(DesiredVelocity,     EcReal);
FWD_DECL_SGPARAM(DesiredVelocity,     EcRealVector);
FWD_DECL_SGPARAM(Gain,                EcReal);
FWD_DECL_SGPARAM(AttachedLink,        EcU32);
FWD_DECL_SGPARAM(AttachedLink,        EcString);
FWD_DECL_SGPARAM(AttachedOffset,      Array3);
FWD_DECL_SGPARAM(AttachedOffset,      Orientation);
FWD_DECL_SGPARAM(AttachedOffset,      CoordinateSystemTransformation);
FWD_DECL_SGPARAM(RelativeLink,        EcU32);
FWD_DECL_SGPARAM(RelativeLink,        EcString);
FWD_DECL_SGPARAM(DesiredPose,         Array3);
FWD_DECL_SGPARAM(DesiredPose,         Orientation);
FWD_DECL_SGPARAM(DesiredPose,         CoordinateSystemTransformation);
FWD_DECL_GPARAM (ActualPose,          Array3);
FWD_DECL_GPARAM (ActualPose,          Orientation);
FWD_DECL_GPARAM (ActualPose,          CoordinateSystemTransformation);

#undef FWD_DECL_SPARAM
#undef FWD_DECL_GPARAM
#undef FWD_DECL_SGPARAM

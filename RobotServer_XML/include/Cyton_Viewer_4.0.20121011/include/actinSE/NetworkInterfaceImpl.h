#ifndef actinSE_NetworkInterfaceImpl_H_
#define actinSE_NetworkInterfaceImpl_H_
//     Copyright (c) 2009-2010 Energid Technologies. All rights reserved. ////
//
// Filename:    NetworkInterfaceImpl.h
//
// Description: Internal implementation detail for the NetworkInterface class.
//              Holds opaque member variables.
//
// Contents:    actinSE::NetworkInterfaceImpl struct
//
/////////////////////////////////////////////////////////////////////////
#include "actinTypes.h"

#include <boost/shared_ptr.hpp>

namespace Ec { namespace network { class Handle; } }

namespace actinSE
{

template <typename T>
struct EC_ACTINSE_DECL NetworkInterfaceImpl
{
   NetworkInterfaceImpl
      (
      ):
   m_pHandle(EcNULL) {}

   void setHandle
      (
      T& intf
      );

   boost::shared_ptr<Ec::network::Handle> m_pNetworkHandle;
   T* m_pHandle;
};

} // namespace actinSE

#endif // actinSE_NetworkInterfaceImpl_H_

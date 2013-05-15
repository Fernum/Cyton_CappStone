#ifndef actinSE_RenderImpl_H_
#define actinSE_RenderImpl_H_
//     Copyright (c) 2012 Energid Technologies. All rights reserved. ////
//
// Filename:    RenderImpl.h
//
// Description: Internal implementation details for rendering.
//
// Contents:    struct actinSE::RenderImpl
//
/////////////////////////////////////////////////////////////////////////
#include <foundCore/ecTypes.h>

#ifdef actinSE_actinSERender_EXPORTS
#  define ACTINSE_RENDER_DECL EC_DECL_EXPORTS
#else
#  define ACTINSE_RENDER_DECL EC_DECL_IMPORTS
#endif

// forward declarations
class EcStatedSystem;
class EcManipulatorSystemState;
class EcRenderWindow;
class EcVisualizationParameters;

namespace actinSE
{

struct ACTINSE_RENDER_DECL RenderImpl
{
   /// Default constructor.  Zeros pointers.
   RenderImpl
      (
      );

   /// Destructor.  Cleans up memory
   ~RenderImpl
      (
      );

   /// @return EcBoolean Success or failure of command
   EcBoolean init
      (
      const EcStatedSystem& statedSystem,
      const EcVisualizationParameters& visParams
      );

   /// @return EcBoolean Success or failure of command
   EcBoolean update
      (
      const EcManipulatorSystemState& state
      );

   /// Enable or disable rendering.
   /// @param[in] renderState Desired state of rendering
   /// @return    EcBoolean   Success or failure of command.  Fails if it can't
   ///                        create a window
   EcBoolean setEnabled
      (
      const EcBoolean renderState
      );

   /// Retrieve current state of rendering
   /// @return EcBoolean Whether rendering is enabled or disabled
   EcBoolean enabled
      (
      ) const;

   EcRenderWindow* m_pRenderer; ///< Rendering window

   static RenderImpl* s_pImpl;
};

} // namespace actinSE

extern "C" EC_DECL_EXPORTS EcBoolean init(const EcStatedSystem&, const EcVisualizationParameters&);
extern "C" EC_DECL_EXPORTS EcBoolean update(const EcManipulatorSystemState&);
extern "C" EC_DECL_EXPORTS EcBoolean setEnabled(const EcBoolean);
extern "C" EC_DECL_EXPORTS EcBoolean enabled();


#endif // actinSE_RenderImpl_H_

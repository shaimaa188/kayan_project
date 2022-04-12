package product.productSystem.view.utils;

    import javax.faces.context.FacesContext;

    import oracle.adf.view.rich.component.rich.RichPopup;

    import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
    import org.apache.myfaces.trinidad.util.Service;

    public class PopupUtils {
        public PopupUtils() {
            super();
        }

        public static void showPopup(RichPopup popup) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExtendedRenderKitService service =
                Service.getRenderKitService(facesContext,
                                            ExtendedRenderKitService.class);
            service.addScript(facesContext,
                              "AdfPage.PAGE.findComponent('" + popup.getClientId(facesContext) +
                              "').show();");
        }

        public static void hidePopup(RichPopup popup) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExtendedRenderKitService service =
                Service.getRenderKitService(facesContext,
                                            ExtendedRenderKitService.class);
            service.addScript(facesContext,
                              "AdfPage.PAGE.findComponent('" + popup.getClientId(facesContext) +
                              "').hide();");
        }

    }

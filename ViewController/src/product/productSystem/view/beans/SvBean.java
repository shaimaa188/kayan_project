package product.productSystem.view.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;

import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPanelWindow;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.event.AttributeChangeEvent;

import org.apache.myfaces.trinidad.event.DisclosureEvent;

import org.apache.myfaces.trinidad.event.SelectionEvent;

import product.productSystem.view.utils.ADFUtils;
import product.productSystem.view.utils.JSFUtils;
import product.productSystem.view.utils.PopupUtils;

public class SvBean {
    private RichPopup delete_msg_popup;
    private RichInputText qtyTxt;
    private RichOutputText remainingQuatntity;
    private RichInputText remainingQtyTxt;
    private RichPopup quantity_popup;


    public SvBean() {
        
       
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public String deleteAction() {
        BindingContainer bindings = getBindings();
        
     OperationBinding operationBinding = bindings.getOperationBinding("Delete");
        OperationBinding operationBinding2 = bindings.getOperationBinding("Commit");
        operationBinding.execute();
        operationBinding2.execute(); 
       
            delete_msg_popup.hide();
        
         if (!operationBinding.getErrors().isEmpty()) {
            return null;
        } 
      
        return null;
    }


    public void setDelete_msg_popup(RichPopup delete_msg_popup) {
        this.delete_msg_popup = delete_msg_popup;
    }

    public RichPopup getDelete_msg_popup() {
        return delete_msg_popup;
    }
    

    
    public String trackProductDistribution_action(int pID,int invId,int inStock,int stockQty, String trxType) {
        int result=0;
        
       
        
        String plsql=
        "begin\n" + 
        "insert into PRODUCT_STOCK_DISTRIBUTION_TRX(P_ID,INV_ID,PRODUCT_QTY,STOCK_QTY, TRX_TYPE)VALUES(?,?,?,?,?); \n" +                                
        "commit;\n" + 
        "end;" ; 
        
        PreparedStatement  stat =null ;
        Connection   con =null;
        
            try {
                 
               DBConnection dbcon = new DBConnection();
               con = dbcon.getConnection(); 
                stat= con.prepareStatement(plsql); 
                stat.setInt(1,pID); 
                stat.setInt(2,invId); 
                stat.setInt(3,inStock); 
                stat.setInt(4,stockQty);
                stat.setString(5,trxType);
               result =stat.executeUpdate();
            
            } catch (Exception e) {
                String errorMsg="error_msg";
                 JSFUtils.addFacesErrorMessage(errorMsg);
                e.printStackTrace();
                 }
            
        finally
            {

            try
                  {
                    stat.close();
                    con.close();
                  }
            catch (Exception e)
                  {
                    // TODO: Add catch code
                    e.printStackTrace();
            }
            } 
       
        return null;
    }


    public String addProductToInventory_action() {
             
        Integer pID = Integer.parseInt(ADFUtils.getBoundAttributeValue("PId").toString());
        Integer invId = Integer.parseInt(ADFUtils.getBoundAttributeValue("InventoryNo").toString());
        Integer productQty = Integer.parseInt(ADFUtils.getBoundAttributeValue("Product_Quantity").toString());
        Integer inStock = Integer.parseInt(ADFUtils.getBoundAttributeValue("InStock").toString());
        Integer stockQty = Integer.parseInt( qtyTxt.getValue().toString());
        Integer remainigQty= Integer.parseInt(ADFUtils.getBoundAttributeValue("remainingQuantity").toString());
        
     
     
    if (stockQty <= remainigQty) {
        
            ADFUtils.findOperation("CreateInsert_product_inventory").execute(); 
                            
            ADFUtils.setBoundAttributeValue("ProductId",pID);
            ADFUtils.setBoundAttributeValue("InventoryId",invId);
            ADFUtils.setBoundAttributeValue("Quantity" , stockQty);
            
            inStock += stockQty;
            int result=0;
                
         //ADFUtils.setBoundAttributeValue("InStock",inStock);
            
         String plsql=
         "begin\n" + 
         "update PRODUCT\n" + 
         "set IN_STOCK=  ? \n"+
         "where\n" + 
         "P_ID=?;\n" +  
         "commit;\n" + 
         "end;" ; 
         
         PreparedStatement  stat =null ;
         Connection   con =null;
         
             try {
                  
                DBConnection dbcon = new DBConnection();
                con = dbcon.getConnection();
                 
                  stat= con.prepareStatement(plsql);
                 
                  stat.setInt(1,inStock);
                  stat.setInt(2,pID); 
         
                // stat.setString(2,ADFContext.getCurrent().getSessionScope().get("EmpCode").toString());
                 result=stat.executeUpdate();
             
             } catch (Exception e) {
                 
         //                     JSFUtils.addFacesErrorMessage(bnd.getTextProp("creation_error_msg"));
                  
                 e.printStackTrace();
                  }
         
         finally
             {

             try
                   {
                     stat.close();
                     con.close();
                   }
             catch (Exception e)
                   {
                     // TODO: Add catch code
                     e.printStackTrace();
             }
             } 
         
         ADFUtils.findOperation("Commit").execute();    
         ADFUtils.findOperation("Execute").execute();
         ADFUtils.findOperation("Execute_Product").execute();
        
        quantity_popup.hide();
         
        //insert in to PRODUCT_STOCK_DISTRIBUTION_TRX
        
        }
        
        else if (stockQty > remainigQty){
            
                JSFUtils.addFacesErrorMessage("«·⁄œœ «·„œŒ· ›Ì «·„Œ“‰ «ﬂ»— „‰ ⁄œœ «·„‰ Ã");
            }
    
        trackProductDistribution_action(pID,invId,remainigQty, stockQty,"Add");
        
        return null;
    }
    

    public String deleteInventoryProduct_action() {
        
    int result=0;
    
    Integer pID =  Integer.parseInt(ADFUtils.getBoundAttributeValue("ProductId").toString());
    Integer productInventoryQty = Integer.parseInt(ADFUtils.getBoundAttributeValue("Quantity").toString());   
    Integer invId = Integer.parseInt(ADFUtils.getBoundAttributeValue("InventoryId").toString());
    Integer remainigQty= Integer.parseInt(ADFUtils.getBoundAttributeValue("remainingQuantity").toString());   
    
    ADFUtils.findOperation("Delete1").execute(); 
   
    
   Integer inStock = Integer.parseInt(ADFUtils.getBoundAttributeValue("InStock").toString());
    //int inStock = 0; 
    
     //productInventoryQty =- inStock;
    //productInventoryQty -= inStock;
   
             String plsql=
             "begin\n" + 
             "update PRODUCT\n" + 
             "set IN_STOCK= IN_STOCK- ? \n"+
             "where\n" + 
             "P_ID=?;\n" +  
             "commit;\n" + 
             "end;" ; 
             
             PreparedStatement  stat =null ;
             Connection   con =null;
             
                 try {
                      
                    DBConnection dbcon = new DBConnection();
                    con = dbcon.getConnection();
                     
                      stat= con.prepareStatement(plsql);
                     
                      stat.setInt(1,productInventoryQty);
                      stat.setInt(2,pID); 
            
                    // stat.setString(2,ADFContext.getCurrent().getSessionScope().get("EmpCode").toString());
                     result=stat.executeUpdate();
                 
                 } catch (Exception e) {
                     
            //                     JSFUtils.addFacesErrorMessage(bnd.getTextProp("creation_error_msg"));
                      
                     e.printStackTrace();
                      }
             
             finally
                 {

                 try
                       {
                         stat.close();
                         con.close();
                       }
                 catch (Exception e)
                       {
                         // TODO: Add catch code
                         e.printStackTrace();
                 }
                 } 
                 
        ADFUtils.findOperation("Commit").execute();      
        ADFUtils.findOperation("Execute").execute();
                
    ADFUtils.findOperation("Execute_Product").execute();
   
    trackProductDistribution_action(pID,invId,remainigQty,0,"Remove"); 
        

     return null;
       
    }
    

    public String deleteInventory_action() {
        
    
        
        ADFUtils.findOperation("Delete").execute(); 
        ADFUtils.findOperation("Commit").execute();      
        ADFUtils.findOperation("Execute").execute();
        ADFUtils.findOperation("Execute_Product").execute();
       
       
        
        return null;
    
}
    

    public void setQtyTxt(RichInputText qtyTxt) {
        this.qtyTxt = qtyTxt;
    }

    public RichInputText getQtyTxt() {
        return qtyTxt;
    }
   
    public void setRemainingQuatntity(RichOutputText remainingQuatntity) {
        this.remainingQuatntity = remainingQuatntity;
    }

    public RichOutputText getRemainingQuatntity() {
        return remainingQuatntity;
    }

    public void setRemainingQtyTxt(RichInputText remainingQtyTxt) {
        this.remainingQtyTxt = remainingQtyTxt;
    }

    public RichInputText getRemainingQtyTxt() {
        return remainingQtyTxt;
    }

    public void sdh1_disclosureListener(DisclosureEvent disclosureEvent) {
        // Add event code here...
    }
    
    //    public void selectProducts(ActionEvent actionEvent) {
    //        
    //        String mesg = "ok";
    //        
    //        Integer inStock = Integer.parseInt(ADFUtils.getBoundAttributeValue("InStock").toString());
    //                    
    //        
    //    try{
    //        BindingContext bindingctx=BindingContext.getCurrent();   
    //        BindingContainer binding=bindingctx.getCurrentBindingsEntry();        
    //        DCBindingContainer bindingsImpl = (DCBindingContainer) binding;       
    //        DCIteratorBinding dciter = bindingsImpl.findIteratorBinding("Product1Iterator");  
    //          
    //        ViewObject vo=dciter.getViewObject();
    //                
    //        Row rr=null;  
    //          
    //        int i=0;  
    //          
    //        int j=0;  
    //          
    //        boolean flag=false;  
    //          
    //        long rowc=vo.getEstimatedRowCount();  
    //          
    //        for(int f=1;f<=rowc;f++)  
    //          
    //        {  
    //          
    //        if(i==0)  
    //          
    //        {  
    //          
    //        rr=vo.first();  
    //          
    //        }  
    //          
    //        else  
    //          
    //        {  
    //          
    //        rr=vo.next();  
    //          
    //        }  
    //          
    //        i++;  
    //           
    //        flag=false;  
    //          
    //        if((rr.getAttribute("Selected")) != null)  
    //          
    //        {  
    //            flag= (Boolean)rr.getAttribute("Selected"); 
    //          
    //        }  
    //          
    //        if (flag)  
    //          
    //        {  
    //                
    //                ADFUtils.findOperation("CreateInsert_product_inventory").execute(); 
    //                
    //                ADFUtils.setBoundAttributeValue("ProductId",rr.getAttribute("PId"));
    //                ADFUtils.setBoundAttributeValue("InventoryId",ADFUtils.getBoundAttributeValue("InventoryNo"));
    //                
    //                System.out.print("---5---");
    //           
    //            if (inStock == 0){
    //            
    //              ADFUtils.setBoundAttributeValue( "InStock" ,"1");         
    //
    //               } 
    //                ADFUtils.findOperation("Commit").execute();    
    //                ADFUtils.findOperation("Execute").execute(); 
    //                
    //               
    //     
    //                JSFUtils.addFacesInformationMessage("product_sucessfully_added");                      
    //            
    //        }
    //        }
    //    }           
    //                catch(Exception ex) {
    //            
    //                 ADFUtils.findOperation("Rollback").execute();  
    //                 ADFUtils.findOperation("Execute").execute();
    //             
    //            //  JOptionPane.showMessageDialog(null, "Œÿ√!  „ ≈œŒ«· ‰›” «·÷„«‰ ”«»ﬁ« «·Ï Â–Â «·Õ—ﬂ…");
    //                JSFUtils.addFacesErrorMessage("Error"); 
    //        }  
    //    
    //    
    //        ADFUtils.findOperation("Execute_Product").execute();
    //       
    //    }


    public void setQuantity_popup(RichPopup quantity_popup) {
        this.quantity_popup = quantity_popup;
    }

    public RichPopup getQuantity_popup() {
        return quantity_popup;
    }
}


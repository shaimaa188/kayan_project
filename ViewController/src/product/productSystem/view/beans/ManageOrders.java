package product.productSystem.view.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.binding.BindingContainer;

import oracle.binding.OperationBinding;

import product.productSystem.view.utils.ADFUtils;
import product.productSystem.view.utils.JSFUtils;
import product.productSystem.view.utils.PopupUtils;

public class ManageOrders {
    
    private RichPopup addCustomerOrder_Popup;
    PopupUtils pop = new PopupUtils();
    private RichPopup addProductToOrder_Popup;
    private RichPopup addQuantityPopup;
    private RichInputText order_qty_txt;
    private RichInputText sale_price_txt;
    private RichPopup quantity_popup;

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }
    
    
    /**------------Order_Block__--------------------*/ 
    
    

        public String CommitNewOrder_action() {
            // Add event code here...
            ADFUtils.findOperation("Commit").execute();
            //System.out.println("I AM New");
            pop.showPopup(addCustomerOrder_Popup);
            
            //addCustomerOrder_Popup.hide();
           // ADFUtils.findOperation("Execute_all_orders").execute();
            
            return null;
        }

        public String UpdateOrder_action() {
        
            ADFUtils.findOperation("Commit").execute(); 
           
           // System.out.println("I AM Update");
            
            ADFUtils.findOperation("Execute_all_orders").execute();
            return null;
        }


     

        public String customerOrder_action() {
            BindingContainer bindings = getBindings();
          
            ADFUtils.setBoundAttributeValue("CustomerNumber" ,ADFUtils.getBoundAttributeValue("CNumber1"));
           
            ADFUtils.findOperation("Commit").execute(); 
    //         ADFUtils.findOperation("ExecuteWithParams").execute();  
            addCustomerOrder_Popup.hide();
            
          ADFUtils.findOperation("ExecuteWithParams").execute();
            
         ADFUtils.findOperation("Execute_all_orders").execute();
            
            return null;
        }
        
    public String RemoveProductOrder() {
        // Add event code here...     
        int result=0;
        
        int productId_ord2 = Integer.parseInt(ADFUtils.getBoundAttributeValue("ProductId_ord2").toString());
        int inventoryId_ord2 = Integer.parseInt(ADFUtils.getBoundAttributeValue("InventoryId_ord2").toString());
        int orderQuantity = Integer.parseInt(ADFUtils.getBoundAttributeValue("OrderQuantity1").toString());
        int inStock = Integer.parseInt(ADFUtils.getBoundAttributeValue("InStock").toString());
        
        inStock += orderQuantity;
            
        String plsql=
        "begin\n" + 
        "update INVENTORY_PRODUCT\n" + 
        "set QUANTITY= QUANTITY + ? \n"+
        "where\n" + 
        "INVENTORY_ID = ?\n" +
        "and\n" + 
        "PRODUCT_ID = ?;\n" +
        "commit;\n" + 
        "end;" ; 
        
        PreparedStatement  stat =null ;
        Connection   con =null;
        
            try {
                 
               DBConnection dbcon = new DBConnection();
               con = dbcon.getConnection();
                
                stat= con.prepareStatement(plsql);
                
                stat.setInt(1,orderQuantity);
                stat.setInt(2,inventoryId_ord2);
                stat.setInt(3,productId_ord2); 
        
               // stat.setString(2,ADFContext.getCurrent().getSessionScope().get("EmpCode").toString());
                result=stat.executeUpdate();
            
            } catch (Exception e) {
                       
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
        
        ADFUtils.findOperation("Delete_Product_Order").execute();
        ADFUtils.findOperation("Commit").execute(); 
        ADFUtils.findOperation("Execute_inventory_product").execute();
        
        return null;
    }
    
//------------orderStatus-----------------//
    
    public String prepareOrder_action() {
        
        int pendening = 2;
       
        
        ADFUtils.setBoundAttributeValue("OrderStatus" ,pendening);
        
        ADFUtils.findOperation("Commit").execute(); 
        
        return null;
    }
    
    public String cancelOrder_action() {
        
        int cancel = 4;
        
        ADFUtils.setBoundAttributeValue("OrderStatus" ,cancel);
        
        ADFUtils.findOperation("Commit").execute(); 
        
       
        return null;
    }
    
//------------endOfOrderStatus-----------------//
    
 ///-----------------------------------------------------------//       
        public String addProductOrder_action() {
                     
             pop.showPopup(addQuantityPopup);
            //ADFUtils.findOperation("Execute_inventory_product").execute();                    
            return null;
        }
        
        public String CommitProductToOrder() {
                 
            int result=0;
            
            int salePrice = Integer.parseInt(sale_price_txt.getValue().toString()); 
            int order_no = Integer.parseInt(ADFUtils.getBoundAttributeValue("OrderNumber").toString());  
            int pId_inventory = Integer.parseInt(ADFUtils.getBoundAttributeValue("PId_inventory").toString());
            int inventoryId = Integer.parseInt(ADFUtils.getBoundAttributeValue("InventoryId").toString());
            int order_qty =  Integer.parseInt(order_qty_txt.getValue().toString());
            int inventoryQuantity = Integer.parseInt(ADFUtils.getBoundAttributeValue("InventoryQuantity").toString());
            int inStock = Integer.parseInt(ADFUtils.getBoundAttributeValue("InStock").toString());
           // ADFUtils.setBoundAttributeValue("InventoryNo_ord" ,"1");
            
            
            /**decrease quantity in the inventory**/
            
            if( order_qty <= inventoryQuantity &  order_qty > 0 ){
            
            inventoryQuantity -= order_qty;
            inStock -= order_qty;
            
            ADFUtils.setBoundAttributeValue("InventoryQuantity",inventoryQuantity);
               
               
                
                String plsql=
                "begin\n" + 
                "update PRODUCT\n" + 
                "set IN_STOCK= IN_STOCK - ? \n"+
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
                         stat.setInt(2,pId_inventory); 
                
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
            ADFUtils.findOperation("Execute_inventory_product").execute();
             
            /**add the prodeucts in to the orders**/
            
            String plsql2=
            "begin\n" + 
            "insert into ORDER_PRODUCTS(ORDER_ID,PRODUCT_ID,INVENTORY_ID,ORDER_QUANTITY,SALE_PRICE)VALUES(?,?,?,?,?); \n" +                                
            "commit;\n" + 
            "end;" ; 
            
            PreparedStatement  stat2 =null ;
            Connection  con2 =null;
            System.out.println("----2----");
                try {
                     
                   DBConnection dbcon = new DBConnection();
                   con = dbcon.getConnection(); 
                    stat= con.prepareStatement(plsql2); 
                    stat.setInt(1,order_no); 
                    stat.setInt(2,pId_inventory); 
                    stat.setInt(3,inventoryId); 
                    stat.setInt(4,order_qty);
                    stat.setInt(5,salePrice);
                   
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
                
            
                
            ADFUtils.findOperation("Execute_order_product").execute(); 
               
            JSFUtils.addFacesInformationMessage(" „ ≈÷«›… „‰ Ã »‰Ã«Õ");
            
            order_qty_txt.setSubmittedValue(null); 
            order_qty_txt.resetValue();
            
            sale_price_txt.setSubmittedValue(null); 
            sale_price_txt.resetValue();
            
            addQuantityPopup.hide();
    //        addProductToOrder_Popup.hide();
           // trackProductDistribution_action(pId_inventory, inventoryId, inStock,);
            }
            
            else{
                    JSFUtils.addFacesErrorMessage("Ì—ÃÏ ⁄œ„  Ã«Ê“ «·ﬂ„Ì… «·„ÊÃÊœ… ›Ì «·„Œ“‰");
                }
            
            
            return null;
        }
              
    public String deleteOrder_action() {
        
        ADFUtils.findOperation("Commit").execute(); 
        return null;
    }

    public void setAddCustomerOrder_Popup(RichPopup addCustomerOrder_Popup) {
        this.addCustomerOrder_Popup = addCustomerOrder_Popup;
    }

    public RichPopup getAddCustomerOrder_Popup() {
        return addCustomerOrder_Popup;
    }

    public void setAddProductToOrder_Popup(RichPopup addProductToOrder_Popup) {
        this.addProductToOrder_Popup = addProductToOrder_Popup;
    }

    public RichPopup getAddProductToOrder_Popup() {
        return addProductToOrder_Popup;
    }


    public void setAddQuantityPopup(RichPopup addQuantityPopup) {
        this.addQuantityPopup = addQuantityPopup;
    }

    public RichPopup getAddQuantityPopup() {
        return addQuantityPopup;
    }

    public void setOrder_qty_txt(RichInputText order_qty_txt) {
        this.order_qty_txt = order_qty_txt;
    }

    public RichInputText getOrder_qty_txt() {
        return order_qty_txt;
    }


    public void setSale_price_txt(RichInputText sale_price_txt) {
        this.sale_price_txt = sale_price_txt;
    }

    public RichInputText getSale_price_txt() {
        return sale_price_txt;
    }


 
}

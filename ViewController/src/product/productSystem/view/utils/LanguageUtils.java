package product.productSystem.view.utils;

import java.util.regex.Pattern;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.input.RichInputText;

public class LanguageUtils {
  
    public LanguageUtils() {
        super();
    }

  
    public String ArabicTxt(String s){

      String res="";
            //??????????
            for (int i = 0; i < s.length(); i++){
                //res=res+(int)s.charAt(i)+':';
                //Log.d("webservice","1:"+(int)s.charAt(i) );
                if ((int)s.charAt(i)==1632){
                    res=res+'0';
                }else if ((int)s.charAt(i)==1633){
                    res=res+"1";
                }else if ((int)s.charAt(i)==1634){
                    res=res+"2";
                }else if ((int)s.charAt(i)==1635){
                    res=res+"3";
                }else if ((int)s.charAt(i)==1636){
                    res=res+"4";
                }else if ((int)s.charAt(i)==1637){
                    res=res+"5";
                }else if ((int)s.charAt(i)==1638){
                    res=res+"6";
                }else if ((int)s.charAt(i)==1639){
                    res=res+"7";
                }else if ((int)s.charAt(i)==1640){
                    res=res+"8";
                }else if ((int)s.charAt(i)==1641){
                    res=res+"9";}
                else if ((int)s.charAt(i)==1642)
            {
             res=res+"%";
             }
                else{
                    res=res+s.charAt(i);
                }
                //System.out.println("number is :  "+((int)s.charAt(i))); 
            } // for
            return res; 
     

            }

     public static void main(String[] args) {
     
        String s="??2?5";
        
        for (int i = 0; i < s.length(); i++){
            
           System.out.println((int)s.charAt(i)); 
            
            }
        
        
     }             
    
}

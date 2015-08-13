package osprey_adphone_hn.cellcom.com.cn.activity.csh;
/**
 *  去除空格
 * @author 周子健
 *
 */
public class RemoveSpacing {
	public static String remove(String resource,char ch)   
	 {   
	     StringBuffer buffer=new StringBuffer();   
	     int position=0;   
	     char currentChar;   

	     while(position<resource.length())   
	     {   
	         currentChar=resource.charAt(position++);  
	         //如果当前字符不是要去除的字符，则将当前字符加入到StringBuffer中
	         if(currentChar!=ch) buffer.append(currentChar); 
	     } 
	     return buffer.toString();   
	 }
}

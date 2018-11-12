package fbTagFrnds;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Keys;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class facebookTag {

	static Fillo fillo = new Fillo();
	//static Logger logger;
	static Logger logger=Logger.getLogger("FBTagFrnds");
  
	
	public static void main(String[] args) {
	    // configure log4j properties file
	    PropertyConfigurator.configure("Log4j.properties");
	

	    // the following statyment is used to log any messages  
	    logger.info("Function started");  
	    //fh.close();
	    		
		ChromeOptions chOption = new ChromeOptions();
        chOption.addArguments("--disable-extensions");
        chOption.addArguments("--disable-notifications");
        chOption.addArguments("test-type");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        chOption.setExperimentalOption("prefs", prefs);
        //driver = new ChromeDriver(chOption);
		
		Recordset recSet = null;
		Connection filCon = null;

		System.setProperty("webdriver.chrome.driver", "D:\\fbtag\\chromedriver.exe");
		WebDriver drvr = new ChromeDriver(chOption);
		
		try{
    			
		filCon = fillo.getConnection("D://fbtag//fb_tag.xlsx");
		
		// Login
		logger.info("Login function started");
		fnLogin(drvr,filCon);
		logger.info("Login function completed");		
		//fnSelectFriendsList(drvr);
		////*[@id="u_0_2"]
		
		drvr.findElement(By.className("_5qtp")).click();
		drvr.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		drvr.findElement(By.xpath("//div[contains(text(),'Tag Friends')]")).click();
		
		logger.info("Tag frineds below");
		
		//drvr.findElement(By.xpath("//*[contains(@id,'js_')]/input")).sendKeys("Karan IC");
		
		recSet = filCon.executeQuery("Select Names from Names");
		filCon.close();
			
		while (recSet.next()){
			drvr.findElement(By.xpath("//*[contains(@id,'js_')]/input")).sendKeys(recSet.getField("Names"));
			drvr.findElement(By.xpath("//*[contains(@id,'js_')]/input")).sendKeys(Keys.RETURN);
		}
		recSet.close();
		drvr.findElement(By.xpath("//*[contains(@id,'js_')]/div[2]/div[3]/div/div[2]/div/button")).click();
		drvr.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		logger.info("Execution completed");
		JOptionPane.showMessageDialog(null, "Execution completed.", "InfoBox", JOptionPane.INFORMATION_MESSAGE);
		//System.out.println("execution completed...");
		drvr.close();
      }
		catch(Exception e){
			logger.error(e.getMessage());
			JOptionPane.showMessageDialog(null, "Error during execution.", "Error", JOptionPane.INFORMATION_MESSAGE);
		}finally{
			//JOptionPane.showMessageDialog(null, "Error during execution.", "Error", JOptionPane.INFORMATION_MESSAGE);
			
		}
     }
	
public static void fnLogin(WebDriver drvr,Connection filCon) throws FilloException{
try{
	
		Recordset rS;
		//drvr.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		drvr.get("https://facebook.com/");
		drvr.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	
		rS = filCon.executeQuery("Select * from Login");
		if (rS.getCount()==0) 
		{
			JOptionPane.showMessageDialog(null, "Please enter login details in Excel sheet." , "Error", JOptionPane.INFORMATION_MESSAGE);
			drvr.close();
		}
		//System.out.println(rS.getCount());		
		while (rS.next()){
			drvr.findElement(By.id("email")).sendKeys(rS.getField("Username"));
			drvr.findElement(By.id("pass")).sendKeys(rS.getField("Password"));
			drvr.findElement(By.id("pass")).sendKeys(Keys.RETURN);
			drvr.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			break;
		}
}	
catch(Exception e){
	logger.error(e.getMessage());
	JOptionPane.showMessageDialog(null, "Error during execution." , "Error", JOptionPane.INFORMATION_MESSAGE);
}finally{
	//JOptionPane.showMessageDialog(null, "Error during execution." , "Error", JOptionPane.INFORMATION_MESSAGE);
	
}

}

public static void fnSelectFriendsList(WebDriver drvr) throws InterruptedException{

	drvr.findElement(By.xpath("//*[@id='navItem_1572366616371383']/a/div")).click();
	
}

}

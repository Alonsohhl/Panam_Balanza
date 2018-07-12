import java.util.List;


//import giovynet.nativelink.SerialPort;
//import giovynet.serial.Baud;
//import giovynet.serial.Com;
//import giovynet.serial.Parameters;
//import giovynet.serial.*;
/*
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
*/

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
//import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Calendar;
import java.sql.PreparedStatement;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;


public class App{
	public static Connection con=null;
	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat sdfyear = new SimpleDateFormat("yyyy");
	private static final DateFormat sdfmonth = new SimpleDateFormat("MM");
	private static final DateFormat sdfday = new SimpleDateFormat("dd");
	private static final DateFormat sdfmin = new SimpleDateFormat("mm");
	private static final DateFormat sdfhor = new SimpleDateFormat("HH");
	private static final DateFormat sdfseg = new SimpleDateFormat("ss");
	
	public static void Insert(String str) {
		Statement stmt = null;
		ResultSet rs = null;
		

		try {
		    stmt = con.createStatement();

		    
			//Date date =  new Date();
		    Calendar cal = Calendar.getInstance();
		    
		    

		    cal.getInstance().add(Calendar.DAY_OF_MONTH,-1);

		    
		    //System.out.println(" Fecha Ingreso>>:"+sdf.format(cal.getTime()));
		 // si esq esta entre las 00 y las 5 sumar una hora
		    
		    //(SELECT (MAX(RegPesNum) + 1) FROM dbpulper.regpespu)
		    Integer Var=Integer.parseInt(sdfseg.format(cal.getTime()))+(Integer.parseInt(sdfhor.format(cal.getTime()))*10000)+(Integer.parseInt(sdfmin.format(cal.getTime()))*100);
		    System.out.println(Var.toString());
		    
		    PreparedStatement stmt2 = con.prepareStatement("INSERT INTO dbpulper.regpespu(RegPesDia,RegPesMes,RegPesAno,RegPesNum,RegPesHora,RegPesCan,RegPesFlaP,RegPesFlaE) VALUES (?,?,?,?,?,?,?,?)");
            
		    int iseg=Integer.parseInt(sdfseg.format(cal.getTime()));
		    int imin=Integer.parseInt(sdfmin.format(cal.getTime()));
		    int ihour=Integer.parseInt(sdfhor.format(cal.getTime()));
		    int iday=Integer.parseInt(sdfday.format(cal.getTime()));
            int imonth=Integer.parseInt(sdfmonth.format(cal.getTime()));
            int iyear=Integer.parseInt(sdfyear.format(cal.getTime()));
            
            
            java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
            
            
		    if(ihour>=0 && ihour<5){
		    	iday=iday-1;
		    	Var=Var+250000;
		    	//ihour=ihour+25;//agregar horas
		    	
		    }

		    stmt2.setInt(1, iday);
            stmt2.setInt(2, Integer.parseInt(sdfmonth.format(cal.getTime())));
            stmt2.setInt(3, Integer.parseInt(sdfyear.format(cal.getTime())));
            stmt2.setInt(4, Var);
//            stmt2.setDate(5, new java.sql.Date(new java.util.Date().getTime()));//.getTime()
            stmt2.setString(5, sdf.format(cal.getTime()));//.getTime()
            stmt2.setDouble (6, Double.parseDouble(str));
            stmt2.setInt(7, 0);
            stmt2.setInt(8, 0);
            
          //  System.out.println("========>>>"+(sdf.format(cal.getTime()))+( new java.sql.Time(new java.util.Date().getTime())).toString());
            
            stmt2.executeUpdate();
       
            System.out.println("===============");
		}
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    //commPort.close();
		}
		finally {
		    // it is a good idea to release
		    // resources in a finally{} block
		    // in reverse-order of their creation
		    // if they are no-longer needed

		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException sqlEx) { } // ignore

		        rs = null;
		    }

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}
		
	}
	
	public static Connection getConexion() {
		
		
		try {
/*			Class.forName("com.mysql.cj.jdbc.Driver");
			String url ="jdbc:mysql://pulper";
			String usuario ="root";
			String contrasena="panam";
	*/		
			//con=DriverManager.getConnection(url,usuario,contrasena);
			con=DriverManager.getConnection("jdbc:mysql://pulper/dbpulper?" +
                    "user=root&password=panam");
			
			
	//	}catch(ClassNotFoundException e) {
	//		e.printStackTrace();
	//		System.out.println("error: con el driver");
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("error: con la Base de Datos");
		}
		
		
		return con;
	}
	

	
	static void listPorts()
    {
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while ( portEnum.hasMoreElements() ) 
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            System.out.println(portIdentifier.getName()  +  " - " +  getPortTypeName(portIdentifier.getPortType()) );
        }        
    }
	
	static String getPortTypeName ( int portType )
    {
        switch ( portType )
        {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }

  //  public static void main(String[] args)throws Exception{ 
    	
   // 	listPorts();
    	
      //  SerialPort serialPort = new SerialPort();
      //  List<String> portsFree = serialPort.getFreeSerialPort(); 
      //  for (String free : portsFree) { 
      //      System.out.println(free); 
      //  }
        /*
        Parameters settings = new Parameters();
        settings.setPort("COM4");
        settings.setBaudRate(Baud._9600);
        settings.setByteSize("8");
        settings.setStopBits("1");
        settings.setMinDelayWrite(10);
        StringBuilder stringBuilder = new StringBuilder();

        Com com3 = new Com(settings);
        for (int i = 0; com3.receiveSingleChar()!='9'; i++) {
            System.out.println("<<Receive "+com3.receiveToString(15)); 
            com3.receiveToStringBuilder(20,stringBuilder);
            System.out.println(" >>"+stringBuilder);
        }*/
 //   }
    public App()
    {
        super();
    }
    
    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Puerto Ocupado");
          
        }
        else
        {
        	System.out.println("Estatus: Escuchando puerto");
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
              //  OutputStream out = serialPort.getOutputStream();
                
          //      (new Thread(new SerialReader(in))).start();
               // (new Thread(new SerialWriter(out))).start();
                
                
                SerialReader reader=new SerialReader(in);
                reader.run(commPort);
                
                
                

            }
            else
            {
                System.out.println("Error: Con el puerto Serial.");
                commPort.close();
            }
        }     
    }
    
    /** */
    public static class SerialReader //implements Runnable 
    {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run (CommPort commPort) 
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            
            
            while(true) {
	            try
	            {
	            	getConexion();
	                while ( ( len = this.in.read(buffer)) > -1 )
	                {
	                	TimeUnit.SECONDS.sleep(2);
	                	String str = new String(buffer,0,len);
	                	
	                	
	                	Integer ini = str.indexOf("GS");
	                	Integer fin = str.indexOf("GS",ini+1);
	                	
	                	if(ini>=0 && fin>=0)
	                	{
	                		str=str.substring(ini, fin);	   
	                		str = str.replaceAll("[^\\d.]", "");
	                		
	                	}else
	                	{
	                		str="0";
	                	
	                	}
	                	           	
	                    System.out.println("Valor ->"+str);
	                    Insert(str);
	                    break;
	                   
	                	
	                    
	                	
	                }
	                System.out.println("  Tomando Lectura -");
	                TimeUnit.SECONDS.sleep(15);
                    
	                
	            }
	            catch ( IOException | InterruptedException e )
	            {
	                e.printStackTrace();
	                commPort.close();
	            }
	            
	          //  System.out.print("  SALIDA!");
	            
            }
        }
    }

    /** */
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {                
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
    
    public static void main ( String[] args ) throws InterruptedException
    {
    	 for(int i = 0; i < args.length; i++) {
             System.out.println(args[i]);
             
         }
    	 
    	 String port="COM3";
    	 if(args.length>0) {
	    	 
	    	 if(args[0]!= null) {
	    		 port=args[0];
	    	 }
	    	 
	    	 if(args[1]!= null) {
	    		 port=args[0];
	    	 }
    	 
    	 }
    	 while(true) {
    		 TimeUnit.SECONDS.sleep(20);
    		 try
	        {
	            (new App()).connect(port);
	        }
	        catch ( Exception e )
	        {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        
    	 }
    }

}
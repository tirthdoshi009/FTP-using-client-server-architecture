import java.io.*;
import java.net.*;

public class server
{
	
	private static int port = 9090;
	 public static String current_dir;
	 public static File    file;
	public static FileInputStream  fr;
	public static void main(String[]args) throws IOException
	{
		
		ServerSocket listener= new ServerSocket(port);
		System.out.println("Server is listening");
		int clientnumber=1;
		try
		{
			while(true)
			{
				new handler(listener.accept(),clientnumber).start();
				System.out.println("Client "+clientnumber+" is connected ");
				clientnumber++;
			}
		}
		finally
		{
			listener.close();
		}
	}
	private static class handler extends Thread 
	{
		private Socket s;
		private int no;
		public PrintWriter out;
		public BufferedReader in;
		public handler(Socket s, int no)
		{
			this.s= s;
			this.no= no;
			
		}
		public void run()
		{
			
			try
			{
				in= new BufferedReader(new InputStreamReader(s.getInputStream()));
				out= new PrintWriter(s.getOutputStream(),true);
				while(true)
				{
					String userpass = in.readLine();
					if((userpass.equals("tirth 009")))
					{
						out.println("User "+no+" authenticated");
						System.out.println("Client "+no+" is authenticated");
						break;
					}
					else
					{
						out.println("Invalid Username/password");
					}
				}
				while(true)
				{
					
					String com = in.readLine();
					//String com =  com1.replaceAll("^\\s+", "");
					
					String[] comsplit = com.split("\\s+");
					current_dir= new java.io.File(".").getCanonicalPath();
					file = new File(current_dir);
					String[] files= file.list();
					
					
					if(com.equals("dir"))
					{
						out.println(files.length);
						
						for(String string :files)
						{
							out.println(string);
						}
						
					}
					else if(comsplit[0].equals("get") && comsplit.length==2)
					{
						try
						{
							out.println(files.length);
							
							for(String string :files)
							{
								out.println(string);
							}
							
							String count=in.readLine();
							if(count.equals("1"))
							{
								fr= new FileInputStream(current_dir+"\\"+comsplit[1]);
								byte [] b= new byte[2020];
								fr.read(b,0,b.length);
								OutputStream os= s.getOutputStream();
								os.write(b,0,b.length);
								os.flush();
							}
						}
						catch(Exception e)
						{
							System.out.println(e);
						}
						
					}
					else if(comsplit[0].equals("upload") && comsplit.length==2)
					{
						String count= in.readLine();
						if(count.equals("1"))
						{
							FileOutputStream fr= new FileOutputStream(current_dir+"\\"+comsplit[1]);
							InputStream is = s.getInputStream();
							byte[]b1= new byte[2020];
							is.read(b1);
							fr.write(b1);
							fr.flush();
							fr.close();
						}
					}
				}
			}
			catch(Exception e)
			{
				System.out.println(e.getClass());
			}
		
		
	}
	
	
}

}
import java.io.*;
import java.net.*;

public class ftpclient
{
	private static int port = 9090;
	public static Socket s;
	public static PrintWriter out;
	public static BufferedReader in;
	public static String current_dir;
	public static FileOutputStream fr;
	public static void main(String[]args) throws IOException
	{
		InputStreamReader r=new InputStreamReader(System.in);  
		
		
		BufferedReader keyboard= new BufferedReader(r);
	
		while(true)
		{
			try
			{
				System.out.println("Enter your command");
				String command= keyboard.readLine();
				String[] commandsplit = command.split("\\s+");
				if(commandsplit[0].equals("ftpclient") && ((commandsplit[1].equals("localhost"))||(commandsplit[1].equals("127.0.0.1"))) && commandsplit[2].equals("9090")) 
				{
					s = new Socket(commandsplit[1],Integer.parseInt(commandsplit[2]));
					System.out.println("Client is now connected to the server");
					break;
				}
				
				else
				{
					System.out.println("Invalid Command");
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
		
		while(true)
		{
			System.out.println("Enter your username and password");
			String userpass = keyboard.readLine();
			out= new PrintWriter(s.getOutputStream(),true);
			out.println(userpass);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String serverresponse= in.readLine();
			System.out.println("server says "+ serverresponse);
			if(serverresponse.contains("authenticated"))
			break;
		}
		
		while(true)
		{
			System.out.println("Enter your command");
			String com= keyboard.readLine();
			String[] comsplit = com.split("\\s+");
			out.println(com);
			if(com.equals("dir"))
			{
				
				String len= in.readLine();
				
				int l= Integer.parseInt(len);
				for(int i=1;i<=l;i++)
				{
					String filename	=in.readLine();
					System.out.println(filename);
				}
				
			}
			
			else if(comsplit[0].equals("get") && comsplit.length==2)
			{
				try
				{
					String len1= in.readLine();
					int l1= Integer.parseInt(len1);
					int count=0;
					for(int i=1;i<=l1;i++)
					{
						String filename1= in.readLine();
						if(filename1.equals(comsplit[1]))
							count++;
					}
					out.println(count);
					if(count==1)
					{
						byte []b=new byte[2020];
						InputStream is = s.getInputStream();
						current_dir= new java.io.File(".").getCanonicalPath();
						fr= new FileOutputStream(current_dir+"\\"+comsplit[1]);
						is.read(b,0,b.length);
						fr.write(b,0,b.length);
						fr.flush();
						fr.close();
						System.out.println("File "+comsplit[1]+" is downloaded");
					}
					else
					{
						System.out.println("file not found");
					}
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
			}
			else if(comsplit[0].equals("upload") && comsplit.length==2)
			{
			    current_dir= new java.io.File(".").getCanonicalPath();
				File file = new File(current_dir);
				String[] files= file.list();
				int count=0;
				for(String string: files)
				{
					if(string.equals(comsplit[1]))
						count++;
				}
				out.println(count);
				
				if(count==1)
				{
					FileInputStream fr= new FileInputStream(current_dir+"\\"+comsplit[1]);
					byte[]b1= new byte[2020];
					fr.read(b1);
					OutputStream os= s.getOutputStream();
					os.write(b1);
					os.flush();
					System.out.println("File uploaded to the server");
				}
				else
					System.out.println("file not found");
			}
			
			else
				System.out.println("Invalid Command");
		}
		
		
	}
}
package triggerpmtest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;


/***
 * This is an example program demonstrating how to use the FTPSClient class.
 * This program connects to an FTP server and retrieves the specified
 * file.  If the -s flag is used, it stores the local file at the FTP server.
 * Just so you can see what's happening, all reply strings are printed.
 * If the -b flag is used, a binary transfer is assumed (default is ASCII).
 * <p>
 * Usage: ftp [-s] [-b] <hostname>    
 * <p>
 ***/
public class GetFileFromFTPS implements Callable
{
public String server;
public String UN;
public String messageType;
public int port;
public int getPort() {
	return port;
}
public void setPort(int port) {
	this.port = port;
}
public String getMessageType() {
	return messageType;
}
public void setMessageType(String messageType) {
	this.messageType = messageType;
}
public String getUN() {
	return UN;
}
public void setUN(String uN) {
	UN = uN;
}
public String password;
    public String getServer() {
	return server;
}
public void setServer(String server1) {
	this.server = server1;
}
	
@Override
    public Object onCall(MuleEventContext eventContext) throws Exception {
    
        int base = 0;
        boolean storeFile = false, binaryTransfer = false, error = false;
       // String server, username, password, remote, local;
        String protocol = "SSL";    // SSL/TLS
        FTPSClient ftps;
        
        InputStream is= null;
        MuleMessage message=eventContext.getMessage();
        String messageType=message.getOutboundProperty("messageType");
           String remote = message.getOutboundProperty("messagePath");
        		 //  "/demo/supplier-inbox/XMLMessage/apessentialsFlow.txt";
 
        ftps = new FTPSClient("TLS",true);
        ftps.connect(server,port);
                    
        ftps.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        try
        {
            int reply;

            
            System.out.println("Connected to " + server + ".");

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftps.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftps.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
        }
        catch (IOException e)
        {
            if (ftps.isConnected())
            {
                try
                {
                    ftps.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
            System.err.println("Could not connect to server.");
            e.printStackTrace();
            System.exit(1);
        }

__main:
        try
        {
            ftps.setBufferSize(1000);

            if (!ftps.login(UN, password))
            {
                ftps.logout();
                error = true;
                break __main;
            }

            
            System.out.println("Remote system is " + ftps.getSystemName());

            if (binaryTransfer) ftps.setFileType(FTP.BINARY_FILE_TYPE);

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftps.enterLocalPassiveMode();

            
                 is=ftps.retrieveFileStream(remote);
                 if(is==null){
                	 throw new FileNotFoundException();
                 }
                

            ftps.logout();
        }
        catch (FTPConnectionClosedException e)
        {
            error = true;
            System.err.println("Server closed connection.");
            e.printStackTrace();
        }
        catch (FileNotFoundException e){
        error =true;
        throw e;
        }
        catch (IOException e)
        {
            error = true;
            e.printStackTrace();
        }
        
        finally
        {
            if (ftps.isConnected())
            {
                try
                {
                    ftps.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
        }

        // System.exit(error ? 1 : 0);
     // end main
return is;
}

public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
}
	
		
	

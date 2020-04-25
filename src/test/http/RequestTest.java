package http;

import org.junit.Assert;
import org.junit.Test;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author lordWang
 * @version 1.0
 * @date 2020/4/24 16:56
 */
public class RequestTest {
    @Test
    public void validRequestHeader(){
        String testFilename = "./source/test/validRequestHeader.txt";
        try{
        InputStream inputStream = new FileInputStream(testFilename);
        Request request = new Request(inputStream,null);
        request.parse();
            Assert.assertTrue(request.isValid());
            Assert.assertEquals(request.getParameter("Host"),"127.0.0.1");
            Assert.assertEquals(request.getParameter("Connection"),"close");
            Assert.assertEquals(request.getRelativeUrl(),"/index.html");
            Assert.assertEquals(request.getProtocol(),"HTTP/1.1");
        }catch (IOException e){
            e.printStackTrace();
        }catch (Request.RequestParseExcetion e){
            e.printStackTrace();
        }
    }
    @Test
    public void invalidRequestMethod(){
        String testFilename = "./source/test/invalidRequestHeader_method.txt";
        Request request = null;
        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream(testFilename);
            request = new Request(inputStream,null);
            request.parse();
        }catch (IOException e){
            e.printStackTrace();
        }catch (Request.RequestParseExcetion e){
            Assert.assertTrue(!request.isValid());
//            e.printStackTrace();
        }
    }

}

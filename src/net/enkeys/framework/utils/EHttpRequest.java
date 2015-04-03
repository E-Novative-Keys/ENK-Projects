package net.enkeys.framework.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.exceptions.ESystemException;

public class EHttpRequest
{
    protected final URL url;
    protected final String data, contentType;
    protected final boolean returnError;
    protected Proxy proxy = null;
    protected HostnameVerifier sslHostnameVerifier = null;
    protected SSLContext sslContext = null;
    
    public EHttpRequest(URL url, Map<String, Object> params)
    {
        this(url, params, "application/x-www-form-urlencoded", false);
    }
    
    public EHttpRequest(URL url, Map<String, Object> params, boolean returnError)
    {
        this(url, params, "application/x-www-form-urlencoded", returnError);
    }
    
    public EHttpRequest(URL url, Map<String, Object> params, String contentType)
    {
        this(url, EHttpRequest.formatParams(params), contentType, false);
    }
    
    public EHttpRequest(URL url, Map<String, Object> params, String contentType, boolean returnError)
    {
        this(url, EHttpRequest.formatParams(params), contentType, returnError);
    }
    
    public EHttpRequest(URL url, String data)
    {
        this(url, data, "application/x-www-form-urlencoded", false);
    }
    
    public EHttpRequest(URL url)
    {
        this(url, "", "application/x-www-form-urlencoded", false);
    }
    
    public EHttpRequest(URL url, String data, String contentType)
    {
        this(url, data, contentType, false);
    }
    
    public EHttpRequest(URL url, String data, boolean returnError)
    {
        this(url, data, "application/x-www-form-urlencoded", returnError);
    }
    
    public EHttpRequest(URL url, String data, String contentType, boolean returnError)
    {
        this.url = url;
        this.data = data;
        this.contentType = contentType;
        this.returnError = returnError;
    }
    
    /**
     * Concatène l'URL et ses paramètres, puis effectue la connexion avec l'URL
     * donnée.
     * @return
     * @throws EHttpRequestException 
     */
    public String get() throws EHttpRequestException
    {
        String line;
        BufferedReader reader;
        StringBuilder response = new StringBuilder();
        URL getURL = concatenateURL();
        HttpURLConnection connection;
                
        try
        {
            if(url.getProtocol().equalsIgnoreCase("HTTPS"))
            {
                connection = (proxy == null) ? (HttpsURLConnection)getURL.openConnection() : (HttpsURLConnection)getURL.openConnection(proxy);
                
                if(this.sslHostnameVerifier != null)
                    ((HttpsURLConnection)connection).setHostnameVerifier(sslHostnameVerifier);
                if(this.sslContext != null)
                    ((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            else
                connection = (proxy == null) ? (HttpURLConnection)getURL.openConnection() : (HttpURLConnection)getURL.openConnection(proxy);
            
            connection.setRequestMethod("GET");

            if(connection.getResponseCode()/100 == 2)
            {
                try
                {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                }
                catch (IOException e)
                {
                    if(returnError)
                    {
                        InputStream stream = connection.getErrorStream();

                        if(stream != null)
                            reader = new BufferedReader(new InputStreamReader(stream));
                        else
                            throw new EHttpRequestException("An error occured while connecting to error stream", e);
                    }
                    else
                        throw new EHttpRequestException("An error occured while connecting to data stream", e);
                }

                while((line = reader.readLine()) != null)
                {
                    response.append(line);
                    response.append("\r\n");
                }

                reader.close();
                return response.toString();
            }
            else
            {
                String error = "Bad reponse code received: HTTP " + connection.getResponseCode() + "/" + connection.getResponseMessage();
                
                if(returnError)
                    return error;
                else
                    throw new EHttpRequestException(error);
            }
        }
        catch(IOException e)
        {
            throw new EHttpRequestException("An error occured while processing GET request", e);
        }
    }
    
    /**
     * Effectue la connexion avec l'URL donnée, envoie les données en POST et
     * renvoie le contenu de la page.
     * @return
     * @throws EHttpRequestException 
     */
    public String post() throws EHttpRequestException
    {
        String line;
        BufferedReader reader;
        DataOutputStream writer;
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection;
        byte[] byteParams;
        
        try
        {
            byteParams = data.getBytes(ECharsets.UTF_8.toCharset());
            
            if(url.getProtocol().equalsIgnoreCase("HTTPS"))
            {
                connection = (proxy == null) ? (HttpsURLConnection)url.openConnection() : (HttpsURLConnection)url.openConnection(proxy);
                
                if(this.sslHostnameVerifier != null)
                    ((HttpsURLConnection)connection).setHostnameVerifier(sslHostnameVerifier);
                if(this.sslContext != null)
                    ((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            else
                connection = (proxy == null) ? (HttpURLConnection)url.openConnection() : (HttpURLConnection)url.openConnection(proxy);

            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
            connection.setFixedLengthStreamingMode(byteParams.length);
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            writer = new DataOutputStream(connection.getOutputStream());
            writer.write(byteParams);
            writer.flush();
            writer.close();
                
            if(connection.getResponseCode()/100 == 2)
            {
                try
                {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                }
                catch (IOException e)
                {
                    if(returnError)
                    {
                        InputStream stream = connection.getErrorStream();

                        if(stream != null)
                            reader = new BufferedReader(new InputStreamReader(stream));
                        else
                            throw new EHttpRequestException("An error occured while connecting to error stream", e);
                    }
                    else
                        throw new EHttpRequestException("An error occured while connecting to data stream", e);
                }

                while((line = reader.readLine()) != null)
                {
                    response.append(line);
                    response.append("\r\n");
                }

                reader.close();
                return response.toString();
            }
            else
            {
                String error = "Bad reponse code received: HTTP " + connection.getResponseCode() + "/" + connection.getResponseMessage();
                
                if(returnError)
                    return error;
                else
                    throw new EHttpRequestException(error);
            }
        }
        catch(IOException e)
        {
            throw new EHttpRequestException("An error occured while processing POST request", e);
        }
    }
    
    /**
     * Téléchargement d'un fichier dans le dossier de destination path
     * @param path
     * @return
     * @throws EHttpRequestException
     * @throws ESystemException 
     */
    public boolean download(String path) throws EHttpRequestException, ESystemException
    {
        String line;
        File file;
        FileOutputStream output = null;
        InputStream input = null;
        StringBuilder response = new StringBuilder();
        URL getURL = concatenateURL();
        HttpURLConnection connection;
                
        try
        {            
            if(url.getProtocol().equalsIgnoreCase("HTTPS"))
            {
                connection = (proxy == null) ? (HttpsURLConnection)getURL.openConnection() : (HttpsURLConnection)getURL.openConnection(proxy);
                
                if(this.sslHostnameVerifier != null)
                    ((HttpsURLConnection)connection).setHostnameVerifier(sslHostnameVerifier);
                if(this.sslContext != null)
                    ((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            else
                connection = (proxy == null) ? (HttpURLConnection)getURL.openConnection() : (HttpURLConnection)getURL.openConnection(proxy);
            
            connection.setRequestMethod("GET");

            if(connection.getResponseCode()/100 == 2)
            {
                String filename = connection.getHeaderField("Content-Disposition");
                
                if(filename != null)
                {
                    int index = filename.indexOf("filename=");
                    if(index > 0)
                        filename = filename.substring(index + 10, filename.length() - 2);
                }
                else
                    return false;
                
                file = new File(path + File.separator + filename);
            
                if(!file.exists())
                {
                    if(!file.getParentFile().exists())
                        file.mkdirs();
                    
                    file.createNewFile();
                }
                   
                try
                {
                    output = new FileOutputStream(file);
                    input  = connection.getInputStream();
                }
                catch (IOException e)
                {
                    if(returnError)
                        return false;
                    else
                        throw new EHttpRequestException("An error occured while connecting to data stream", e);
                }

                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                
                while((bytesRead = input.read(buffer)) != -1)
                    output.write(buffer, 0, bytesRead);
                
                input.close();
                output.close();
                
                ESystem.open(path + File.separator + filename);
                return true;
            }
            else
            {
                String error = "Bad reponse code received: HTTP " + connection.getResponseCode() + "/" + connection.getResponseMessage();
                
                if(returnError)
                    return false;
                else
                    throw new EHttpRequestException(error);
            }
        }
        catch(IOException e)
        {
            throw new EHttpRequestException("An error occured while processing GET request", e);
        }
    }
    
    public String upload(Map<String, String> data, File file) throws EHttpRequestException
    {
        String line;
        HttpURLConnection connection;
        PrintWriter writer;
        BufferedReader reader;
        OutputStream output;
        StringBuilder response  = new StringBuilder();
        String LINE_FEED        = "\r\n";
        URL getURL              = concatenateURL();
        String boundary         = "===" + System.currentTimeMillis() + "===";
        
        try
        {            
            if(url.getProtocol().equalsIgnoreCase("HTTPS"))
            {
                connection = (proxy == null) ? (HttpsURLConnection)getURL.openConnection() : (HttpsURLConnection)getURL.openConnection(proxy);
                
                if(this.sslHostnameVerifier != null)
                    ((HttpsURLConnection)connection).setHostnameVerifier(sslHostnameVerifier);
                if(this.sslContext != null)
                    ((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            else
                connection = (proxy == null) ? (HttpURLConnection)getURL.openConnection() : (HttpURLConnection)getURL.openConnection(proxy);
            
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            output = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(output, "utf-8"), true);
            
            // Envoi des données en POST (token, ...)
            if(data != null)
            {
                for(String key : data.keySet())
                {
                    writer.append("--" + boundary).append(LINE_FEED);
                    writer.append("Content-Disposition: form-data; name=\"" + key + "\"").append(LINE_FEED);
                    writer.append("Content-Type: text/plain; charset=utf-8").append(LINE_FEED);
                    writer.append(LINE_FEED);
                    writer.append(data.get(key)).append(LINE_FEED);
                    writer.flush();
                }
            }
            
            // Envoi du fichier
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"" + file.getName() + "\"; filename=\"" + file.getName() + "\"").append(LINE_FEED);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            FileInputStream inputStream = new FileInputStream(file);
            
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while((bytesRead = inputStream.read(buffer)) != -1)
                output.write(buffer, 0, bytesRead);
            
            output.flush();
            inputStream.close();

            writer.append(LINE_FEED);
            writer.flush();		
            
            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();
            
            if(connection.getResponseCode()/100 == 2)
            {
                try
                {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                }
                catch (IOException e)
                {
                    if(returnError)
                    {
                        InputStream stream = connection.getErrorStream();

                        if(stream != null)
                            reader = new BufferedReader(new InputStreamReader(stream));
                        else
                            throw new EHttpRequestException("An error occured while connecting to error stream", e);
                    }
                    else
                        throw new EHttpRequestException("An error occured while connecting to data stream", e);
                }

                while((line = reader.readLine()) != null)
                {
                    response.append(line);
                    response.append("\r\n");
                }

                reader.close();
                return response.toString();
            }
            else
            {
                String error = "Bad reponse code received: HTTP " + connection.getResponseCode() + "/" + connection.getResponseMessage();
                
                if(returnError)
                    return error;
                else
                    throw new EHttpRequestException(error);
            }
        }
        catch(IOException e)
        {
            throw new EHttpRequestException("An error occured while processing Upload request", e);
        }
    }
    /*public String upload(File file) throws EHttpRequestException
    {
        String line;
        BufferedReader reader;
        DataOutputStream writer;
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection;
        byte[] byteParams;
        
        try
        {
            byteParams = data.getBytes(ECharsets.UTF_8.toCharset());
            
            if(url.getProtocol().equalsIgnoreCase("HTTPS"))
            {
                connection = (proxy == null) ? (HttpsURLConnection)url.openConnection() : (HttpsURLConnection)url.openConnection(proxy);
                
                if(this.sslHostnameVerifier != null)
                    ((HttpsURLConnection)connection).setHostnameVerifier(sslHostnameVerifier);
                if(this.sslContext != null)
                    ((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            else
                connection = (proxy == null) ? (HttpURLConnection)url.openConnection() : (HttpURLConnection)url.openConnection(proxy);

            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
            connection.setFixedLengthStreamingMode(byteParams.length + file.length());
            connection.setRequestProperty("Content-Language", "en-US");
            
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            writer = new DataOutputStream(connection.getOutputStream());
            writer.write(byteParams);
            
            writer.writeBytes("Content-Disposition: multipart/form-data;filename=\"" + file.getName() + "\";");
            writer.writeBytes("Content-Transfer-Encoding: binary");
            
            FileInputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while((bytesRead = inputStream.read(buffer)) != -1) {
                writer.write(buffer, 0, bytesRead);
            }
            writer.flush();
            writer.close();
                
            if(connection.getResponseCode()/100 == 2)
            {
                try
                {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                }
                catch (IOException e)
                {
                    if(returnError)
                    {
                        InputStream stream = connection.getErrorStream();

                        if(stream != null)
                            reader = new BufferedReader(new InputStreamReader(stream));
                        else
                            throw new EHttpRequestException("An error occured while connecting to error stream", e);
                    }
                    else
                        throw new EHttpRequestException("An error occured while connecting to data stream", e);
                }

                while((line = reader.readLine()) != null)
                {
                    response.append(line);
                    response.append("\r\n");
                }

                reader.close();
                return response.toString();
            }
            else
            {
                String error = "Bad reponse code received: HTTP " + connection.getResponseCode() + "/" + connection.getResponseMessage();
                
                if(returnError)
                    return error;
                else
                    throw new EHttpRequestException(error);
            }
        }
        catch(IOException e)
        {
            throw new EHttpRequestException("An error occured while processing POST request", e);
        }
    }*/

    /**
     * Formate les paramètres depuis une liste mappée et renvoie une chaîne.
     * @param params
     * @return
     * @throws EHttpRequestException 
     */
    public static final String formatParams(Map<String, Object> params) throws EHttpRequestException
    {
        StringBuilder builder = new StringBuilder();

        for(Map.Entry entry : params.entrySet())
        {
            if (builder.length() > 0) 
                builder.append('&');
            
            try
            {
                builder.append(URLEncoder.encode((String)entry.getKey(), "UTF-8"));
                
                if (entry.getValue() != null)
                {
                    builder.append('=');
                    builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                }
            }
            catch (UnsupportedEncodingException e)
            {
                throw new EHttpRequestException("An unexpected encoding error occured while processing URL params", e);
            }
        }

        return builder.toString();
    }
    
    /**
     * Renvoie une URL complète contenant le FQN, le chemin d'accès et les données
     * GET spécifiées.
     * Exemple: domaine.loc/page?data1=0&data2
     * @return
     * @throws EHttpRequestException 
     */
    public final URL concatenateURL() throws EHttpRequestException
    {
        try
        {
            //if((url.getQuery() != null) && (url.getQuery().length() > 0))
            if(!url.toString().contains("?"))
                return new URL(url.getProtocol(), url.getHost(), url.getFile() + "?" + data);
            else
                return new URL(url.getProtocol(), url.getHost(), url.getFile() + "&" + data);
            
        }
        catch(MalformedURLException e)
        {
            throw new EHttpRequestException("An unexpected error occured while concatenating request URL and data", e);
        }
    }
    
    /**
     * Définit le proxy à utiliser pour la requête.
     * @param proxy 
     */
    public final void setProxy(Proxy proxy)
    {
        this.proxy = proxy;
    }
    
    /**
     * Définit le nouveau contexte de vérification des noms d'hôte pour validation
     * des certificats SSL.
     * @param hostnameVerifier 
     */
    public final void setHostnameVerifier(HostnameVerifier hostnameVerifier) throws EHttpRequestException
    {
        if(url.getProtocol().equalsIgnoreCase("HTTPS"))
            this.sslHostnameVerifier = hostnameVerifier;
        else
            throw new EHttpRequestException("Trying to set a new HostnameVerifier for a HTTP request");
    }
    
    /**
     * Définit le nouveau contexte SLL de la requête.
     * @param sslContext 
     */
    public final void setSSLContext(SSLContext sslContext) throws EHttpRequestException
    {
        if(url.getProtocol().equalsIgnoreCase("HTTPS"))
            this.sslContext = sslContext;
        else
            throw new EHttpRequestException("Trying to set a new SSLContext for a HTTP request");
    }
    
    /**
     * Définit un HostnameVerifier et un SSLContext sans vérification pour
     * la requête.
     */
    public void setUnsafeSSL()
    {
        if(url.getProtocol().equalsIgnoreCase("HTTPS"))
        {
            SSLContext context;
            TrustManager[] trustManager = new TrustManager[]
            {
                new X509TrustManager()
                {
                    @Override
                    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}

                    @Override
                    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}

                    @Override
                    public X509Certificate[] getAcceptedIssuers()
                    {
                        return null;
                    }
                }
            };

            try
            {
                context = SSLContext.getInstance("SSL");
                context.init(null, trustManager, new SecureRandom());
            }
            catch (KeyManagementException | NoSuchAlgorithmException e)
            {
                throw new EHttpRequestException("An unexpected error occured wihle generating a new SSLContext", e);
            }

            setHostnameVerifier((String hostname, SSLSession session) -> true);
            setSSLContext(context);
        }
        else
            throw new EHttpRequestException("Trying to set an unsafe SSL context for a HTTP request");
    }
    
    /**
     * Renvoie le protcole utilisé par la requête.
     * @return 
     */
    public final String getProtocol()
    {
        return url.getProtocol();
    }

    /**
     * Renvoie l'URL de la requête.
     * @return 
     */
    public final URL getUrl()
    {
        return url;
    }

    /**
     * Renvoie les données de la requête.
     * @return 
     */
    public final String getData()
    {
        return data;
    }

    /**
     * Renvoie le type de données de la requête.
     * @return 
     */
    public final String getContentType()
    {
        return contentType;
    }

    /**
     * Renvoie le booléen d'erreur de la requête.
     * @return 
     */
    public final boolean doReturnError()
    {
        return returnError;
    }
    
    /**
     * Renvoie le proxy utilisé par la requête.
     * @return 
     */
    public final Proxy getProxy()
    {
        return proxy;
    }
}

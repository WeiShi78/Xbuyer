import java.util.*;
import java.net.*;
import java.io.*;
import java.util.logging.Logger;


public class processimage{
    public static void main(String[] args) {
        processimage a = new processimage();
        File file = new File("C:\\Users\\15647\\OneDrive\\UWM\\Mad2019\\Xbuyer\\Training_samples\\Receipt_1.jpg");
        System.out.print(a.multipartRequest("http://0.0.0.0:5000/image", null,file,"file",""));
    }
    
    public String[][] multipartRequest(String urlTo, Map<String, String> parmas, File file, String filefield, String fileMimeType)  {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        // String[] q = filepath.split("/");
        // int idx = q.length - 1;

        try {
            
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + file.getName() + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            // // Upload POST Data
            // Iterator<String> keys = parmas.keySet().iterator();
            // while (keys.hasNext()) {
            //     String key = keys.next();
            //     String value = parmas.get(key);

            //     outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            //     outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
            //     outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
            //     outputStream.writeBytes(lineEnd);
            //     outputStream.writeBytes(value);
            //     outputStream.writeBytes(lineEnd);
            // }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            if (200 != connection.getResponseCode()) {
                // throw new CustomException("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            inputStream = connection.getInputStream();

            result = this.convertStreamToString(inputStream);

            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            // throw new CustomException(e);
            return null;
        }

    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
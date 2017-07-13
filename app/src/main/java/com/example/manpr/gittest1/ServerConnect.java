package com.example.manpr.gittest1;

/**
 * Created by Best Buy on 13-07-2017.
 */

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.preference.PreferenceManager;
        import android.util.Log;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.DataOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpRetryException;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.text.SimpleDateFormat;
        import java.util.Date;


public class ServerConnect {

    Context con;


    public ServerConnect(Context c)

    {
        con=c;
    }

    public String getList(String ph,String time,String date) {
        String ipp = PreferenceManager.getDefaultSharedPreferences(con).getString("ipport", "192.168.43.65:8080");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(con);

        String site = sp.getString("siteaddr", "");

        HttpURLConnection connection = null;

        Log.e("555555555", ipp);
        Log.e("555555555", ph);


        Boolean mode = sp.getBoolean("mode", false);

        String out = null, request;

        try {

            //Toast.makeText(con, "upload Start", Toast.LENGTH_SHORT).show();

            String urlParameters = "";
            if (mode) {
                request = "http://" + site + "/VisitorManagement/GetList";
                Log.e("555555555", site);
            } else {
                request = "http://" + ipp + "/ConnectionTry2/Hey";
                Log.e("555555555", ipp);
            }


            URL url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            //connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setUseCaches(false);
            connection.connect();

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            //wr.writeBytes(urlParameters);


            JSONObject js = new JSONObject();
            js.put("phone",ph);
            js.put("time",time);
            js.put("date",date);

            wr.writeBytes(js.toString());



            wr.flush();
            wr.close();


            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            //Toast.makeText(con, response.toString(), Toast.LENGTH_SHORT).show();

            return response.toString();


        }

        catch (HttpRetryException e)
        {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    public String serverLogin(String email, String pass) {
        String ipp = PreferenceManager.getDefaultSharedPreferences(con).getString("ipport", "192.168.43.28:8080");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(con);

        String site = sp.getString("siteaddr", "");

        HttpURLConnection connection = null;

        Log.e("555555555", ipp);



        Boolean mode = sp.getBoolean("mode", false);

        String out = null, request;

        try {

            //Toast.makeText(con, "upload Start", Toast.LENGTH_SHORT).show();

            mode = false;
            String urlParameters = "";
            if (mode) {
                request = "http://" + site + "/VisitorManagement/GetList";
                Log.e("555555555", site);
            } else {
                request = "http://" + ipp + "/VisitorManagement/JsonRece";
                Log.e("555555555", ipp);
            }


            URL url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            //connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setUseCaches(false);
            connection.connect();

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            //wr.writeBytes(urlParameters);


            JSONObject js = new JSONObject();
            js.put("email",email);
            js.put("pass",pass);


            wr.writeBytes(js.toString());



            wr.flush();
            wr.close();


            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                //response.append('\r');
            }
            rd.close();
            //Toast.makeText(con, response.toString(), Toast.LENGTH_SHORT).show();

            return response.toString();


        }

        catch (HttpRetryException e)
        {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

        return "no conn";
    }



    public String uploadAttendance(String code, String exam_id, String comm) {
        String ipp = PreferenceManager.getDefaultSharedPreferences(con).getString("ipport", "192.168.43.28:8080");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(con);

        String site = sp.getString("siteaddr", "");

        HttpURLConnection connection = null;

        Log.e("555555555", ipp);
        Log.e("555555555", code);


        Boolean mode = sp.getBoolean("mode", false);

        String out = null, request;

        try {

            //Toast.makeText(con, "upload Start", Toast.LENGTH_SHORT).show();

            String urlParameters = "";
            if (mode) {
                request = "http://" + site + "/VisitorManagement/UploadCodes";
                Log.e("555555555", site);
            } else {
                request = "http://" + ipp + "/VisitorManagement/UploadCodes";
                Log.e("555555555", ipp);
            }


            URL url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            //connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setUseCaches(false);
            connection.connect();

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            //wr.writeBytes(urlParameters);


            JSONObject js = new JSONObject();
            js.put("code",code);
            js.put("f_id",exam_id);
            js.put("comments",comm);




            wr.writeBytes(js.toString());



            wr.flush();
            wr.close();


            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            //Toast.makeText(con, response.toString(), Toast.LENGTH_SHORT).show();

            return response.toString();


        }

        catch (HttpRetryException e)
        {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }




    public String viewTask() {
        String ipp = PreferenceManager.getDefaultSharedPreferences(con).getString("ipport", "172.200.5.19:8084");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(con);

        String site = sp.getString("siteaddr", "");

        String id = sp.getString("id", "");
        String cname = sp.getString("cname", "");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("cname",cname);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        HttpURLConnection connection = null;

        Log.e("555555555", ipp);

        Boolean mode = sp.getBoolean("mode", false);

        String out = null, request;

        try {

            //Toast.makeText(con, "upload Start", Toast.LENGTH_SHORT).show();

            String urlParameters = "";
            if (mode) {
                request = "http://" + site + "/EmployeeTracking1/ReceJsonServ";
            } else {
                request = "http://" + ipp + "/EmployeeTracking1/ReceJsonServ";
            }


            URL url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            //connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setUseCaches(false);
            connection.connect();

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            //wr.writeBytes(urlParameters);


            wr.writeBytes(jsonObject.toString());



            wr.flush();
            wr.close();


            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            //Toast.makeText(con, response.toString(), Toast.LENGTH_SHORT).show();

            return response.toString();


        }

        catch (HttpRetryException e)
        {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }



    public String putTestJson() {
        String ipp = PreferenceManager.getDefaultSharedPreferences(con).getString("ipport", "172.200.5.19:8084");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(con);

        String site = sp.getString("siteaddr", "");

        HttpURLConnection connection = null;

        Log.e("555555555", ipp);

        Boolean mode = sp.getBoolean("mode", false);

        String id = sp.getString("id", "0");
        String com = sp.getString("cname", "test");



        String out = null, request;




        try {

            //Toast.makeText(con, "upload Start", Toast.LENGTH_SHORT).show();

            String urlParameters = "";
            if (mode) {
                request = "http://" + site + "/EmployeeTracking1/Jsonrece";
            } else {
                request = "http://" + ipp + "/EmployeeTracking1/Jsonrece";
            }


            SimpleDateFormat df = new SimpleDateFormat("dd:MM:yyyy");

            String d = df.format(new Date());

            SimpleDateFormat df2 = new SimpleDateFormat("hh:mm");

            String t = df2.format(new Date());



            JSONObject jsonParam = new JSONObject();
            jsonParam.put("empid", id);
            jsonParam.put("company", com);
            jsonParam.put("latitude", "23.344333");
            jsonParam.put("longitude", "76.344333");
            jsonParam.put("date", d);
            jsonParam.put("time", t);
            jsonParam.put("address", "Sector 13, Chandigarh");


            URL url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            //connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setUseCaches(false);
            connection.connect();




            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);

            wr.writeBytes(jsonParam.toString());
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;

            StringBuilder response = new StringBuilder();

            line = rd.readLine();
            while (line != null) {

                response.append(line);
                line = rd.readLine();
            }
            rd.close();

            return response.toString();


        }

        catch (HttpRetryException e)
        {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }



    private String convertStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (Exception e) {
        }

        return total.toString();
    }



}









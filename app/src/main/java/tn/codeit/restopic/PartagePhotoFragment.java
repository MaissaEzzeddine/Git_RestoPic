package tn.codeit.restopic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import tn.codeit.restopic.webservice.UserFunctions;

public class PartagePhotoFragment extends Fragment {

    ImageView picture ;
    SessionManager session;
    private String filePath = null;
    Bitmap bitmap;
    int session_id ;
    String user_id , timeStamp ;
    Boolean isImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity().getApplicationContext());
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.partage_photo_layout, container, false);
        filePath = getArguments().getString("filePath");
        timeStamp = getArguments().getString("datePrise");
        isImage = getArguments().getBoolean("isImage");
        session_id = getArguments().getInt("id");
        user_id = String.valueOf(session_id);
        picture = (ImageView) view.findViewById(R.id.picture);
        if (filePath != null) {
            previewPicture(isImage);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActionBar actionBar=((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle("Partager la photo");
        actionBar.show();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem acceuilItem = menu.findItem(R.id.acceuil);
        MenuItem okItem = menu.findItem(R.id.ok);
        okItem.setVisible(true);
        acceuilItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acceuil:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.ok:
                new UploadFileToServer().execute();
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void previewPicture(boolean isImage) {
        if (isImage) {
            picture.setVisibility(View.VISIBLE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bitmap = BitmapFactory.decodeFile(filePath, options);
            picture.setImageBitmap(bitmap);
        }
    }

    private class UploadFileToServer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args)
        {
            int i=uploadFile(filePath);
            return "response:"+i ;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public int uploadFile(final String sourceFileUri) {
        String fileName = sourceFileUri;
        int serverResponseCode = 0 ;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :" + filePath);
            return 0;

        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                UserFunctions userFunctions=new UserFunctions();
                HttpURLConnection conn = userFunctions.PartagerPhoto(fileName, boundary);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
                //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(user_id); // mobile_no is String variable
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"date_de_prise\"" + lineEnd);
                //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(timeStamp); // mobile_no is String variable
                dos.writeBytes(lineEnd);


                //Adding Parameter media file(audio,video and image)
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0)
                {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                Log.e("uploadPicture", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                }
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (final Exception e) {

                e.printStackTrace();
                Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
            }
            return serverResponseCode;
        }
    }
}
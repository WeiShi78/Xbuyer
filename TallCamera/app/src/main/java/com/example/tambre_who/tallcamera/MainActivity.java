package com.example.tambre_who.tallcamera;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView today;
    List<ListViewCell> list = new ArrayList<>();
    ListViewAdapter Adapter;

    String pathToFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCamera = (Button)findViewById(R.id.btnCamera);
        listView = (ListView) findViewById(R.id.select_dialog_listview);


//        list.add(new ListViewCell("apple", "5"));
//        list.add(new ListViewCell("banana", "3"));
//        list.add(new ListViewCell("potato", "9"));
//        list.add(new ListViewCell("canteloupe", "6"));
//        list.add(new ListViewCell("juice", "14"));

        Adapter =  new ListViewAdapter(list);

        listView.setAdapter(Adapter);

        today = (TextView) findViewById(R.id.day);
        String input = Calendar.getInstance().getTime().toString();
        String formatted = input.substring(0, 10);

        today.setText(formatted);

        if (Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET},2);
        }
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPictureTakerAction();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == 1) {
                File requestfile = new File(pathToFile);
                File[] stupidParsing = new File[1];
                stupidParsing[0] = requestfile;
                new ProcessImage(Adapter).execute(stupidParsing);

            }
        }

    }

    private void dispatchPictureTakerAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createPhotoFile();

            if(photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(MainActivity.this, "Madhack", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(intent, 1);
            }
        }
    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("mylog", "Excep : " + e.toString());
        }
        return image;
    }


     class ListViewAdapter extends BaseAdapter {

        private List<ListViewCell> listViewCells;
        // 1
        public ListViewAdapter(List<ListViewCell> listViewCells) {
            this.listViewCells = listViewCells;
        }

        // 2
        @Override
        public int getCount() {
            return listViewCells.size();
        }

        // 3
        @Override
        public long getItemId(int position) {
            return 0;
        }

        // 4
        @Override
        public Object getItem(int position) {
            return listViewCells.get(position);
        }

        // 5
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.listview_layout, null);

            TextView textView_1 = (TextView)view.findViewById(R.id.text);
            TextView textView_2 = (TextView)view.findViewById(R.id.text2);

            textView_1.setText(listViewCells.get(position).getFood());
            textView_2.setText(listViewCells.get(position).getTime()+" Days");

            return view;
        }

        public void addFood(ListViewCell listViewCell) {
            listViewCells.add(listViewCell);
            notifyDataSetChanged();
        }

    }

}

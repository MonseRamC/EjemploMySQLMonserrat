package com.example.ejemplomysql;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String url = "jdbc:mysql://sql9.freesqldatabase.com/sql9579875";
    private static final String user = "sql9579875";
    private static final String pass = "bumTZS8dLL";
    Button btnFetch,btnInsert;
    ListView txtData;
    EditText dataToInsert;

    ConnectMySql connectMySql;
    InsertDataMySql insertMySql;

    private ArrayList<String> paisesNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtData = (ListView) this.findViewById(R.id.txtData);
        btnFetch = (Button) findViewById(R.id.btnFetch);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        dataToInsert = (EditText) findViewById(R.id.et_data);
        btnFetch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                connectMySql = new ConnectMySql();
                connectMySql.execute("");
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertMySql = new InsertDataMySql();
                insertMySql.execute("");
            }
        });

    }

    private class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                System.out.println("Databaseection success");

                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select distinct Country from tblCountries");
                ResultSetMetaData rsmd = rs.getMetaData();

                while (rs.next()) {
                    //result += rs.getString(1).toString() + "\n";
                    System.out.println(rs.getString(1).toString());
                    paisesNames.add(rs.getString(1).toString());
                }

                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, paisesNames);
            txtData.setAdapter(adapter);
            paisesNames = new ArrayList<String>();;
            //txtData.setText(result);

        }
    }








    private class InsertDataMySql extends AsyncTask<String, Void, String> {
        String res = "";
        boolean isData = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String data = dataToInsert.getText().toString();

                if(!data.equals("")){
                    isData = true;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(url, user, pass);
                    System.out.println("Databaseection success");

                    String result = "Database Connection Successful\n";
                    Statement st = con.createStatement();
                    int rs = st.executeUpdate("Insert into tblCountries (Country) values ('"+data+"')");

                }
                else{
                    isData = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            if(isData){
                Toast.makeText(MainActivity.this, "Dato Insertado", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

}











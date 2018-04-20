package vn.edu.hcmut.currencyconverter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    ListView lvCountry;
    ArrayList<Item> Countrys;
    DanhBaAdapter danhBaAdapter;
    String listCountry[]=new String[]{"AUD","BRL","CAD","CNY","EUR","INR","JPY","MXN",
            "VND","MYR","USD","PEN","QAR","RON", };

    String result="";

    ImageButton ibtnStartCurrCon,ibtnEndCurrCon;
    TextView txtBase,txtLastUpdate,txtOne,txtTwo;
    ImageView imgOne,imgTwo;
    EditText etxtSearch,etxtConvert,etxtResult;
    Button btnGetData;
    int temp1,temp2;

    String base,date,timestamp;
    JSONObject object;
    private boolean isGetData=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControlls();
        addEvents();
    }

    private void addEvents() {

        ibtnStartCurrCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(ibtnStartCurrCon,imgOne,txtOne);
            }
        });
        ibtnEndCurrCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(ibtnEndCurrCon,imgTwo,txtTwo);
            }
        });
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
                new ReadJSON().execute("http://data.fixer.io/api/latest?access_key=3cdf985a98508d00b91b531e027c36c0");
            }
        });
        etxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isGetData){
                    if(s.toString().equals("")){
                        initdata();
                    }
                    else{
                        searchItem(s.toString());
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etxtConvert.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(isGetData){
                    if(s.toString().equals("")){
                        etxtResult.setText("0.01");

                    }
                    else{
                        etxtResult.setText(convertCurrency(s.toString(),txtOne.getText().toString(),txtTwo.getText().toString()).toString());
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showMenu(ImageButton ibtn, final ImageView img, final TextView txt) {
        PopupMenu popupMenu=new PopupMenu(this,ibtn);
        popupMenu.getMenuInflater().inflate(R.menu.pop_menu,popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                reload();
                switch (item.getItemId()){
                    case R.id.it_VND:
                        reloadCountry(img,R.drawable.vnd,txt,item.getTitle().toString());
                        img.setTag(R.drawable.vnd);
                        break;
                    case R.id.it_USD:
                        reloadCountry(img,R.drawable.usd,txt,item.getTitle().toString());
                        img.setTag(R.drawable.usd);
                        break;
                    case R.id.it_EUR:
                        reloadCountry(img,R.drawable.eur,txt,item.getTitle().toString());
                        img.setTag(R.drawable.eur);
                        break;
                    case R.id.it_AUD:

                        reloadCountry(img,R.drawable.aud,txt,item.getTitle().toString());
                        img.setTag(R.drawable.aud);
                        break;
                    case R.id.it_BRL:
                        reloadCountry(img,R.drawable.brl,txt,item.getTitle().toString());
                        img.setTag(R.drawable.brl);
                        break;
                    case R.id.it_CAD:
                        reloadCountry(img,R.drawable.cad,txt,item.getTitle().toString());
                        img.setTag(R.drawable.cad);
                        break;
                    case R.id.it_CNY:

                        reloadCountry(img,R.drawable.cny,txt,item.getTitle().toString());
                        img.setTag(R.drawable.cny);
                        break;
                    case R.id.it_INR:
                        reloadCountry(img,R.drawable.inr,txt,item.getTitle().toString());
                        img.setTag(R.drawable.inr);
                        break;
                    case R.id.it_JPY:
                        reloadCountry(img,R.drawable.jpy,txt,item.getTitle().toString());
                        img.setTag(R.drawable.jpy);
                        break;
                    case R.id.it_MXN:
                        reloadCountry(img,R.drawable.mxn,txt,item.getTitle().toString());
                        img.setTag(R.drawable.mxn);
                        break;
                    case R.id.it_MYR:
                        reloadCountry(img,R.drawable.myr,txt,item.getTitle().toString());
                        img.setTag(R.drawable.myr);
                        break;
                    case R.id.it_PEN:
                        reloadCountry(img,R.drawable.pen,txt,item.getTitle().toString());
                        img.setTag(R.drawable.pen);
                        break;
                    case R.id.it_QAR:
                        reloadCountry(img,R.drawable.qar,txt,item.getTitle().toString());
                        img.setTag(R.drawable.qar);
                        break;
                    case R.id.it_RON:
                        reloadCountry(img,R.drawable.ron,txt,item.getTitle().toString());
                        img.setTag(R.drawable.ron);
                        break;

                }
                return false;
            }
        });
    }


    public void reload(){
        etxtConvert.setText("");
        etxtResult.setText("");
    }
    private void reloadCountry(ImageView img, int src,TextView txt,String id){
        img.setImageResource(src);
        txt.setText(id);

    }
    private void searchItem(String s) {


            for(   String item: listCountry){

                        Iterator<Item> iterator = Countrys.iterator();
                        while(iterator.hasNext()) {
                            Item item1 = iterator.next();
                            if(!item1.getId().contains(s)){
                                iterator.remove();

                            }
                        }

            }
            danhBaAdapter.notifyDataSetChanged();

    }

    private void addControlls() {

        lvCountry = (ListView) findViewById(R.id.lvCountry);
        Countrys=new ArrayList<>();


        danhBaAdapter= new DanhBaAdapter(MainActivity.this,R.layout.item,Countrys);
        lvCountry.setAdapter(danhBaAdapter);

        ibtnStartCurrCon=findViewById(R.id.ibtnStartCurrCon);
        ibtnEndCurrCon=findViewById(R.id.ibtnEndCurrCon);
        txtBase=findViewById(R.id.txtBase);
        txtLastUpdate=findViewById(R.id.txtLastUpdate);
        btnGetData=findViewById(R.id.btnGetData);
        etxtSearch=findViewById(R.id.etxtSearch);
        txtOne=findViewById(R.id.txtOne);
        txtTwo=findViewById(R.id.txtTwo);
        imgOne=findViewById(R.id.imgOne);
        imgTwo=findViewById(R.id.imgTwo);
        etxtResult=findViewById(R.id.etxtResult);
        etxtConvert=findViewById(R.id.etxtConvert);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class ReadJSON extends AsyncTask<String,Void,String> {
        StringBuilder content=new StringBuilder();
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                InputStreamReader inputStreamReader=new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    content.append(line);
                }
                bufferedReader.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result=s;
            ReadJSONLanguage(result);
            isGetData=true;
        }
    }
    public String convertCurrency(String num ,String countryOne,String countryTwo){
        String resu="null";
        try {

            double one = Double.parseDouble(object.getString(countryOne).toString());

            double two = Double.parseDouble(object.getString(countryTwo).toString());

            double number = Double.parseDouble(num);

            double res=number*two/one;

            resu=String.format("%.4f", res);
            return resu;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resu;

    }
    public void ReadJSONLanguage(String language){
        try {
            JSONObject jsonObject=new JSONObject(result);
            object=jsonObject.getJSONObject("rates");
            txtBase.setText("1 "+jsonObject.getString("base")+ " = ");
            String date = jsonObject.getString("date");
            String tstamp = jsonObject.getString("timestamp");
            txtLastUpdate.setText("Last Update:     "+ date);
            initdata();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void initdata()  {
        Countrys.clear();
            try {
                Countrys.add(new Item(R.drawable.aud,listCountry[0],Double.parseDouble(object.getString(listCountry[0]))));
                Countrys.add(new Item(R.drawable.brl,listCountry[1],Double.parseDouble(object.getString(listCountry[1]))));
                Countrys.add(new Item(R.drawable.cad,listCountry[2],Double.parseDouble(object.getString(listCountry[2]))));
                Countrys.add(new Item(R.drawable.cny,listCountry[3],Double.parseDouble(object.getString(listCountry[3]))));
                Countrys.add(new Item(R.drawable.eur,listCountry[4],Double.parseDouble(object.getString(listCountry[4]))));
                Countrys.add(new Item(R.drawable.inr,listCountry[5],Double.parseDouble(object.getString(listCountry[5]))));
                Countrys.add(new Item(R.drawable.jpy,listCountry[6],Double.parseDouble(object.getString(listCountry[6]))));
                Countrys.add(new Item(R.drawable.mxn,listCountry[7],Double.parseDouble(object.getString(listCountry[7]))));
                Countrys.add(new Item(R.drawable.vnd,listCountry[8],Double.parseDouble(object.getString(listCountry[8]))));
                Countrys.add(new Item(R.drawable.myr,listCountry[9],Double.parseDouble(object.getString(listCountry[9]))));
                Countrys.add(new Item(R.drawable.usd,listCountry[10],Double.parseDouble(object.getString(listCountry[10]))));
                Countrys.add(new Item(R.drawable.pen,listCountry[11],Double.parseDouble(object.getString(listCountry[11]))));
                Countrys.add(new Item(R.drawable.qar,listCountry[12],Double.parseDouble(object.getString(listCountry[12]))));
                Countrys.add(new Item(R.drawable.ron,listCountry[13],Double.parseDouble(object.getString(listCountry[13]))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        danhBaAdapter= new DanhBaAdapter(MainActivity.this,R.layout.item,Countrys);
        lvCountry.setAdapter(danhBaAdapter);

    }

}

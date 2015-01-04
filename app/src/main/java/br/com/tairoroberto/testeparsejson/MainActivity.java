package br.com.tairoroberto.testeparsejson;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendJson(View view){
        Carro carro = new Carro();
        carro.setMarca("Fiat");
        carro.setModelo("Palio");
        carro.setPotencias(new ArrayList<Potencia>());
        carro.getPotencias().add(new Potencia(1.0f,60));
        carro.getPotencias().add(new Potencia(1.5f,80));
        carro.getPotencias().add(new Potencia(2.0f,100));

        String json = generateJson(carro);
        callServer("send-json",json);
    }

    public void getJson(View view){
        callServer("get-json","");
    }

    //Codifica o json para ser enviado ao servidor
    private String generateJson(Carro carro){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            //Preenche o Jsonobject com os valores do carro
            jsonObject.put("marca", carro.getMarca());
            jsonObject.put("modelo", carro.getModelo());

            //Preenche o jsonArray com os valores
            for (int i = 0; i < carro.getPotencias().size(); i++) {
                JSONObject aux = new JSONObject();
                aux.put("motor",carro.getPotencias().get(i).getMotor());
                aux.put("cavalos",carro.getPotencias().get(i).getCavalos());
                jsonArray.put(aux);
            }
            jsonObject.put("potencias",jsonArray);

        }catch (JSONException e){e.printStackTrace();}

        return (jsonObject.toString());
    }


    //decodifica a resposta do servidor
    private Carro degenerateJson(String data){
        Carro carro = new Carro();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray;

            //Atibui os valores ao objeto Carro
            carro.setMarca(jsonObject.getString("marca"));
            carro.setModelo(jsonObject.getString("modelo"));

            //Inicializa o arraylist
            carro.setPotencias(new ArrayList<Potencia>());

            //Preenche o Jsonobject com os valores do carro
            jsonObject.put("marca", carro.getMarca());
            jsonObject.put("modelo", carro.getModelo());

            jsonArray = jsonObject.getJSONArray("potencias");

            //Preenche o jsonArray com os valores
            for (int i = 0; i < jsonArray.length(); i++) {
                Potencia potencia = new Potencia();
                potencia.setMotor(jsonArray.getJSONObject(i).getDouble("motor"));
                potencia.setCavalos(jsonArray.getJSONObject(i).getInt("cavalos"));

                carro.getPotencias().add(potencia);
            }

            //APRESENTAÇÃO
            Log.i("Script","Marca: " + carro.getMarca());
            Log.i("Script","Modelo: " + carro.getModelo());
            for (int i = 0; i < carro.getPotencias().size(); i++) {
                Log.i("Script", "Motor: " + carro.getPotencias().get(i).getMotor());
                Log.i("Script","Cavalos: " + carro.getPotencias().get(i).getCavalos());
            }


        }catch (JSONException e){e.printStackTrace();}

        return (carro);
    }


    @SuppressLint("NewApi")
    private void callServer(final String method, final String data){
        new Thread(){
           public void run(){
               String url = "http://www.tairoroberto.kinghost.net/packages/teste_webservice/process.php";
               String answer = HttpConnection.getSetDataWeb(url,method,data);

               //Mostra a resposta
               Log.i("Script","Resposta: "+answer);

               //verifica se resposta é null
               if (data.isEmpty()){
                   degenerateJson(answer);
               }

           }
        }.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package clcmo.com.br.gasolinaetanol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.text.NumberFormat;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    private double percent = 0.0, percent2 = 0.0;

    private TextView PrecoGasolina, PrecoEtanol, ResultadoTxt;
    private SeekBar SeekGasolina, SeekEtanol;
    private ImageView ResultadoImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //identificando as TextViews
        PrecoGasolina = (TextView) findViewById(R.id.textViewGasolina);
        PrecoEtanol = (TextView) findViewById(R.id.textViewEtanol);
        ResultadoTxt = (TextView) findViewById(R.id.textViewResultado);
        PrecoEtanol.setText(currencyFormat.format(0));
        PrecoGasolina.setText(currencyFormat.format(0));

        //identificando as SeekBar
        SeekGasolina = (SeekBar) findViewById(R.id.seekBarGasolina);
        SeekEtanol = (SeekBar) findViewById(R.id.seekbarEtanol);
        SeekGasolina.setOnSeekBarChangeListener(seekBarListener);
        SeekEtanol.setOnSeekBarChangeListener(seekBarListener);

        //identificando o campo de imagem Resultado
        ResultadoImg = (ImageView) findViewById(R.id.imgResultado);
    }

    private SeekBar.OnSeekBarChangeListener seekBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    if (seekBar.getId() == R.id.seekbarEtanol) {
                        percent = (progress / 10);
                        PrecoEtanol.setText(currencyFormat.format(percent));
                    }
                    else {
                        percent2 = (progress / 10);
                        PrecoGasolina.setText(currencyFormat.format(percent2));
                    }
                    calculate ();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };

    private void calculate (){
        double resultado = percent/percent2;
        if (resultado >= 0.7) {
            ResultadoTxt.setText(R.string.resultado_gasolina);
            ResultadoImg.setImageResource(R.drawable.gasolina);
        }
        else{
            ResultadoTxt.setText(R.string.resultado_etanol);
            ResultadoImg.setImageResource(R.drawable.etanol);
        }
    }
}
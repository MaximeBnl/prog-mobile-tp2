package com.example.calculimc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button envoyer = null;
    Button reset = null;
    EditText taille = null;
    EditText poids = null;
    CheckBox commentaire = null;
    RadioGroup group = null;
    TextView result = null;
    RadioButton centimetre = null;
    RadioButton metre = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // On récupère toutes les vues dont on a besoin
        envoyer = (Button) findViewById(R.id.calcul);
        reset = (Button) findViewById(R.id.reset);
        taille = (EditText) findViewById(R.id.taille);
        poids = (EditText) findViewById(R.id.poids);
        commentaire = (CheckBox) findViewById(R.id.commentaire);
        group = (RadioGroup) findViewById(R.id.group);
        result = (TextView) findViewById(R.id.result);
        centimetre = (RadioButton) findViewById(R.id.radio_centimetre);
        metre = (RadioButton) findViewById(R.id.radio_metre);


        // On attribue un listener adapté aux vues qui en ont besoin
        envoyer.setOnClickListener(envoyerListener);
        reset.setOnClickListener(resetListener);
        commentaire.setOnClickListener(checkedListener);
        taille.setOnKeyListener(modifListener);
        poids.setOnKeyListener(modifListener);
    }

    private View.OnClickListener envoyerListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //  on récupère la taille
            String t = taille.getText().toString();
            // On récupère le poids
            String p = poids.getText().toString();
            float tValue = Float.valueOf(t);
            if (t.contains(".")) {
                centimetre.setChecked(false);
                metre.setChecked(true);
            } else {
                centimetre.setChecked(true);
                metre.setChecked(false);
            }

            // Puis on vérifie que la taille est cohérente
            if (tValue <= 0)
                Toast.makeText(MainActivity.this,R.string.height_must_be_positive, Toast.LENGTH_SHORT).show();
            else {
                float pValue = Float.valueOf(p);
                if (pValue <= 0)
                    Toast.makeText(MainActivity.this,R.string.weight_must_be_positive, Toast.LENGTH_SHORT).show();
                else {
                    // Si l'utilisateur a indiqué que la taille était en centimètres
                    // On vérifie que la Checkbox sélectionnée est la deuxième à l'aide de son identifiant
                    if (group.getCheckedRadioButtonId() == R.id.radio_centimetre)
                        tValue = tValue / 100;
                    float imc = pValue / (tValue * tValue);
                    String resultat = getString(R.string.your_BMI_is) + imc + " . ";
                    if(commentaire.isChecked()) resultat += interpreteIMC(imc);

                    result.setText(resultat);
                }
            }
        }
    };

    private View.OnClickListener resetListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            poids.getText().clear();
            taille.getText().clear();
            result.setText(getString(R.string.click_on_the_button_calculate_BMI_to_obtain_result));
        }
    };

    private View.OnClickListener checkedListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (((CheckBox) v).isChecked()) {
                result.setText(getString(R.string.click_on_the_button_calculate_BMI_to_obtain_result));
            }
        }
    };

  /*  private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            result.setText(texteInit);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };*/

    public String interpreteIMC(float imc){
        if(imc<16.5) return getString(R.string.famine);
        if(imc>16.5 && imc <18.5) return getString(R.string.thinness);
        if(imc>18.5 && imc <25) return getString(R.string.normal_corpulence);
        if(imc>25 && imc <30) return getString(R.string.overweight);
        if(imc>30 && imc <35) return getString(R.string.moderate_obesity);
        if(imc>35 && imc <40) return getString(R.string.severe_obesity);
        if(imc <= 40) return getString(R.string.morbid_obesity);
        else return "error";
    }

    // Se lance à chaque fois qu'on appuie sur une touche en étant sur un EditText
    private View.OnKeyListener modifListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // On remet le texte à sa valeur par défaut
            result.setText(getString(R.string.click_on_the_button_calculate_BMI_to_obtain_result));
            return false;
        }
    };
}
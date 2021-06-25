package com.example.listapersonagem.ui.activities;

//Aula Gravada 26/03/21 00:36:22.

//Importando as referencias

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.listapersonagem.R;
import com.example.listapersonagem.dao.PersonagemDAO;
import com.example.listapersonagem.model.Personagem;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import static com.example.listapersonagem.ui.activities.ConstantesActivities.CHAVE_PERSONAGEM;

public class FormularioPersonagemActivity extends AppCompatActivity {

    //Criando variaveis staticas
    private static final String TITLE_APPBAR_EDITA_PERSONAGEM = "Editar Contato";
    private static final String TITLE_APPBAR_NOVO_PERSONAGEM = "Novo Contato";

    //criando as variaveis EditText
    private EditText campoNome;
    private EditText campoAltura;
    private EditText campoNascimento;
    private EditText campoTelefone;
    private EditText campoEndereco;
    private EditText campoRg;
    private EditText campoCep;
    private EditText campoGenero;

    private final PersonagemDAO dao = new PersonagemDAO(); //criando um  objeto DAO banco de dados do personagem
    private Personagem personagem; // Criando um objeto privado tipo personagem

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //para executar o menu ao clicar no check
        getMenuInflater().inflate(R.menu.activity_formulario_personagem_menu_salvar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.activity_formulario_personagem_menu_salvar) {
            //Usando a Função de Salvar o Cadastro
            finalizaFormulario();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_personagem);

        inicializacaoCampos();
        //Configurando o botao Salvar
        configuraBotao();
        carregaPersonagem();
    }

    private void carregaPersonagem() {

        Intent dados = getIntent();
        if (dados.hasExtra(CHAVE_PERSONAGEM)) {
            //Trocando o nome do titulo
            setTitle(TITLE_APPBAR_EDITA_PERSONAGEM);
            personagem = (Personagem) dados.getSerializableExtra(CHAVE_PERSONAGEM);

            preencheCampos();
        } else {

            setTitle(TITLE_APPBAR_NOVO_PERSONAGEM);
            //criar um novo personagem caso não tenha nenhum
            personagem = new Personagem();
        }
    }

    private void preencheCampos() {
        //preenche os campos com as variaveis salvas
        campoNome.setText(personagem.getNome());
        campoAltura.setText(personagem.getAltura());
        campoNascimento.setText(personagem.getNascimento());
        campoTelefone.setText(personagem.getTelefone());
        campoEndereco.setText(personagem.getEndereco());
        campoRg.setText(personagem.getRg());
        campoCep.setText(personagem.getCep());
        campoGenero.setText(personagem.getGenero());
    }

    private void configuraBotao() {
        //colocando uma funcao no botaosalvar
        Button botaoSalvar = findViewById(R.id.button_salvar);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Salva ou edita o formulario
                finalizaFormulario();
            }
        });
    }

    private void finalizaFormulario() {
        preenchePersonagem();
        //Verifica se o id é valido
        if (personagem.IdValido()) {
            //pega as informações para editar
            dao.edita(personagem);
        } else {
            //Salva as informações do personagem no dao
            dao.salva(personagem);
        }
        //Finalizando o Activity
        finish();
    }

    private void inicializacaoCampos() {
        //Pegando as referencias do EditText adicionando a variavel
        campoNome = findViewById(R.id.editText_nome);
        campoAltura = findViewById(R.id.editText_altura);
        campoNascimento = findViewById(R.id.editText_nascimento);
        campoTelefone = findViewById(R.id.editText_telefone);
        campoEndereco = findViewById(R.id.editText_endereco);
        campoRg = findViewById(R.id.editText_rg);
        campoCep = findViewById(R.id.editText_cep);
        campoGenero = findViewById(R.id.editText_genero);

        //Colocando as Mascaras no cep
        SimpleMaskFormatter smfCep = new SimpleMaskFormatter("NNNNN-NNN");
        MaskTextWatcher mtwCep = new MaskTextWatcher(campoCep, smfCep);
        campoCep.addTextChangedListener(mtwCep);

        //Colocando as Mascaras no RG
        SimpleMaskFormatter smfRG = new SimpleMaskFormatter("NN.NNN.NNN-N");
        MaskTextWatcher mtwRG = new MaskTextWatcher(campoRg, smfRG);
        campoRg.addTextChangedListener(mtwRG);

        //Colocando as Mascaras De telefone
        SimpleMaskFormatter smfTelefone = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtwTelefone = new MaskTextWatcher(campoTelefone, smfTelefone);
        campoTelefone.addTextChangedListener(mtwTelefone);

        //Colocando as Mascaras de altura
        SimpleMaskFormatter smfAltura = new SimpleMaskFormatter("N,NN");
        MaskTextWatcher mtwAltura = new MaskTextWatcher(campoAltura, smfAltura);
        campoAltura.addTextChangedListener(mtwAltura);

        //Colocando as Mascaras de altura
        SimpleMaskFormatter smfNascimento = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwNascimento = new MaskTextWatcher(campoNascimento, smfNascimento);
        campoNascimento.addTextChangedListener(mtwNascimento);
    }

    private void preenchePersonagem() {

        String nome = campoNome.getText().toString();
        String altura = campoAltura.getText().toString();
        String nascimento = campoNascimento.getText().toString();
        String telefone = campoTelefone.getText().toString();
        String endereco = campoEndereco.getText().toString();
        String rg = campoRg.getText().toString();
        String cep = campoCep.getText().toString();
        String genero = campoGenero.getText().toString();


        personagem.setNome(nome);
        personagem.setAltura(altura);
        personagem.setNascimento(nascimento);
        personagem.setTelefone(telefone);
        personagem.setEndereco(endereco);
        personagem.setRg(rg);
        personagem.setCep(cep);
        personagem.setGenero(genero);
    }
}
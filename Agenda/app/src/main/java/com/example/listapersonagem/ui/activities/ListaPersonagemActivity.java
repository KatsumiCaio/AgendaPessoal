package com.example.listapersonagem.ui.activities;

//Importando as referencias

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.listapersonagem.R;
import com.example.listapersonagem.dao.PersonagemDAO;
import com.example.listapersonagem.model.Personagem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.listapersonagem.ui.activities.ConstantesActivities.CHAVE_PERSONAGEM;

public class ListaPersonagemActivity extends AppCompatActivity {

    //Declarando as variaveis
    public static final String TITLE_APPBAR = "Agenda Pessoal";
    private final PersonagemDAO dao = new PersonagemDAO();
    private ArrayAdapter<Personagem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_personagem);
        setTitle(TITLE_APPBAR);
        //criando o botão FAB e colocando uma função
        botaoFAB();
        configuraLista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //atualizando os itens do formulario
        atualizaPersonagem();
    }

    private void atualizaPersonagem() {
        //Limpa a lista
        adapter.clear();

        adapter.addAll(dao.todos());
    }

    private void remove(Personagem personagem) {
        //Remove o personagem do dao
        dao.remove(personagem);
        //Remove o personagem da lista
        adapter.remove(personagem);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //Menu para remover um item ao ficar clicado
        getMenuInflater().inflate(R.menu.activity_lista_personagens_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //deleta e cria um menu para apagar o item selecionado
        configuraMenu(item);
        return super.onContextItemSelected(item);
    }

    private void configuraMenu(@NonNull MenuItem item) {
        //pegando o id do menu
        int itemId = item.getItemId();
        if (itemId == R.id.activity_lista_personagem_menu_remover) {
            //ao clicar em remover cria um menu
            new AlertDialog.Builder(this)
                    .setTitle("Removendo Contato")
                    .setMessage("Tem certeza que deseja remover?")
                    //clicando no sim apaga o personagem
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                            Personagem personagemEscolhido = adapter.getItem(menuInfo.position);
                            remove(personagemEscolhido);
                        }
                    })
                    .setNegativeButton("Nao", null)
                    .show();
        }
    }

    private void configuraLista() {
        ListView listaDePersonagens = findViewById(R.id.activity_main_lista_personagem);
        //adiciona os personagens a lista
        listaDePersonagem(listaDePersonagens);
        configuraItemPerClique(listaDePersonagens);
        registerForContextMenu(listaDePersonagens);
    }
    private void configuraItemPerClique(ListView listaDePersonagens) {
        listaDePersonagens.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Adiciona a posição da lista que o usuário clica
                Personagem personagemEscolhido = (Personagem) adapterView.getItemAtPosition(position);
                abreFormularioModoEditar(personagemEscolhido);
            }
        });
    }

    //Abre o formulario de edição
    private void abreFormularioModoEditar(Personagem personagem) {
        Intent vaiParaFormulario = new Intent(this, FormularioPersonagemActivity.class);
        //traz as informações do personagem
        vaiParaFormulario.putExtra(CHAVE_PERSONAGEM, personagem);
        //muda para a activity FormularioPersonagemActivity
        startActivity(vaiParaFormulario);
    }

    private void listaDePersonagem(ListView listaDePersonagens) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaDePersonagens.setAdapter(adapter);
    }

    // setando o botão fab
    private void botaoFAB() {

        FloatingActionButton botaoNovoPersonagem = findViewById(R.id.fab_novo_personagem);
        //quando clicar no botao fab realiza a função
        botaoNovoPersonagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //muda para o FormularioPersonagemActivity
                abreFormularioSalvar();
            }
        });
    }

    private void abreFormularioSalvar() {
        startActivity(new Intent(this, FormularioPersonagemActivity.class));
    }
}



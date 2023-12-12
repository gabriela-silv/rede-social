package com.example.redesocial.controller;

import com.example.redesocial.model.Postagem;
import com.example.redesocial.model.Usuario;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.redesocial.repository.PostagemRepository;
import com.example.redesocial.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class RedeSocialController {

    private PostagemRepository postagemRepository;
    private UsuarioRepository usuarioRepository;

//Construtor gerado para iniciar a base com dados, e injetar os repositórios no controller.
    public RedeSocialController(UsuarioRepository usuarioRepository, PostagemRepository postagemRepository) {
        this.usuarioRepository = usuarioRepository;
        this.postagemRepository = postagemRepository;
        postagemRepository.save(new Postagem("Lorem Ipsum is simply dummy text of the printing and typesetting industry.","Ana"));
        postagemRepository.save(new Postagem("Contrary to popular belief, Lorem Ipsum is not simply random text.","Gabi"));
        postagemRepository.save(new Postagem("The standard chunk of Lorem Ipsum used since the 1500s.","Abella"));
        usuarioRepository.save(new Usuario("Ana"));
        usuarioRepository.save(new Usuario("Gabi"));
        usuarioRepository.save(new Usuario("Abella"));
    }


    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }


//Método responsável pela listagem reversa.
    @GetMapping("/postagens-data")
    public String listarPostagensData(Model model) {
        model.addAttribute("postagem", new Postagem());
        ArrayList<Postagem> postagem = new ArrayList<>();
        postagem = (ArrayList<Postagem>) postagemRepository.findAll();
        Collections.reverse(postagem);//Reverte o array list para exibir as postagens da mais recente para a mais antiga.

        model.addAttribute("nome", retornarUsuarioLogado().getNome());
        model.addAttribute("postagens", postagem);
        return "postagem";
    }

//Método responsável pela renderização da tela de postagem, adicionando os atributos nome, postagem, postagens.
    @GetMapping("/postagens")
    public String listarPostagens(Model model, String nomeUsuario) {
        model.addAttribute("nome", nomeUsuario);
        model.addAttribute("postagem", new Postagem());
        model.addAttribute("postagens", postagemRepository.findAll());//postagensRepository.findAll() retorna uma lista de Postagem.
        return "postagem";
    }


    @PostMapping("/nova-postagem")
    public String novaPostagem(@ModelAttribute Postagem postagem, Model model) {
        postagemRepository.save(new Postagem(postagem.getConteudo(),retornarUsuarioLogado().getNome()));//Salvando a postagem com o usuário logado.
        return listarPostagens(model,retornarUsuarioLogado().getNome());
    }


//Renderizar a tela, possibilitando exibir a tela no browser.
    @GetMapping("/novo-login")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

//Salva o usuário no repository e chama o método listarPostagens.
    @PostMapping("/salvar-usuario")
    public String salvarUsuario(@ModelAttribute Usuario usuario, Model model) {
        usuarioRepository.save(usuario);
        return listarPostagens(model, usuario.getNome());
    }

    @GetMapping("/usuario-conteudo")
    public String usuarioConteudo(Model model){
        model.addAttribute("nome", retornarUsuarioLogado().getNome());
        model.addAttribute("postagem", new Postagem());
        model.addAttribute("postagens", postagemRepository.findByUsuario(retornarUsuarioLogado().getNome()));
        return "postagem";
    }

    public Usuario retornarUsuarioLogado(){
        ArrayList<Usuario> usuarioListaRepository = new ArrayList<>();
        usuarioListaRepository = (ArrayList<Usuario>) usuarioRepository.findAll();//A lista retornada pelo findAll é uma lista do  tipo usuário.
        int tamanhoLista = usuarioListaRepository.size();//Pegando o tamanho da lista.
        Usuario usuarioLogado = (Usuario) usuarioListaRepository.get(tamanhoLista - 1);//Retornando o ultimo usuário logado.
        return usuarioLogado;
    }
}

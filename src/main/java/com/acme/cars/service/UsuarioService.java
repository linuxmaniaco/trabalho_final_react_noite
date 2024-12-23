package com.acme.cars.service;

import com.acme.cars.exception.RecursoNaoEncontradoException;
import com.acme.cars.model.Usuario;
import com.acme.cars.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final EntityManager entityManager;
    private final UsuarioRepository usuarioRepository;
    public Optional<Usuario> findByEmail(String email) {
        Usuario byEmail = usuarioRepository.findByEmail(email);
        if (byEmail == null) {return Optional.empty(); }
        else { return Optional.of(byEmail); }
    }
    public List<Usuario> findAll(int page, int size) {
        return usuarioRepository.findAll(PageRequest.of(page, size)).stream().toList();
    }

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        usuarioRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com id: " + id));
        usuarioRepository.deleteById(id);
    }

    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        if(!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado com id: " + id);
        }
        usuarioAtualizado.setId(id);
        return usuarioRepository.save(usuarioAtualizado);

    }

    public long count(){
        return usuarioRepository.count();
    }
}

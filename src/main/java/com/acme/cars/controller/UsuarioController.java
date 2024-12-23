package com.acme.cars.controller;

import com.acme.cars.dto.AuthUserDTO;
import com.acme.cars.exception.AuthenticationException;
import com.acme.cars.exception.RecursoNaoEncontradoException;
import com.acme.cars.model.Usuario;
import com.acme.cars.payload.AuthPayload;
import com.acme.cars.service.SecurityService;
import com.acme.cars.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final SecurityService securityService;


    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuario(@RequestHeader(value = "page", defaultValue = "0") String page, @RequestHeader(value = "size", defaultValue = "30") String size) {
        log.info("page: " + page + " size: " + size);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Total-Count", String.valueOf(usuarioService.count()));
        List<Usuario> allUsuarios = usuarioService.findAll(Integer.parseInt(page), Integer.parseInt(size));
        return new ResponseEntity<>(allUsuarios, headers, HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<?> autenticate(@RequestBody AuthUserDTO authUserDTO){
        try{
            String authenticate = securityService.authenticate(authUserDTO);
            return ResponseEntity.ok(new AuthPayload(authenticate));
        }catch (AuthenticationException ex){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Usuario ou senha invalidos"));
        }
    }

    @PostMapping
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody Usuario usuario){
        Usuario usuarioSalvar = usuarioService.salvar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado){
        try {
            Usuario usuario = usuarioService.atualizar(id, usuarioAtualizado);
            return ResponseEntity.ok(usuario);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            usuarioService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}

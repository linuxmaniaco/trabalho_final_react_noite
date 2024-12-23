package com.acme.cars.controller;

import com.acme.cars.exception.RecursoNaoEncontradoException;
import com.acme.cars.model.Carro;
import com.acme.cars.service.CarroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController  @RequestMapping("/api/carros")
@RequiredArgsConstructor  @CrossOrigin(origins = "*")
public class CarroController {
    private final CarroService carroService;

    @GetMapping
    public ResponseEntity<List<Carro>> listarTodos(@RequestHeader(value = "page", defaultValue = "0") String page, @RequestHeader(value = "size", defaultValue = "10") String size) {
//        log.info("page: " + page + " size " + size);
        List<Carro> carros = carroService.listarTodos();
        return ResponseEntity.ok(carros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carro> buscarPorId(@PathVariable Long id) {
        try {
            Carro carro = carroService.buscarPorId(id);
            return ResponseEntity.ok(carro);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Carro> salvar(@RequestBody Carro carro) {
        Carro carroSalvo = carroService.salvar(carro);
        return ResponseEntity.status(HttpStatus.CREATED).body(carroSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carro> atualizar(@PathVariable Long id, @RequestBody Carro carroAtualizado) {
        try {
            Carro carro = carroService.atualizar(id, carroAtualizado);
            return ResponseEntity.ok(carro);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            carroService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

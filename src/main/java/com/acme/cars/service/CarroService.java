package com.acme.cars.service;

import com.acme.cars.exception.RecursoNaoEncontradoException;
import com.acme.cars.model.Carro;
import com.acme.cars.payload.CriteriaRequest;
import com.acme.cars.repository.CarroRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service@RequiredArgsConstructor
public class CarroService {

    private final CarroRepository carroRepository;
    private final EntityManager entityManager;

    public List<Carro> listarTodos() {
        return carroRepository.findAll();
    }

    public List<Carro> listarTodos(int page, int size) {
        return carroRepository.findAll(PageRequest.of(page, size)).stream().toList();
    }

    public Carro buscarPorId(Long id) {
        return carroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carro não encontrado com id: " + id));
    }

    public Carro salvar(Carro carro) {
        return carroRepository.save(carro);
    }
    public void deletar(Long id) {
        carroRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Carro não encontrado com id: " + id));
        carroRepository.deleteById(id);
    }

    public Carro atualizar(Long id, Carro carroAtualizado) {
        if (!carroRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Carro não encontrado com id: " + id);
        }
        carroAtualizado.setId(id);
        return carroRepository.save(carroAtualizado);
    }

    public long count(){
        return carroRepository.count();
    }

    public List<Carro> search(CriteriaRequest criteriaRequest){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Carro> cq = cb.createQuery(Carro.class);
        Root<Carro> carro = cq.from(Carro.class);
        List<Predicate> predicates = new ArrayList<>();

        if(criteriaRequest.getModelo().isPresent()){
            predicates.add(cb.like(cb.lower(carro.get("modelo")), "%"+criteriaRequest.getModelo().get().toLowerCase()+"%"));
        }
        if(criteriaRequest.getFabicante().isPresent()){
            predicates.add(cb.like(cb.lower(carro.get("fabricante")), "%"+criteriaRequest.getFabicante().get().toLowerCase()+"%"));
        }
        if(criteriaRequest.getPais().isPresent()){

            predicates.add(cb.like(cb.lower(carro.get("pais")), "%"+criteriaRequest.getPais().get().toLowerCase()+"%"));
        }
        if(criteriaRequest.getCor().isPresent()){
            predicates.add(cb.like(cb.lower(carro.get("cor")), "%" +criteriaRequest.getCor().get().toLowerCase()+"%"));
        }
        if (criteriaRequest.getAno().isPresent()) {
            predicates.add(cb.equal(carro.get("ano"), criteriaRequest.getAno().get()));
        }
        cq.where(predicates.toArray(Predicate[]::new));
        return entityManager.createQuery(cq).getResultList();
    }
}

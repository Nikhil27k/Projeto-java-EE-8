package br.com.projeto.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.projeto.domain.Cliente;

/**
 * Repository para operações de persistência da entidade Cliente.
 */
@ApplicationScoped
public class ClienteRepository implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ClienteRepository.class.getName());
    
    @PersistenceContext(unitName = "Projeto")
    private EntityManager entityManager;
    
    /**
     * Salva ou atualiza um cliente.
     */
    public Cliente save(Cliente cliente) {
        try {
            if (cliente.getId() == null) {
                LOGGER.log(Level.INFO, "Criando novo cliente: {0}", cliente.getNome());
                entityManager.persist(cliente);
                return cliente;
            } else {
                LOGGER.log(Level.INFO, "Atualizando cliente: {0}", cliente.getNome());
                return entityManager.merge(cliente);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar cliente", e);
            throw e;
        }
    }
    
    /**
     * Busca um cliente por ID.
     */
    public Optional<Cliente> findById(Long id) {
        try {
            Cliente cliente = entityManager.find(Cliente.class, id);
            return Optional.ofNullable(cliente);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar cliente por ID: " + id, e);
            return Optional.empty();
        }
    }
    
    /**
     * Busca um cliente por CPF.
     */
    public Optional<Cliente> findByCpf(String cpf) {
        try {
            TypedQuery<Cliente> query = entityManager.createQuery(
                "SELECT c FROM Cliente c WHERE c.cpf = :cpf", Cliente.class);
            query.setParameter("cpf", cpf);
            List<Cliente> resultados = query.getResultList();
            return resultados.isEmpty() ? Optional.empty() : Optional.of(resultados.get(0));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar cliente por CPF: " + cpf, e);
            return Optional.empty();
        }
    }
    
    /**
     * Lista todos os clientes.
     */
    public List<Cliente> findAll() {
        try {
            TypedQuery<Cliente> query = entityManager.createQuery(
                "SELECT c FROM Cliente c ORDER BY c.nome", Cliente.class);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar clientes", e);
            throw e;
        }
    }
    
    /**
     * Remove um cliente.
     */
    public void delete(Cliente cliente) {
        try {
            LOGGER.log(Level.INFO, "Removendo cliente: {0}", cliente.getNome());
            if (!entityManager.contains(cliente)) {
                cliente = entityManager.merge(cliente);
            }
            entityManager.remove(cliente);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao remover cliente", e);
            throw e;
        }
    }
    
    /**
     * Verifica se existe um cliente com o CPF informado (excluindo o próprio).
     */
    public boolean existsByCpfAndIdNot(String cpf, Long id) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Cliente c WHERE c.cpf = :cpf AND c.id != :id", Long.class);
            query.setParameter("cpf", cpf);
            query.setParameter("id", id == null ? 0L : id);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao verificar CPF duplicado", e);
            return false;
        }
    }
}


package br.com.projeto.service;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import br.com.projeto.domain.Cliente;
import br.com.projeto.dto.ClienteDTO;
import br.com.projeto.mapper.ClienteMapper;
import br.com.projeto.repository.ClienteRepository;

/**
 * Service para regras de negócio da entidade Cliente.
 * Trabalha com DTOs para isolar a camada de apresentação das entidades JPA.
 */
@Named
@ApplicationScoped
public class ClienteService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ClienteService.class.getName());
    
    @Inject
    private ClienteRepository clienteRepository;
    
    @Inject
    private ClienteMapper clienteMapper;
    
    /**
     * Salva ou atualiza um cliente (usando DTO).
     */
    @Transactional
    public ClienteDTO salvar(ClienteDTO clienteDTO) {
        try {
            // Converter DTO para entidade
            Cliente cliente = clienteMapper.toEntity(clienteDTO);
            
            // Validar CPF duplicado
            if (clienteRepository.existsByCpfAndIdNot(cliente.getCpf(), cliente.getId())) {
                throw new IllegalArgumentException("CPF já cadastrado no sistema");
            }
            
            // Limpar CPF (remover pontos e traços)
            cliente.setCpf(cliente.getCpf().replaceAll("[^0-9]", ""));
            
            Cliente clienteSalvo = clienteRepository.save(cliente);
            LOGGER.log(Level.INFO, "Cliente salvo com sucesso: {0}", clienteSalvo.getId());
            
            // Converter entidade para DTO e retornar
            return clienteMapper.toDTO(clienteSalvo);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar cliente", e);
            throw e;
        }
    }
    
    /**
     * Busca um cliente por ID (retorna DTO).
     */
    public ClienteDTO buscarPorId(Long id) {
        Cliente cliente = buscarEntidadePorId(id);
        return clienteMapper.toDTO(cliente);
    }
    
    /**
     * Busca um cliente por ID como entidade (para uso interno).
     */
    public Cliente buscarEntidadePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }
    
    /**
     * Lista todos os clientes (retorna lista de DTOs).
     */
    public List<ClienteDTO> listarTodos() {
        try {
            List<Cliente> clientes = clienteRepository.findAll();
            return clienteMapper.toDTOList(clientes);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar clientes", e);
            throw e;
        }
    }
    
    /**
     * Lista todos os clientes como entidades (para uso interno, ex: no converter).
     */
    public List<Cliente> listarTodosComoEntidades() {
        try {
            return clienteRepository.findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar clientes", e);
            throw e;
        }
    }
    
    /**
     * Remove um cliente.
     */
    @Transactional
    public void remover(Long id) {
        try {
            Cliente cliente = buscarEntidadePorId(id);
            clienteRepository.delete(cliente);
            LOGGER.log(Level.INFO, "Cliente removido com sucesso: {0}", id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao remover cliente: " + id, e);
            if (e.getMessage() != null && e.getMessage().contains("foreign key")) {
                throw new IllegalStateException(
                    "Não é possível excluir cliente com pedidos vinculados. " +
                    "Remova os pedidos primeiro.");
            }
            throw e;
        }
    }
    
    /**
     * Valida CPF (validação básica de formato).
     */
    public boolean validarCpf(String cpf) {
        if (cpf == null) {
            return false;
        }
        
        // Remover caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");
        
        // Verificar se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        
        // Verificar se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        return true;
    }
}


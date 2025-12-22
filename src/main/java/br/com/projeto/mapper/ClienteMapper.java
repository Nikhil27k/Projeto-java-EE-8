package br.com.projeto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import br.com.projeto.domain.Cliente;
import br.com.projeto.dto.ClienteDTO;

/**
 * Mapper para conversão entre Cliente (entidade JPA) e ClienteDTO.
 */
@ApplicationScoped
public class ClienteMapper {
    
    /**
     * Converte Cliente (entidade) para ClienteDTO.
     */
    public ClienteDTO toDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setCpf(cliente.getCpf());
        dto.setEmail(cliente.getEmail());
        dto.setDataCadastro(cliente.getDataCadastro());
        
        return dto;
    }
    
    /**
     * Converte ClienteDTO para Cliente (entidade).
     */
    public Cliente toEntity(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setEmail(dto.getEmail());
        cliente.setDataCadastro(dto.getDataCadastro());
        
        return cliente;
    }
    
    /**
     * Converte lista de Cliente para lista de ClienteDTO.
     */
    public List<ClienteDTO> toDTOList(List<Cliente> clientes) {
        if (clientes == null) {
            return null;
        }
        
        return clientes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Converte lista de ClienteDTO para lista de Cliente.
     */
    public List<Cliente> toEntityList(List<ClienteDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}


package ro.ubb.catalog.web.converter;


import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Client;
import ro.ubb.catalog.web.dto.ClientDto;

@Component
public class ClientConverter extends BaseConverter<Client, ClientDto> {
    @Override
    public Client convertDtoToModel(ClientDto dto) {
        Client client = Client.builder()
                .name(dto.getName())
                .age(dto.getAge())
                .build();
        client.setId(dto.getId());
        return client;
    }

    @Override
    public ClientDto convertModelToDto(Client client) {
        ClientDto dto = ClientDto.builder()
                .name(client.getName())
                .age(client.getAge())
                .build();
        dto.setId(client.getId());
        return dto;
    }
}

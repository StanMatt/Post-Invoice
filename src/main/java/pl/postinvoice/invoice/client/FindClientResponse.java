package pl.postinvoice.invoice.client;

import lombok.Value;

@Value
public class FindClientResponse {
    Long id;


    String name;


    public static FindClientResponse from(Client clients) {
        return new FindClientResponse(clients.getId(), clients.getName());
    }
}

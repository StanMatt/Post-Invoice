package pl.postinvoice.user.adress;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.postinvoice.user.User;
import pl.postinvoice.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    @Transactional
    public void createOrUpdate(CreateAddressRequest createAddressRequest) {
        User user = userService.findById(createAddressRequest.getUserId());
        Address address = addressRepository.findById(createAddressRequest.getUserId())
                .orElse(Address.builder()
                        .user(user)
                        .build()
                );
        address.setStreet(createAddressRequest.getStreet());
        address.setCity(createAddressRequest.getCity());
        addressRepository.save(address);
    }
}



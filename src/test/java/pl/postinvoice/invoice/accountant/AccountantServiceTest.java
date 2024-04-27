package pl.postinvoice.invoice.accountant;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import pl.postinvoice.BaseUnitTest;
import pl.postinvoice.invoice.client.Client;
import pl.postinvoice.invoice.client.ClientService;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountantServiceTest  extends BaseUnitTest {
    @InjectMocks
    private AccountantService underAccountant;
    @Mock
    private AccountantRepository accountantRepository;
    @Mock
    private ClientService clientService;
    @Captor
    private ArgumentCaptor<Accountant> accountantCaptor;


    @Test
    void givenCorrectAccountant_whenCreate_thenCreateAccountant() {
        //given
        CreateAccountantRequest createAccountantRequest= new CreateAccountantRequest("name");
        //when
        underAccountant.create(createAccountantRequest);
        //then

        verify(accountantRepository).save(accountantCaptor.capture());
        Accountant accountantCaptorValue = accountantCaptor.getValue();
        assertThat(accountantCaptorValue).isNotNull();
        assertThat(accountantCaptorValue.getId()).isNull();
        assertThat(accountantCaptorValue.getVersion()).isNull();
        assertThat(accountantCaptorValue.getName()).isEqualTo(createAccountantRequest.getName());
        assertThat(accountantCaptorValue.getCreatedDateTime()).isNull();
        assertThat(accountantCaptorValue.getModifiedDateTime()).isNull();
        assertThat(accountantCaptorValue.getClients()).isNull();



    }

    @Test
    void givenClientNotExist_whenjointAccountantToClient_thenEntityNotFoundExepciton() {
        //give
        JoinClientToAccountantRequest request= new JoinClientToAccountantRequest(22L, 1L);
        Mockito.when(accountantRepository.findByIdFetchGroupsInfo(request.getAccountantId()))
                .thenReturn(Optional.empty());

        //when
        Executable executable = () -> underAccountant.jointClientToAccountant(request);

        //then

        assertThrows(EntityNotFoundException.class, executable);
        verifyNoInteractions(clientService);
    }

    @Test
    void givenAccountantNotExist_whenjointAccountantToClient_thenEntityNotFoundExepciton() {
        //give
        JoinClientToAccountantRequest request= new JoinClientToAccountantRequest(22L, 1L);

        Mockito.when(accountantRepository.findByIdFetchGroupsInfo(request.getAccountantId()))
                .thenReturn(Optional.of(mock(Accountant.class)));
        Mockito.when(clientService.findById(request.getClientId()))
                .thenThrow(EntityNotFoundException.class);

        //when
        Executable executable = () -> underAccountant.jointClientToAccountant(request);

        //then

        assertThrows(EntityNotFoundException.class, executable);
    }
    @Test
    void givenCoorectRequest_whenjointClientToAccountant_thenAccountantJoinToClient() {
        //give
        JoinClientToAccountantRequest request= new JoinClientToAccountantRequest(22L, 1L);
        Accountant accountant = Accountant.builder()
                .id(request.getAccountantId())
                .clients(new HashSet<>())
                .build();
        Client client = Client.builder()
                .id(request.getClientId())
                .build();

        Mockito.when(accountantRepository.findByIdFetchGroupsInfo(request.getAccountantId()))
                .thenReturn(Optional.of(accountant));

        Mockito.when(clientService.findById(request.getClientId()))
                .thenReturn(client);

        //when
        underAccountant.jointClientToAccountant(request);

        //then
        assertThat(accountant.getClients())
                .hasSize(1)
                .containsExactly(client);

    }
}
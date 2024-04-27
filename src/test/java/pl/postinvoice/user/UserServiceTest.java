package pl.postinvoice.user;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.function.Executable;
import org.mockito.*;

import pl.postinvoice.BaseUnitTest;

import pl.postinvoice.groupInfo.GroupInfo;
import pl.postinvoice.groupInfo.GroupInfoService;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class UserServiceTest extends BaseUnitTest {
    @InjectMocks
    private UserService underTest;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupInfoService groupInfoService;
    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void giveCorrectRequest_whenCreate_thenCreateUser() {
        //give
        CreateUserRequest createUserRequest = new CreateUserRequest("login");

        //when
        underTest.create(createUserRequest);

        //then
        verify(userRepository).save(userCaptor.capture());
        User user = userCaptor.getValue();
        assertThat(user).isNotNull();
        assertThat(user.getLogin()).isEqualTo(createUserRequest.getLogin());
        assertThat(user.getVersion()).isNull();
        assertThat(user.getCreatedDateTime()).isNull();
        assertThat(user.getLastModifiedDateTime()).isNull();
        assertThat(user.getGroupsInfo()).isNull();
        assertThat(user.getAddress()).isNull();
    }

    @Test
    void givenUserIdNotExist_whenjoinGroup_thenEntityNotFoundException() {

        //given
        JoinGroupRequest request = new JoinGroupRequest(45L, 21L);
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> underTest.joinGroup(request);
        //then
        Assertions.assertThrows(EntityNotFoundException.class, executable);
        verify(userRepository).findById(request.getUserId());
        verifyNoMoreInteractions(groupInfoService);
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    void givenGroupIdNotExist_whenjoinGroup_thenEntityNotFoundException() {

        //given
        JoinGroupRequest request = new JoinGroupRequest(45L, 21L);
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(User.builder()
                .groupsInfo(new HashSet<>())
                .build()));
        when(groupInfoService.finById(request.getGroupId())).thenThrow(EntityNotFoundException.class);

        //when
        Executable executable = () -> underTest.joinGroup(request);
        //then
        Assertions.assertThrows(EntityNotFoundException.class, executable);
        verify(userRepository).findById(request.getUserId());
        verify(groupInfoService).finById(request.getGroupId());
        verifyNoMoreInteractions(userRepository, groupInfoService);

    }

    @Test
    void givenCorrectRequest_whenjoinGroup_thenAddGroupToUser() {

        //given
        JoinGroupRequest request = new JoinGroupRequest(45L, 21L);
        User user = User.builder()
                .id(request.getUserId())
                .groupsInfo(new HashSet<>())
                .build();
        GroupInfo groupInfo = GroupInfo.builder()
                .id(request.getGroupId())
                .build();

        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));

        when(groupInfoService.finById(request.getGroupId())).thenReturn(groupInfo);

        //when
        underTest.joinGroup(request);
        //then
        assertThat(user.getGroupsInfo())
                .hasSize(1)
                .containsExactly(groupInfo);
        verify(userRepository).findById(request.getUserId());
        verify(groupInfoService).finById(request.getGroupId());

    }
}
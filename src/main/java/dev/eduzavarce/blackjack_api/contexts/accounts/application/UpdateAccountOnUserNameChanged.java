package dev.eduzavarce.blackjack_api.contexts.accounts.application;

import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccount;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountEntity;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountRepository;
import dev.eduzavarce.blackjack_api.contexts.accounts.infrastructure.UserAccountMySqlEntity;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserNameChanged;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEventSubscriber;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserDto;
import dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain.DatabaseException;
import org.springframework.stereotype.Service;

@Service
@DomainEventSubscriber({UserNameChanged.class})
public class UpdateAccountOnUserNameChanged {
    private final UserAccountRepository userAccountRepository;

    public UpdateAccountOnUserNameChanged(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public void on(UserNameChanged event) {
        try {
            UserDto dto = (UserDto) event.toPrimitives().body();
            updateUserAccount(dto);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private void updateUserAccount(UserDto dto) {
        userAccountRepository
                .findById(dto.id())
                .flatMap(
                        userAccountEntity -> {

                            // Create a new UserAccount with the updated name
                            UserAccount updatedUserAccount = UserAccount.create(dto);

                            UserAccountEntity updatedEntity =
                                    UserAccountMySqlEntity.fromDomain(updatedUserAccount, false);
                            return userAccountRepository.save(updatedEntity);
                        })
                .subscribe();
    }
}

package dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.User;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserEntity;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserDto;

@Document(collection = "authUsers")
public class UserMongoEntity implements UserEntity {
  @Id private String id;
  private String name;

  @Indexed(unique = true, name = "email_index")
  private String email;

  private double balance;

  public UserMongoEntity() {}

  public UserMongoEntity(String id, String name, String email, double balance) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.balance = balance;
  }

  public static UserMongoEntity fromDomain(User user) {
    UserDto dto = user.toPrimitives();
    return new UserMongoEntity(dto.id(), dto.name(), dto.email(), dto.balance());
  }

  public User toDomain() {
    return User.create(new UserDto(id, name, email, balance));
  }
}

package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ToString(exclude = {"friends"})
public class User {
    private int id;
    @NonNull
    private final String email;
    @NonNull
    private final String login;
    private String name;
    @NonNull
    private final LocalDate birthday;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    private final Set<Integer> friends = new HashSet<>();

    public void addFriend(Integer friend) {
        friends.add(friend);
    }

    public void removeFriend(Integer friend) {
        friends.remove(friend);
    }

    public List<Integer> getFriends() {
        return List.copyOf(friends);
    }
}

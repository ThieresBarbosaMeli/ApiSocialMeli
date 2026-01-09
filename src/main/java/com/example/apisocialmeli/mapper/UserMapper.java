package com.example.apisocialmeli.mapper;

import com.example.apisocialmeli.domain.User;
import com.example.apisocialmeli.dto.response.FollowedDTO;
import com.example.apisocialmeli.dto.response.FollowerDTO;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class UserMapper {

    private UserMapper() {
    }

    public static FollowerDTO toFollowerDTO(User user) {
        return new FollowerDTO(user.getId(), user.getName());
    }

    public static FollowedDTO toFollowedDTO(User user) {
        return new FollowedDTO(user.getId(), user.getName());
    }

    public static List<FollowerDTO> toFollowerDTOList(Collection<User> users, String order) {
        Stream<User> stream = users.stream();
        if ("name_asc".equalsIgnoreCase(order)) {
            stream = stream.sorted(Comparator.comparing(User::getName));
        } else if ("name_desc".equalsIgnoreCase(order)) {
            stream = stream.sorted(Comparator.comparing(User::getName).reversed());
        }
        return stream.map(UserMapper::toFollowerDTO).toList();
    }

    public static List<FollowedDTO> toFollowedDTOList(Collection<User> users, String order) {
        Stream<User> stream = users.stream();
        if ("name_asc".equalsIgnoreCase(order)) {
            stream = stream.sorted(Comparator.comparing(User::getName));
        } else if ("name_desc".equalsIgnoreCase(order)) {
            stream = stream.sorted(Comparator.comparing(User::getName).reversed());
        }
        return stream.map(UserMapper::toFollowedDTO).toList();
    }
}

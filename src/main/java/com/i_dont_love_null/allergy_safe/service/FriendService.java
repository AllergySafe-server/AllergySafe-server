package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.FriendRequest;
import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.model.Friend;
import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.repository.FriendRepository;
import com.i_dont_love_null.allergy_safe.repository.UserRepository;
import com.i_dont_love_null.allergy_safe.security.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;

    private final UserRepository userRepository;
    private final UserService userService;
    private final IdResponse idResponse;

    public IdResponse createFriend(User user, FriendRequest friendRequest) {
        if (Objects.equals(user.getId(), friendRequest.getUserId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신은 친구로 추가할 수 없습니다.");
        userService.checkIfExists(friendRequest.getUserId());
//        if (isMyFriend(user, friendRequest.getUserId()))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 등록된 친구입니다.");

        Friend newFriend = Friend.builder().userId(friendRequest.getUserId()).build();
        friendRepository.save(newFriend);
        List<Friend> newFriends = user.getFriends();
        newFriends.add(newFriend);
        userRepository.save(user.toBuilder().friends(newFriends).build());
        idResponse.setId(newFriend.getId());
        return idResponse;
    }

    public IdResponse deleteFriend(User user, Long friendId) {
        checkIfExists(friendId);
//        if (!isMyFriend(user, friendId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근할 수 없는 친구입니다.");
        friendRepository.deleteById(friendId);
        idResponse.setId(friendId);

        return idResponse;
    }

    public void checkIfExists(Long friendId) {
        if (friendRepository.findById(friendId).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 친구입니다.");
    }


    public boolean isMyFriend(User user, Long friendId) {
        return Objects.nonNull(friendRepository.findByIdAndUserId(friendId, user.getId()));
    }
}

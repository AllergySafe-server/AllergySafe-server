package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.FriendRequest;
import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.model.Friend;
import com.i_dont_love_null.allergy_safe.model.Profile;
import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.repository.FriendRepository;
import com.i_dont_love_null.allergy_safe.repository.UserRepository;
import com.i_dont_love_null.allergy_safe.security.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// POST /api/profile/{profile_id} with type(allergy, material, ingredient) and id
// DELETE /api/profile/{profile_id} with type(allergy, material, ingredient) id

@Slf4j
@Service
@AllArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;

    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final ProfileService profileService;
    private final IdResponse idResponse;

    public IdResponse createFriend(User user, FriendRequest friendRequest) {
        if (Objects.equals(user.getId(), friendRequest.getUserId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신은 친구로 추가할 수 없습니다.");
        userService.checkIfExists(friendRequest.getUserId());

        if (isMyFriend(user, friendRequest.getUserId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 등록된 친구입니다.");

        Friend newFriend = Friend.builder().userId(friendRequest.getUserId()).build();
        friendRepository.save(newFriend);
        List<Friend> newFriends = user.getFriends();
        newFriends.add(newFriend);
        userRepository.save(user.toBuilder().friends(newFriends).build());
        idResponse.setId(newFriend.getId());
        return idResponse;
    }

    public IdResponse deleteFriend(User user, Long profileId) {
        Profile profile = profileService.getProfileById(profileId);
        if (!isMyFriend(user, profile.getUser().getId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근할 수 없는 친구입니다.");

        List<Friend> friends = user.getFriends();
        Long friendIdToDelete = null;

        for (Friend friend : friends) {
            if (profileId.equals(userService.getUserById(friend.getUserId()).getProfiles().get(0).getId())) friendIdToDelete = friend.getId();
        }

        if (Objects.isNull(friendIdToDelete)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "내 친구 목록에 없는 친구입니다.");

        List<Friend> newFriends = new ArrayList<>();
        for (Friend newFriend : friends) {
            if (!newFriend.getId().equals(friendIdToDelete)) newFriends.add(newFriend);
        }
        userRepository.save(user.toBuilder().friends(newFriends).build());
        friendRepository.deleteById(friendIdToDelete);
        idResponse.setId(profileId);

        return idResponse;
    }

    public void checkIfExists(Long friendId) {
        if (friendRepository.findById(friendId).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 친구입니다.");
    }


    public boolean isMyFriend(User user, Long userIdToAddForFriend) {
        for (Friend friend : user.getFriends()) {
            if (friend.getUserId().equals(userIdToAddForFriend)) return true;
        }
        return false;
    }

    public Friend getFriendById(Long id) {
        Optional<Friend> friend = friendRepository.findById(id);

        if (friend.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 친구입니다.");
        }

        return friend.get();
    }
}

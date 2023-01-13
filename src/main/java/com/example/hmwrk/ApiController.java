package com.example.hmwrk;

import jdk.dynalink.linker.support.TypeUtilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final UserRepository userRepository;

    Comparator<User> comparatorByName = new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
            return o1.getUsername().compareTo(o2.getUsername());
        }
    };
    Comparator<User> comparatorByAge = new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
            if(o1.getAge() > o2.getAge())
                return 1;
            if(o2.getAge() < o1.getAge())
                return -1;
            return 0;
        }
    };
    Comparator<User> comparatorById = new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
            if(o1.getId() > o2.getId())
                return 1;
            if(o2.getId() < o1.getId())
                return -1;
            return 0;
        }
    };

    public ApiController(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @GetMapping("/users")
    public List<UserData> getAllUsers(@RequestParam(required = false) Integer age,
                                      @RequestBody(required = false) String sortBy,
                                      @RequestBody(required = false) String direction,
                                      @RequestBody(required = false) Integer PageNumber,
                                      @RequestBody(required = false) Integer PageSize){
        List<User> users = new ArrayList<>();
        if(age == null) {
            if (sortBy.equals("username")) {
                if (direction.equals("asc")) {
                    userRepository.findByOrderByUsernameAsc().forEach(users::add);
                }
                if(direction.equals("desc")) {
                    userRepository.findByOrderByUsernameDesc().forEach(users::add);
                }
            }
            if (sortBy.equals("id")) {
                if (direction.equals("asc")) {
                    userRepository.findByOrderByIdAsc().forEach(users::add);
                }
                if(direction.equals("desc")) {
                    userRepository.findByOrderByIdDesc().forEach(users::add);
                }
            }
            if (sortBy.equals("Age")) {
                if (direction.equals("asc")) {
                    userRepository.findByOrderByAgeAsc().forEach(users::add);
                }
                if(direction.equals("desc")) {
                    userRepository.findByOrderByAgeDesc().forEach(users::add);
                }
            }
            else {
                userRepository.findAll().forEach(users::add);
            }
        }else {
            if (sortBy.equals("username")) {
                if (direction.equals("asc")) {
                    userRepository.findByStartGreaterThanEqualAndEndLessThanEqualOrderByUsernameAsc(age-5,age+5).forEach(users::add);
                }
                if (direction.equals("desc")) {
                    userRepository.findByStartGreaterThanEqualAndEndLessThanEqualOrderByUsernameDesc(age-5,age+5).forEach(users::add);
                }
            }
            if (sortBy.equals("id")) {
                if (direction.equals("asc")) {
                    userRepository.findByStartGreaterThanEqualAndEndLessThanEqualOrderByIdAsc(age-5,age+5).forEach(users::add);
                }
                if (direction.equals("desc")) {
                    userRepository.findByStartGreaterThanEqualAndEndLessThanEqualOrderByIdDesc(age-5,age+5).forEach(users::add);
                }
            }
            if (sortBy.equals("Age")) {
                if (direction.equals("asc")) {
                    userRepository.findByStartGreaterThanEqualAndEndLessThanEqualOrderByAgeAsc(age-5,age+5).forEach(users::add);
                }
                if (direction.equals("desc")) {
                    userRepository.findByStartGreaterThanEqualAndEndLessThanEqualOrderByAgeDesc(age-5,age+5).forEach(users::add);
                }
            }
            else {
                userRepository.findByStartGreaterThanEqualAndEndLessThanEqual(age-5,age+5).forEach(users::add);
            }
        }
        List<UserData> returnList = new ArrayList<>();
        if(PageSize != null && PageNumber != null){
            if(PageSize > 0 || PageNumber >= 0 ) {
                List<User> page = new ArrayList();
                label:
                {
                    if ((int) (PageNumber * PageSize) > users.size()) {
                        page = null;
                        break label;
                    }
                    int FinalIndex = (PageSize*(PageNumber+1) > users.size()) ?
                            users.size() : PageSize*(PageNumber+1);
                    for(int i = PageSize*PageNumber; i < FinalIndex; i++)
                        page.add(users.get(i));
                }
                for(int i = 0; i < page.size(); i++)
                    returnList.add(new UserData(page.get(i)));
                return returnList;
            }
        }

        for(int i = 0; i < users.size(); i++)
            returnList.add(new UserData(users.get(i)));
        return returnList;
    }
    @GetMapping("/users/{id}")
    public UserData getUserById(@PathVariable("id") long id){
        Optional<User> userdata = userRepository.findById(id);
        if(userdata.isPresent())
            return new UserData(userdata.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/users")
    public UserData createUser(@RequestBody UserDataPacket user){
        if(user.passwordsMatch())
            return new UserData(userRepository.save(new  User(user.getUsername(),
                                                        user.getPassword(),
                                                        user.getAge())));
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("users/{id}")
    public UserData updateUser(@PathVariable("id") long id,
                               @RequestBody UserDataPacket newUser){
        if(!newUser.passwordsMatch())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            User _user = user.get();
            _user.setUsername(newUser.getUsername());
            _user.setPassword(newUser.getPassword());
            _user.setAge(newUser.getAge());
            return new UserData(userRepository.save(_user));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/users/{id}")
    public HttpStatus deleteUserById(@PathVariable("id") long id){
        userRepository.deleteById(id);
        return HttpStatus.NO_CONTENT;
    }

}

package com.example.hmwrk;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByStartGreaterThanEqualAndEndLessThanEqual(int start, int end);
    List<User> findByStartGreaterThanEqualAndEndLessThanEqualOrderByUsernameAsc(int start, int end);
    List<User> findByStartGreaterThanEqualAndEndLessThanEqualOrderByUsernameDesc(int start, int end);
    List<User> findByStartGreaterThanEqualAndEndLessThanEqualOrderByAgeAsc(int start, int end);
    List<User> findByStartGreaterThanEqualAndEndLessThanEqualOrderByAgeDesc(int start, int end);
    List<User> findByStartGreaterThanEqualAndEndLessThanEqualOrderByIdAsc(int start, int end);
    List<User> findByStartGreaterThanEqualAndEndLessThanEqualOrderByIdDesc(int start, int end);
    List<User> findByOrderByUsernameAsc();
    List<User> findByOrderByUsernameDesc();
    List<User> findByOrderByAgeAsc();
    List<User> findByOrderByAgeDesc();
    List<User> findByOrderByIdAsc();
    List<User> findByOrderByIdDesc();
}

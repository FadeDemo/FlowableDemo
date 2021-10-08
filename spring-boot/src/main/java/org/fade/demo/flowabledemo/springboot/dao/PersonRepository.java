package org.fade.demo.flowabledemo.springboot.dao;

import org.fade.demo.flowabledemo.springboot.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author fade
 * @date 2021/10/08
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * 根据用户名查找Person
     * @param username 用户名
     * @return {@link Person}
     */
    Person findByUsername(String username);

}

/**
 * 
 */
package com.nerve24.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nerve24.entity.User;

/**
 * @author JOGIREDDY
 *
 */

@Repository
public interface UserRepository extends JpaRepository<User, Serializable>{
	
	User findByUsername(String username)throws Exception;

}
